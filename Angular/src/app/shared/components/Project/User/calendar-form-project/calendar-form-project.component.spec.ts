import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarFormProjectComponent } from './calendar-form-project.component';

describe('CalendarFormProjectComponent', () => {
  let component: CalendarFormProjectComponent;
  let fixture: ComponentFixture<CalendarFormProjectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CalendarFormProjectComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CalendarFormProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
