import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionCoursesComponent } from './election-courses.component';

describe('ElectionCoursesComponent', () => {
  let component: ElectionCoursesComponent;
  let fixture: ComponentFixture<ElectionCoursesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectionCoursesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectionCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
