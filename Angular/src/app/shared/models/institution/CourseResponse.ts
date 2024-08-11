import {CoursePostResponse} from './CoursePostResponse';

export interface CourseResponse {
    id?: string;
    name?: string;
    description?: string;
    credit?: number;
    teacher?: string;
    group?: string;
    institutionID?: string;
    posts?: CoursePostResponse[];
}
