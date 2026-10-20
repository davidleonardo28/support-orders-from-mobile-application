import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import {
  FormBuilder,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import {
  OrderStatus,
  Priority,
  SupportOrder,
  SupportOrderSummary,
} from './models/support-order.model';

import { SupportOrderService } from './services/support-order.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  private readonly fb = inject(FormBuilder);

  private readonly supportOrderService = inject(SupportOrderService);

  orders: SupportOrderSummary[] = [];

  selectedOrder: SupportOrder | null = null;

  loading = false;

  errorMessage = '';

  successMessage = '';

  selectedStatus = '';

  readonly statuses: OrderStatus[] = [
    'OPEN',
    'IN_PROGRESS',
    'RESOLVED',
    'CANCELLED',
  ];

  readonly priorities: Priority[] = ['LOW', 'MEDIUM', 'HIGH'];

  orderForm = this.fb.nonNullable.group({
    externalReference: ['', Validators.required],

    customerId: ['', Validators.required],

    category: ['', Validators.required],

    description: [
      '',
      [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(500),
      ],
    ],

    channel: ['MOBILE_APP', Validators.required],

    priority: ['MEDIUM' as Priority, Validators.required],
  });

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.errorMessage = '';

    const status = this.selectedStatus
      ? (this.selectedStatus as OrderStatus)
      : undefined;

    this.supportOrderService.getOrders(status).subscribe({
      next: (orders) => {
        this.orders = orders;
        this.loading = false;
      },

      error: () => {
        this.errorMessage = 'No fue posible cargar las órdenes.';

        this.loading = false;
      },
    });
  }

  createOrder(): void {
    if (this.orderForm.invalid) {
      this.orderForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request = this.orderForm.getRawValue();

    this.supportOrderService.createOrder(request).subscribe({
      next: (order) => {
        this.successMessage = `Orden ${order.externalReference} creada correctamente.`;

        this.orderForm.reset({
          externalReference: '',
          customerId: '',
          category: '',
          description: '',
          channel: 'MOBILE_APP',
          priority: 'MEDIUM',
        });

        this.loading = false;

        this.loadOrders();

        this.selectOrder(order.id);
      },

      error: (error) => {
        this.errorMessage =
          error?.error?.message ?? 'No fue posible crear la orden.';

        this.loading = false;
      },
    });
  }

  selectOrder(id: number): void {
    this.loading = true;
    this.errorMessage = '';

    this.supportOrderService.getOrderById(id).subscribe({
      next: (order) => {
        this.selectedOrder = order;
        this.loading = false;
      },

      error: () => {
        this.errorMessage = 'No fue posible consultar la orden.';

        this.loading = false;
      },
    });
  }

  updateStatus(status: OrderStatus): void {
    if (!this.selectedOrder) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.supportOrderService
      .updateStatus(this.selectedOrder.id, { status })
      .subscribe({
        next: (order) => {
          this.selectedOrder = order;

          this.successMessage = `Estado actualizado a ${status}.`;

          this.loading = false;

          this.loadOrders();
        },

        error: (error) => {
          this.errorMessage =
            error?.error?.message ?? 'No fue posible actualizar el estado.';

          this.loading = false;
        },
      });
  }

  getAllowedTransitions(status: OrderStatus): OrderStatus[] {
    switch (status) {
      case 'OPEN':
        return ['IN_PROGRESS', 'CANCELLED'];

      case 'IN_PROGRESS':
        return ['RESOLVED', 'CANCELLED'];

      default:
        return [];
    }
  }
}
