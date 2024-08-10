import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { throwError } from 'rxjs';
import { Post } from 'src/app/shared/models/Forum/Post';
import { PostService } from 'src/app/shared/services/Forum/post.service';
import { ListPostComponent } from '../Post/list-post/list-post.component';
import { UpdatePostComponent } from './update-post/update-post.component';

@Component({
  selector: 'app-view-sub',
  templateUrl: './view-sub.component.html',
  styleUrls: ['./view-sub.component.scss']
})
export class ViewSubComponent implements OnInit {
  selectedPostId: string | null = null; // Variable to hold the selected post ID
  subId: string;
  posts: Post[] = [];
  
  constructor(private postService: PostService,
     private activateRoute: ActivatedRoute,
     public dialog: MatDialog,
     private router: Router) {
    this.subId = this.activateRoute.snapshot.params.id;
  }

  ngOnInit(): void {
    this.getPostById();
    console.log("le sub id",this.subId)
    this.selectedPostId=this.subId;
  }


  private getPostById() {
    this.postService.getPostsByForum(this.subId).subscribe(data => {
      this.posts = data;
      console.log("le data",this.posts,data)
    }, error => {
      throwError(error);
    });
  }
  /*read(id:any){
    const dialogRef = this.dialog.open(ListPostComponent,{
      width : "60%",
      height: "50%",
      data: { post:id}
    });
    dialogRef.afterClosed().subscribe(res =>{
     this.ngOnInit();
    })   
  }     */ 
  read2(id:any){
    this.router.navigate(['/forum/listpost', id]);
    }  


    update(id:any){
      const dialogRef = this.dialog.open(UpdatePostComponent,{
        width : "40%",
        height: "10%",
        data: { post:id}
      });
      dialogRef.afterClosed().subscribe(res =>{
       this.ngOnInit();
      })   
    }

}
