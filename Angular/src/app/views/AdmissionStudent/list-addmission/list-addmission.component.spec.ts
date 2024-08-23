import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListAddmissionComponent } from './list-addmission.component';

describe('ListAddmissionComponent', () => {
  let component: ListAddmissionComponent;
  let fixture: ComponentFixture<ListAddmissionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListAddmissionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListAddmissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
