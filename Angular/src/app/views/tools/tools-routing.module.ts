import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {SuperAdminComponent} from './super-admin/super-admin.component';

const routes: Routes = [
  {
    path: 'superadmin',
    component: SuperAdminComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ToolsRoutingModule { }
