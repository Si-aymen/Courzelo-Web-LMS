import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {AttendanceService} from '../../../shared/services/attendance.service';

@Component({
  selector: 'app-attendance-settings',
  templateUrl: './attendance-settings.component.html',
  styleUrls: ['./attendance-settings.component.scss']
})
export class AttendanceSettingsComponent implements OnInit {
  settingsForm: FormGroup;

  constructor(
      private fb: FormBuilder,
      private toastr: ToastrService,
      private attendanceSettingsService: AttendanceService
  ) {
    this.settingsForm = this.fb.group({
      lateThreshold: [15, [Validators.required, Validators.min(1)]],
      absenceThreshold: [30, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.loadSettings();
  }

  loadSettings(): void {
    this.attendanceSettingsService.getSettings().subscribe(
        settings => {
          this.settingsForm.patchValue(settings);
        },
        error => {
          this.toastr.error('Error loading settings', 'Error');
        }
    );
  }

  onSubmit(): void {
    if (this.settingsForm.valid) {
      this.attendanceSettingsService.saveSettings(this.settingsForm.value).subscribe(
          () => {
            this.toastr.success('Settings saved successfully', 'Success');
          },
          error => {
            this.toastr.error('Error saving settings', 'Error');
          }
      );
    }
  }
}

