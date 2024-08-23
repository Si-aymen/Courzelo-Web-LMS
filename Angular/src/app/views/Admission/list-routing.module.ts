import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateAdmissionComponent } from './create-admission/create-admission.component';
import { ListAdmissionComponent } from './list-admission/list-admission.component';
import { ListApplicationsComponent } from './list-applications/list-applications.component';
import { ScheduleComponent } from './schedule/schedule.component';

const routes: Routes = [
  {path:"create",component:CreateAdmissionComponent},
  {path:"list",component:ListAdmissionComponent},
  {path:"applications/:id",component:ListApplicationsComponent},
  {path:"schedule",component:ScheduleComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ListRoutingModule { }
