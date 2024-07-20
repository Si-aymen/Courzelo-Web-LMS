import { Project } from "./Project";


export enum status {
    ToDo = 'ToDo',
    InProgress = 'InProgress',
    Done = 'Done'
  }
export class Tasks {
    id!: number;
    name!: string;
    project!:Project;
    status: status = status.ToDo;
    
  
}