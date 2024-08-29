import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Project } from 'src/app/shared/models/Project/Project';
import { ProjectService } from 'src/app/shared/services/Project/project.service';
import { PublicationService } from 'src/app/shared/services/Project/publication.service';
import { AddpublicationComponent } from '../addpublication/addpublication.component';
import { Publication } from 'src/app/shared/models/Project/Publication';
import { Reaction } from 'src/app/shared/models/Project/Reaction';
import {  Commment } from 'src/app/shared/models/Project/Commment';



@Component({
  selector: 'app-publication',
  templateUrl: './publication.component.html',
  styleUrls: ['./publication.component.scss']
})
export class PublicationComponent implements OnInit {
  project: Project | undefined;
  @Input() projectId!: string;

  publications: Publication[] = [];
  commentContent: string[] = [];
  currentUser: any; 

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private publicationService: PublicationService,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.projectId = this.route.snapshot.paramMap.get('id')!;
    if (this.projectId) {
      this.getProjectDetails(this.projectId);
      this.loadPublications(this.projectId);
    }
  }

  loadPublications(projectId: string): void {
    this.publicationService.getPublicationsByProjectId(projectId).subscribe(
      (data) => {
        this.publications = data.map(pub => ({
          ...pub,
          likes: pub.likes || 0,
          dislikes: pub.dislikes || 0,
          comments: [] // Initialize comments array
        }));
        // Fetch comments for each publication
        this.publications.forEach(pub => this.getComments(pub.id));
      },
      (error) => {
        console.error(error);
      }
    );
  }

  getProjectDetails(projectId: string) {
    this.projectService.getProjectById(projectId).subscribe(
      (data: Project) => {
        this.project = data;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  getComments(publicationId: string): void {
    this.publicationService.getCommentsByPublicationId(publicationId).subscribe(
      (comments: Commment[]) => {
        const publication = this.publications.find(pub => pub.id === publicationId);
        if (publication) {
          publication.comments = comments;
        }
      },
      (error) => {
        console.error('Error fetching comments:', error);
      }
    );
  }

  openAddPublicationModal() {
    const modalRef = this.modalService.open(AddpublicationComponent);
    modalRef.result.then(
      () => {
        // Refresh publications after adding a new publication
        this.loadPublications(this.projectId);
      },
      (error) => {
        // Handle errors if needed
        console.error('Error:', error);
      }
    );
  }

  likePublication(id: string): void {
    this.publicationService.likePublication(id).subscribe({
      next: (updatedPublication) => {
        this.updatePublicationInList(updatedPublication);
      },
      error: (error) => {
        console.error('Error liking publication:', error);
      }
    });
  }

  dislikePublication(id: string): void {
    this.publicationService.dislikePublication(id).subscribe({
      next: (updatedPublication) => {
        this.updatePublicationInList(updatedPublication);
      },
      error: (error) => {
        console.error('Error disliking publication:', error);
      }
    });
  }

  private updatePublicationInList(updatedPublication: Publication): void {
    const index = this.publications.findIndex(p => p.id === updatedPublication.id);
    if (index !== -1) {
      this.publications[index] = updatedPublication;
    }
  }

  addComment(publicationId: string, index: number): void {
    const newComment: Commment = {
      content: this.commentContent[index],
      dateTime: new Date(),
      publication: { id: publicationId } as Publication
    };

    this.publicationService.addComment(publicationId, newComment).subscribe({
      next: (comment: Commment) => {
        // Find the publication and add the comment to it
        const publication = this.publications.find(pub => pub.id === publicationId);
        if (publication) {
          publication.comments.push(comment); // Push the actual comment instance
          publication.commentsCount++; // Optionally update the comments count
        }

        // Clear the input field
        this.commentContent[index] = '';
      },
      error: (err) => {
        console.error('Error adding comment:', err);
      }
    });
  }
}