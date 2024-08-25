import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketsRoutingModule } from './tickets-routing.module';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxEchartsModule } from 'ngx-echarts';
import { SharedComponentsModule } from 'src/app/shared/components/shared-components.module';
import { ListTicketComponent } from './list-ticket/list-ticket.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ForwardComponent } from './forward/forward.component';
import { FaqComponent } from './faq/faq.component';
import { TickettypeComponent } from './Type/tickettype/tickettype.component';
import { ListComponent } from './Type/list/list.component';
import { AddfaqComponent } from './faq/addfaq/addfaq.component';
import { UpdateTypeComponent } from './Type/update-type/update-type.component';
import { UpdatefaqComponent } from './faq/updatefaq/updatefaq.component';



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
  declarations: [ListTicketComponent,ForwardComponent, FaqComponent,
     TickettypeComponent, ListComponent, AddfaqComponent, UpdateTypeComponent, UpdatefaqComponent]
})
export class TicketsModule { }