import {CoursePostResponse} from './CoursePostResponse';
import {Quiz} from '../Quiz';

export interface CourseResponse {
    id?: string;
    name?: string;
    description?: string;
    credit?: number;
    teacher?: string;
    group?: string;
    institutionID?: string;
    posts?: CoursePostResponse[];
    quizzes?: Quiz[];
}
