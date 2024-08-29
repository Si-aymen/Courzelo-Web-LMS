import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddpublicationComponent } from './addpublication.component';

describe('AddpublicationComponent', () => {
  let component: AddpublicationComponent;
  let fixture: ComponentFixture<AddpublicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddpublicationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddpublicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
