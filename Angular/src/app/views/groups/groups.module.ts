import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GroupsRoutingModule } from './groups-routing.module';
import { ListGroupComponent } from './list-group/list-group.component';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { AddGroupComponent } from './add-group/add-group.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ChatComponent } from './chat/chat.component';
import { ContentComponent } from './content/content.component';
import { AddMemberComponent } from './add-member/add-member.component';
import { VideoCallComponent } from './video-call/video-call.component';
import { ListMemberComponent } from './list-member/list-member.component';


@NgModule({
  declarations: [
    ListGroupComponent,
    AddGroupComponent,
    ChatComponent,
    ContentComponent,
    AddMemberComponent,
    VideoCallComponent,
    ListMemberComponent
  ],
  imports: [
    CommonModule,
    GroupsRoutingModule,
    PerfectScrollbarModule,
    ReactiveFormsModule,
  ]
})
export class GroupsModule { }
