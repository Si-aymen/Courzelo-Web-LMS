import { InterviewService } from 'src/app/shared/services/admission/interview.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
declare var createGoogleEvent: any;


@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.scss']
})
export class ScheduleComponent implements OnInit {
  appointmentForm!: FormGroup;
  users:any[]=[];
  connectedUser:UserResponse;
  constructor(private fb: FormBuilder,
    private sessionStorageService: SessionStorageService,
    private interviewService:InterviewService,
    ) {}
  ngOnInit() {
    this.connectedUser = this.sessionStorageService.getUserFromSession();
     this.loadMem();
        this.appointmentForm = this.fb.group({
          appointmentTime: ['', [Validators.required, this.futureDateValidator]]
        });
  }

  futureDateValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const selectedDate = new Date(control.value);
    const currentDate = new Date();

    if (selectedDate <= currentDate) {
      return { futureDate: true };
    }
    return null;
  }
loadMem(){
  console.log("Fetching interview data for:", this.connectedUser.email);

  this.interviewService.getInterviewByUser(this.connectedUser.email).subscribe({
    next: (res) => {
      console.log("Response from service:", res);

      // Create a Set to store unique emails
      const uniqueEmails = new Set<string>();

      // Ensure response is an array and process each interview
      if (Array.isArray(res)) {
        res.forEach(interview => {
          // Check if interview object is valid
          if (interview && interview.interviewer && interview.interviewer.email) {
            uniqueEmails.add(interview.interviewer.email);
          } else {
            console.warn("Interviewer data is missing or invalid in interview:", interview);
          }

          // Check if interviewee is a valid array
          if (interview && Array.isArray(interview.interviewee)) {
            interview.interviewee.forEach(email => uniqueEmails.add(email));
          } else {
            console.warn("Interviewee data is missing or invalid in interview:", interview);
          }
        });

        // Convert Set to Array
        this.users = Array.from(uniqueEmails);
      } else {
        console.warn("Response is not an array.");
      }

      console.log("Unique users array after processing:", this.users);
    },
    error: (error) => {
      console.error("Error fetching interview data:", error);
    }
  });
}
  // Function to handle the button click event to schedule a meeting.
  scheduleMeeting() {
    let appointmentTime = new Date(this.appointmentForm.value.appointmentTime);
    // Convert the date to the desired format with a custom offset (e.g., -07:00)
    const startTime = appointmentTime.toISOString().slice(0, 18) + '-07:00';
    const endTime = this.getEndTime(appointmentTime);
    const eventDetails = {
      email: this.users,
      startTime: startTime,
      endTime: endTime,
    };
    console.info(eventDetails);
    //this.generateICSFile()
    createGoogleEvent(eventDetails);
  }

  getEndTime(appointmentTime: Date) {
    // Add one hour to the date
    appointmentTime.setHours(appointmentTime.getHours() + 1);
    const endTime = appointmentTime.toISOString().slice(0, 18) + '-07:00';
    return endTime;
  }

  generateICSFile() {
    const datetimeValue = this.appointmentForm.value.appointmentTime;
    const date = new Date(datetimeValue);
    const endTime = new Date(date);
    endTime.setHours(endTime.getHours() + 1);
    // Format dates to be in the proper format for the .ics file
    const formattedStartDate = date
      .toISOString()
      .replace(/-/g, '')
      .replace(/:/g, '')
      .slice(0, -5);
    const formattedEndDate = endTime
      .toISOString()
      .replace(/-/g, '')
      .replace(/:/g, '')
      .slice(0, -5);
    // Event details
    const eventName = 'Sample Event';
    const eventDescription = 'This is a sample event';
    const location = 'Sample Location';
    // Create the .ics content
    const icsContent = `BEGIN:VCALENDAR
  VERSION:2.0
  BEGIN:VEVENT
  DTSTAMP:${formattedStartDate}Z
  DTSTART:${formattedStartDate}Z
  DTEND:${formattedEndDate}Z
  SUMMARY:${eventName}
  DESCRIPTION:${eventDescription}
  LOCATION:${location}
  END:VEVENT
  END:VCALENDAR`;
    // Create a Blob containing the .ics content
    const blob = new Blob([icsContent], {
      type: 'text/calendar;charset=utf-8',
    });
    // Create a download link for the Blob
    const downloadLink = document.createElement('a');
    downloadLink.href = URL.createObjectURL(blob);
    downloadLink.download = 'event.ics';
    // Trigger the download
    downloadLink.click();
  }
}

