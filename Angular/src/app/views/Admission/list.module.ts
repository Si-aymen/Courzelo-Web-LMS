import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ListRoutingModule } from './list-routing.module';
import { CreateAdmissionComponent } from './create-admission/create-admission.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ListAdmissionComponent } from './list-admission/list-admission.component';
import { ListApplicationsComponent } from './list-applications/list-applications.component';
import { ScheduleComponent } from './schedule/schedule.component';


@NgModule({
  declarations: [
    CreateAdmissionComponent,
    ListAdmissionComponent,
    ListApplicationsComponent,
    ScheduleComponent
  ],
  imports: [
    CommonModule,
    ListRoutingModule,
    ReactiveFormsModule,
  ]
})
export class ListModule { }
