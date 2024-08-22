import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientRevisionComponent } from './client-revision.component';

describe('ClientRevisionComponent', () => {
  let component: ClientRevisionComponent;
  let fixture: ComponentFixture<ClientRevisionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientRevisionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientRevisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
