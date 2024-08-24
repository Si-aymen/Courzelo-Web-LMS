import {Component, OnInit} from '@angular/core';
import {DayOfWeek, Period} from '../professor-availability-component/professor-availability-component.component';
import {ToastrService} from 'ngx-toastr';
import {TimetableService} from '../../../shared/services/timetable.service';
import {Professor} from '../../../shared/models/Professor';
import {ProfessorService} from '../../../shared/services/professor.service';
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
    professors: Professor[] = [];
    selectedProfessorId = '';
    selectedProfessor: Professor | null = null;
    timetable: Timetable[] = [];
    daysOfWeek: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
    periods: string[] = ['P1', 'P2', 'P3', 'P4'];
    newSlot: any = {
        dayOfWeek: '',
        period: '',
        courseName: '',
        professorName: ''
    };
    constructor(private timetableService: TimetableService, private toastr: ToastrService, private professorService: ProfessorService) { }

    ngOnInit(): void {
        this.loadProfessors();
    }

    generateTimetable(): void {
        const courseIds = ['course1', 'course2']; // Replace with actual course IDs

        // Step 1: Get professor IDs
        this.timetableService.getProfessorIds().subscribe(
            (professorIds: string[]) => {
                // Step 2: Get professor names by IDs
                this.timetableService.getProfessorNames(professorIds).subscribe(
                    (professors: Professor[]) => {
                        // Map professors to names
                        const professorNames = professors.map(prof => prof.name);

                        // Step 3: Generate the timetable with professor names
                        this.timetableService.generateTimetable(courseIds, professorNames).subscribe(
                            (data) => {
                                this.timetable = [data];
                                console.log('Generated Timetable: ', this.timetable);
                            },
                            (error) => {
                                console.error('Error generating timetable: ', error);
                            }
                        );
                    },
                    (error) => {
                        console.error('Error fetching professor names: ', error);
                    }
                );
            },
            (error) => {
                console.error('Error fetching professor IDs: ', error);
            }
        );
    }
    addTimetableSlot() {
        if (this.newSlot.dayOfWeek && this.newSlot.period && this.newSlot.courseName && this.newSlot.professorName) {
            this.timetable.push({...this.newSlot}); // Add a copy of the new slot to the timetable
            this.newSlot = { dayOfWeek: '', period: '', courseName: '', professorName: '' }; // Reset form
        }
    }
    loadProfessors(): void {
        this.professorService.getAllProfessorNames().subscribe(
            (data: Professor[]) => {
                this.professors = data;
            },
            error => {
                console.error('Error fetching professors:', error);
            }
        );
    }
    onProfessorChange(professorId: string): void {
        if (professorId) {
            this.professorService.getProfessorById(professorId).subscribe(
                (professor: Professor) => {
                    this.selectedProfessor = professor;
                },
                error => {
                    console.error('Error fetching professor details:', error);
                }
            );
        } else {
            this.selectedProfessor = null;
        }
    }
  /*getTimetable(): void {
    this.timetableService.getTimetable().subscribe(
        (data: Timetable[]) => {
          this.timetable = data;
        },
        error => {
          console.error('Error:', error);
        }
    );
  }*/
}


