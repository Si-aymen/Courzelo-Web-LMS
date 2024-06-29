import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Quiz} from '../models/Quiz';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class QuizService {
  private apiUrl = 'http://localhost:8080/api/quizzes';

  constructor(private http: HttpClient) { }

  createQuiz(quiz: Quiz): Observable<Quiz> {
    return this.http.post<Quiz>(`${this.apiUrl}/create`, quiz);
  }

  getQuizzesByTeacherId(teacherId: string): Observable<Quiz[]> {
    return this.http.get<Quiz[]>(`${this.apiUrl}/teacher/${teacherId}`);
  }
}
