import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Event } from '../../models/Project/Event';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private  apiUrl ='http://localhost:8080';

  constructor(private http: HttpClient) { }

  

  getEventsByProjectId(projectId: string): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/projectcalendar/${projectId}`);
  }
  getEventById(id: string): Observable<Event> {
    return this.http.get<Event>(`${this.apiUrl}/${id}`);
  }

  createEvent(projectId: string, event: Event): Observable<Event> {
    console.log(`Creating event for project ${projectId}:`, event);
    return this.http.post<Event>(`${this.apiUrl}/projectcalendar/${projectId}/add`, event)
      .pipe(
        tap((createdEvent) => console.log('Created event:', createdEvent)),
        catchError(error => {
          console.error('Error creating event:', error);
          return throwError(error);
        })
      );
  }
  updateEvent(id: string, event: Event): Observable<Event> {
    console.log(`Updating event with ID ${id}:`, event);
    return this.http.put<Event>(`${this.apiUrl}/updateevent/${id}`, event)
      .pipe(
        tap((updatedEvent) => console.log('Updated event:', updatedEvent)),
        catchError(error => {
          console.error('Error updating event:', error);
          return throwError(error);
        })
      );
  }

  deleteEvent(id: string): Observable<void> {
    const url = `${this.apiUrl}/deleteevent/${id}`;
    console.log('Deleting event at URL:', url); 
    return this.http.delete<void>(url);
  }
}

