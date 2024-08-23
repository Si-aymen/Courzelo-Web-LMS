import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdmissionService {

  private baseURL = '/tk/admission';

constructor(private http: HttpClient) { }

create(admission: any): Observable<any> {
  return this.http.post(`${this.baseURL}/add`, admission);
}

getAdmissions(user: string): Observable<any> {
  return this.http.get(`${this.baseURL}/user/${user}`);
}

getAllAdmissions(): Observable<any> {
  return this.http.get(`${this.baseURL}/all`);
}

getAllAdmissionById(id :any): Observable<any> {
  return this.http.get(`${this.baseURL}/${id}`);
}
}
