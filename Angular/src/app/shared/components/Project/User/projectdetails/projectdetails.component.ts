import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Project } from 'src/app/shared/models/Project/Project';
import { ProjectService } from 'src/app/shared/services/Project/project.service';

@Component({
  selector: 'app-projectdetails',
  templateUrl: './projectdetails.component.html',
  styleUrls: ['./projectdetails.component.scss']
})
export class ProjectDetailsComponent implements OnInit {
  project: Project | undefined; // Initialize as undefined or provide default values

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private router: Router
  ) {}

  ngOnInit() {
    this.getProjectDetails();
  }

  getProjectDetails() {
    const projectId = this.route.snapshot.paramMap.get('id');
    if (projectId) {
      this.projectService.getProjectById(projectId).subscribe(
        (data: Project) => {
          this.project = data;
        },
        (error) => {
          console.error(error);
        }
      );
    }
  }

  chat()   {
    this.router.navigate(['/chat']);
  }

  calendar(projectId: string) {
    this.router.navigate(['/projectcalendar', projectId]);
  }

  ProgressDashboard(projectId: string) {
    this.router.navigate(['/ProgressDashboard', projectId]);
  }
  publication(projectId: string)  {
    this.router.navigate(['/publication', projectId]);
  }
}