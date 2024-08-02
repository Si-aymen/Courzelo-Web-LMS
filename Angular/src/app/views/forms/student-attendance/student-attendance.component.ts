import {Component, OnInit} from '@angular/core';
import { AttendanceDTO } from 'src/app/shared/models/AttendanceDTO';
import {AttendanceService} from '../../../shared/services/attendance.service';

@Component({
  selector: 'app-student-attendance',
  templateUrl: './student-attendance.component.html',
  styleUrls: ['./student-attendance.component.scss']
})
export class StudentAttendanceComponent implements OnInit {
  attendanceRecords: AttendanceDTO[] = [];
  filteredRecords: AttendanceDTO[] = [];
  studentId = 'student1'; // Current student ID
  searchDate = ''; // Date to search for

  // Pagination properties
  currentPage = 1;
  itemsPerPage = 5;
  totalPages = 0;

  constructor(private attendanceService: AttendanceService) {}

  ngOnInit(): void {
    this.loadAttendance();
  }

  loadAttendance(): void {
    this.attendanceService.getAttendanceByStudentId(this.studentId).subscribe(
        data => {
          this.attendanceRecords = data;
          this.filteredRecords = data;
          this.totalPages = Math.ceil(this.filteredRecords.length / this.itemsPerPage);
          this.updatePage();
        },
        error => {
          console.error('Error fetching attendance records:', error);
        }
    );
  }

  updatePage(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.filteredRecords = this.filteredRecords.slice(startIndex, endIndex);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.updatePage();
    }
  }

  onSearchDateChange(): void {
    if (this.searchDate) {
      const filtered = this.attendanceRecords.filter(record => record.date.startsWith(this.searchDate));
      this.filteredRecords = filtered;
      this.totalPages = Math.ceil(this.filteredRecords.length / this.itemsPerPage);
      this.currentPage = 1; // Reset to the first page
      this.updatePage();
    } else {
      this.filteredRecords = this.attendanceRecords;
      this.totalPages = Math.ceil(this.filteredRecords.length / this.itemsPerPage);
      this.updatePage();
    }
  }
}
