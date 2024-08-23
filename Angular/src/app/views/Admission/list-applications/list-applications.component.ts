import { AdmissionService } from 'src/app/shared/services/admission/admission.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { AdmissionREQ } from 'src/app/shared/models/admission/InterviewREQ';
import { ApplicationService } from 'src/app/shared/services/admission/application.service';
import { InterviewService } from 'src/app/shared/services/admission/interview.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-applications',
  templateUrl: './list-applications.component.html',
  styleUrls: ['./list-applications.component.scss']
})
export class ListApplicationsComponent implements OnInit {
  admissionID: string;
  list: any[] = []; // Initialize as an empty array
  Teachers: string[] = [];
  students: string[] = [];
  Status:any;
  distribution: { teacher: string, students: string[] }[] = []; // Initialize as an empty array

  constructor(
    private applicationService: ApplicationService,
    private activateRoute: ActivatedRoute,
    private interviewService:InterviewService,
    private admissionService:AdmissionService,
  ) {
    this.admissionID = this.activateRoute.snapshot.params.id;
  }

  ngOnInit(): void {
    this.getApplications();
    this.loadData();
    this.distributeStudentsToTeachers();
    console.log("distrubtion",this.distribution)
    this.isClosed();
  }
  loadData() {
    this.getTeachers();
    this.getStudents();
  }
  getApplications(): void {
    this.applicationService.getApplicationsByAdmission(this.admissionID).subscribe({
      next: (res) => {
        console.log("API Response:", res); // Log the entire response to inspect its structure
        
        this.list = res; 
  
        if (res && res.admission && res.admission.institution) {
          this.Teachers = res.admission.institution.teachers || []; 
        }
  
        // Ensure res.user exists and has the email property
        if (res && res.user) {
          console.log("User object:", res.user); // Log user object
          if (res.user.email) {
            this.students.push(res.user.email);
          } else {
            console.warn("Email property is missing in user object");
          }
        } else {
          console.warn("User object is missing in response");
        }
  
        console.log("Students array:", this.students);
      },
      error: (err) => {
        console.error("Error fetching applications", err);
      }
    });
  }
  
  checkAndDistribute() {
    if (this.Teachers.length > 0 && this.students.length > 0) {
      this.distributeStudentsToTeachers();
    }
  }

  distributeStudentsToTeachers() {
    const numberOfTeachers = this.Teachers.length;
    const numberOfStudents = this.students.length;
    const studentsPerTeacher = Math.floor(numberOfStudents / numberOfTeachers);
    let remainingStudents = numberOfStudents % numberOfTeachers;

    let studentIndex = 0;
    console.log("Teachers", this.Teachers);

    this.Teachers.forEach((teacher, index) => {
      const numberOfStudentsForThisTeacher = studentsPerTeacher + (remainingStudents > 0 ? 1 : 0);
      this.distribution.push({
        teacher: teacher,
        students: this.students.slice(studentIndex, studentIndex + numberOfStudentsForThisTeacher)
      });
      studentIndex += numberOfStudentsForThisTeacher;
      remainingStudents--;
    });

    console.log("Distribution:", this.distribution);
  }
  getTeachers(): void {
    this.applicationService.getTeachers(this.admissionID).subscribe({
      next: (res) => {
        this.Teachers = res;
        console.log("Teachers array:", this.Teachers);
        this.checkAndDistribute();
      },
      error: (err) => {
        console.error("Error fetching teachers", err);
      }
    });
  }

  isClosed(){
    this.admissionService.getAllAdmissionById(this.admissionID).subscribe((res)=>{
      console.log("Status",res.status)
      this.Status=res.status;
    })
  }

  getStudents(): void {
    this.applicationService.getStudents(this.admissionID).subscribe({
      next: (res) => {
        this.students = res;
        console.log("Student array:", this.students);
        this.checkAndDistribute();
      },
      error: (err) => {
        console.error("Error fetching students", err);
      }
    });
  }

 // Method to create interview records based on distribution
 createInterviews() {
  Swal.fire({
    title: 'Are you sure?',
    text: "Do you want to create interviews for all selected applications?",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#007bff',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Yes, create them!',
    cancelButtonText: 'Cancel'
  }).then((result) => {
    if (result.isConfirmed) {
      // Show a loading spinner while processing
      Swal.fire({
        title: 'Processing...',
        text: 'Creating interviews, please wait.',
        icon: 'info',
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });

      // Process each entry to create interviews
      const interviewRequests = this.distribution.map(entry => {
        // Sanitize the interviewer field to remove any extra quotes
        const sanitizedInterviewer = entry.teacher ? entry.teacher.replace(/['"]+/g, '').trim() : 'N/A';

        const interview: AdmissionREQ = {
          id: this.generateUniqueId(), // Generate a unique ID for each interview
          interviewer: sanitizedInterviewer, // Use sanitized interviewer
          interviewee: entry.students || [], // Default to empty array if null
          admission: this.admissionID // Assuming you have an admission ID to associate
        };

        // Debugging: Log interview details before sending
        console.log("Creating interview with details:", interview);

        // Ensure entry fields are properly populated
        if (!interview.interviewer) {
          console.error("Missing interviewer for entry:", entry);
        }

        return this.interviewService.create(interview);
      });

      forkJoin(interviewRequests).subscribe({
        next: (responses) => {
          console.log('Interviews created successfully:', responses);
          
          Swal.fire({
            title: 'Success!',
            text: 'All interviews have been created successfully.',
            icon: 'success',
            confirmButtonText: 'OK'
          });
        },
        error: (err) => {
          console.error('Error creating interviews:', err);

          Swal.fire({
            title: 'Error!',
            text: 'There was an error creating some interviews. Please try again.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        },
        complete: () => {
          // Hide the loading spinner when the process is complete
          Swal.close();
        }
      });
    }
  });
}


// Utility method to generate unique ID
generateUniqueId() {
  return 'xxxxxx'.replace(/[x]/g, () => (Math.random() * 36 | 0).toString(36));
}

  
  
  
}
