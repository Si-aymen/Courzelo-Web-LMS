import {Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {ProfessorService} from '../../../shared/services/professor.service';
import {ToastrService} from 'ngx-toastr';
import {Professor} from '../../../shared/models/Professor';


export interface TimeSlot {
  dayOfWeek: DayOfWeek;
  period: Period;
}
export enum DayOfWeek {
  MONDAY = 'MONDAY',
  TUESDAY = 'TUESDAY',
  WEDNESDAY = 'WEDNESDAY',
  THURSDAY = 'THURSDAY',
  FRIDAY = 'FRIDAY',
  SATURDAY = 'SATURDAY',
  SUNDAY = 'SUNDAY',
}
export enum Period {
  P1 = 'P1',
  P2 = 'P2',
  P3 = 'P3',
  P4 = 'P4',
}


@Component({
  selector: 'app-professor-availability-component',
  templateUrl: './professor-availability-component.component.html',
  styleUrls: ['./professor-availability-component.component.scss']
})
export class ProfessorAvailabilityComponentComponent implements OnInit {
  professorId: string | null = null; // Initialized to null
  unavailableTimeSlots: TimeSlot[] = [];
  dayOfWeekOptions = Object.values(DayOfWeek);
  periodOptions = Object.values(Period);
  selectedTimeSlot: TimeSlot = { dayOfWeek: DayOfWeek.MONDAY, period: Period.P1 };
  professors: Professor[] = [];

  constructor(
      private route: ActivatedRoute,
      private professorService: ProfessorService,
      private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.professorId = params.get('id');

      if (this.professorId) {
        this.loadProfessorData(this.professorId);
      } else {
        this.toastr.error('Professor ID is not available');
      }
    });
  }

  loadProfessorData(professorId: string): void {
    this.professorService.getProfessorById(professorId).subscribe(
        professor => {
          if (professor) {
            this.professors = [professor]; // Store the single professor in an array if needed
          } else {
            this.toastr.warning('Professor not found.');
          }
        },
        error => {
          console.error('An error occurred while fetching professor data:', error);
          this.toastr.error('Failed to load professor data');
        }
    );
  }


  addTimeSlot(): void {
    if (this.professorId && !this.isTimeSlotAvailable(this.selectedTimeSlot)) {
      this.toastr.warning('Time slot already selected as unavailable.');
      return;
    }
    this.unavailableTimeSlots.push({ ...this.selectedTimeSlot });
    this.toastr.success('Time slot added.');
  }

  removeTimeSlot(timeSlot: TimeSlot): void {
    this.unavailableTimeSlots = this.unavailableTimeSlots.filter(
        slot => !(slot.dayOfWeek === timeSlot.dayOfWeek && slot.period === timeSlot.period)
    );
    this.toastr.success('Time slot removed.');
  }

  updateUnavailableTimeSlots(): void {
    if (this.professorId) {
      this.professorService.updateUnavailableTimeSlots(this.professorId, this.unavailableTimeSlots).subscribe(
          () => {
            this.toastr.success('Unavailable time slots updated successfully');
          },
          error => {
            console.error('An error occurred while updating unavailable time slots:', error);
            this.toastr.error('Failed to update unavailable time slots');
          }
      );
    } else {
      console.error('Professor ID is null');
      this.toastr.error('Professor ID is not available');
    }
  }


  isTimeSlotAvailable(timeSlot: TimeSlot): boolean {
    return !this.unavailableTimeSlots.some(
        slot => slot.dayOfWeek === timeSlot.dayOfWeek && slot.period === timeSlot.period
    );
  }
}
