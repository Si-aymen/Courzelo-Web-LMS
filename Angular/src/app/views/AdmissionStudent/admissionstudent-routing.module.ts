import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListAddmissionComponent } from './list-addmission/list-addmission.component';
import { ApplyComponent } from './apply/apply.component';

const routes: Routes = [
  {path:"list",component:ListAddmissionComponent},
  {path:"apply/:id",component:ApplyComponent}

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdmissionstudentRoutingModule { }
