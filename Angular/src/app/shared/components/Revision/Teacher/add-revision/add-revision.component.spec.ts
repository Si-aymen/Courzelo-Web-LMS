import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRevisionComponent } from './add-revision.component';

describe('AddRevisionComponent', () => {
  let component: AddRevisionComponent;
  let fixture: ComponentFixture<AddRevisionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddRevisionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRevisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
