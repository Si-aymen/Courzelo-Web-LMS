import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeforumComponent } from './homeforum.component';

describe('HomeforumComponent', () => {
  let component: HomeforumComponent;
  let fixture: ComponentFixture<HomeforumComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomeforumComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeforumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
