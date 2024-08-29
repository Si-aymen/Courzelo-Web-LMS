import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuizrevisionComponent } from './quizrevision.component';

describe('QuizrevisionComponent', () => {
  let component: QuizrevisionComponent;
  let fixture: ComponentFixture<QuizrevisionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QuizrevisionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuizrevisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
