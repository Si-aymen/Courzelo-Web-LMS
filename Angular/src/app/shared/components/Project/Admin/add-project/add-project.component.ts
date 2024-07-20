import {  AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProjectService } from 'src/app/shared/services/Project/project.service';
import { Difficulty, Project } from 'src/app/shared/models/Project/Project';
import { Speciality } from 'src/app/shared/models/Project/Speciality';
import { Tasks } from 'src/app/shared/models/Project/Tasks';

@Component({
  selector: 'app-add-project',
  templateUrl: './add-project.component.html',
  styleUrls: ['./add-project.component.scss']
})
export class AddProjectComponent  {
  projectForm: FormGroup;
  submitted = false;
  difficultyValues = Object.values(Difficulty);
  project: Project = new Project(); // Instantiate a new Project object
  specialities: { name: string }[] = [];
  tasks: Tasks[] = [];
  task: Tasks = new Tasks();
  constructor(private projectService: ProjectService, private router: Router, private formBuilder: FormBuilder) {
    this.projectForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description:['',[Validators.required, Validators.minLength(3)]],
      difficulty: ['', Validators.required],
      deadline: ['', Validators.required],
      createdBy: ['', Validators.required],
      datedebut: ['', Validators.required],
      specialities: [[], Validators.required],
      number: ['', [Validators.required, Validators.min(1)]],
      taskName: ['']
    }, {
      validator: this.dateValidator('datedebut', 'deadline')
    });
  }

  ngOnInit() {
    // Convert Speciality enum to array of objects with name property
    this.specialities = Object.values(Speciality).map(speciality => ({ name: speciality }));
  }

  addTask(): void {
    const taskName = this.f['taskName'].value.trim();
    if (taskName === '') {
      return;
    }
  
    // Create a new task object with the provided name
    const newTask: Tasks = new Tasks();
    newTask.name = taskName;
  
    // Push the new task to the tasks array
    this.tasks.push(newTask);
  
    // Clear the task name input field
    this.f['taskName'].setValue('');
  }
  customValidator(control: AbstractControl) {
    if (control.value && control.value.trim() === '') {
      return { 'required': true };
    }
    return null;
  }

  
  dateValidator(start: string, end: string) {
    return (group: FormGroup): {[key: string]: any} | null => { // Adjusted return type
      const startDate = group.controls[start];
      const endDate = group.controls[end];
      
      if (startDate.value && endDate.value && startDate.value > endDate.value) {
        return { dateMismatch: true };
      }
  
      return null;
    };
  }
  

  get f() {
    return this.projectForm.controls;
  }

  saveProject(): void {
      // Set submitted flag after immediate operations
  setTimeout(() => {
    this.submitted = true;
  
    if (this.projectForm.invalid) {
      console.log('Form is invalid');
      return;
    }
  
    this.project.name = this.f['name'].value;
    this.project.description = this.f['description'].value;
    this.project.difficulty = this.f['difficulty'].value;
    this.project.datedebut = this.f['datedebut'].value;
    this.project.deadline = this.f['deadline'].value;
    this.project.createdBy = this.f['createdBy'].value;
    this.project.specialities = this.f['specialities'].value;
    this.project.number = this.f['number'].value;
    this.project.tasks = this.tasks;
  
    console.log('JSON to send:', JSON.stringify(this.project));
  
    this.projectService.createProject(this.project).subscribe(
      (response) => {
        console.log('Project created successfully:', response);
   
      },
      (error) => {
        console.error('Error creating project:', error);
      }
    ); })
  }
  navigateToProjectsDashboard() {
    this.router.navigate(['/projects']);
  }

}