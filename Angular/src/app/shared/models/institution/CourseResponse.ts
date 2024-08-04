import {CoursePostResponse} from './CoursePostResponse';

export interface CourseResponse {
    id: string;
    name: string;
    description: string;
    credit: number;
    teachers: string[];
    students: string[];
    posts: CoursePostResponse[];
}
