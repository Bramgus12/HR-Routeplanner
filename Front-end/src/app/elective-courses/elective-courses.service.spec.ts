import { TestBed } from '@angular/core/testing';

import { ElectiveCourseService } from './election-course.service';

describe('ElectiveCourseService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ElectiveCourseService = TestBed.get(ElectiveCourseService);
    expect(service).toBeTruthy();
  });
});
