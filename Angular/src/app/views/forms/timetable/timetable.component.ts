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
    professorIds: string[] = [];
    newSlot: any = {
        dayOfWeek: '',
        period: '',
        courseName: '',
        professorName: ''
    };
    constructor(private timetableService: TimetableService, private toastr: ToastrService, private professorService: ProfessorService) { }

    ngOnInit(): void {
        this.fetchProfessorNamesByIds();
    }

    generateTimetable(): void {
        const courseIds = ['course1', 'course2'];

        // Fetch professor IDs
        this.professorService.getProfessorIds().subscribe(
            (professorIds: string[]) => {
                if (professorIds.length === 0) {
                    this.toastr.warning('No professor IDs found.');
                    return;
                }
                this.professorService.getAllProfessorNames(professorIds).subscribe(
                    (professors: Professor[]) => {
                        if (professors.length === 0) {
                            this.toastr.warning('No professor names found.');
                            return;
                        }

                        const professorNames = professors.map(prof => prof.name);

                        // Generate the timetable with professor names
                        this.timetableService.generateTimetable(courseIds, professorNames).subscribe(
                            (data) => {
                                this.timetable = [data];
                                console.log('Generated Timetable: ', this.timetable);
                            },
                            (error) => {
                                console.error('Error generating timetable: ', error);
                                this.toastr.error('Failed to generate timetable.');
                            }
                        );
                    },
                    (error) => {
                        console.error('Error fetching professor names: ', error);
                        this.toastr.error('Failed to fetch professor names.');
                    }
                );
            },
            (error) => {
                console.error('Error fetching professor IDs: ', error);
                this.toastr.error('Failed to fetch professor IDs.');
            }
        );
    }
    addTimetableSlot() {
        if (this.newSlot.dayOfWeek && this.newSlot.period && this.newSlot.courseName && this.newSlot.professorName) {
            this.timetable.push({...this.newSlot}); // Add a copy of the new slot to the timetable
            this.newSlot = { dayOfWeek: '', period: '', courseName: '', professorName: '' }; // Reset form
        }
    }
    fetchProfessorNamesByIds(): void {
        this.professorService.getAllProfessorNames(this.professorIds).subscribe(
            (professors: Professor[]) => {
                this.professors = professors;
                console.log('Fetched Professor Names: ', this.professors);
            },
            (error) => {
                console.error('An error occurred while fetching professor names:', error);
                this.toastr.error('Failed to load professor names');
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


