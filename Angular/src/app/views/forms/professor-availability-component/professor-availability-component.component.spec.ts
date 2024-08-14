import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfessorAvailabilityComponentComponent } from './professor-availability-component.component';

describe('ProfessorAvailabilityComponentComponent', () => {
  let component: ProfessorAvailabilityComponentComponent;
  let fixture: ComponentFixture<ProfessorAvailabilityComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfessorAvailabilityComponentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfessorAvailabilityComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
