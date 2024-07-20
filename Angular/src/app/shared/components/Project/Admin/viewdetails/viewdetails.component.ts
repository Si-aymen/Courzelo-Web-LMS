import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { FileMetadata } from 'src/app/shared/models/Project/FileMetadata ';
import { Difficulty, Project } from 'src/app/shared/models/Project/Project';
import { status, Tasks } from 'src/app/shared/models/Project/Tasks';
import { ProjectService } from 'src/app/shared/services/Project/project.service';

@Component({
  selector: 'app-viewdetails',
  templateUrl: './viewdetails.component.html',
  styleUrls: ['./viewdetails.component.scss']
})
export class ViewdetailsComponent implements OnInit {
  project: Project;
  invoices: any[] = [];
  todoTasks$: Tasks[] = [];
  inProgressTasks$: Tasks[] = [];
  doneTasks$: Tasks[] = [];
  list!: Tasks[];
  files: FileMetadata[] = [];
  selectedFile: File | null = null;
  projectId: string = '';
  pdfSrc: string | ArrayBuffer | null = null;
  tasks: Tasks[] = []; // Ensure you have a Tasks[] type
  projects: any[] = [];
  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const projectId = params.get('id');
      if (projectId) {
        console.log(`Project ID from route params: ${projectId}`);
        this.projectId = projectId;
        this.getProjectDetails(projectId);
        this.getProjectFiles(projectId);
        this.loadTasks(projectId); // Call loadTasks after setting the projectId
      } else {
        console.warn('No projectId found in route params');
      }
    });
  }

  loadTasks(projectId: string): void {
    this.projectService.getTasksByProjectId(projectId).subscribe(
      (data) => {
        this.tasks = data;
      },
      (error) => {
        console.error('Error fetching tasks', error);
      }
    );
  }

  openPdf(file: FileMetadata): void {
    const fileName = file.fileName;

    this.projectService.getFile(fileName).subscribe(response => {
      const url = window.URL.createObjectURL(response);
      this.pdfSrc = url;
      window.open(this.pdfSrc); // This opens the PDF in a new tab
    }, error => {
      console.error('Error fetching PDF file:', error);
    });
  }

  getProjectDetails(id: string): void {
    this.projectService.getProjectById(id).subscribe(
      data => {
        this.project = data;
      },
      error => {
        console.error(error);
      }
    );
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

  onUpdateProject(): void {
    if (this.project.id) {
      this.projectService.updateproject(this.project).subscribe(
        updatedProject => {
          console.log('Project updated successfully:', updatedProject);
          this.navigateToProjects();
        },
        error => {
          console.error('Error updating project:', error);
        }
      );
    } else {
      console.error('Project ID is missing.');
    }
  }

  onCancel(): void {
    this.navigateToProjects();
  }

  private navigateToProjects(): void {
    this.router.navigate(['/projects']);
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadFile(): void {
    if (this.selectedFile) {
      this.projectService.upload(this.selectedFile, this.projectId).subscribe(
        response => {
          console.log('File uploaded successfully', response);
          this.getProjectFiles(this.projectId); // Reload files after upload
        },
        error => {
          console.error('Error uploading file', error);
        }
      );
    } else {
      alert('Please select a file.');
    }
  }

  getProjectFiles(id: string): void {
    console.log(`Calling getProjectFiles with projectId: ${id}`);
    this.projectService.getFilesByProjectId(id).subscribe(
      data => {
        console.log('Response received from getFilesByProjectId:', data);
        this.files = data;
        console.log('Files array after assignment:', this.files);
      },
      error => {
        console.error('Error fetching files:', error);
      }
    );
  }

  validateProject(): void {
    if (this.projectId) {
      this.projectService.validateProject(this.projectId).subscribe(
        updatedProject => {
          this.project = updatedProject;
          console.log('Project validated successfully:', updatedProject);
        },
        error => {
          console.error('Error validating project:', error);
        }
      );
    }
  }

  checkProjectStatus(): void {
    this.projectService.checkProjectStatus().subscribe(
      () => {
        console.log('Project statuses checked and updated.');
      },
      error => {
        console.error('Error checking project statuses:', error);
      }
    );
  }


  
  
}