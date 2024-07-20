import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuizTableComponent } from './quiz-table.component';

describe('QuizTableComponent', () => {
  let component: QuizTableComponent;
  let fixture: ComponentFixture<QuizTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QuizTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuizTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
