import {Component, OnInit, ViewChild} from '@angular/core';
import { AttendanceDTO } from 'src/app/shared/models/AttendanceDTO';
import {AttendanceService} from '../../../shared/services/attendance.service';
import * as XLSX from 'xlsx';
@Component({
  selector: 'app-attendane-history',
  templateUrl: './attendane-history.component.html',
  styleUrls: ['./attendane-history.component.scss']
})
export class AttendaneHistoryComponent implements OnInit{
  studentId = 'student1';
  attendanceHistory: AttendanceDTO[] = [];
  paginatedHistory: AttendanceDTO[] = [];
  displayedColumns: string[] = ['date', 'studentName', 'status', 'minutesLate'];

  // Pagination
  pageSize = 10;
  pageIndex = 0;
  length = 0;
  pageSizeOptions: number[] = [5, 10, 20];

  constructor(private attendanceService: AttendanceService) { }

  ngOnInit(): void {
    this.loadAttendanceHistory();
  }

  loadAttendanceHistory() {
    this.attendanceService.getAttendanceHistory(this.studentId).subscribe(history => {
      this.attendanceHistory = history;
      this.length = history.length;
      this.paginateHistory();
    });
  }

  paginateHistory() {
    const startIndex = this.pageIndex * this.pageSize;
    this.paginatedHistory = this.attendanceHistory.slice(startIndex, startIndex + this.pageSize);
  }

  onPageChange(newPageIndex: number) {
    this.pageIndex = newPageIndex;
    this.paginateHistory();
  }

  onPageSizeChange(newPageSize: number) {
    this.pageSize = newPageSize;
    this.pageIndex = 0; // Reset to the first page
    this.paginateHistory();
  }

  exportToExcel() {
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.attendanceHistory);
    const workbook: XLSX.WorkBook = { Sheets: { 'attendance': worksheet }, SheetNames: ['attendance'] };
    XLSX.writeFile(workbook, 'attendance.xlsx');
  }

  protected readonly Math = Math;

}

