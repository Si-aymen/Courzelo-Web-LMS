// forum-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddForumComponent } from './add-forum/add-forum.component';
import { ListForumComponent } from './list-forum/list-forum.component';
import { AddPostComponent } from './Post/add-post/add-post.component';
import { ListPostComponent } from './Post/list-post/list-post.component';
import { ViewSubComponent } from './view-sub/view-sub.component';

const routes: Routes = [
  {
    path: 'addforum',
    component: AddForumComponent
  },
  {
    path: 'list',
    component: ListForumComponent
  },
  {
    path: 'addpost/:id',
    component: AddPostComponent
  },
  {
    path: 'listpost/:id',
    component: ListPostComponent
  },
  {
    path: 'view-sub/:id',
    component: ViewSubComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForumRoutingModule { }
