import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TickettypeComponent } from './tickettype.component';

describe('TickettypeComponent', () => {
  let component: TickettypeComponent;
  let fixture: ComponentFixture<TickettypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TickettypeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TickettypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
