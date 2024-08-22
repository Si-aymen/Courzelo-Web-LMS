import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Revision } from 'src/app/shared/models/Revision/Revision';
import { subjectRevision } from 'src/app/shared/models/Revision/SubjectRevision';
import { RevisionService } from 'src/app/shared/services/Revision/revision.service';

@Component({
  selector: 'app-add-revision',
  templateUrl: './add-revision.component.html',
  styleUrls: ['./add-revision.component.scss']
})
export class AddRevisionComponent implements OnInit {
  titre: string = '';
  nbrmax: number | undefined;
  subjectRevisions = Object.values(subjectRevision); // Array of all SubjectRevision values
  selectedSubjectRevision: subjectRevision | undefined;

  constructor(public activeModal: NgbActiveModal, private revisionService: RevisionService) {}

  ngOnInit(): void {
    // Any initialization if needed
  }

  addRevision() {
    const newRevision: Omit<Revision, 'id'> = {
      titre: this.titre,
      nbrmax: this.nbrmax!,
      subjectRevision: this.selectedSubjectRevision!
    };

    this.revisionService.createRevision(newRevision).subscribe(
      (response) => {
        this.activeModal.close(response);
      },
      (error) => {
        console.error(error);
      }
    );
  }
}