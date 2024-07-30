export interface AttendanceDTO {
    studentId: string;
    studentName: string;
    date: string;
    status: AttendanceStatus;
    minutesLate?: number;
}

export enum AttendanceStatus {
    PRESENT = 'PRESENT',
    ABSENT = 'ABSENT',
    LATE = 'LATE',
    EXCUSED = 'EXCUSED',
    UNEXCUSED_ABSENT = 'UNEXCUSED_ABSENT'
 }
