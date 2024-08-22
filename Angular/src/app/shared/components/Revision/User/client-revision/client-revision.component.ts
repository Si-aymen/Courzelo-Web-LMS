import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { Revision } from 'src/app/shared/models/Revision/Revision';
import { RevisionService } from 'src/app/shared/services/Revision/revision.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-client-revision',
  templateUrl: './client-revision.component.html',
  styleUrls: ['./client-revision.component.scss'] ,
  animations: [SharedAnimations]
})
export class ClientRevisionComponent implements OnInit {
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

 
  participate(revisionId: string): void {
    console.log('Navigating to ConsultRevisionComponent with ID:', revisionId);
    this.router.navigate(['/participaterevision', revisionId]);
  }
  
}