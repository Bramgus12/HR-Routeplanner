import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NextStateButtonComponent } from './next-state-button.component';

describe('NextStateButtonComponent', () => {
  let component: NextStateButtonComponent;
  let fixture: ComponentFixture<NextStateButtonComponent>;

  beforeEach(async(() => {
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
