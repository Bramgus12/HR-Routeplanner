import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectiveCourseDescriptionComponent } from './elective-course-description.component';

describe('ElectionCourseDescriptionComponent', () => {
  let component: ElectiveCourseDescriptionComponent;
  let fixture: ComponentFixture<ElectiveCourseDescriptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectiveCourseDescriptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectiveCourseDescriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
