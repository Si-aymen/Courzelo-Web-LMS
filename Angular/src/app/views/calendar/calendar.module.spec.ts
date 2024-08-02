import { CalendarModule } from "angular-calendar";


describe('CalendarModule', () => {
  let calendarModule: CalendarModule;

  beforeEach(() => {
    calendarModule = new CalendarModule();
  });

  it('should create an instance', () => {
    expect(calendarModule).toBeTruthy();
  });
});
