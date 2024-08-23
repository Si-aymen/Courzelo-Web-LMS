import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdmissionstudentRoutingModule } from './admissionstudent-routing.module';
import { ListAddmissionComponent } from './list-addmission/list-addmission.component';
import { ApplyComponent } from './apply/apply.component';


@NgModule({
  declarations: [
    ListAddmissionComponent,
    ApplyComponent
  ],
  imports: [
    CommonModule,
    AdmissionstudentRoutingModule
  ]
})
export class AdmissionstudentModule { }
