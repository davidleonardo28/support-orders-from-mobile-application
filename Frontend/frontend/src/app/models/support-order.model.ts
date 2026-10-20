export type OrderStatus = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CANCELLED';

export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface SupportOrderSummary {
  id: number;
  externalReference: string;
  category: string;
  priority: Priority;
  status: OrderStatus;
  createdAt: string;
}

export interface SupportOrder {
  id: number;
  externalReference: string;
  customerId: string;
  category: string;
  description: string;
  channel: string;
  priority: Priority;
  status: OrderStatus;
  createdAt: string;
}

export interface CreateSupportOrderRequest {
  externalReference: string;
  customerId: string;
  category: string;
  description: string;
  channel: string;
  priority: Priority;
}

export interface UpdateOrderStatusRequest {
  status: OrderStatus;
}
