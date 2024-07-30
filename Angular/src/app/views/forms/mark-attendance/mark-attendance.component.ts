import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AttendanceDTO, AttendanceStatus} from '../../../shared/models/AttendanceDTO';
import {AttendanceService} from '../../../shared/services/attendance.service';

@Component({
  selector: 'app-mark-attendance',
  templateUrl: './mark-attendance.component.html',
  styleUrls: ['./mark-attendance.component.scss']
})
export class MarkAttendanceComponent implements OnInit {
  attendanceForm: FormGroup;
  students = [
    { id: 'student1', name: 'John Doe' },
    { id: 'student2', name: 'Jane Smith' },
    { id: 'student3', name: 'Alice Johnson' }
  ];
  statuses = Object.values(AttendanceStatus);
  filteredStudents = this.students;

  constructor(
      private fb: FormBuilder,
      private attendanceService: AttendanceService
  ) {
    this.attendanceForm = this.fb.group({
      className: ['']
    });
  }
  ngOnInit(): void {
    this.onClassNameChange();
  }
  onClassNameChange() {
    this.attendanceForm.get('className')?.valueChanges.subscribe(className => {
      if (className) {
        this.filterStudentsByClassName(className);
      }
    });
  }
  filterStudentsByClassName(className: string) {
    this.filteredStudents = this.students.filter(student => student.name.toLowerCase().includes(className.toLowerCase()));
  }

  markAttendance(studentId: string, status: AttendanceStatus) {
    this.attendanceService.markAttendance(studentId, status).subscribe(() => {
      console.log(`Attendance marked for studentId: ${studentId}, status: ${status}`);
    });
  }
}
