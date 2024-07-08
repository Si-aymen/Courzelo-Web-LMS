import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ListTicketComponent } from './list-ticket/list-ticket.component';
import { AddTicketComponent } from './add-ticket/add-ticket.component';
import { ForwardComponent } from './forward/forward.component';
import { FaqComponent } from './faq/faq.component';

const routes: Routes = [
  { path: 'list', component: ListTicketComponent },
  { path: 'add', component: AddTicketComponent },
  { path: 'forward', component: ForwardComponent },
  { path: 'faq', component: FaqComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TicketsRoutingModule { }
