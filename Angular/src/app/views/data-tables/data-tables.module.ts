import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxPaginationModule } from 'ngx-pagination';

import { DataTablesRoutingModule } from './data-tables-routing.module';
import { FullscreenTableComponent } from './fullscreen-table/fullscreen-table.component';
import { PagingTableComponent } from './paging-table/paging-table.component';
import { FilterTableComponent } from './filter-table/filter-table.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ListPaginationComponent } from './list-pagination/list-pagination.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { QuizTableComponent } from './Quiz/quiz-table/quiz-table.component';
import { QuizListComponent } from './Quiz/quiz-list/quiz-list.component';
import {AppFormsModule} from '../forms/forms.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        NgxPaginationModule,
        NgxDatatableModule,
        NgbModule,
        DataTablesRoutingModule,
        AppFormsModule,
    ],
declarations: [FullscreenTableComponent, PagingTableComponent, FilterTableComponent, ListPaginationComponent, QuizTableComponent, QuizListComponent]
})
export class DataTablesModule { }
