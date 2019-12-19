import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionCourseDescriptionComponent } from './election-course-description.component';

describe('ElectionCourseDescriptionComponent', () => {
  let component: ElectionCourseDescriptionComponent;
  let fixture: ComponentFixture<ElectionCourseDescriptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectionCourseDescriptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectionCourseDescriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
