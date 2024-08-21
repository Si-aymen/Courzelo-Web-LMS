import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ToolsRoutingModule } from './tools-routing.module';
import { Users } from './users/users.component';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {NgxPaginationModule} from 'ngx-pagination';
import {SharedComponentsModule} from '../../shared/components/shared-components.module';
import {TagInputModule} from 'ngx-chips';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { InstitutionsComponent } from './institutions/institutions.component';
import {NgbDatepickerModule} from "@ng-bootstrap/ng-bootstrap";


@NgModule({
  declarations: [
    Users,
    InstitutionsComponent,
  ],
    imports: [
        CommonModule,
        ToolsRoutingModule,
        NgxDatatableModule,
        NgxPaginationModule,
        SharedComponentsModule,
        TagInputModule,
        FormsModule,
        ReactiveFormsModule,
        NgbDatepickerModule
    ]
})
export class ToolsModule { }
