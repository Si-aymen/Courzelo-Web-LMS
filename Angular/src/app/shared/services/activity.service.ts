import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {

  constructor(
    private http: HttpClient
  ) { }

  getActivities(): Observable<any[]> {
    return this.http.get<any[]>('api/activities');
  }

  addActivity(activity: any): Observable<any> {
    return this.http.post<any>('api/activities', activity);
  }

  updateActivity(activity: any): Observable<any> {
    return this.http.put<any>(`api/activities/${activity.id}`, activity);
  }

  deleteActivity(activityId: number): Observable<any> {
    return this.http.delete<any>(`api/activities/${activityId}`);
  }
}
