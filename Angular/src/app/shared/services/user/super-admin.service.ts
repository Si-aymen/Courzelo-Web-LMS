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
  getUsers(page: number, size: number, keyword: string) {
    return this.http.get<PaginatedUsersResponse>(`${this.baseUrl}/users`, {params: {page: page, size: size, keyword: keyword}});
  }
  toggleBan(email: string) {
    return this.http.get<StatusMessageResponse>(`${this.baseUrl}/toggle-user-ban`, {params: {email: email}});
  }
  toggleEnable(email: string) {
    return this.http.get<StatusMessageResponse>(`${this.baseUrl}/toggle-user-enabled`, {params: {email: email}});
  }
  addRole(email: string, role: string) {
    return this.http.get<StatusMessageResponse>(`${this.baseUrl}/add-role`, {params: {email: email, role: role}});
  }
    removeRole(email: string, role: string) {
        return this.http.get<StatusMessageResponse>(`${this.baseUrl}/remove-role`, {params: {email: email, role: role}});
    }
}
