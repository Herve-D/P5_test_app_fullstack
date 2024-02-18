import { HttpClient, HttpClientModule } from '@angular/common/http';
import { expect } from '@jest/globals';

import { AuthService } from './auth.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';


describe('AuthService', () => {
  let service: AuthService;
  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule,
      ]
    });
    service = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('should log in', () => {
    const user = {
      email: 'mail@mail.com',
      password: 'password'
    }

    service.login(user).subscribe((user) => {
      const req = httpTestingController.expectOne('api/auth/login');
      expect(req.request.method).toEqual('POST');
      req.flush({});
      httpTestingController.verify();
    })

  });

  it('should register', () => {
    const user = {
      email: 'mail@mail.com',
      firstName: 'first',
      lastName: 'last',
      password: 'password'
    };

    service.register(user).subscribe((user) => {
      expect(user).toBeTruthy();
      const req = httpTestingController.expectOne('api/auth/register');
      expect(req.request.method).toEqual('POST');
      req.flush({});
      httpTestingController.verify();
    })

  })

});
