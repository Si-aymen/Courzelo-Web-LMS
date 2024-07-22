import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatefaqComponent } from './updatefaq.component';

describe('UpdatefaqComponent', () => {
  let component: UpdatefaqComponent;
  let fixture: ComponentFixture<UpdatefaqComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdatefaqComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatefaqComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
