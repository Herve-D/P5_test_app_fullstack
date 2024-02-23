/// <reference types="cypress" />

describe('Register spec', () => {
    beforeEach(() => {
        cy.visit('/register');
    });

    it('Register successfull', () => {
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 200
        });
        cy.get('input[formControlName=firstName]').type('first');
        cy.get('input[formControlName=lastName]').type('last');
        cy.get('input[formControlName=email]').type('test@mail.com');
        cy.get('input[formControlName=password]').type(`${'password000'}{enter}{enter}`);
        cy.url().should('include', '/login');
    });

    it('Error if email already registered', () => {
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400
        });
        cy.get('input[formControlName=firstName]').type('first');
        cy.get('input[formControlName=lastName]').type('last');
        cy.get('input[formControlName=email]').type('test@mail.com');
        cy.get('input[formControlName=password]').type(`${'password000'}{enter}{enter}`);
        cy.get('.error').should('be.visible');
    });

    it('Button not enabled if empty email', () => {
        cy.get('input[formControlName=firstName]').type('first');
        cy.get('input[formControlName=lastName]').type('last');
        cy.get('input[formControlName=password]').type(`${'password000'}{enter}{enter}`);
        cy.get('input[formControlName=email]').clear();
        cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
        cy.get('button[type=submit]').should('be.disabled');
    });

    it('Button not enabled if incorrect password', () => {
        cy.get('input[formControlName=firstName]').type('first');
        cy.get('input[formControlName=lastName]').type('last');
        cy.get('input[formControlName=email]').type('test@mail.com');
        cy.get('input[formControlName=password]').type('000');
        cy.get('input[formControlName=password]').should('have.class', 'ng-invalid');
        cy.get('button[type=submit').should('be.disabled');
    });

    it('Register failed', () => {
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 401,
            body: {
                message: 'Unauthorized'
            }
        });
        cy.get('input[formControlName=email]').type("yogazer@studio.com")
        cy.get('input[formControlName=firstName]').type("yoga")
        cy.get('input[formControlName=lastName]').type("studio")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.get('.error').should('contain', 'An error occurred');
    });
});