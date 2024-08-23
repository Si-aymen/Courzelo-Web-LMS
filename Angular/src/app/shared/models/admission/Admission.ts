import { UserResponse } from "../user/UserResponse";
import { AdmissionStatus } from "./AdmissionStatus";

export interface Admission {
    id: string;
    title: string;
    description: string;
    user: UserResponse;
    startDate: string;  
    endDate: string;    
    universityId: string;
    status: AdmissionStatus;  
}
