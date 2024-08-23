import { AbstractControl, ValidatorFn } from '@angular/forms';

// Custom validator function
export function dateRangeValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: boolean } | null => {
    const startDate = control.get('startDate')?.value;
    const endDate = control.get('endDate')?.value;
    const currentDate = new Date();

    // Convert dates to Date objects if they aren't already
    const start = new Date(startDate);
    const end = new Date(endDate);

    // Check if start date is greater than the current date
    if (startDate && start <= currentDate) {
      return { 'startDateInvalid': true };
    }

    // Check if start date is greater than end date
    if (startDate && endDate && start > end) {
      return { 'dateRangeInvalid': true };
    }

    return null;
  };
}
