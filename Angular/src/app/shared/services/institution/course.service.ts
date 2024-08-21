import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {CourseRequest} from '../../models/institution/CourseRequest';
import {CourseResponse} from '../../models/institution/CourseResponse';
import {CoursePostRequest} from '../../models/institution/CoursePostRequest';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private baseUrl = 'http://localhost:8080/api/v1/course';

  constructor(private http: HttpClient) { }

  addCourse(institutionID: string, courseRequest: CourseRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${institutionID}/add`, courseRequest);
  }

  updateCourse(courseID: string, courseRequest: CourseRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${courseID}/update`, courseRequest);
  }

  deleteCourse(courseID: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${courseID}/delete`);
  }

  getCourse(courseID: string): Observable<CourseResponse> {
    return this.http.get<CourseResponse>(`${this.baseUrl}/${courseID}`);
  }
  downloadFile(courseID: string, fileName: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${courseID}/${fileName}/download`, { responseType: 'blob' });
  }
  setTeacher(courseID: string, email: string): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${courseID}/setTeacher`, null, { params: { email } });
  }

  addStudent(courseID: string, email: string): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${courseID}/addStudent`, null, { params: { email } });
  }

  removeStudent(courseID: string, email: string): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${courseID}/removeStudent`, null, { params: { email } });
  }

  leaveCourse(courseID: string): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${courseID}/leave`, null);
  }

  addPost(courseID: string, coursePostRequest: CoursePostRequest, files: File[]): Observable<void> {
    const formData: FormData = new FormData();
    // Append the coursePostRequest fields to the form data
    formData.append('title', coursePostRequest.title);
    formData.append('description', coursePostRequest.description);
    // Append each file to the form data
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i], files[i].name);
    }

    return this.http.put<void>(`${this.baseUrl}/${courseID}/addPost`, formData);
  }


  deletePost(courseID: string, postID: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${courseID}/deletePost`, { params: { postID } });
  }
}
