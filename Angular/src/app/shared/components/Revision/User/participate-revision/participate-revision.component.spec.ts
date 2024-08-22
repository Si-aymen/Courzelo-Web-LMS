import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParticipateRevisionComponent } from './participate-revision.component';

describe('ParticipateRevisionComponent', () => {
  let component: ParticipateRevisionComponent;
  let fixture: ComponentFixture<ParticipateRevisionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParticipateRevisionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParticipateRevisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
