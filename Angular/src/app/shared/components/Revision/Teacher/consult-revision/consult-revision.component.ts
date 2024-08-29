import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FileMetadatarevision } from 'src/app/shared/models/Revision/FileMetadatarevision';
import { RevisionService } from 'src/app/shared/services/Revision/revision.service';

@Component({
  selector: 'app-consult-revision',
  templateUrl: './consult-revision.component.html',
  styleUrls: ['./consult-revision.component.scss']
})
export class ConsultRevisionComponent implements OnInit {
  selectedFile: File | null = null;
  revisionId: string = '';
  uploadUrl: string = '';
  pdfSrc: string | ArrayBuffer | null = null;
  files: FileMetadatarevision[] = []; 

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private revisionService: RevisionService
  ) {}

  ngOnInit(): void {
    // Fetch the revisionId from the route parameters
    this.revisionId = this.route.snapshot.paramMap.get('id')!;
    // Construct the upload URL with the correct revisionId
    this.uploadUrl = `http://localhost:8080/consultrevision/${this.revisionId}/uploads`;

    // Load existing files for this revision
    this.loadFiles();
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadFile(): void {
    if (this.selectedFile && this.revisionId) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      formData.append('revisionId', this.revisionId);

      this.http.post(this.uploadUrl, formData).subscribe(
        response => {
          console.log('File uploaded successfully', response);
          this.loadFiles(); // Refresh the file list after successful upload
        },
        error => {
          console.error('Error uploading file', error);
        }
      );
    } else {
      alert('Please select a file.');
    }
  }

  openPdf(file: FileMetadatarevision): void {
    const fileName = file.fileName;

    this.revisionService.getFile(fileName).subscribe(response => {
      const url = window.URL.createObjectURL(response);
      this.pdfSrc = url;
      window.open(this.pdfSrc); // This opens the PDF in a new tab
    }, error => {
      console.error('Error fetching PDF file:', error);
    });
  }

  loadFiles(): void {
    this.revisionService.getFilesByProjectId(this.revisionId).subscribe(
      (files: FileMetadatarevision[]) => {
        this.files = files; // Update the files array
      },
      error => {
        console.error('Error loading files:', error);
      }
    );
  }
}