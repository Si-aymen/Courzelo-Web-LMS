import { ForumService } from 'src/app/shared/services/Forum/forum.service';
import { Component, OnInit } from '@angular/core';
import { SubForum } from 'src/app/shared/models/Forum/SubForum';

@Component({
  selector: 'app-sub-side-bar',
  templateUrl: './sub-side-bar.component.html',
  styleUrls: ['./sub-side-bar.component.scss']
})
export class SubSideBarComponent implements OnInit {
  subforums: Array<SubForum> = [];
  displayViewAll: boolean;

  constructor(private ForumService: ForumService) {
    this.ForumService.getSubForums().subscribe(data => {
      if (data.length > 3) {
        this.subforums = data.splice(0, 3);
        this.displayViewAll = true;
      } else {
        this.subforums = data;
      }
    });
  }

  ngOnInit(): void { }

}
