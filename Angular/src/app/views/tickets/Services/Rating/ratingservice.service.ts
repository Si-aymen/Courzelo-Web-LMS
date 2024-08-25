import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Rating } from 'src/app/shared/models/Rating';

@Injectable({
  providedIn: 'root'
})
export class RatingserviceService {

  constructor(private http:HttpClient) { }
  private apiUrl = '/tk/v1/ratings/'; 

  addRating(ticketId: string, rating: any):any{
    return this.http.post(`${this.apiUrl}/${ticketId}`,rating);
    } 
    updateRating(ratingId: string, rating: any): Observable<Rating> {
      const url = `${this.apiUrl}/${ratingId}`;
      return this.http.put<Rating>(url, rating);
    }
  
    // Get a rating by ID
    getRatingById(ratingId: string): Observable<Rating> {
      const url = `${this.apiUrl}/${ratingId}`;
      return this.http.get<Rating>(url);
    }
  
    // Get all ratings
    getRatings(): Observable<Rating[]> {
      const url = `${this.apiUrl}/all`;
      return this.http.get<Rating[]>(url);
    }
  
    // Delete a rating
    deleteRating(ratingId: string): Observable<void> {
      const url = `${this.apiUrl}/${ratingId}`;
      return this.http.delete<void>(url);
    }
}
