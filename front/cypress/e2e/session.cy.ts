/// <reference types="cypress" />

import { admin, user, sessionOne, sessionTwo, teacher } from "./util/const";

describe('Session spec', () => {
    it('should login as admin successfully', () => {
        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body: admin,
        });

        cy.intercept('GET', '/api/session', []).as('session');

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

        cy.url().should('include', '/sessions');
    })

    it('should create a session successfully', () => {

        cy.url().should('include', '/sessions');

        cy.intercept('GET', '/api/teacher', { body: [teacher] });

        cy.intercept('POST', '/api/session', {
            body: sessionOne
        });

        cy.intercept('GET', '/api/session', {
            body: sessionOne
        });


        cy.url().should('include', '/sessions');
        cy.get('button[routerLink=create]').click();
        cy.get('input[formControlName=name]').type("Session One");
        cy.get('input[formControlName=date]').type("2024-02-16");
        cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('first last').click();
        cy.get('textarea[formControlName=description]').type("One");
        cy.get('button[type=submit]').click()
        cy.contains('Session created !').should('be.visible');
        cy.url().should('include', '/sessions');
    })

    it('should update a session successfully', () => {

        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', admin);

        cy.intercept('GET', '/api/session', [
            sessionOne
        ]);
        cy.intercept('GET', '/api/teacher', [
            teacher
        ]);

        cy.get('input[formControlName="email"]').type('yoga@studio.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();

        cy.intercept('GET', '/api/session/1', sessionOne);

        cy.intercept('GET', '/api/teacher/1', teacher);
        cy.contains('Edit').click();

        cy.get('input[formControlName="name"]').clear();

        cy.get('input[formControlName="name"]').type('Session One again');

        cy.get('textarea[formControlName="description"]').clear();

        cy.get('textarea[formControlName="description"]').type('One again');

        cy.intercept('PUT', '/api/session/1', {});
        cy.get('button[type="submit"]').click();

        cy.contains('Session updated !').should('be.visible');
    })

    it('should delete a session successfully', () => {

        cy.intercept('GET', '/api/teacher/1', { body: [teacher] });

        cy.intercept('GET', '/api/session/1', {
            body: sessionOne
        });

        cy.intercept('GET', '/api/session', {});

        cy.intercept('DELETE', '/api/session/1', { statusCode: 200 })

        cy.url().should('include', '/sessions');
        cy.contains('Detail').click();
        cy.url().should('include', '/sessions/detail')
        cy.contains('Delete').click();
        cy.url().should('include', '/sessions');
        cy.contains('Session deleted !').should('be.visible');
    })

});

describe('Session with user credential successfully', () => {
    it('should login as user', () => {
        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body: user,
        });


        cy.intercept('GET', '/api/session', {
            body: [sessionTwo]
        });


        cy.get('input[formControlName=email]').type("user@mail.com");
        cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);

        cy.url().should('include', '/sessions');

    })

    it('should access to session and particpate and unparticipate successfully', () => {

        cy.intercept('GET', '/api/teacher/1', { body: teacher });

        cy.intercept('GET', '/api/session/2', {
            body: { ...sessionOne, users: [] }
        });

        cy.url().should('include', '/sessions');
        cy.contains('Detail').click();
        cy.url().should('include', '/sessions/detail')
        cy.contains('Participate').should('exist');
        cy.intercept('GET', '/api/session/2', {
            body: { ...sessionOne, users: [1] }
        });
        cy.contains('Participate').click();
        cy.intercept('POST', '/api/session/2/participate/1', {})
        cy.intercept('GET', '/api/session/2', {
            body: { ...sessionOne, users: [] }
        });
    })

})