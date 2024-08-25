import {SimplifiedCourseResponse} from './SimplifiedCourseResponse';

export interface GroupResponse {
    id?: string;
    name?: string;
    institutionID?: string;
    students?: string[];
    courses?: SimplifiedCourseResponse[];
}
