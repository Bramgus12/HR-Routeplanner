import { TestBed } from '@angular/core/testing';

import { OpenrouteserviceService } from './openrouteservice.service';

describe('OpenrouteserviceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: OpenrouteserviceService = TestBed.get(OpenrouteserviceService);
    expect(service).toBeTruthy();
  });
});
