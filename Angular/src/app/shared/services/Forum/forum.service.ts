import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubForum } from '../../models/Forum/SubForum';
import { Observable } from 'rxjs';
import { SubForumREQ } from '../../models/Forum/SubForumREQ';

@Injectable({
  providedIn: 'root'
})
export class ForumService {
  private baseURL1 ='/tk/v1/subforum';

  constructor(private http:HttpClient) {}
  getSubForums(): Observable<SubForum[]> {
    return this.http.get<SubForum[]>('/tk/v1/subforum/all');
  }

  addSubForum(SubForum: SubForumREQ): Observable<Object>{
    return this.http.post('/tk/v1/subforum/add', SubForum);
  }
}
