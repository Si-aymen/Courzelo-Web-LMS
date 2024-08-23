import { CommentService } from './../../../../shared/services/Forum/comment.service';
import { PostService } from 'src/app/shared/services/Forum/post.service';
import { Component, Inject, Optional, OnInit, ChangeDetectorRef } from '@angular/core';
import { Post } from 'src/app/shared/models/Forum/Post';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, forkJoin, of, throwError } from 'rxjs';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import { Comment } from 'src/app/shared/models/Forum/Comment';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { map, catchError, tap } from 'rxjs/operators';
import { UserService } from 'src/app/shared/services/user/user.service';
import { MatDialog } from '@angular/material/dialog';
import Swal from 'sweetalert2';
import { EditCommentComponent } from '../edit-comment/edit-comment.component';

@Component({
  selector: 'app-list-post',
  templateUrl: './list-post.component.html',
  styleUrls: ['./list-post.component.scss']
})
export class ListPostComponent implements OnInit {

id:any;
postt: Post;
postlike:any;
postdislike:any;
connectedUser: UserResponse;
commentForm: FormGroup;
Comment: Comment;
comments: Comment[];
postid: string | null = null;
imageSrc: any;
imageCache: { [email: string]: SafeUrl } = {};


  constructor(
    private postService:PostService,
    private commentService:CommentService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private sessionStorageService: SessionStorageService,
    private userService: UserService,
    private sanitizer: DomSanitizer,
    public dialog: MatDialog,
    private cdr: ChangeDetectorRef // Add this line

    ){
      }
      ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
          this.postid = params.get('id');
          console.log('Received post ID:', this.postid);
          // Use the postId as needed
        });
        this.getPost();
        this.connectedUser = this.sessionStorageService.getUserFromSession();
        this.createForm();
      
        console.log("le id", this.postid);
        console.log("le email", this.connectedUser.email);
        this.getCommentsForPost();
        this.loadToMe();
        this.userService.getProfileImageBlobUrl(this.connectedUser.email).subscribe((blob: Blob) => {
          const objectURL = URL.createObjectURL(blob);
          this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        }); 
        this.determineVoteStatusPost(this.postid,this.connectedUser.email);
        this.updatePostsWithDownvoteCounts(this.postid)
        this.updatePostsWithUpvoteCounts(this.postid)
        console.log("le vote ",this.postdislike,this.postlike)
      }


      getPost(){
        this.postService.getPostById(this.postid).subscribe((res: any) => {
          this.postt = res;
          console.log("LE Post",this.postt);
        });
      }
      
  createForm() {
    this.commentForm = this.formBuilder.group({
      text: ['', Validators.required],
      post: this.postid,
      user: this.connectedUser.email,
    });
  }
  loadProfileImage(email: string): Observable<SafeUrl> {
    if (this.imageCache[email]) {
      return of(this.imageCache[email]); // Return cached image if it exists
    }

    return this.userService.getProfileImageBlobUrl(email).pipe(
      map(blob => {
        const objectURL = URL.createObjectURL(blob);
        const safeURL = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        this.imageCache[email] = safeURL;
        return safeURL;
      }),
      catchError(() => {
        const defaultImageURL = 'assets/default-profile-image.png'; // Default image path
        const safeDefaultURL = this.sanitizer.bypassSecurityTrustUrl(defaultImageURL);
        this.imageCache[email] = safeDefaultURL;
        return of(safeDefaultURL);
      })
    );
  }
  

  postComment() {
    console.log("leform value",this.commentForm.value);
    this.commentService.addcomment(this.commentForm.value).subscribe((res:any)=>{
      console.log("adad",res)
    });
    this.ngOnInit();
    }
    getCommentsForPost(): void {
      this.commentService.getCommentByPost(this.postid).subscribe(data => {
        this.comments = data.sort((a, b) => new Date(b.createdDate).getTime() - new Date(a.createdDate).getTime());
        // Create a map to store downvote counts
        const downvoteCounts = new Map<string, number>();
        const upvoteCounts = new Map<string, number>();
        this.comments.forEach(comment => {
          this.loadProfileImage(comment.user.email).subscribe(image => {
            comment.user['profileImageUrl'] = image; // Dynamically add the property
            this.determineVoteStatus(comment, this.connectedUser.email);
            // Fetch downvote count
        this.commentService.CommentDownvotedNumber(comment.id).subscribe(downvoteCount => {
          downvoteCounts.set(comment.id, downvoteCount);
          // After processing all comments, update the view
          if (downvoteCounts.size === this.comments.length) {
            this.updateCommentsWithDownvoteCounts(downvoteCounts);
          }
        })
        this.commentService.CommentUpvotedNumber(comment.id).subscribe(upvoteCount => {
          upvoteCounts.set(comment.id, upvoteCount);
          // After processing all comments, update the view
          if (upvoteCounts.size === this.comments.length) {
            this.updateCommentsWithUpvoteCounts(upvoteCounts);
          }
        })
           // this.determineVoteCount(comment);
            this.cdr.markForCheck(); // Ensure change detection runs
          });
        });
      }, error => {
        throwError(error);
      });
    }
    updateCommentsWithUpvoteCounts(upvoteCounts: Map<string, number>): void {
      this.comments.forEach(comment => {
        // Get the downvote count from the map
        comment['upvoteCount'] = upvoteCounts.get(comment.id) || 0;
      });
    
      this.cdr.markForCheck(); // Ensure change detection runs
    }
    updatePostsWithUpvoteCounts(postId:any) {
      this.postService.PostUpvotedNumber(postId).subscribe((res)=>{
        this.postlike=res;
      })
    }
    updatePostsWithDownvoteCounts(postId:any) {
      this.postService.PostDownvotedNumber(postId).subscribe((res)=>{
        this.postdislike=res;
      })
    }
    updateCommentsWithDownvoteCounts(downvoteCounts: Map<string, number>): void {
      this.comments.forEach(comment => {
        // Get the downvote count from the map
        comment['downvoteCount'] = downvoteCounts.get(comment.id) || 0;
      });
    
      this.cdr.markForCheck(); // Ensure change detection runs
    }

    
    /*
    determineVoteCount(comment:any){
      this.commentService.CommentDownvotedNumber(comment.id).subscribe(downvoteCount => {
        comment.downvoteCount = downvoteCount; // Set the downvote count
        this.cdr.markForCheck(); // Ensure change detection runs after updating the count
      }, error => {
        console.error('Error fetching downvote count:', error);
      });
    }*/

    likePost(post: any) {
      if (!post) {
        console.error('Comment is null or undefined');
        return;
      }
      this.postService.upvotePost(post.id, this.connectedUser.email).subscribe(() => {
        console.log(`Post ${post} dowvoted successfully.`);
        // Update the local state if needed
      }, error => {
        console.error('Error dowvoting comment:', error);
      });
    

      
      post.isLiked = !post.isLiked;
      if (post.isLiked) {
        post.isDisliked = false; // Deselect dislike if like is selected
      }
    }
    
    likeComment(comment: any) {
      if (!comment) {
        console.error('Comment is null or undefined');
        return;
      }
      this.commentService.upvoteComment(comment.id, this.connectedUser.email).subscribe(() => {
        console.log(`Comment ${comment.id} dowvoted successfully.`);
        // Update the local state if needed
      }, error => {
        console.error('Error dowvoting comment:', error);
      });
      
      comment.isLiked = !comment.isLiked;
      if (comment.isLiked) {
        comment.isDisliked = false; // Deselect dislike if like is selected
      }
    }
    
    dislikeComment(comment: any) {
      if (!comment) {
        console.error('Comment is null or undefined');
        return;
      }
          // Only upvote if it has not been upvoted by the user
          this.commentService.downvoteComment(comment.id, this.connectedUser.email).subscribe(() => {
            console.log(`Comment ${comment.id} dowvoted successfully.`);
            // Update the local state if needed
          }, error => {
            console.error('Error dowvoting comment:', error);
          });
        
      comment.isDisliked = !comment.isDisliked;
      if (comment.isDisliked) {
        comment.isLiked = false; // Deselect like if dislike is selected
      }
    }

    dislikePost(post: any) {
      if (!post) {
        console.error('Comment is null or undefined');
        return;
      }
          // Only upvote if it has not been upvoted by the user
          this.postService.downvotePost(post.id, this.connectedUser.email).subscribe(() => {
            console.log(`Post ${post} dowvoted successfully.`);
            // Update the local state if needed
          }, error => {
            console.error('Error dowvoting comment:', error);
          });
        
          post.isDisliked = !post.isDisliked;
      if (post.isDisliked) {
        post.isLiked = false; // Deselect like if dislike is selected
      }
    }
    
    determineVoteStatusPost(post: any, userId: string): void {
      if (typeof post === 'string') {
        // If post is just an ID, you should fetch the post object first.
        this.postService.getPostById(post).subscribe(fetchedPost => {
          this.updatePostVoteStatus(fetchedPost, userId);
        });
      } else {
        // If post is already an object
        this.updatePostVoteStatus(post, userId);
      }
    }
    
    private updatePostVoteStatus(post: any, userId: string): void {
      const isUpvoted$ = this.postService.isPostUpvoted(post.id, userId);
      const isDownvoted$ = this.postService.isPostDownvoted(post.id, userId);
    
      forkJoin([isUpvoted$, isDownvoted$]).subscribe(([isUpvoted, isDownvoted]) => {
        console.log("upvoted", isUpvoted);
        console.log("downvoted", isDownvoted);
    
        // Ensure post is an object before setting properties
        if (typeof post === 'object') {
          post.isLiked = isUpvoted;
          post.isDisliked = isDownvoted;
        }
    
        this.cdr.markForCheck(); // Ensure change detection runs after updating the post
      });
    }
    


    determineVoteStatus(comment: any, userId: string): void {
      const isUpvoted$ = this.commentService.isCommentUpvoted(comment.id, userId);
      const isDownvoted$ = this.commentService.isCommentDownvoted(comment.id, userId);
    
      forkJoin([isUpvoted$, isDownvoted$]).subscribe(([isUpvoted, isDownvoted]) => {
        console.log("upvoted", isUpvoted);
        console.log("downvoted", isDownvoted);
    
        if (isUpvoted) {
          comment.isLiked = true;
          comment.isDisliked = false;
        } else if (isDownvoted) {
          comment.isLiked = false;
          comment.isDisliked = true;
        } else {
          comment.isLiked = false;
          comment.isDisliked = false;
        }
    
        this.cdr.markForCheck(); // Ensure change detection runs after updating the comment
      });
    }
    
  loadToMe(): void {
    this.commentService.getCommentByPost(this.postid).pipe(
      tap(mails => {
        mails.forEach(mail => {
          console.log("le mail ",mail)
          this.loadProfileImage(mail.user.email).subscribe(image => {
           // mail.user.profile.profileImage = image;
           console.log("le mail ")
            console.log(mail.user)
            this.cdr.markForCheck(); // Notify Angular about the change
          });
        });
      })
    );
  }
  
  update(id:any){
    const dialogRef = this.dialog.open(EditCommentComponent,{
      width : "50%",
      height: '300px',
      data: { comment:id.id}
    });
    dialogRef.afterClosed().subscribe(res =>{
     this.ngOnInit();
    })   
  }

  deletecomment(id:any){
  Swal.fire({
    title: 'Êtes-vous sûr ?',
    text: "Voulez-vous vraiment supprimer cette appartment ?",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Oui, supprimez-le!'
  }).then((result) => {
    if (result.isConfirmed) {
      this.commentService.deleteComment(id.id).subscribe((res:any) =>{
        if (res.message){
          Swal.fire({
            icon: 'success',
            title: 'Success...',
            text: 'Supprimé avec succès !',
          })
          this.ngOnInit();
        }
        else{
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: "Quelque chose s'est mal passé!",
          })
        }
      },
      err =>{
        Swal.fire({
          icon: 'warning',
          title: 'La suppression a échoué!...',
          text: err.error.message,
        })
      }
      )
    }
    this.ngOnInit();
  }
  )
}
}

