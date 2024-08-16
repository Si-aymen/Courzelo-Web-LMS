import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TimeSlot} from '../../views/forms/professor-availability-component/professor-availability-component.component';

@Injectable({
  providedIn: 'root'
})
export class ProfessorService {

  private apiUrl = 'http://localhost:8081/professors';

  constructor(private http: HttpClient) {
  }
  getAllProfessors(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  getProfessorById(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  updateUnavailableTimeSlots(id: string, unavailableTimeSlots: TimeSlot[]): Observable<any> {
    const url = `${this.apiUrl}/${id}/unavailable-timeslots`;
    return this.http.put(url, unavailableTimeSlots, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }
}
