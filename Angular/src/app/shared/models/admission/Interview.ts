import { UserResponse } from "../user/UserResponse";
import { Admission } from "./Admission";

export interface Interview {
    id: string;
    interviewer: UserResponse;
    interviewee: string[];
    admission: Admission;
}