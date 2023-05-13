describe('LoginForm Component', () => {
    const uniqueEmail = `valid${Date.now()}@email.com`;
    before(() => {
        cy.visit('/register');
        cy.get('#inpName').type('Test User');
        cy.get('#inpEmail').type(uniqueEmail);
        cy.get('#inpPassword').type('validpassword');

        cy.request({
            method: 'POST',
            url: 'http://localhost:8080/api/auth/register',
            headers: {
                'Content-Type': 'application/json',
            },
            body: {
                name: 'Test User',
                email: uniqueEmail,
                password: 'validpassword',
            },
        }).then((response) => {
            expect(response.status).to.equal(200);
            expect(response.body.userId).to.not.be.null;
            expect(response.body.userRole).to.not.be.null;
        });
    });


    beforeEach(() => {
        cy.visit('/login');
    });

    it('should render the LoginForm component correctly', () => {
        cy.get('.form').should('be.visible');
        cy.get('#inpEmail').should('be.visible');
        cy.get('#inpPassword').should('be.visible');
        cy.get('.button-wrapper').should('be.visible');
        cy.get('.divider').should('be.visible');
    });

    it('should log in successfully with valid credentials', () => {
        cy.get('#inpEmail').type(uniqueEmail);
        cy.get('#inpPassword').type("validpassword");

        cy.request({
            method: 'POST',
            url: 'http://localhost:8080/api/auth/login',
            headers: {
                'Content-Type': 'application/json',
            },
            body: {
                name: 'Test User',
                email: uniqueEmail,
                password: 'validpassword',
            },
        }).then((response) => {
            expect(response.status).to.equal(200);
            expect(response.body.token).to.not.be.null;
            expect(response.body.userRole).to.not.be.null;
            expect(response.body.userId).to.not.be.null;
        });
    });
});
