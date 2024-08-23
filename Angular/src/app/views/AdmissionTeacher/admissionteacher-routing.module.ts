import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NoteApplicationComponent } from './note-application/note-application.component';
import { ListAdmissionComponent } from './list-admission/list-admission.component';

const routes: Routes = [
  {path:"note/:id",component:NoteApplicationComponent},
  {path:"list",component:ListAdmissionComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdmissionteacherRoutingModule { }
