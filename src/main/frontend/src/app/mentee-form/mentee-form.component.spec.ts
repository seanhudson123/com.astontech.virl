import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MenteeFormComponent } from './mentee-form.component';

describe('MenteeFormComponent', () => {
  let component: MenteeFormComponent;
  let fixture: ComponentFixture<MenteeFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MenteeFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenteeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
