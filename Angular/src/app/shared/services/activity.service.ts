import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  private apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient
  ) { }

  getActivities(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activities/retrieve-activities`);
  }

  addActivity(activity: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/activities`, activity);
  }

  updateActivity(activity: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/activities/${activity.id}`, activity);
  }

  deleteActivity(activityId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/activities/${activityId}`);
  }
}
