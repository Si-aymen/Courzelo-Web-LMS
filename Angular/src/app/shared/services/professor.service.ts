import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TimeSlot} from '../../views/forms/professor-availability-component/professor-availability-component.component';
import {Professor} from '../models/Professor';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProfessorService {

  private apiUrl = 'http://localhost:8080/professors';

  constructor(private http: HttpClient) {
  }
  getAllProfessors(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  getProfessorById(professorId: string): Observable<Professor> {
    return this.http.get<Professor>(`${this.apiUrl}/${professorId}`);
  }
  getAllProfessorNames(professorIds: string[]): Observable<Professor[]> {
    return this.http.post<Professor[]>(`/api/professors/names1`, professorIds)
        .pipe(
            tap(professors => {
              if (professors.length === 0) {
                console.warn('No professors found for the given IDs');
              }
            })
        );
  }
  getProfessorIds(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}`);
  }
  updateUnavailableTimeSlots(professorId: string, timeSlots: TimeSlot[]): Observable<void> {
    const url = `${this.apiUrl}/${professorId}/unavailable-timeslots`;
    return this.http.put<void>(url, timeSlots);
  }
}
