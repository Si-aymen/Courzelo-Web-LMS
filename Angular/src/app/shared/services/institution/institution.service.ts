import {HttpClient, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {PaginatedInstitutionsResponse} from '../../models/institution/PaginatedInstitutionsResponse';
import {Observable} from 'rxjs';
import {InstitutionRequest} from '../../models/institution/InstitutionRequest';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';
import {InstitutionResponse} from '../../models/institution/InstitutionResponse';

@Injectable({
  providedIn: 'root'
})
export class InstitutionService {

  private baseUrl = 'http://localhost:8080/api/v1/institution';

  constructor(private http: HttpClient) { }

  getInstitutions(page: number = 0, sizePerPage: number = 10, keyword?: string): Observable<PaginatedInstitutionsResponse> {
    let params = new HttpParams()
        .set('page', page.toString())
        .set('sizePerPage', sizePerPage.toString());

    if (keyword) {
      params = params.set('keyword', keyword);
    }

    return this.http.get<PaginatedInstitutionsResponse>(`${this.baseUrl}/all`, { params });
  }

  addInstitution(institutionRequest: InstitutionRequest): Observable<StatusMessageResponse> {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/add`, institutionRequest);
  }

  updateInstitution(institutionID: string, institutionRequest: InstitutionRequest) {
    return this.http.put(`${this.baseUrl}/update/${institutionID}`, institutionRequest);
  }

  deleteInstitution(institutionID: string): Observable<StatusMessageResponse> {
    return this.http.delete<StatusMessageResponse>(`${this.baseUrl}/delete/${institutionID}`);
  }
  addInstitutionUser(institutionID: string, email: string, role: string) {
    const params = new HttpParams()
        .set('email', email)
        .set('role', role);
    return this.http.put(`${this.baseUrl}/${institutionID}/add-user`, null, { params });
  }
  removeInstitutionUserRole(institutionID: string, email: string, role: string) {
    const params = new HttpParams()
        .set('email', email)
        .set('role', role);
    return this.http.delete(`${this.baseUrl}/${institutionID}/remove-user-role`, { params });
  }

  getInstitutionByID(institutionID: string): Observable<InstitutionResponse> {
    return this.http.get<InstitutionResponse>(`${this.baseUrl}/${institutionID}`);
  }
  getInstitutionUsers(institutionID: string, keyword?: string, role?: string, page: number = 0, sizePerPage: number = 10):
      Observable<PaginatedInstitutionsResponse> {
    let params = new HttpParams()
        .set('page', page.toString())
        .set('sizePerPage', sizePerPage.toString());

    if (role) {
      params = params.set('role', role);
    }
    if (keyword) {
      params = params.set('keyword', keyword);
    }

    return this.http.get<PaginatedInstitutionsResponse>(`${this.baseUrl}/${institutionID}/users`, { params });
  }
}
