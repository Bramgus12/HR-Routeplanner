import { TestBed } from '@angular/core/testing';

import { GoogleMapsApiLoaderService } from './google-maps-api-loader.service';

describe('GoogleMapsApiLoaderService', () => {
  let service: GoogleMapsApiLoaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GoogleMapsApiLoaderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
