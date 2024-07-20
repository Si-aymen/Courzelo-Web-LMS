import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {SuperAdminComponent} from './super-admin/super-admin.component';
import {AuthGuard} from '../../shared/services/auth-guard.service';

const routes: Routes = [
  {
    path: 'superadmin',
    component: SuperAdminComponent,
    canActivate: [AuthGuard],
    data: { roles: ['SUPERADMIN'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ToolsRoutingModule { }
