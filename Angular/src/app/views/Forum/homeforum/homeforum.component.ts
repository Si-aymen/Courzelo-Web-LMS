import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/shared/models/Forum/Post';
import { PostService } from 'src/app/shared/services/Forum/post.service';

@Component({
  selector: 'app-homeforum',
  templateUrl: './homeforum.component.html',
  styleUrls: ['./homeforum.component.scss']
})
export class HomeforumComponent implements OnInit {

  posts: Array<Post> = [];

  constructor(private postService: PostService) {
    this.postService.getAllPosts().subscribe(post => {
      this.posts = post;
    });
  }

  ngOnInit(): void {
  }
}
