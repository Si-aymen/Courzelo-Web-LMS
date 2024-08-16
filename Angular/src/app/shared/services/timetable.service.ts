import { Injectable } from '@angular/core';
import {Timetable} from '../../views/forms/timetable/timetable.component';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TimetableService {

  private baseUrl = 'http://localhost:8081/api/timetable';

  constructor(private http: HttpClient) { }

  generateTimetable(): Observable<Timetable[]> {
    return this.http.get<Timetable[]>(`${this.baseUrl}/generate`);
  }

  getTimetable(): Observable<Timetable[]> {
    return this.http.get<Timetable[]>(`${this.baseUrl}`);
  }
}
