import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Interview } from 'src/app/shared/models/admission/Interview';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { ApplicationService } from 'src/app/shared/services/admission/application.service';
import { InterviewService } from 'src/app/shared/services/admission/interview.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';

@Component({
  selector: 'app-note-application',
  templateUrl: './note-application.component.html',
  styleUrls: ['./note-application.component.scss']
})
export class NoteApplicationComponent implements OnInit {
  interviews: Interview[] = [];
  interviewees: string[] = []; 
  admissionID: any;
  resault: any[] = []; // Ensure this is an array
  noteForm: FormGroup;
  connectedUser: UserResponse;

  constructor(
    private applicationService: ApplicationService,
    private formBuilder: FormBuilder,
    private activatedRoute:ActivatedRoute,
    private interviewService: InterviewService,
    private sessionStorageService: SessionStorageService,
  ) {}

  ngOnInit(): void {
    this.connectedUser = this.sessionStorageService.getUserFromSession();
    console.log("Connected User:", this.connectedUser);
    this.LoadInterview();
    this.createForm();
  }

  createForm() {
    this.noteForm = this.formBuilder.group({
      note: [''],
      user: [''],
    });
  }

  LoadInterview(): void {
    this.interviewService.getInterviewByUser(this.connectedUser.email).subscribe({
      next: (res: Interview[]) => {
        console.log('Full Response:', res);
        this.interviews = res;

        // Collect all interviewees
        let allInterviewees: string[] = [];
        this.interviews.forEach(interview => {
          if (interview && interview.interviewee) {
            allInterviewees = allInterviewees.concat(interview.interviewee);
          }
        });

        // Remove duplicates by converting to a Set and then back to an array
        this.interviewees = Array.from(new Set(allInterviewees));
        console.log('Unique Interviewees:', this.interviewees);

        // After interviews are loaded, get the admission IDs
        this.getAdmissionIds();
        // Fetch applications for interviewees using the first admission ID
        this.fetchApplicationsForInterviewees();
      },
      error: (err) => {
        console.error('Error fetching interviews:', err);
      }
    });
  }

  getAdmissionIds(): void {
    let alladmissions: string[] = this.interviews.map(interview => interview.admission.id);
    this.admissionID = Array.from(new Set(alladmissions));
    console.log('Admission IDs:', this.admissionID);
  }

  fetchApplicationsForInterviewees(): void {
    if (this.admissionID && this.admissionID.length > 0) {
      const firstAdmissionID = this.admissionID[0];

      // Loop through each interviewee's email and use the first admission ID
      this.interviewees.forEach((email: string) => {
        this.getApplications(firstAdmissionID, email);
      });
    } else {
      console.error('No admission IDs available.');
    }
  }

  getApplications(admission: any, email: string): void {
    this.applicationService.getApps(admission, email).subscribe((res) => {
      // Append the new results to the existing resault array
      this.resault = this.resault.concat(res);
      console.log(`Applications for ${email} in admission ${admission}:`, this.resault);
    });
  }
  

  save(applicationId: any) {
    const inputElement = document.getElementById(`note${applicationId}`) as HTMLInputElement;
    const noteValue = inputElement ? parseFloat(inputElement.value) : null;

    if (noteValue !== null && noteValue >= 0 && noteValue <= 20) {
      console.log('Saved note:', noteValue, 'for application ID:', applicationId);
      
      // Call the updateNote method from your service
      this.applicationService.Note(applicationId.toString(), noteValue).subscribe(
        (res) => {
          console.log('Response from backend:', res);
          this.ngOnInit();
          // Handle the successful response here
        },
        (error) => {
          console.error('Error updating note:', error);
          // Handle the error here
        }
      );
    } else {
      console.log('Invalid note value');
    }
  }

  isNoted(){

  }
  cancel(applicationId: number) {
    console.log('Cancelled for application ID:', applicationId);
    // Handle the cancel logic here
  }
}
