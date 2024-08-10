import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListGroupComponent } from './list-group/list-group.component';
import { AddGroupComponent } from './add-group/add-group.component';
import { ChatComponent } from './chat/chat.component';
import { ContentComponent } from './content/content.component';
import { VideoCallComponent } from './video-call/video-call.component';

const routes: Routes = [
  {
  path: 'list',
  component: ListGroupComponent
}, {
  path: 'add',
  component: AddGroupComponent
},
{
  path:'chat',component:ChatComponent
},
{
  path:'content',component:ContentComponent
},
{
  path:'call',component:VideoCallComponent
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GroupsRoutingModule { }
