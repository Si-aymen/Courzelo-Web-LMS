import { SessionStorageService } from './../../../../shared/services/user/session-storage.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { throwError } from 'rxjs';
import { PostREQ } from 'src/app/shared/models/Forum/PostREQ';
import { SubForum } from 'src/app/shared/models/Forum/SubForum';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { ForumService } from 'src/app/shared/services/Forum/forum.service';
import { PostService } from 'src/app/shared/services/Forum/post.service';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.scss']
})
export class AddPostComponent implements OnInit {

  postId: string | null = null;
  createPostForm: FormGroup;
  postPayload: PostREQ;
  subs: Array<SubForum>;
  connectedUser: UserResponse;
  constructor(private router: Router, 
    private postService: PostService,private formBuilder: FormBuilder,
    private sessionStorageService: SessionStorageService,
    private subService: ForumService,
    private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.createForm();
    this.connectedUser = this.sessionStorageService.getUserFromSession();
    this.subService.getSubForums().subscribe((data) => {
      this.subs = data;
    }, error => {
      throwError(error);
    });
    this.route.paramMap.subscribe(params => {
      this.postId = params.get('id');
      console.log('Received post ID:', this.postId);
      // Use the postId as needed
    });
  }
  createForm() {
    this.createPostForm = this.formBuilder.group({
      postName: ['', Validators.required],
      content: ['', Validators.required],
      description :['',Validators.required],
      subforum: [''],
      user: [''],
    });
  }
  reclamationtypeValue() {
    return this.createPostForm.get('subforum')?.value;
  }

  onSubmit() {
    this.createPostForm.patchValue({ user: this.connectedUser.email });
    this.createPostForm.patchValue({subforum:this.postId});
    console.log("le form",this.createPostForm)
    this.postService.addpost(this.createPostForm.value).subscribe((data) => {
      console.log(this.createPostForm.value)
     // this.router.navigateByUrl('/');
    }, error => {
      throwError(error);
    })
  }

  discardPost() {
    this.router.navigateByUrl('/');
  }
}
