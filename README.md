# Personal-finance-manager
Develop a Personal Finance Manager, a RESTful web application that allows users to manage financial transactions securely. The application will include user authentication, CRUD operations for transactions, and a balance calculation feature, all within a BFSI context. Candidates will use this project to learn and demonstrate proficiency in the specified tech stack.

### Testing API Endpoints
``` bash

#register - data( username, email, password )
POST - http://localhost:8080/api/auth/register  - open route

#login - data( email, password )
POST - http://localhost:8080/api/auth/login - open route

#create transaction - data ( amount, description, category )
POST - http://localhost:8080/api/transactions - authenticated route

#get single transaction
GET - http://localhost:8080/api/transactions/?id={id}  - authenticated route

#get all transaction (if user autheticated only shows currentuser
GET - http://localhost:8080/api/transactions/all  - authenticated route

#update transaction - ( amount, description, category )
PUT - http://localhost:8080/api/transactions/?id={id}  - authenticated route

#delete single transaction
DELETE - http://localhost:8080/api/transactions/?id={id} - authenticated route

#get balance
GET - http://localhost:8080/api/account/getBalance  - authenticated route

```


## H2-CONSOLE

### Modifying and Displaying Dummy User & Transactions Data
``` bash
UPDATE users
    SET password = (
    SELECT password
FROM users
WHERE username = 'akashhkrishh'
);

SELECT * FROM USERS;
SELECT * FROM TRANSACTIONS;
```


## Challenges Faced & Solutions

### 1. Implementing Secure JWT Authentication

**Challenge:**  
Configuring stateless JWT authentication was tricky initially, especially handling token creation, validation, and securing routes without breaking the application flow.

**Solution:**
- I broke down the problem by creating dedicated components `JWTService` for token handling, `JwtFilter` for request interception, and a custom `UserService` to load user data. 
- Additionally, I created a static function `getCurrentUser()` to retrieve the currently logged-in user. 
- This modular approach helped isolate issues and made debugging easier.
---

### 2. Data Persistence with H2 Database

**Challenge:**  
Since the H2 database runs in-memory by default, data was lost every time the app restarted, making testing difficults.

**Solution:**  
- I used SQL initialization scripts (`schema.sql`, `data.sql`) to preload test data at startup.

---

### 3. Testing APIs Without a Frontend UI

**Challenge:**  
Without a frontend, validating the complete user and transaction flows, including authentication, was challenging.

**Solution:**  
- Postman to manually test each endpoint, including authentication flows with token handling. 
- Used the H2 console to inspect and verify backend data consistency during development.

 
