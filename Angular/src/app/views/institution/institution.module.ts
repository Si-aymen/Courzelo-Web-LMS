import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {InstitutionRoutingModule} from './institution-routing.module';
import {UsersComponent} from './users/users.component';
import {EditComponent} from './edit/edit.component';
import {HomeComponent} from './home/home.component';
import {NgxPaginationModule} from 'ngx-pagination';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SharedComponentsModule} from '../../shared/components/shared-components.module';
import {NgbDatepickerModule, NgbTabsetModule} from '@ng-bootstrap/ng-bootstrap';
import { CourseComponent } from './course/course.component';


@NgModule({
  declarations: [
    UsersComponent,
    EditComponent,
    HomeComponent,
    CourseComponent
  ],
    imports: [
        CommonModule,
        InstitutionRoutingModule,
        NgxPaginationModule,
        NgxDatatableModule,
        FormsModule,
        ReactiveFormsModule,
        SharedComponentsModule,
        NgbTabsetModule,
        NgbDatepickerModule
    ]
})
export class InstitutionModule { }
