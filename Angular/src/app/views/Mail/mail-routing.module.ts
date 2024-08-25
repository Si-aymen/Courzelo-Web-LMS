import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MailComponent } from './mail/mail.component';
import { ComposeComponent } from './compose/compose.component';

const routes: Routes = [
  { path: '', component: MailComponent },  // Default route for 'mailing'
  { path: 'compose', component: ComposeComponent }  // Compose route
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MailRoutingModule { }