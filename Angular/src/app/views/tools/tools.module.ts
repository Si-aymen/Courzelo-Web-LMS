import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ToolsRoutingModule } from './tools-routing.module';
import { SuperAdminComponent } from './super-admin/super-admin.component';
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {NgxPaginationModule} from "ngx-pagination";
import {SharedComponentsModule} from "../../shared/components/shared-components.module";
import {TagInputModule} from "ngx-chips";


@NgModule({
  declarations: [
    SuperAdminComponent
  ],
    imports: [
        CommonModule,
        ToolsRoutingModule,
        NgxDatatableModule,
        NgxPaginationModule,
        SharedComponentsModule,
        TagInputModule
    ]
})
export class ToolsModule { }
