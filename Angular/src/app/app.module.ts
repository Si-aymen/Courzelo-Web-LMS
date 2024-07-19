import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService } from './shared/inmemory-db/inmemory-db.service';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxPaginationModule } from 'ngx-pagination';
import { TicketsRoutingModule } from './views/tickets/tickets-routing.module';
import { MatDialogModule } from '@angular/material/dialog';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {Interceptor} from './shared/services/user/Interceptor';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    NgxPaginationModule,
    NgxDatatableModule,
    BrowserModule,
    SharedModule,
    HttpClientModule,
    BrowserAnimationsModule,
    InMemoryWebApiModule.forRoot(InMemoryDataService, { passThruUnknownUrl: true }),
    AppRoutingModule,
    FormsModule,
    TicketsRoutingModule,
    MatDialogModule,
  ],
  providers: [
     {
      provide: HTTP_INTERCEPTORS,
       useClass: Interceptor,
       multi: true
     }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
