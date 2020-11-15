import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { NextStateButtonComponent } from './next-state-button.component';

describe('NextStateButtonComponent', () => {
  let component: NextStateButtonComponent;
  let fixture: ComponentFixture<NextStateButtonComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ NextStateButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NextStateButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
