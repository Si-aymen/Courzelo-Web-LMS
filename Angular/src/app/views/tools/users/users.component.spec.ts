import { ComponentFixture, TestBed } from '@angular/core/testing';

<<<<<<<< HEAD:Angular/src/app/views/forms/timetable/timetable.component.spec.ts
import { TimetableComponent } from './timetable.component';

describe('TimetableComponent', () => {
  let component: TimetableComponent;
  let fixture: ComponentFixture<TimetableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimetableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TimetableComponent);
========
import { Users } from './users.component';

describe('SuperAdminComponent', () => {
  let component: Users;
  let fixture: ComponentFixture<Users>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Users ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Users);
>>>>>>>> develop:Angular/src/app/views/tools/users/users.component.spec.ts
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
