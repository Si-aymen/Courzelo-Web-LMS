import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoteApplicationComponent } from './note-application.component';

describe('NoteApplicationComponent', () => {
  let component: NoteApplicationComponent;
  let fixture: ComponentFixture<NoteApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NoteApplicationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NoteApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
