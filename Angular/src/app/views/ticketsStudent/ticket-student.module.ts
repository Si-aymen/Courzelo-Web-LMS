import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TicketStudentRoutingModule } from './ticket-student-routing.module';
import { AddTicketComponent } from './add-ticket/add-ticket.component';
import { ListTicketComponent } from './list-ticket/list-ticket.component';
import { UpdateTicketComponent } from './update-ticket/update-ticket.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxEchartsModule } from 'ngx-echarts';
import { SharedComponentsModule } from 'src/app/shared/components/shared-components.module';
import { ListFaqComponent } from './list-faq/list-faq.component';
import { RatingComponent } from './rating/rating.component';


@NgModule({
  declarations: [
    AddTicketComponent,
    ListTicketComponent,
    UpdateTicketComponent,
    ListFaqComponent,
    RatingComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    SharedComponentsModule,
    NgxEchartsModule,
    NgxDatatableModule,
    ReactiveFormsModule,
    NgbModule,
    TicketStudentRoutingModule
  ]
})
export class TicketStudentModule { }
