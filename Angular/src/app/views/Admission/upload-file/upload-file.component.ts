import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.scss']
})
export class UploadFileComponent {

  extractedText: string = ''; // Declare the extractedText property

  constructor(private http: HttpClient) {}

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      this.http.post('/tk/v1/pdf/extract', formData, { responseType: 'text' })
        .subscribe(response => {
          this.extractedText = response; // Store the response in the extractedText property
        });
    }
  }
}
