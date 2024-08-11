import {SimplifiedCourseResponse} from '../institution/SimplifiedCourseResponse';

export interface UserEducationResponse {
    institutionID?: string;
    institutionName?: string;
    courses?: SimplifiedCourseResponse[];
    groupID?: string;
}
