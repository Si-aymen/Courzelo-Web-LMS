import { Injectable } from '@angular/core';
import {Timetable} from '../../views/forms/timetable/timetable.component';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Professor} from '../models/Professor';

@Injectable({
  providedIn: 'root'
})
export class TimetableService {

  private baseUrl = 'http://localhost:8080/api/timetable';

  constructor(private http: HttpClient) { }

  generateTimetable(courseIds: string[], professorIds: string[]): Observable<Timetable> {
    const courseIdsParam = courseIds.join(',');
    const professorIdsParam = professorIds.join(',');
    return this.http.get<Timetable>(`${this.baseUrl}/generate?courseIds=${courseIdsParam}&professorIds=${professorIdsParam}`);
  }

  getTimetables(): Observable<Timetable[]> {
    return this.http.get<Timetable[]>(this.baseUrl);
  }

  getProfessorIds(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/professors/ids`);
  }

  // Fetch professor names by IDs


  getProfessorById(professorId: string) {
    return this.http.get<Professor>(`${this.baseUrl}/professor/${professorId}`);
  }
}
