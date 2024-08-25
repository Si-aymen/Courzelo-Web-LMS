import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { Project } from '../../models/Project/Project';
import { FileMetadata } from '../../models/Project/FileMetadata ';
import { catchError } from 'rxjs/operators';

const addproject = 'http://localhost:8080/addProject'; 
const getallprojecturl = 'http://localhost:8080/listofProjects'; 
const deleteprojecturl="http://localhost:8080/DeleteProject";
const Updateprojeturl="http://localhost:8080/UpdatelProject";
const getidprojecturl = "http://localhost:8080/getProjectbyid";
const uploadUrl = 'http://localhost:8080/projectId/upload';
const getpdfs = 'http://localhost:8080/project';
 const  getfileUrl = 'http://localhost:8080'
 const getgeneratgroupurl="http://localhost:8081/projects/assignStudentsToGroup";
@Injectable({
  providedIn: 'root'
})
export class ProjectService {


  constructor(private http: HttpClient) { }


  createProject(project: Project): Observable<Project> {
    return this.http.post<Project>(addproject, project);
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      console.error(`Backend returned code ${error.status}, ` + `body was: ${error.error}`);
    }
    return throwError('Something bad happened; please try again later.');
  }
  getAllproject(): Observable<Project[]> {
    return this.http.get<Project[]>(getallprojecturl); 
  }
  delete(id: any) {
    return this.http.delete(`${deleteprojecturl}/${id}`);
  }

  updateproject(project: Project): Observable<Project> {
    // Replace with your actual update endpoint
      return this.http.put<Project>(Updateprojeturl, project);
    }

    getProjectById(id: string): Observable<Project> {
      return this.http.get<Project>(`${getidprojecturl}/${id}`);
    }

    uploadFile(file: File, projectId: string): Observable<any> {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('projectId', projectId);
  
      return this.http.post(uploadUrl, formData);
    }

    upload(file: File, projectId: string): Observable<any> {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('projectId', projectId);
    
      const pdfUrl = `http://localhost:8080/project/${projectId}/upload`;
      return this.http.post(pdfUrl, formData);
    }
    

    getFilesByProjectId(projectId: string): Observable<FileMetadata[]> {
      return this.http.get<FileMetadata[]>( `${getpdfs}/${projectId}`);
    }


    getFile(fileName: string): Observable<Blob> {
      return this.http.get(`${getfileUrl}/${fileName}`, { responseType: 'blob' });
    }


    getTasksByProjectId(projectId: string): Observable<any> {
      return this.http.get(`${getfileUrl}/${projectId}/tasks`);
    }

    validateProject(projectId: string): Observable<Project> {
      return this.http.put<Project>(`${getfileUrl}/${projectId}/validate`, {});
    }
  
    checkProjectStatus(): Observable<void> {
      return this.http.put<void>(`${getfileUrl}/check-status`, {});
    }

    assignStudentsToGroup(projectId: any): Observable<string> {
      const params = new HttpParams().set('projectId', projectId);
      return this.http.post(`${getfileUrl}/projects/assignStudentsToGroup`, null, { 
        params: params,
        responseType: 'text' 
      });
    }
}
