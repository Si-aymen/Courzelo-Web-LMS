
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Publication } from 'src/app/shared/models/Project/Publication';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { PublicationService } from 'src/app/shared/services/Project/publication.service';
import { Component, Input } from '@angular/core';
@Component({
  selector: 'app-addpublication',
  templateUrl: './addpublication.component.html',
  styleUrls: ['./addpublication.component.scss']
})
export class AddpublicationComponent {
  @Input() projectId!: string;
  content: string = '';

  // Initialize currentUser with appropriate values
  currentUser: UserResponse ;

  constructor(public activeModal: NgbActiveModal, private publicationService: PublicationService) {}

  addPublication() {
    const newPublication: Omit<Publication, 'id'> = {
      author: this.currentUser,
      dateTime: new Date(),
      content: this.content,
      likes: 0,
      dislikes: 0,
      commentsCount: 0,
      userReactions: {},
      comments: [],
      projectId: this.projectId
    };

    this.publicationService.createPublication(newPublication, this.projectId).subscribe(
      (response) => {
        this.activeModal.close(response);
      },
      (error) => {
        console.error(error);
      }
    );
  }
}