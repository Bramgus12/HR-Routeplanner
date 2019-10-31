import { TestBed } from '@angular/core/testing';

import { OvApiService } from './ov-api.service';

describe('OvApiService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: OvApiService = TestBed.get(OvApiService);
    expect(service).toBeTruthy();
  });
});
