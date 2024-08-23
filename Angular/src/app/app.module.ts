import { BrowserModule } from '@angular/platform-browser';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService } from './shared/inmemory-db/inmemory-db.service';
import { MatIconModule } from '@angular/material/icon';
import {CreateQuizComponent} from './views/forms/Quiz/create-quiz/create-quiz.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { AddProjectComponent } from './shared/components/Project/Admin/add-project/add-project.component';
import { DashboardProjectComponent } from './shared/components/Project/Admin/dashboard-project/dashboard-project.component';
import { ViewdetailsComponent } from './shared/components/Project/Admin/viewdetails/viewdetails.component';
import { PdfComponent } from './shared/components/Project/User/pdf/pdf.component';


import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxPaginationModule } from 'ngx-pagination';
import { TicketsRoutingModule } from './views/tickets/tickets-routing.module';
import { MatDialogModule } from '@angular/material/dialog';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {Interceptor} from './shared/services/user/Interceptor';
import { ForumRoutingModule } from './views/Forum/forum-routing.module';
import { DossierComponent } from './views/dossier/dossier.component';
import { UploadFileComponent } from './views/Admission/upload-file/upload-file.component';


@NgModule({
  declarations: [
    AppComponent,
    AddProjectComponent,
    DashboardProjectComponent,
    ViewdetailsComponent ,
    PdfComponent,
    DossierComponent,
    UploadFileComponent,    
  ],
  imports: [
    NgxPaginationModule,
    NgxDatatableModule,
    BrowserModule,
    SharedModule,
    HttpClientModule,
    BrowserAnimationsModule,
    InMemoryWebApiModule.forRoot(InMemoryDataService, { passThruUnknownUrl: true }),
    AppRoutingModule ,
    FormsModule,
    CommonModule,
    ReactiveFormsModule ,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    ForumRoutingModule,
    TicketsRoutingModule,
    MatDialogModule,
    MatIconModule
  ],

  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
     {
      provide: HTTP_INTERCEPTORS,
       useClass: Interceptor,
       multi: true
     },
    {
        provide: DatePipe
    }
  ]
})
export class AppModule { }
