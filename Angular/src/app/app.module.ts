import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService } from './shared/inmemory-db/inmemory-db.service';
import { HttpClientModule } from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CreateQuizComponent} from './views/forms/create-quiz/create-quiz.component';

@NgModule({
  declarations: [
    AppComponent,
  ],
    imports: [
        BrowserModule,
        SharedModule,
        HttpClientModule,
        BrowserAnimationsModule,
        InMemoryWebApiModule.forRoot(InMemoryDataService, {passThruUnknownUrl: true}),
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,

    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
