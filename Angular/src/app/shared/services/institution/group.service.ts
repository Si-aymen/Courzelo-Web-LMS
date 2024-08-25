import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GroupRequest } from '../../models/institution/GroupRequest';
import { GroupResponse } from '../../models/institution/GroupResponse';
import { PaginatedGroupsResponse } from '../../models/institution/PaginatedGroupsResponse';

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private baseUrl = 'http://localhost:8080/api/v1/group';

  constructor(private http: HttpClient) { }

  getGroup(groupID: string): Observable<GroupResponse> {
    return this.http.get<GroupResponse>(`${this.baseUrl}/${groupID}`);
  }

  getGroupsByInstitution(institutionID: string, page: number, sizePerPage: number): Observable<PaginatedGroupsResponse> {
    const params = new HttpParams()
        .set('page', page.toString())
        .set('sizePerPage', sizePerPage.toString());
    return this.http.get<PaginatedGroupsResponse>(`${this.baseUrl}/groups/${institutionID}`, { params });
  }

  createGroup(groupRequest: GroupRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/create`, groupRequest);
  }

  updateGroup(groupID: string, groupRequest: GroupRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${groupID}/update`, groupRequest);
  }

  deleteGroup(groupID: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${groupID}/delete`);
  }

  addStudentToGroup(groupID: string, student: string): Observable<void> {
    const params = new HttpParams().set('student', student);
    return this.http.put<void>(`${this.baseUrl}/${groupID}/addStudent`, null, { params });
  }

  removeStudentFromGroup(groupID: string, student: string): Observable<void> {
    const params = new HttpParams().set('student', student);
    return this.http.put<void>(`${this.baseUrl}/${groupID}/removeStudent`, null, { params });
  }
}
