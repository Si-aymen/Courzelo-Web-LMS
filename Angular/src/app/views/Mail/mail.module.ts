import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';

import { MailRoutingModule } from './mail-routing.module';
import { MailComponent } from 'src/app/views/Mail/mail/mail.component';
import { ComposeComponent } from './compose/compose.component';
import { InboxRoutingModule } from '../inbox/inbox-routing.module';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SharedDirectivesModule } from 'src/app/shared/directives/shared-directives.module';

@NgModule({
  declarations: [
    MailComponent,
    ComposeComponent
  ],
  imports: [
    CommonModule,
    MailRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    QuillModule.forRoot(),
    SharedDirectivesModule,
    PerfectScrollbarModule,
    InboxRoutingModule,
    NgbModule  // Ensure NgbModule is imported
  ],
  providers: [NgbActiveModal]  // Provide NgbActiveModal here
})
export class MailModule { }
