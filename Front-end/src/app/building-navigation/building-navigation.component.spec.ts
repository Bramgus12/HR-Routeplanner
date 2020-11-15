import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { BuildingNavigationComponent } from './building-navigation.component';

describe('BuildingNavigationComponent', () => {
  let component: BuildingNavigationComponent;
  let fixture: ComponentFixture<BuildingNavigationComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildingNavigationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
