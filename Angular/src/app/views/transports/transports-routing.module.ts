import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransportsComponent } from './transports/transports.component';

const routes: Routes = [
  {
    path: 'transports',
    component: TransportsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransportsRoutingModule { }
