import { Component, ElementRef, OnInit, Renderer2, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { status, Tasks } from 'src/app/shared/models/Project/Tasks';
import { ProjectService } from 'src/app/shared/services/Project/project.service';

@Component({
  selector: 'app-progress-dashboard',
  templateUrl: './progress-dashboard.component.html',
  styleUrls: ['./progress-dashboard.component.scss']
})
export class ProgressDashboardComponent implements OnInit {
  @ViewChild('winAudio') winAudio: ElementRef;
  todoTasks$: Tasks[] = [];
  inProgressTasks$: Tasks[] = [];
  doneTasks$: Tasks[] = [];
  projectId: string = ''; // Initialize with an empty string or null
  projectName: string = ''; // Optionally, store project name for display purposes

  constructor(
    private route: ActivatedRoute,
    private tasksService: ProjectService,
    private renderer: Renderer2
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.projectId = params.get('projectId')!;
      this.loadTasks();
    });
  }

  playWinningMusic(): void {
    const audio: HTMLAudioElement = this.winAudio.nativeElement;
    audio.play().catch(error => console.error('Audio playback error:', error));
  }
  
  loadTasks() {
    this.tasksService.getTasksByStatus(status.ToDo)
      .subscribe(tasks => this.todoTasks$ = tasks);
    this.tasksService.getTasksByStatus(status.InProgress)
      .subscribe(tasks => this.inProgressTasks$ = tasks);
    this.tasksService.getTasksByStatus(status.Done)
      .subscribe(tasks => {
        this.doneTasks$ = tasks;
        if (this.areAllTasksDone()) {
          this.playWinningMusic();
         
        }
      });
  }

  onTaskDropped(event: any, newStatus: string) {
    const taskId = event.item.element.nativeElement.id;
    const newStatusEnum: status = status[newStatus as keyof typeof status]; // Convert string to enum
    this.tasksService.moveTask(taskId, newStatusEnum).subscribe(() => { this.loadTasks(); });
  }

  calculateProgress(): number {
    let totalTasks = this.todoTasks$.length + this.inProgressTasks$.length + this.doneTasks$.length;
    let completedTasks = this.doneTasks$.length;
    return (completedTasks / totalTasks) * 100;
  }

  areAllTasksDone(): boolean {
    return this.todoTasks$.length === 0 && this.inProgressTasks$.length === 0;
  }

 
}