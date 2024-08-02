import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BasicFormComponent } from './basic-form/basic-form.component';
import { TagInputsComponent } from './tag-inputs/tag-inputs.component';
import { AppImgCropperComponent } from './img-cropper/img-cropper.component';
import { WizardComponent } from './wizard/wizard.component';
import { InputMaskComponent } from './input-mask/input-mask.component';
import { InputGroupsComponent } from './input-groups/input-groups.component';
import { FormLayoutsComponent } from './form-layouts/form-layouts.component';
import {MarkAttendanceComponent} from './mark-attendance/mark-attendance.component';
import {AttendaneHistoryComponent} from './attendane-history/attendane-history.component';
import {AttendanceReportComponent} from './attendance-report/attendance-report.component';
import {StudentAttendanceComponent} from './student-attendance/student-attendance.component';

const routes: Routes = [
  {
    path: 'basic',
    component: BasicFormComponent
  },
  {
    path: 'layouts',
    component: FormLayoutsComponent
  },
  {
    path: 'input-group',
    component: InputGroupsComponent
  },
  {
    path: 'input-mask',
    component: InputMaskComponent
  },
  {
    path: 'tag-input',
    component: TagInputsComponent
  },
  {
    path: 'wizard',
    component: WizardComponent
  },
  {
    path: 'img-cropper',
    component: AppImgCropperComponent
  },
  {
    path: 'Attendance',
    component: MarkAttendanceComponent
  },
  {
    path: 'AttendanceHistory',
    component: AttendaneHistoryComponent
  },
  {
    path: 'AttendanceReport',
    component: AttendanceReportComponent
  },
  {
    path: 'StudentAttendance',
    component: StudentAttendanceComponent
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FormsRoutingModule { }
