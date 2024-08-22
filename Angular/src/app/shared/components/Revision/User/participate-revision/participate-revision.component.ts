import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { FileMetadatarevision } from 'src/app/shared/models/Revision/FileMetadatarevision';
import { QuestionRevision } from 'src/app/shared/models/Revision/QuizzRevisison/QuestionRevision';
import { QuizRevision } from 'src/app/shared/models/Revision/QuizzRevisison/QuizRevision ';
import { RevisionService } from 'src/app/shared/services/Revision/revision.service';

@Component({
  selector: 'app-participate-revision',
  templateUrl: './participate-revision.component.html',
  styleUrls: ['./participate-revision.component.scss']
})
export class ParticipateRevisionComponent  implements OnInit {
  selectedFile: File | null = null;
  revisionId: string = '';
  uploadUrl: string = '';
  pdfSrc: string | ArrayBuffer | null = null;
  files: FileMetadatarevision[] = []; 
  quizzes: QuizRevision[] = [];

  constructor(private http: HttpClient,
     private route: ActivatedRoute,private revisionService: RevisionService,
   
     private router: Router) {}

  ngOnInit(): void {
    // Fetch the revisionId from the route parameters
    this.revisionId = this.route.snapshot.paramMap.get('id')!;
    // Construct the upload URL with the correct revisionId
    this.uploadUrl = `http://localhost:8080/consultrevision/${this.revisionId}/uploads`;

    // Load existing files for this revision
    this.loadQuizzes();
    this.loadFiles();
  }

  loadQuizzes(): void {
    this.revisionService.getQuizByRevisionId(this.revisionId).subscribe(
      (quizzes: QuizRevision[]) => {
        this.quizzes = quizzes; // Load the quiz data
      },
      (error) => {
        console.error('Error loading quizzes:', error);
      }
    );
  }


  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
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
    this.revisionService.getFileByProjectId(this.revisionId).subscribe(
      (files: FileMetadatarevision[]) => {
        this.files = files; // Assign the fetched files to the files array
      },
      error => {
        console.error('Error loading files:', error);
      }
    );
  }

  fetchFile(fileName: string): Observable<Blob> {
    return this.http.get(`http://localhost:8080/files/${fileName}`, { responseType: 'blob' });
  }
  generateQuestions(file: FileMetadatarevision): void {
    const id: string = file.id.toString();
    
    // Log the file ID to the console
    console.log(`Attempting to generate questions for file with ID: ${id}`);
    
    this.revisionService.generateQuestions(id).subscribe({
      next: () => {
        console.log(`Questions generated successfully for file with ID: ${id}`);
      },
      error: (error) => {
        console.error(`Error generating questions for file with ID: ${id}`, error);
        alert('Failed to generate questions: ' + error.message);
      }
    });
}
QandA(id: string): void {
  console.log('Navigating to ConsultRevisionComponent with ID:', id);
  this.router.navigate(['/QandA', id]);
}
}