import {TimeSlot} from '../../views/forms/professor-availability-component/professor-availability-component.component';

export interface Professor {
    id: string;
    name: string;
    unavailableTimeSlots: TimeSlot[];
}
