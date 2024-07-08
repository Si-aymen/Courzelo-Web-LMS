import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketsRoutingModule } from './tickets-routing.module';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxEchartsModule } from 'ngx-echarts';
import { SharedComponentsModule } from 'src/app/shared/components/shared-components.module';
import { ListTicketComponent } from './list-ticket/list-ticket.component';
import { AddTicketComponent } from './add-ticket/add-ticket.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ForwardComponent } from './forward/forward.component';
import { FaqComponent } from './faq/faq.component';



@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SharedComponentsModule,
    NgxEchartsModule,
    NgxDatatableModule,
    ReactiveFormsModule,
    NgbModule,
    TicketsRoutingModule // Import the routing module here
  ],
  declarations: [ListTicketComponent, AddTicketComponent,ForwardComponent, FaqComponent]
})
export class TicketsModule { }