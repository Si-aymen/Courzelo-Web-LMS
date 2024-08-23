import { UserResponse } from "../user/UserResponse";
import { Admission } from "./Admission";

export interface Application {
    id:string,
    userid:UserResponse,
    admissionid:Admission,
    universityid:string,
}