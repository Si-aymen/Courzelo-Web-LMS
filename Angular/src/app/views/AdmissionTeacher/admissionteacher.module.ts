import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdmissionteacherRoutingModule } from './admissionteacher-routing.module';
import { NoteApplicationComponent } from './note-application/note-application.component';
import { ListAdmissionComponent } from './list-admission/list-admission.component';


@NgModule({
  declarations: [
    NoteApplicationComponent,
    ListAdmissionComponent
  ],
  imports: [
    CommonModule,
    AdmissionteacherRoutingModule
  ]
})
export class AdmissionteacherModule { }
