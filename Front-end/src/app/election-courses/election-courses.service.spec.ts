import { TestBed } from '@angular/core/testing';

import { ElectionCourseService } from './election-course.service';

describe('ElectionCourseService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ElectionCourseService = TestBed.get(ElectionCourseService);
    expect(service).toBeTruthy();
  });
});
