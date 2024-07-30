import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AttendanceDTO, AttendanceStatus} from '../../../shared/models/AttendanceDTO';
import {AttendanceService} from '../../../shared/services/attendance.service';
import {ToastrService} from 'ngx-toastr';
import * as XLSX from 'xlsx';


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
  attendances: { studentId: string, studentName: string, status: string, minutesLate?: number }[] = [];

  constructor(
      private fb: FormBuilder,
      private attendanceService: AttendanceService,
      private toastr: ToastrService
  ) {
    this.attendanceForm = this.fb.group({
      className: [''],
      status: [''],
      minutesLate: [''] // Add minutes late field
    });
  }

  ngOnInit(): void {
    this.onClassNameChange();
  }

  onClassNameChange() {
    this.attendanceForm.get('className')?.valueChanges.subscribe(className => {
      if (className) {
        this.filterStudentsByClassName(className);
        this.filterStudentsByClassName(className);
      }
    });
  }

  filterStudentsByClassName(className: string) {
    this.filteredStudents = this.students.filter(student => student.name.toLowerCase().includes(className.toLowerCase()));
  }

  markAttendance(studentId: string, status: AttendanceStatus) {
    this.attendanceService.markAttendance(studentId, status).subscribe(response => {
      if (response) {
        this.toastr.success(`Attendance marked for ${response.studentName}`, 'Success');
        this.attendances.push({ studentId: response.studentId, studentName: response.studentName, status: response.status });
        console.log(this.attendances); // Log the attendances array to verify
      } else {
        this.toastr.error('Failed to mark attendance', 'Error');
      }
    });
  }

  markAllAttendance(status: AttendanceStatus) {
    this.filteredStudents.forEach((student, index) => {
      setTimeout(() => {
        this.markAttendance(student.id, status);
      }, index * 1000); // 1 second interval between each marking
    });
  }

  exportToExcel() {
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.attendances);
    const workbook: XLSX.WorkBook = { Sheets: { 'attendance': worksheet }, SheetNames: ['attendance'] };
    XLSX.writeFile(workbook, 'attendance.xlsx');
  }
}

