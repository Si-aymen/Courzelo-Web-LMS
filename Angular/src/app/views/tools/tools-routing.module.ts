import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Users} from './users/users.component';
import {InstitutionsComponent} from './institutions/institutions.component';

const routes: Routes = [
  {
    path: 'users',
    component: Users
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
