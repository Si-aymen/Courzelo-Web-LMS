import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OthersRoutingModule } from './others-routing.module';
import { NotFoundComponent } from './not-found/not-found.component';
import { RouterModule } from '@angular/router';
import { LayoutsModule } from "../../shared/components/layouts/layouts.module";

@NgModule({
    declarations: [NotFoundComponent],
    imports: [
        CommonModule,
        RouterModule,
        OthersRoutingModule,
        LayoutsModule
    ]
})
export class OthersModule { }
