import {Component, OnInit} from '@angular/core';
import {DayOfWeek, Period} from '../professor-availability-component/professor-availability-component.component';
import {ToastrService} from 'ngx-toastr';
import {TimetableService} from '../../../shared/services/timetable.service';
export interface Timetable {
  dayOfWeek: DayOfWeek;
  period: Period;
  courseName: string;
  professorName: string;
}
@Component({
  selector: 'app-timetable',
  templateUrl: './timetable.component.html',
  styleUrls: ['./timetable.component.scss']
})
export class TimetableComponent implements OnInit {
  timetable: Timetable[] = [];
  daysOfWeek: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
  periods: string[] = ['P1', 'P2', 'P3', 'P4'];
  newSlot: any = {
        dayOfWeek: '',
        period: '',
        courseName: '',
        professorName: ''
    };

  constructor(private timetableService: TimetableService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.getTimetable();
  }

  generateTimetable(): void {
    this.timetableService.generateTimetable().subscribe(
        (data: Timetable[]) => {
          this.timetable = data;
          this.toastr.success('Timetable generated successfully!');
        },
        error => {
          this.toastr.error('Failed to generate timetable.');
          console.error('Error:', error);
        }
    );
  }


    // Handle form submission
    addTimetableSlot() {
        if (this.newSlot.dayOfWeek && this.newSlot.period && this.newSlot.courseName && this.newSlot.professorName) {
            this.timetable.push({...this.newSlot}); // Add a copy of the new slot to the timetable
            this.newSlot = { dayOfWeek: '', period: '', courseName: '', professorName: '' }; // Reset form
        }
    }

  getTimetable(): void {
    this.timetableService.getTimetable().subscribe(
        (data: Timetable[]) => {
          this.timetable = data;
        },
        error => {
          console.error('Error:', error);
        }
    );
  }
}


