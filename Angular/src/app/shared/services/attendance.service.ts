import { Injectable } from '@angular/core';
import {catchError, tap} from 'rxjs/operators';
import {HttpClient, HttpParams} from '@angular/common/http';
import {AttendanceDTO, AttendanceStatus} from '../models/AttendanceDTO';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private apiUrl = 'http://localhost:8081/api/attendance';

  constructor(private http: HttpClient) { }

  markAttendance(studentId: string, status: AttendanceStatus): Observable<AttendanceDTO> {
    const params = new HttpParams()
        .set('studentId', studentId)
        .set('status', status);

    console.log(`Marking attendance for studentId: ${studentId}, status: ${status}`);

    return this.http.post<AttendanceDTO>(`${this.apiUrl}/mark`, {}, { params }).pipe(
        tap(response => {
          console.log('Attendance marked successfully:', response);
        }),
        catchError(error => {
          console.error('Error marking attendance:', error);
          return of(null);
        })
    );
  }

  getAttendanceByDate(date: string): Observable<AttendanceDTO[]> {
    return this.http.get<AttendanceDTO[]>(`${this.apiUrl}/date/${date}`);
  }
}
