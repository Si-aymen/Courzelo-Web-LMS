import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Post } from '../../models/Forum/Post';
import { Observable } from 'rxjs';
import { PostREQ } from '../../models/Forum/PostREQ';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private baseURL1 ='/tk/v1/post';
  constructor(private http:HttpClient) {}

  getPostsByForum(id:any): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseURL1}/all/${id}`);
  }

  getAllPosts(): Observable<Post[]> {
    return this.http.get<Post[]>('/tk/v1/post/getall');
  }
  getPostById(id: string): Observable<Post>{
    return this.http.get<Post>(`${this.baseURL1}/get/${id}`);
  }
  addpost(SubForum: PostREQ): Observable<Object>{
    return this.http.post('/tk/v1/post/add', SubForum);
  }
  updatePost(id:any,post:PostREQ):Observable<any>{
    return this.http.put(`${this.baseURL1}/update1/${id}`,post);
  }

  upvotePost(postId: string, userId: string): Observable<any> {
    // Use HttpParams to append the userId as a query parameter
    const params = new HttpParams().set('userId', userId);
    
    return this.http.post(`${this.baseURL1}/${postId}/upvote`, null, { params });
  }
  
  // Downvote a comment
  downvotePost(postId: string, userId: string): Observable<any> {
    // Use HttpParams to append the userId as a query parameter
    const params = new HttpParams().set('userId', userId);
    
    return this.http.post(`${this.baseURL1}/${postId}/downvote`, null, { params });
  }

  // Remove upvote (optional)
  removeUpvote(postId: string, userId: string): Observable<any> {
    return this.http.post(`${this.baseURL1}/${postId}/remove-upvote`, { userId });
  }

  // Remove downvote (optional)
  removeDownvote(postId: string, userId: string): Observable<any> {
    return this.http.post(`${this.baseURL1}/${postId}/remove-downvote`, { userId });
  }

  // Get the current upvotes and downvotes (optional)
  getPostVotes(postId: string): Observable<any> {
    return this.http.get(`${this.baseURL1}/${postId}/votes`);
  }
  isPostUpvoted(postId: string, userId: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseURL1}/${postId}/is-upvoted?userId=${userId}`);
  }
  isPostDownvoted(postId: string, userId: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseURL1}/${postId}/is-downvoted?userId=${userId}`);
  }

  PostUpvotedNumber(postId: string): Observable<any> {
    return this.http.get<any>(`${this.baseURL1}/${postId}/upvote-count`);
  }

  PostDownvotedNumber(postId: string): Observable<any> {
    return this.http.get<any>(`${this.baseURL1}/${postId}/downvote-count`);
  }
}
