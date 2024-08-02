import {Component, OnInit} from '@angular/core';
import * as XLSX from 'xlsx';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {AttendanceService} from '../../../shared/services/attendance.service';
import {AttendanceDTO} from '../../../shared/models/AttendanceDTO';
@Component({
  selector: 'app-attendance-report',
  templateUrl: './attendance-report.component.html',
  styleUrls: ['./attendance-report.component.scss']
})
export class AttendanceReportComponent implements OnInit {
  reportForm: FormGroup;
  reportData: AttendanceDTO[] = [];
  reportGenerated = false;

  constructor(
      private fb: FormBuilder,
      private attendanceService: AttendanceService,
      private toastr: ToastrService
  ) {
    this.reportForm = this.fb.group({
      studentName: [''],
      startDate: [''],
      endDate: ['']
    });
  }

  ngOnInit(): void {}

  generateReport() {
    const { studentName, startDate, endDate } = this.reportForm.value;
    console.log('Form values:', { studentName, startDate, endDate }); // Debugging statement

    this.attendanceService.getAttendanceReport(studentName, startDate, endDate).subscribe(
        data => {
          console.log('Report data:', data); // Debugging statement
          this.reportData = data;
          this.reportGenerated = true;
          this.toastr.success('Report generated successfully', 'Success');
        },
        error => {
          console.error('Error generating report:', error);
          this.toastr.error('Failed to generate report', 'Error');
        }
    );
  }

  exportReportToExcel() {
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.reportData);
    const workbook: XLSX.WorkBook = { Sheets: { 'attendance': worksheet }, SheetNames: ['attendance'] };
    XLSX.writeFile(workbook, 'attendance_report.xlsx');
  }

}
