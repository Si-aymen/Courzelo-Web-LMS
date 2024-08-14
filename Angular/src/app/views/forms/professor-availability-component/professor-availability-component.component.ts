import {Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {ProfessorService} from '../../../shared/services/professor.service';



@Component({
  selector: 'app-professor-availability-component',
  templateUrl: './professor-availability-component.component.html',
  styleUrls: ['./professor-availability-component.component.scss']
})
export class ProfessorAvailabilityComponentComponent implements OnInit {
  professorId: string;
  unavailableTimeSlots: TimeSlot[] = [];
  timeSlots: TimeSlot[] = [
    { dayOfWeek: 'MONDAY', period: 'P1' },
    { dayOfWeek: 'MONDAY', period: 'P2' },
    { dayOfWeek: 'TUESDAY', period: 'P1' },
    { dayOfWeek: 'TUESDAY', period: 'P2' },
    // ... add more time slots as needed
  ];

  constructor(private route: ActivatedRoute, private professorService: ProfessorService) { }

  ngOnInit(): void {
    this.professorId = this.route.snapshot.paramMap.get('id');
    this.professorService.getProfessorById(this.professorId).subscribe(professor => {
      this.unavailableTimeSlots = professor.unavailableTimeSlots;
    });
  }

  updateUnavailableTimeSlots(): void {
    this.professorService.updateUnavailableTimeSlots(this.professorId, this.unavailableTimeSlots).subscribe(() => {
      alert('Unavailable time slots updated successfully');
    });
  }

}
