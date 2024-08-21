import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransportsRoutingModule } from './transports-routing.module';

import { NgxEchartsModule } from 'ngx-echarts';
import { SharedComponentsModule } from 'src/app/shared/components/shared-components.module';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { TransportsComponent } from './transports/transports.component';
import { AppFormsModule } from '../forms/forms.module';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  imports: [
    CommonModule,
    TransportsRoutingModule,
    SharedComponentsModule,
    NgxEchartsModule,
    NgxDatatableModule,
    NgbModule,
    AppFormsModule,
    ReactiveFormsModule
  ],

  declarations: [TransportsComponent]

})
export class TransportsModule { }
