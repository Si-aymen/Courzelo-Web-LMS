import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultRevisionComponent } from './consult-revision.component';

describe('ConsultRevisionComponent', () => {
  let component: ConsultRevisionComponent;
  let fixture: ComponentFixture<ConsultRevisionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsultRevisionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsultRevisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
