import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyRevisionComponent } from './modify-revision.component';

describe('ModifyRevisionComponent', () => {
  let component: ModifyRevisionComponent;
  let fixture: ComponentFixture<ModifyRevisionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifyRevisionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyRevisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
