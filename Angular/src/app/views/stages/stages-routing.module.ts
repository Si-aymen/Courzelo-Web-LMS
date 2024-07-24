import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StagesComponent } from './stages/stages.component';
import { StageDetailsComponent } from './stage-details/stage-details.component';

const routes: Routes = [
  {
    path: 'stages',
    component: StagesComponent
  },
  {
    path: 'details/:id',
    component: StageDetailsComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StagesRoutingModule { }
