import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Interview } from '../../models/admission/Interview';

@Injectable({
  providedIn: 'root'
})
export class InterviewService {

  private baseURL = '/tk/api/interview';

constructor(private http: HttpClient) { }

create(interview: any): Observable<any> {
  return this.http.post(`${this.baseURL}/add`, interview);
}

getInterviewByUser(user: string): Observable<Interview[]> {
  return this.http.get<Interview[]>(`${this.baseURL}/user/${user}`);
}

getAllAdmissions(): Observable<any> {
  return this.http.get(`${this.baseURL}/all`);
}
}
