import { HttpClient, HttpErrorResponse, HttpEvent, HttpEventType } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';



@Component({
  selector: 'app-pdf',
  templateUrl: './pdf.component.html',
  styleUrls: ['./pdf.component.scss']
})
export class PdfComponent {
  selectedFile: File | null = null;
  // Hardcoded project ID for testing
  projectId: string = '';
  // URL pointing to the backend server
  private uploadUrl: string = 'http://localhost:8080/projectId/upload';

  constructor(private http: HttpClient) {}

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadFile(): void {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      formData.append('projectId', this.projectId); // Use hardcoded project ID

      this.http.post(this.uploadUrl, formData).subscribe(
        response => {
          console.log('File uploaded successfully', response);
        },
        error => {
          console.error('Error uploading file', error);
        }
      );
    } else {
      alert('Please select a file.');
    }
  }
}