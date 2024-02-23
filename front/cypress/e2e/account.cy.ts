/// <reference types="cypress" />

describe('Accound spec', () => {
    beforeEach(() => {
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 4,
                username: 'user',
                firstName: 'first',
                lastName: 'last',
                admin: false
            },
        });

        cy.intercept('GET', '/api/user/4', {
            body: {
                id: 4,
                email: 'mail@mail.com',
                firstName: 'first',
                lastName: 'last',
                admin: false,
                createdAt: '2024-02-16',
                updatedAt: '2024-02-16'
            }
        });

        cy.intercept({
            method: 'GET',
            url: '/api/session',
        }, []).as('session');
        cy.get('input[formControlName=email').type('mail@mail.com');
        cy.get('input[formControlName=password').type(`${'password'}{enter}{enter}`);
        cy.url().should('include', '/sessions');
    });

    it('should display user information', () => {
        cy.contains('span.link', 'Account').click();
        cy.url().should('include', '/me');
        cy.get('p:contains("Name:")').should('contain', 'first LAST');
        cy.get('p:contains("Email:")').should('contain', 'mail@mail.com');
        cy.get('.my2 > .mat-focus-indicator').should('exist');
    });

    it('should allow user to go back', () => {
        cy.intercept('GET', '/api.session', {});
        cy.contains('span.link', 'Account').click();
        cy.url().should('include', '/me');
        cy.get('button.mat-icon-button').click();
        cy.url().should('not.include', '/me');
    });

    it('should delete account', () => {
        cy.intercept('GET', '/api.session', {});
        cy.intercept('DELETE', '/api/user/4', {
            statusCode: 200
        }).as('delete user');
        cy.contains('span.link', 'Account').click();
        cy.url().should('include', '/me');
        cy.get('.my2 > .mat-focus-indicator').click();
        cy.get('.mat-simple-snack-bar-content').should('exist');
        cy.contains('span.link', 'Account').should('not.exist');
        cy.url().should('not.include', '/me');
    });
});