import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Project } from 'src/app/shared/models/Project/Project';
import { ProjectService } from 'src/app/shared/services/Project/project.service';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent {
  projects!: Project [];
  constructor(
    private projectService: ProjectService,

    private router: Router,
   
  ) { }


  ngOnInit() {
    this.loadProjects();
   
  }

  loadProjects() {
        this.projectService.getAllproject().subscribe(
          (data: any) => {
            if (Array.isArray(data)) {
              this.projects = data;
              console.log('Projects with tasks:', this.projects); 
            } else {
              console.error("Expected an array of projects, but received:", data);
            }
          },
          (error) => {
            console.log(error);
          }
        );
      }

 
  navigateToProjectDetails(projectId: string) {
    this.router.navigate(['/projectdetails', projectId]);
  }
}
