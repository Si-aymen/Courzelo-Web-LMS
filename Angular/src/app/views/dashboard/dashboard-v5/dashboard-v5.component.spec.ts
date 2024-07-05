import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DashboardV5Component } from './dashboard-v5.component';

describe('DashboardV5Component', () => {
  let component: DashboardV5Component;
  let fixture: ComponentFixture<DashboardV5Component>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardV5Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardV5Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
