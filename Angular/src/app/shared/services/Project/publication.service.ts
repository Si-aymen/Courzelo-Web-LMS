import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Publication } from '../../models/Project/Publication';
import { Observable } from 'rxjs';
import {  Commment } from '../../models/Project/Commment';


const  apiUrl ='http://localhost:8080';
@Injectable({
  providedIn: 'root'
})
export class PublicationService {
  constructor(private http: HttpClient) { }

  createPublication(publication: Omit<Publication, 'id'>, projectId: string): Observable<Publication> {
    return this.http.post<Publication>(`${apiUrl}/publication/${projectId}`, publication);
  }
  getPublicationsByProjectId(projectId: string): Observable<Publication[]> {
    return this.http.get<Publication[]>(`${apiUrl}/publication/${projectId}`);
  }
  likePublication(id: string): Observable<Publication> {
    return this.http.post<Publication>(`${apiUrl}/${id}/like`, {});
  }

  dislikePublication(id: string): Observable<Publication> {
    return this.http.post<Publication>(`${apiUrl}/${id}/dislike`, {});
  }


  addComment(publicationId: string, Commment: Commment): Observable<Commment> {
    return this.http.post<Commment>(`${apiUrl}/${publicationId}/comments`, Commment);
  }

  getCommentsByPublicationId(publicationId: string): Observable<Commment[]> {
    return this.http.get<Commment[]>(`${apiUrl}/${publicationId}/comments`);
  }

}

    


