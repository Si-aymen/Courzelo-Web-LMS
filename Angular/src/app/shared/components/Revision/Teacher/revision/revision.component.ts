import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { Revision } from 'src/app/shared/models/Revision/Revision';
import { RevisionService } from 'src/app/shared/services/Revision/revision.service';
import { AddRevisionComponent } from '../add-revision/add-revision.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeleteConfirmationComponent } from '../../../Project/Admin/delete-confirmation/delete-confirmation.component';
import { ModifyRevisionComponent } from '../modify-revision/modify-revision.component';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-revision',
  templateUrl: './revision.component.html',
  styleUrls: ['./revision.component.scss'],
  animations: [SharedAnimations]
})
export class RevisionComponent implements OnInit {
  viewMode: 'list' | 'grid' = 'list';
  allSelected: boolean;
  page = 1;
  pageSize = 4; 
  revision: Revision | undefined; 
  revisions: Revision[] = [];

  constructor(
    private revisionService: RevisionService,
    private modalService: NgbModal ,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadRevisions();
    console.log('ngOnInit called with revision:', this.revisions);
  }

  loadRevisions(): void {
    this.revisionService.getAllRevisions().subscribe(
      (revisions: Revision[]) => {
        this.revisions = revisions;
       
      },
      error => {
        console.error('Error fetching revisions:', error);
      }
    );
  }

  openAddRevisionModal(): void {
    const modalRef = this.modalService.open(AddRevisionComponent);
    modalRef.result.then(
      (newRevision: Revision) => {
        if (newRevision) {
          this.revisions.push(newRevision);
         
        }
      },
      reason => {
        console.log('Modal dismissed with reason:', reason);
      }
    );
  }

  openDeleteModal(revision: Revision): void {
    if (!revision) {
      console.error('Revision is undefined');
      return;
    }

    const modalRef = this.modalService.open(DeleteConfirmationComponent);
    modalRef.componentInstance.id = revision.id;

    modalRef.result.then(
      (result) => {
        if (result === 'Ok click') {
          this.deleteRevision(revision);
        }
      },
      reason => {
        console.log(`Dismissed: ${reason}`);
      }
    );
  }

  deleteRevision(revision: Revision): void {
    if (revision && revision.id) {
      this.revisionService.delete(revision.id).subscribe(
        () => {
          this.revisions = this.revisions.filter(r => r.id !== revision.id);
          this.revisions = this.revisions.filter(r => r.id !== revision.id);
          console.log('Revision deleted successfully.');
        },
        error => {
          console.error('Error deleting revision:', error);
        }
      );
    }
  }

  openUpdateRevisionModal(revision: Revision): void {
    if (!revision) {
      console.error('Revision is undefined');
      return;
    }
    
    if (!revision.id) {
      console.error('Revision ID is undefined');
      return;
    }
  
    console.log('Opening update modal for revision:', revision.id);
  
    const modalRef = this.modalService.open(ModifyRevisionComponent, {
      size: 'lg',
      centered: true,
      backdrop: 'static',
      keyboard: false,
    });
  
    modalRef.componentInstance.revision = revision;
  
    modalRef.result.then(
      (updatedRevision: Revision) => {
        if (updatedRevision) {
          const index = this.revisions.findIndex(r => r.id === updatedRevision.id);
          if (index !== -1) {
            this.revisions[index] = updatedRevision;
          }
        }
      },
      reason => {
        console.log(`Dismissed: ${reason}`);
      }
    );
  }

  getrevisionDetails() {
    const revisionId = this.route.snapshot.paramMap.get('id');
    if (revisionId) {
      this.revisionService.getRevisionById(revisionId).subscribe(
        (data: Revision) => {
          this.revision = data;
        },
        (error) => {
          console.error(error);
        }
      );
    }
  }

 
  consult(revisionId: string): void {
    console.log('Navigating to ConsultRevisionComponent with ID:', revisionId);
    this.router.navigate(['/consultrevision', revisionId]);
  }
  
}