export interface CoursePostResponse {
    id: string;
    title: string;
    author: string;
    created: Date;
    files: Uint8Array[];
}
