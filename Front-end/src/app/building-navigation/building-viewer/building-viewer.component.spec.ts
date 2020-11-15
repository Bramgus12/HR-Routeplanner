import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { BuildingViewerComponent } from './building-viewer.component';

describe('BuildingViewerComponent', () => {
  let component: BuildingViewerComponent;
  let fixture: ComponentFixture<BuildingViewerComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildingViewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
