import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentREQ } from '../../models/Forum/CommentREQ';
import { Comment } from '../../models/Forum/Comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
private baseURL='/tk/v1/comment';
  constructor(private http:HttpClient) { }
  getCommentByPost(id:any): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.baseURL}/post/${id}`);
  }

  getCommentByid(id:any): Observable<Comment> {
    return this.http.get<Comment>(`${this.baseURL}/get/${id}`);
  }
  addcomment(SubForum: CommentREQ): Observable<Object>{
    return this.http.post('/tk/v1/comment/add', SubForum);
  }
  deleteComment(id:any):Observable<any>{
    return this.http.delete(`${this.baseURL}/delete/${id}`);
  }
  updateComment(id:any,comment:CommentREQ):Observable<any>{
    return this.http.put(`${this.baseURL}/update1/${id}`,comment);
  }
  upvoteComment(commentId: string, userId: string): Observable<any> {
    // Use HttpParams to append the userId as a query parameter
    const params = new HttpParams().set('userId', userId);
    
    return this.http.post(`${this.baseURL}/${commentId}/upvote`, null, { params });
  }
  
  // Downvote a comment
  downvoteComment(commentId: string, userId: string): Observable<any> {
    // Use HttpParams to append the userId as a query parameter
    const params = new HttpParams().set('userId', userId);
    
    return this.http.post(`${this.baseURL}/${commentId}/downvote`, null, { params });
  }

  // Remove upvote (optional)
  removeUpvote(commentId: string, userId: string): Observable<any> {
    return this.http.post(`${this.baseURL}/${commentId}/remove-upvote`, { userId });
  }

  // Remove downvote (optional)
  removeDownvote(commentId: string, userId: string): Observable<any> {
    return this.http.post(`${this.baseURL}/${commentId}/remove-downvote`, { userId });
  }

  // Get the current upvotes and downvotes (optional)
  getCommentVotes(commentId: string): Observable<any> {
    return this.http.get(`${this.baseURL}/${commentId}/votes`);
  }
  isCommentUpvoted(commentId: string, userId: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseURL}/${commentId}/is-upvoted?userId=${userId}`);
  }
  isCommentDownvoted(commentId: string, userId: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseURL}/${commentId}/is-downvoted?userId=${userId}`);
  }

  CommentUpvotedNumber(commentId: string): Observable<any> {
    return this.http.get<any>(`${this.baseURL}/${commentId}/upvote-count`);
  }

  CommentDownvotedNumber(commentId: string): Observable<any> {
    return this.http.get<any>(`${this.baseURL}/${commentId}/downvote-count`);
  }
}
