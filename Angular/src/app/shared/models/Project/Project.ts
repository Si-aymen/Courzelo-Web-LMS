import { Event } from "./Event";
import { Publication } from "./Publication";
import { Speciality } from "./Speciality";
import { Tasks } from "./Tasks";
import { Validate } from "./Validate";



export class Project {
    id!: number;
    name!: string;
    description!: string;
    difficulty!: Difficulty;
    validate!:Validate ;
    createdBy!:string;
    datedebut!: Date;
    deadline!: Date;
    number!: number;
    hasGroupProject!: boolean;
    tasks: Tasks[];
    events:Event [];
    publications:Publication [];


    // Define the specialities array within the class
    specialities: Speciality[] = [
        Speciality.BD,
        Speciality.Angular,
        Speciality.Spring,
        Speciality.DotNet,
        Speciality.Reseau,
        Speciality.IA,
        Speciality.Mobile,
        Speciality.Web,
        Speciality.Cloud,
        Speciality.DevOps,
        Speciality.Security,
        Speciality.Design,
        Speciality.Management,
        Speciality.Marketing,
        Speciality.Finance
    ];
}

export enum Difficulty {
    Hard, Easy, Medium
}
