import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Revision } from 'src/app/shared/models/Revision/Revision';

import { subjectRevision } from 'src/app/shared/models/Revision/SubjectRevision';
import { RevisionService } from 'src/app/shared/services/Revision/revision.service';

@Component({
  selector: 'app-modify-revision',
  templateUrl: './modify-revision.component.html',
  styleUrls: ['./modify-revision.component.scss']
})
export class ModifyRevisionComponent implements OnInit {
  @Input() revision: Revision ;

  titre: string;
  nbrmax: number;
  subjectRevisions = Object.values(subjectRevision); 
  selectedSubjectRevision: subjectRevision;

  constructor(
    public activeModal: NgbActiveModal, 
    private revisionService: RevisionService
  ) {}

  ngOnInit(): void {
    if (!this.revision) {
      console.error('No revision data was provided');
      this.activeModal.dismiss('No revision data');
      return;
    }
  
    // Load the details of the selected revision
    this.titre = this.revision.titre;
    this.nbrmax = this.revision.nbrmax;
    this.selectedSubjectRevision = this.revision.subjectRevision;
  }
  
  updateRevision() {
    if (!this.revision || !this.revision.id) {
      console.error('No valid revision data to update');
      return;
    }
  
    const updatedRevision: Revision = {
      ...this.revision,
      titre: this.titre,
      nbrmax: this.nbrmax,
      subjectRevision: this.selectedSubjectRevision
    };
  
    this.revisionService.updateRevision(updatedRevision.id, updatedRevision).subscribe(
      (response) => {
        this.activeModal.close(updatedRevision);  // Pass the updated revision back to the parent component
      },
      (error) => {
        console.error('Error updating revision:', error);
      }
    );
  }
}