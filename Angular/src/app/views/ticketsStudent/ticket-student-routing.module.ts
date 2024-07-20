import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListTicketComponent } from './list-ticket/list-ticket.component';
import { AddTicketComponent } from './add-ticket/add-ticket.component';
import { UpdateTicketComponent } from './update-ticket/update-ticket.component';

const routes: Routes = [
  {path:'list',component:ListTicketComponent},
  {path:'add',component:AddTicketComponent},
  {path:'update',component:UpdateTicketComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TicketStudentRoutingModule { }
