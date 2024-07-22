import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Project } from 'src/app/shared/models/Project/Project';
import { Speciality } from 'src/app/shared/models/Project/Speciality';
import { status, Tasks } from 'src/app/shared/models/Project/Tasks';
import { DataLayerService } from 'src/app/shared/services/data-layer.service';
import { ProjectService } from 'src/app/shared/services/Project/project.service';
import { DeleteConfirmationComponent } from '../delete-confirmation/delete-confirmation.component';


@Component({
  selector: 'app-dashboard-project',
  templateUrl: './dashboard-project.component.html',
  styleUrls: ['./dashboard-project.component.scss']
})
export class DashboardProjectComponent implements OnInit{
  invoices: any[]
  todoTasks$: Tasks[]=  [];
  inProgressTasks$: Tasks[]=  [];
  doneTasks$:Tasks[]=  [];
  list!:Tasks[];
  projects!: Project [];
  projectId: string = '';
  project: Project = new Project();
  specialities: Speciality[] = this.project.specialities;
  constructor(
    private projectService: ProjectService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
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


  navigateToAddProject() {
    this.router.navigate(['/addprojects']);
  }

  calculateProgress(project: Project): number {
    const totalTasks = project.tasks.length;
    if (totalTasks === 0) return 0;

    const completedTasks = project.tasks.filter(task => task.status === status.Done).length;
    const inProgressTasks = project.tasks.filter(task => task.status === status.InProgress).length;
    
    return ((completedTasks + (inProgressTasks / 2)) / totalTasks) * 100;
  }

  getProjectStatus(project: Project): string {
    const today = new Date();
    const deadline = new Date(project.deadline);
    const diffInTime = deadline.getTime() - today.getTime();
    const diffInDays = Math.ceil(diffInTime / (1000 * 3600 * 24));

    if (diffInDays <= 0) {
      return 'Finished';
    } else {
      return `${diffInDays} days left`;
    }
  }

  isProjectFinished(project: Project): boolean {
    return this.getProjectStatus(project) === 'Finished';
  }


  openDeleteModal(project: any) {
    const modalRef = this.modalService.open(DeleteConfirmationComponent);
    modalRef.componentInstance.id = project.id;

    modalRef.result.then((result) => {
      if (result === 'Ok click') {
        this.deleteProject(project);
      }
    }, (reason) => {
      console.log(`Dismissed ${reason}`);
    });
  }
  deleteProject(project: any): void {
   
    this.projectService.delete(project.id).subscribe(
      () => {
       
        this.projects = this.projects.filter(p => p.id !== project.id);
        console.log('Project deleted successfully.');
      },
      error => {
        console.error('Error deleting project:', error);
     
      }
    );
  }



  navigateToView(id: number): void {
    this.router.navigate(['/project', id]);
  }

  assignStudentsToGroup(projectId: any, project: any): void {
    this.projectService.assignStudentsToGroup(projectId).subscribe(
      () => {
        // Update hasGroupProject field for the project after successfully assigning students to group
        const foundProject = this.projects.find(p => p.id === projectId);
        if (foundProject) {
          foundProject.hasGroupProject = true; // Assuming group assignment means the project has a group
        }
      },
      (error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred.';
        if (error.status === 400) {
          errorMessage = 'Bad request. The group might already be assigned to the project.';
        } else if (error.status === 500) {
          errorMessage = 'Internal server error. Please try again later.';
        } else if (error.status === 0) {
          errorMessage = 'Network error. Please check your connection.';
        }
        console.error('Error assigning group to project:', error.message);
        console.error('Error status:', error.status);
        console.error('Error details:', error.error);
        alert(errorMessage);
      }
    );
  }

 
}

