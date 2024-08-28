import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StageDetailsComponent } from './stage-details.component';

describe('StageDetailsComponent', () => {
  let component: StageDetailsComponent;
  let fixture: ComponentFixture<StageDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StageDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StageDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
