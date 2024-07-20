import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Users} from './users/users.component';
import {InstitutionsComponent} from './institutions/institutions.component';
import {AuthGuard} from '../../shared/services/auth-guard.service';

const routes: Routes = [
  {
    path: 'users',
    component: Users,
    canActivate: [AuthGuard],
    data: { roles: ['SUPERADMIN'] }
  },
  {
    path: 'institutions',
    component: InstitutionsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ToolsRoutingModule { }
