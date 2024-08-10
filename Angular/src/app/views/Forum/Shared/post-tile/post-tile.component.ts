import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Post } from 'src/app/shared/models/Forum/Post';

@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.scss']
})
export class PostTileComponent {
  posts: Array<Post> = [];
  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

}
