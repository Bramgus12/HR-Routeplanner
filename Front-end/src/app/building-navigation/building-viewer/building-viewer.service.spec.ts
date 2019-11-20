import { TestBed } from '@angular/core/testing';

import { BuildingViewerService } from './building-viewer.service';

describe('BuildingViewerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BuildingViewerService = TestBed.get(BuildingViewerService);
    expect(service).toBeTruthy();
  });
});
