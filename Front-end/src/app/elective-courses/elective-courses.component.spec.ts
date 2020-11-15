import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ElectiveCoursesComponent } from './election-courses.component';

describe('ElectiveCoursesComponent', () => {
  let component: ElectiveCoursesComponent;
  let fixture: ComponentFixture<ElectiveCoursesComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectiveCoursesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectiveCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
