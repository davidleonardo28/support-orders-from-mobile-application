package com.davidbarrera.supportorders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.davidbarrera.supportorders.repository.SupportOrderRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.datasource.url=jdbc:h2:mem:supportorderstest",
		"spring.jpa.hibernate.ddl-auto=create-drop" })
class SupportOrderIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SupportOrderRepository repository;

	@BeforeEach
	void cleanDatabase() {
		repository.deleteAll();
	}

	@Test
	void shouldCreateSupportOrder() throws Exception {

		String body = """
				{
				  "externalReference": "TEST-1001",
				  "customerId": "CUST-001",
				  "category": "PAYMENT",
				  "description": "El pago aparece pendiente desde hace varios minutos.",
				  "channel": "MOBILE_APP",
				  "priority": "MEDIUM"
				}
				""";

		mockMvc.perform(post("/api/support-orders").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.externalReference").value("TEST-1001"))
				.andExpect(jsonPath("$.status").value("OPEN"));
	}

	@Test
	void shouldRejectInvalidRequest() throws Exception {

		String body = """
				{
				  "externalReference": "",
				  "customerId": "CUST-001",
				  "category": "PAYMENT",
				  "description": "corta",
				  "channel": "MOBILE_APP",
				  "priority": "MEDIUM"
				}
				""";

		mockMvc.perform(post("/api/support-orders").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldRejectInvalidStatusTransition() throws Exception {

		Long id = createOrder("TEST-2001");

		String body = """
				{
				  "status": "RESOLVED"
				}
				""";

		mockMvc.perform(
				patch("/api/support-orders/{id}/status", id).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isConflict());
	}

	@Test
	void shouldFindExistingOrder() throws Exception {

		Long id = createOrder("TEST-3001");

		mockMvc.perform(get("/api/support-orders/{id}", id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.externalReference").value("TEST-3001"));
	}

	@Test
	void shouldReturnNotFoundForMissingOrder() throws Exception {

		mockMvc.perform(get("/api/support-orders/{id}", 99999L)).andExpect(status().isNotFound());
	}

	private Long createOrder(String externalReference) throws Exception {

		String body = """
				{
				  "externalReference": "%s",
				  "customerId": "CUST-TEST",
				  "category": "PAYMENT",
				  "description": "Descripción válida para una orden de prueba.",
				  "channel": "MOBILE_APP",
				  "priority": "MEDIUM"
				}
				""".formatted(externalReference);

		String response = mockMvc
				.perform(post("/api/support-orders").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		return Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));
	}
}