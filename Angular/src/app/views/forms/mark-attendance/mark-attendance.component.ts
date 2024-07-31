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
    {id: 'student1', name: 'John Doe', minutesLate: 0},
    {id: 'student2', name: 'Jane Smith', minutesLate: 0},
    {id: 'student3', name: 'Alice Johnson', minutesLate: 0}
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
      minutesLate: ['']
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

  markAttendance(studentId: string, status: string, minutesLate: string) {
    const student = this.students.find(s => s.id === studentId);
    if (!student) {
      this.toastr.error('Invalid student ID', 'Error');
      return;
    }

    if (status === 'LATE') {
      if (!minutesLate || Number(minutesLate) > 15) {
        this.toastr.error('Attendance cannot be marked. The student is more than 15 minutes late.', 'Error');
        return;
      }
      student.minutesLate = Number(minutesLate);
    } else {
      student.minutesLate = 0;
    }
    const attendanceStatus = this.attendanceService.statusMap[status];
    if (!attendanceStatus) {
      this.toastr.error('Invalid attendance status', 'Error');
      return;
    }

    this.attendanceService.markAttendance(studentId, attendanceStatus, student.minutesLate).subscribe(response => {
      if (response) {
        this.toastr.success(`Attendance marked for ${response.studentName}`, 'Success');
        this.attendances.push({
          studentId: response.studentId,
          studentName: response.studentName,
          status: response.status,
          minutesLate: student.minutesLate // Use student.minutesLate instead of response.minutesLate
        });
      } else {
        this.toastr.error('Failed to mark attendance', 'Error');
      }
    });
  }
  markAllAttendance() {
    this.filteredStudents.forEach((student, index) => {
      setTimeout(() => {
        this.markAttendance(student.id, 'PRESENT', '0');
      }, index * 1000);
    });
  }
  exportToExcel() {
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.attendances);
    const workbook: XLSX.WorkBook = { Sheets: { 'attendance': worksheet }, SheetNames: ['attendance'] };
    XLSX.writeFile(workbook, 'attendance.xlsx');
  }
}
