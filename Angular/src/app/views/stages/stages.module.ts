import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StagesRoutingModule } from './stages-routing.module';



import { NgxEchartsModule } from 'ngx-echarts';
import { SharedComponentsModule } from 'src/app/shared/components/shared-components.module';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppFormsModule } from '../forms/forms.module';
import { ReactiveFormsModule } from '@angular/forms';
import { StagesComponent } from './stages/stages.component';



@NgModule({
  declarations: [StagesComponent],
  imports: [
    CommonModule,
    StagesRoutingModule,
    SharedComponentsModule,
    NgxEchartsModule,
    NgxDatatableModule,
    NgbModule,
    AppFormsModule,
    ReactiveFormsModule
  ]
})
export class StagesModule { }
