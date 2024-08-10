import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.scss']
})
export class SideBarComponent implements OnInit {
  @Input() postId: string | null = null; // Input property to receive the ID

  constructor(private router: Router) { }

  ngOnInit() {
    if (this.postId) {
      // Example: Fetch additional data based on postId
      console.log('Received post ID:', this.postId);
    }
  }

  goToCreatePost() {
    console.log("post", this.postId);
    if (this.postId) {
      this.router.navigate(['/forum/addpost', this.postId]);
    } else {
      this.router.navigate(['/forum/addpost']);
    }
  }

  goToCreateSubreddit() {
    this.router.navigateByUrl('/create-subreddit');
  }
}
