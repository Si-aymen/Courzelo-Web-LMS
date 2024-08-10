import { Component, OnInit } from '@angular/core';
import { throwError } from 'rxjs';
import { SubForum } from 'src/app/shared/models/Forum/SubForum';
import { ForumService } from 'src/app/shared/services/Forum/forum.service';

@Component({
  selector: 'app-list-forum',
  templateUrl: './list-forum.component.html',
  styleUrls: ['./list-forum.component.scss']
})
export class ListForumComponent implements OnInit{

  subforums: Array<SubForum>;

  constructor(private forumservice: ForumService) { }
  ngOnInit() {
    this.forumservice.getSubForums().subscribe(data => {
      this.subforums = data;
      console.log("subs",this.subforums)
    }, error => {
      throwError(error);
    })
  }
}
