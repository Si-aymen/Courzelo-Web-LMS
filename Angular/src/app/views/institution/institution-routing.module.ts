import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UsersComponent} from './users/users.component';
import {EditComponent} from './edit/edit.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from '../../shared/services/auth-guard.service';

const routes: Routes = [
  {
    path: ':institutionID/users',
    component: UsersComponent,
    canActivate: [AuthGuard],
    data: {
        roles: ['ADMIN']
    }
  },
  {
    path: ':institutionID/edit',
    component: EditComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN']
    }
  },
  {
    path: ':institutionID',
    component: HomeComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InstitutionRoutingModule { }
