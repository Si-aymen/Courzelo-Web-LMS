import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RatingserviceService {

  constructor(private http:HttpClient) { }
  private apiUrl = '/tki/v1/ratings/tickets'; 

  addRating(ticketId: string, rating: any):any{
    const url = `${this.apiUrl}/${ticketId}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/${ticketId}`,rating,{headers});
    } 
}
