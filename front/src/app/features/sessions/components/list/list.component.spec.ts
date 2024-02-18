import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get session list', () => {
    let response: Session[] = [{
      id: 1,
      name: 'Name',
      description: 'Description',
      date: new Date("2024-02-09T00:00:00"),
      teacher_id: 0,
      users: [],
      createdAt: new Date("2024-02-09T00:00:00"),
      updatedAt: new Date("2024-02-09T00:00:00"),
    }]

    component.sessions$.subscribe(session => {
      response = session;
    })

    let expectedResponse: Session[] = [{
      id: 1,
      name: 'Name',
      description: 'Description',
      date: new Date("2024-02-09T00:00:00"),
      teacher_id: 0,
      users: [],
      createdAt: new Date("2024-02-09T00:00:00"),
      updatedAt: new Date("2024-02-09T00:00:00"),
    }]

    expect(expectedResponse).toEqual(response);
  })

});
