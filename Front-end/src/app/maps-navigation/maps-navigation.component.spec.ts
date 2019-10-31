import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapsNavigationComponent } from './maps-navigation.component';

describe('MapsNavigationComponent', () => {
  let component: MapsNavigationComponent;
  let fixture: ComponentFixture<MapsNavigationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MapsNavigationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapsNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
