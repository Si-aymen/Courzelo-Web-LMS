import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ListTicketComponent } from './list-ticket/list-ticket.component';
import { AddTicketComponent } from './add-ticket/add-ticket.component';
import { ForwardComponent } from './forward/forward.component';
import { FaqComponent } from './faq/faq.component';
import { TickettypeComponent } from './Type/tickettype/tickettype.component';
import { ListComponent } from './Type/list/list.component';
import { UpdateTicketComponent } from './update-ticket/update-ticket.component';
import { RatingComponent } from './rating/rating.component';
import { AddfaqComponent } from './faq/addfaq/addfaq.component';

const routes: Routes = [
  { path: 'list', component: ListTicketComponent },
  { path: 'add', component: AddTicketComponent },
  { path: 'forward', component: ForwardComponent },
  { path: 'typeticket', component: TickettypeComponent },
  { path: 'listtype', component: ListComponent },
  { path: 'faq', component: FaqComponent },
  { path: 'rate', component: RatingComponent },
  { path: 'update',component:UpdateTicketComponent},
  { path: 'faq/add',component:AddfaqComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TicketsRoutingModule { }
