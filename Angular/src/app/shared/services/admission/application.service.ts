import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private baseURL = '/tk/api/applications';

  constructor(private http:HttpClient) { }

  create(application: any): Observable<any> {
    return this.http.post(`${this.baseURL}`, application);
  }
  
  getApplicationsByAdmission(admission: string): Observable<any> {
    return this.http.get(`${this.baseURL}/admission/${admission}`);
  }

  getApplicationsByUser(user: string): Observable<any> {
    return this.http.get(`${this.baseURL}/admission/${user}`);
  }

  getTeachers(id: string): Observable<any> {
    return this.http.get(`${this.baseURL}/teachers/${id}`);
  }

  getStudents(id: string): Observable<any> {
    return this.http.get(`${this.baseURL}/students/${id}`);
  }

  getApps(admission :string,id: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseURL}/user/${id}/${admission}`);
  }
  
  Note(id: any, note: any) {
    const body = { note };
    return this.http.post(`${this.baseURL}/note/${id}`,body);
  }
  
}
