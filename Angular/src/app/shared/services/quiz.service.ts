import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Quiz} from '../models/Quiz';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {ToastrService} from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class QuizService {
  private apiUrl = 'http://localhost:8081/api/quizzes';
  quizzes: Quiz[] = [];
  constructor(private http: HttpClient, public toastr: ToastrService) { }

  createQuiz(quiz: Quiz): Observable<Quiz> {
    return this.http.post<Quiz>(`${this.apiUrl}/create`, quiz);
  }

  saveQuiz(quiz: Quiz): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/create`, quiz).pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('Error occurred:', error);
          return throwError(error);
        })
    );  }

  getQuizzesByTeacherId(teacherId: string): Observable<Quiz[]> {
    return this.http.get<Quiz[]>(`${this.apiUrl}/teacher/${teacherId}`);
  }

  getAllQuizzes(): Observable<Quiz[]> {
    return this.http.get<Quiz[]>(this.apiUrl);
  }

  getQuizById(id: string): Observable<Quiz> {
    return this.http.get<Quiz>(`${this.apiUrl}/${id}`);
  }

  updateQuiz(id: string, quiz: Quiz): Observable<Quiz> {
    return this.http.put<Quiz>(`${this.apiUrl}/${id}`, quiz);
  }

  deleteQuiz(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

 /* getQuizById(id: string): Observable<Quiz> {
    return this.http.get<Quiz>(`${this.apiUrl}/quizzes/${id}`);
  }*/

  submitQuiz(quizId: string, answers: any[]): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/quizzes/${quizId}/submit`, { answers });
  }

  calculateScore(id: string, answers: string[]) {
    return 0;
  }
}
