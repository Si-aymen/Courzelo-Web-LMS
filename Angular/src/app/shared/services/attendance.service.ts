import { Injectable } from '@angular/core';
import {catchError, tap} from 'rxjs/operators';
import {HttpClient, HttpParams} from '@angular/common/http';
import {AttendanceDTO, AttendanceStatus} from '../models/AttendanceDTO';
import {Observable, of} from 'rxjs';
import {ToastrService} from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {
  statusMap: { [key: string]: AttendanceStatus } = {
    'PRESENT': AttendanceStatus.PRESENT,
    'ABSENT': AttendanceStatus.ABSENT,
    'LATE': AttendanceStatus.LATE,
    'EXCUSED': AttendanceStatus.EXCUSED,
    'UNEXCUSED_ABSENT': AttendanceStatus.UNEXCUSED_ABSENT
  };

  private apiUrl = 'http://localhost:8081/api/attendance';

  constructor(private http: HttpClient, private toastr: ToastrService) { }

  markAttendance(studentId: string, status: AttendanceStatus, minutesLate?: number): Observable<AttendanceDTO> {
    let params = new HttpParams()
        .set('studentId', studentId)
        .set('status', status);

    if (minutesLate !== undefined && minutesLate !== null) {
      params = params.set('minutesLate', minutesLate.toString());
    }

    console.log(`Marking attendance for studentId: ${studentId}, status: ${status}, minutesLate: ${minutesLate}`);

    return this.http.post<AttendanceDTO>(`${this.apiUrl}/mark`, {}, { params }).pipe(
        tap(response => {
          console.log('Attendance marked successfully:', response);
        }),
        catchError(error => {
          console.error('Error marking attendance:', error);
          this.toastr.error('Failed to mark attendance', 'Error');
          return of(null);
        })
    );
  }

  getAttendanceByDate(date: string): Observable<AttendanceDTO[]> {
    return this.http.get<AttendanceDTO[]>(`${this.apiUrl}/date/${date}`);
  }
  getAttendanceHistory(studentId: string): Observable<AttendanceDTO[]> {
    const params = new HttpParams().set('studentId', studentId);
    return this.http.get<AttendanceDTO[]>(`${this.apiUrl}/history`, { params });
  }
  getAttendanceReport(studentName: string, startDate: string, endDate: string): Observable<AttendanceDTO[]> {
    const params = new HttpParams()
        .set('studentName', studentName)
        .set('startDate', startDate)
        .set('endDate', endDate);

    return this.http.get<AttendanceDTO[]>(`${this.apiUrl}/report`, { params });
  }
}
