import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PaginatedUsersResponse} from '../../models/user/PaginatedUsersResponse';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';

@Injectable({
  providedIn: 'root'
})
export class SuperAdminService {

  private baseUrl = 'http://localhost:8080/api/v1/super-admin';

  constructor(private http: HttpClient) { }
  getUsers(page: number, size: number) {
    return this.http.get<PaginatedUsersResponse>(`${this.baseUrl}/users`, {params: {page: page, size: size}});
  }
  toggleBan(email: string) {
    return this.http.get<StatusMessageResponse>(`${this.baseUrl}/toggle-user-ban`, {params: {email: email}});
  }
  toggleEnable(email: string) {
    return this.http.get<StatusMessageResponse>(`${this.baseUrl}/toggle-user-enabled`, {params: {email: email}});
  }
}
