import { TestBed } from '@angular/core/testing';

import { BuildingNavigationService } from './building-navigation.service';

describe('BuildingNavigationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BuildingNavigationService = TestBed.get(BuildingNavigationService);
    expect(service).toBeTruthy();
  });
});
