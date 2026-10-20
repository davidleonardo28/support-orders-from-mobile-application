import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  CreateSupportOrderRequest,
  OrderStatus,
  SupportOrder,
  SupportOrderSummary,
  UpdateOrderStatusRequest,
} from '../models/support-order.model';

@Injectable({
  providedIn: 'root',
})
export class SupportOrderService {
  private readonly apiUrl = 'http://localhost:8080/api/support-orders';

  constructor(private readonly http: HttpClient) {}

  getOrders(status?: OrderStatus): Observable<SupportOrderSummary[]> {
    let params = new HttpParams();

    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<SupportOrderSummary[]>(this.apiUrl, { params });
  }

  getOrderById(id: number): Observable<SupportOrder> {
    return this.http.get<SupportOrder>(`${this.apiUrl}/${id}`);
  }

  createOrder(request: CreateSupportOrderRequest): Observable<SupportOrder> {
    return this.http.post<SupportOrder>(this.apiUrl, request);
  }

  updateStatus(
    id: number,
    request: UpdateOrderStatusRequest,
  ): Observable<SupportOrder> {
    return this.http.patch<SupportOrder>(
      `${this.apiUrl}/${id}/status`,
      request,
    );
  }
}
