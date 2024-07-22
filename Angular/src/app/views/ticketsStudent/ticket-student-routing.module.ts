import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListTicketComponent } from './list-ticket/list-ticket.component';
import { AddTicketComponent } from './add-ticket/add-ticket.component';
import { UpdateTicketComponent } from './update-ticket/update-ticket.component';
import { ListFaqComponent } from './list-faq/list-faq.component';
import { RatingComponent } from './rating/rating.component';

const routes: Routes = [
  {path:'list',component:ListTicketComponent},
  {path:'add',component:AddTicketComponent},
  {path:'update',component:UpdateTicketComponent},
  { path:'rate', component: RatingComponent },
  {path:'faq',component:ListFaqComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TicketStudentRoutingModule { }
