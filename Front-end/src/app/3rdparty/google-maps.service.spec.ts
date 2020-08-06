import { TestBed } from '@angular/core/testing';

import { GoogleMapsService } from './google-maps.service';

describe('GoogleMapsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GoogleMapsService = TestBed.get(GoogleMapsService);
    expect(service).toBeTruthy();
  });
});
