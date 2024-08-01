import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendaneHistoryComponent } from './attendane-history.component';

describe('AttendaneHistoryComponent', () => {
  let component: AttendaneHistoryComponent;
  let fixture: ComponentFixture<AttendaneHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AttendaneHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AttendaneHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
