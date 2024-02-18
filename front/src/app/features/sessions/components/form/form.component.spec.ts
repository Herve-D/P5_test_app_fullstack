import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  describe('session', () => {
    let formComponent: FormComponent;
    let formBuilder: FormBuilder;
    let sessionApiService: any;
    let matSnackBar: any;
    let router: any;

    let activatedRoute: any;
    let sessionService: any;
    let teacherService: any;

    formBuilder = new FormBuilder();

    sessionApiService = {
      create: jest.fn().mockReturnValue({ subscribe: jest.fn() }),
      update: jest.fn().mockReturnValue(of({}))
    };

    matSnackBar = {
      open: jest.fn()
    };

    router = {
      navigate: jest.fn()
    }
    activatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn()
        }
      }
    };
    teacherService = { all: jest.fn() };

    beforeEach(() => {
      formComponent = new FormComponent(
        activatedRoute as ActivatedRoute,
        formBuilder,
        matSnackBar as MatSnackBar,
        sessionApiService as SessionApiService,
        sessionService,
        teacherService,
        router as Router
      );
    });

    it('should create a session', () => {
      const session: Session = {
        id: 1,
        name: 'name',
        description: 'description',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      };

      const sessionForm = formComponent.sessionForm?.setValue(session);
      formComponent.onUpdate = false;
      formComponent.submit();

      expect(sessionApiService.create).toHaveBeenCalledTimes(1);
      expect(sessionApiService.create).toHaveBeenCalledWith(sessionForm);

      let message = 'Session has been created.';
      let navigationRoute = ['sessions'];

      sessionApiService.create.mockReturnValue(of(
        matSnackBar.open(message),
        router.navigate(navigationRoute)
      ));
      expect(matSnackBar.open).toHaveBeenCalledTimes(1);
      expect(matSnackBar.open).toHaveBeenCalledWith(message);
      expect(router.navigate).toHaveBeenCalledWith(navigationRoute);
    });

    it('should update the session', () => {
      formComponent.onUpdate = true;

      formComponent.teachers$ = teacherService.all();

      const identity = activatedRoute.snapshot.paramMap.get.mockReturnValue('1');
      const session: Session = {
        id: 1,
        name: 'name_bis',
        description: 'description_bis',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      };

      formComponent.submit();
      sessionApiService.update.mockReturnValueOnce(
        of(session).subscribe((sessions) => {
          expect(sessionApiService.update).toHaveBeenCalledTimes(1);
          expect(sessionApiService.update).toHaveBeenCalledWith(identity, sessions);

          expect(matSnackBar.open).toHaveBeenCalledWith('Session has been updated.');
          expect(router.navigate).toHaveBeenCalledWith(['sessions']);
        })
      );
    });

    it('show error  if required fields are empty', () => {
      const emptySession: Session = {
        id: 1,
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
        name: '',
        description: '',
        date: new Date(),
      };

      const sessionForm = formComponent.sessionForm?.setValue(emptySession);
      formComponent.onUpdate = false;
      let message = "Required field is empty."
      sessionApiService.create.mockReturnValue(
        of(sessionForm).subscribe((error: any) => {
          expect(sessionApiService.create).toBe(message);
        })
      );

      formComponent.onUpdate = true;

      sessionApiService.update.mockReturnValue(
        of(sessionForm).subscribe((error: any) => {
          expect(sessionApiService.update).toBe(message);
        })
      );
    });
  });

});
