# Personal-finance-manager
Develop a Personal Finance Manager, a RESTful web application that allows users to manage financial transactions securely. The application will include user authentication, CRUD operations for transactions, and a balance calculation feature, all within a BFSI context. Candidates will use this project to learn and demonstrate proficiency in the specified tech stack.

# H2-CONSOLE

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

 
