import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProfessorService {

  private apiUrl = '/api/professors';

  constructor(private http: HttpClient) { }

  getProfessorById(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  updateUnavailableTimeSlots(id: string, unavailableTimeSlots: string[]): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/unavailable-timeslots`, { unavailableTimeSlots });
  }
}
