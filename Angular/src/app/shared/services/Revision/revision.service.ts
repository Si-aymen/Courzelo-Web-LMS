import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Utils } from '../../utils';
import { Revision } from '../../models/Revision/Revision';
import { Observable, throwError } from 'rxjs';
import { FileMetadatarevision } from '../../models/Revision/FileMetadatarevision';
import { catchError } from 'rxjs/operators';
import { QuestionRevision } from '../../models/Revision/QuizzRevisison/QuestionRevision';
import { QuizRevision } from '../../models/Revision/QuizzRevisison/QuizRevision ';
import { AnswerRevision } from '../../models/Revision/QuizzRevisison/AnswerRevision';
const uploadUrl = 'http://localhost:8080/consultrevision/revisionId/uploads';
const getpdfs = 'http://localhost:8080/consultrevision';
const getpdf = 'http://localhost:8080/participaterevision';
const  apiUrl ='http://localhost:8080';
@Injectable({
  providedIn: 'root'
})
export class RevisionService {

  constructor(private http: HttpClient ) { }

  getAllRevisions(): Observable<Revision[]> {
    return this.http.get<Revision[]>(`${apiUrl}/revision`);
  }
  createRevision(revision: Omit<Revision, 'id'>): Observable<Revision> {
    return this.http.post<Revision>(`${apiUrl}/revision`, revision);
  }

  getRevisionById(id: string): Observable<Revision> {
    return this.http.get<Revision>(`${apiUrl}/revision/${id}`);
  }

  updateRevision(id: string, revision: Revision): Observable<Revision> {
    return this.http.put<Revision>(`${apiUrl}/revision/${id}`, revision);
  }

  delete(id: any) {
    return this.http.delete(`${apiUrl}/${id}`);
  }

  ////////////file section /////////
  uploadFile(file: File, revisionId: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('revisionId', revisionId);

    return this.http.post(uploadUrl, formData);
  }

  upload(file: File, revisionId: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('revisionId', revisionId);
  
    const pdfUrl = `http://localhost:8080/consultrevision/${revisionId}/uploads`;
    return this.http.post(pdfUrl, formData);
  }
  
 

  getFilesByProjectId(revisionId: string): Observable<FileMetadatarevision[]> {
    return this.http.get<FileMetadatarevision[]>( `${getpdfs}/${revisionId}`);
  }
 
  getFileByProjectId(revisionId: string): Observable<FileMetadatarevision[]> {
    return this.http.get<FileMetadatarevision[]>( `${getpdf}/${revisionId}`);
  }
  getFile(fileName: string): Observable<Blob> {
    return this.http.get(`${apiUrl}/files/${fileName}`, { responseType: 'blob' });
  }
///////////// client revision ///////////

generateQuestions(id: string): Observable<void> {
  const url = `${apiUrl}/generate-questions/${id}`;
  return this.http.post<void>(url, null).pipe(
    catchError(this.handleError)
  );
}

// Error handling
private handleError(error: HttpErrorResponse) {
  if (error.error instanceof ErrorEvent) {
    // A client-side or network error occurred.
    console.error('An error occurred:', error.error.message);
  } else {
    // The backend returned an unsuccessful response code.
    console.error(
      `Backend returned code ${error.status}, ` +
      `body was: ${error.error}`);
  }
  // Return an observable with a user-facing error message.
  return throwError('Something bad happened; please try again later.');
}


///////////// client quiz ///////////
getQuizByRevisionId(revisionId: string): Observable<QuizRevision[]> {
  return this.http.get<QuizRevision[]>(`${apiUrl}/participaterevision/${revisionId}/quizz`);
}

getQuestionsByQuizRevisionId(quizRevisionId: string): Observable<QuestionRevision[]> {
  return this.http.get<QuestionRevision[]>(`${apiUrl}/quizrevision/${quizRevisionId}/questions`);
}


submitAnswer(answerSubmission: any): Observable<boolean> {
  const params = new HttpParams()
    .set('questionId', answerSubmission.questionId)
    .set('userAnswerText', answerSubmission.userAnswerText);

  return this.http.post<boolean>(`${apiUrl}/submitAnswer`, null, { params });
}

}
  
 










 


