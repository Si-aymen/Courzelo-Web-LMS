import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListForumComponent } from './list-forum.component';

describe('ListForumComponent', () => {
  let component: ListForumComponent;
  let fixture: ComponentFixture<ListForumComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListForumComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListForumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
