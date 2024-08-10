import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxEchartsModule } from 'ngx-echarts';
import { SharedComponentsModule } from 'src/app/shared/components/shared-components.module';

import { ForumRoutingModule } from './forum-routing.module';
import { ListForumComponent } from './list-forum/list-forum.component';
import { AddForumComponent } from './add-forum/add-forum.component';
import { PostTileComponent } from './Shared/post-tile/post-tile.component';
import { SideBarComponent } from './Shared/side-bar/side-bar.component';
import { SubSideBarComponent } from './Shared/sub-side-bar/sub-side-bar.component';
import { HomeforumComponent } from './homeforum/homeforum.component';
import { AddPostComponent } from './Post/add-post/add-post.component';
import { ListPostComponent } from './Post/list-post/list-post.component';
import { ViewSubComponent } from './view-sub/view-sub.component';
import { UpdatePostComponent } from './view-sub/update-post/update-post.component';
import { EditCommentComponent } from './Post/edit-comment/edit-comment.component';

@NgModule({
  declarations: [
    ListForumComponent,
    AddForumComponent,
    PostTileComponent,
    SideBarComponent,
    SubSideBarComponent,
    HomeforumComponent,
    AddPostComponent,
    ListPostComponent,
    ViewSubComponent,
    UpdatePostComponent,
    EditCommentComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule, // Ensure ReactiveFormsModule is imported here
    NgbModule,
    NgxDatatableModule,
    NgxEchartsModule,
    SharedComponentsModule,
    ForumRoutingModule
  ]
})
export class ForumModule { }
