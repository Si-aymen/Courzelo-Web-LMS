import { BrowserModule } from '@angular/platform-browser';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService } from './shared/inmemory-db/inmemory-db.service';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { AddProjectComponent } from './shared/components/Project/Admin/add-project/add-project.component';
import { DashboardProjectComponent } from './shared/components/Project/Admin/dashboard-project/dashboard-project.component';
import { ViewdetailsComponent } from './shared/components/Project/Admin/viewdetails/viewdetails.component';
import { PdfComponent } from './shared/components/Project/User/pdf/pdf.component';
import { ProjectComponent } from './shared/components/Project/User/project/project.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ProjectDetailsComponent } from './shared/components/Project/User/projectdetails/projectdetails.component';
import { ProgressDashboardComponent } from './shared/components/Project/User/progress-dashboard/progress-dashboard.component';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxPaginationModule } from 'ngx-pagination';
import { TicketsRoutingModule } from './views/tickets/tickets-routing.module';
import { MatDialogModule } from '@angular/material/dialog';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {Interceptor} from './shared/services/user/Interceptor';
import { ProjectCalendarComponent } from './shared/components/Project/User/project-calendar/project-calendar.component';
import { ColorPickerModule } from 'ngx-color-picker';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CalendarRoutingModule } from './views/calendar/calendar-routing.module';
import { CalendarFormProjectComponent } from './shared/components/Project/User/calendar-form-project/calendar-form-project.component';
import { NgbDatepickerModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PublicationComponent } from './shared/components/Project/User/publication/publication.component';
import { AddpublicationComponent } from './shared/components/Project/User/addpublication/addpublication.component';
import { SharedDirectivesModule } from './shared/directives/shared-directives.module';
import { SharedPipesModule } from './shared/pipes/shared-pipes.module';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { ChatRoutingModule } from './views/chat/chat-routing.module';

import { DataTablesRoutingModule } from './views/data-tables/data-tables-routing.module';
import { AddRevisionComponent } from './shared/components/Revision/Teacher/add-revision/add-revision.component';
import { RevisionComponent } from './shared/components/Revision/Teacher/revision/revision.component';
import { ModifyRevisionComponent } from './shared/components/Revision/Teacher/modify-revision/modify-revision.component';
import { ConsultRevisionComponent } from './shared/components/Revision/Teacher/consult-revision/consult-revision.component';
import { ClientRevisionComponent } from './shared/components/Revision/User/client-revision/client-revision.component';
import { ParticipateRevisionComponent } from './shared/components/Revision/User/participate-revision/participate-revision.component';
import { QuizrevisionComponent } from './shared/components/Revision/User/quizrevision/quizrevision.component';

@NgModule({
  declarations: [
    AppComponent,
    AddProjectComponent,
    DashboardProjectComponent,
    ViewdetailsComponent ,
   
    PdfComponent ,
    ProjectComponent,
    ProjectDetailsComponent,
    ProgressDashboardComponent,
   ProjectCalendarComponent ,
   CalendarFormProjectComponent,
   PublicationComponent,
   AddpublicationComponent,
  RevisionComponent,
   AddRevisionComponent,
   ModifyRevisionComponent,
   ConsultRevisionComponent,
   ClientRevisionComponent,
   ParticipateRevisionComponent,
   QuizrevisionComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    InMemoryWebApiModule.forRoot(InMemoryDataService, { passThruUnknownUrl: true }),
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    TicketsRoutingModule,
    MatDialogModule,
    DragDropModule,
    RouterModule.forRoot([]),
    ColorPickerModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory
    }),
    CalendarRoutingModule,
    NgbDatepickerModule,
    NgbModule,
    SharedDirectivesModule,
    SharedPipesModule,
    PerfectScrollbarModule,
    ChatRoutingModule,
    NgxPaginationModule,
    NgxDatatableModule,
    DataTablesRoutingModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: Interceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
