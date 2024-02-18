import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    expect(service.isLogged).toBe(false);
  });

  it('should log in', () => {
    const user = {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'username',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    }
    service.logIn(user);
    expect(service.isLogged).toBe(true);
  });

  it('should log out', () => {
    service.logOut();
    expect(service.isLogged).toBe(false);
  });

});
