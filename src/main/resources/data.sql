INSERT INTO users (id, email, password, username) VALUES
    ('11111111-1111-1111-1111-111111111111', 'alice.smith@example.com', 'encodedPassword1', 'alice'),
    ('22222222-2222-2222-2222-222222222222', 'bob.johnson@example.com', 'encodedPassword2', 'bob'),
    ('33333333-3333-3333-3333-333333333333', 'carol.williams@example.com', 'encodedPassword3', 'carol'),
    ('44444444-4444-4444-4444-444444444444', 'dave.brown@example.com', 'encodedPassword4', 'dave'),
    ('55555555-5555-5555-5555-555555555555', 'eva.jones@example.com', 'encodedPassword5', 'eva'),
    ('66666666-6666-6666-6666-666666666666', 'frank.miller@example.com', 'encodedPassword6', 'frank'),
    ('77777777-7777-7777-7777-777777777777', 'grace.davis@example.com', 'encodedPassword7', 'grace'),
    ('88888888-8888-8888-8888-888888888888', 'henry.garcia@example.com', 'encodedPassword8', 'henry'),
    ('99999999-9999-9999-9999-999999999999', 'irene.martinez@example.com', 'encodedPassword9', 'irene'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'jack.rodriguez@example.com', 'encodedPassword10', 'jack'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'kate.lee@example.com', 'encodedPassword11', 'kate'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'leo.walker@example.com', 'encodedPassword12', 'leo'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'mia.hall@example.com', 'encodedPassword13', 'mia'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'noah.allen@example.com', 'encodedPassword14', 'noah'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'olivia.young@example.com', 'encodedPassword15', 'olivia'),
    ('12345678-1234-1234-1234-123456789012', 'paul.hernandez@example.com', 'encodedPassword16', 'paul'),
    ('87654321-4321-4321-4321-210987654321', 'quinn.king@example.com', 'encodedPassword17', 'quinn'),
    ('11223344-5566-7788-99aa-bbccddeeff00', 'rachel.wright@example.com', 'encodedPassword18', 'rachel'),
    ('00ffeedd-ccbb-a099-8877-665544332211', 'samuel.lopez@example.com', 'encodedPassword19', 'samuel'),
    ('abcdefab-cdef-abcd-efab-cdefabcdefab', 'tina.hill@example.com', 'encodedPassword20', 'tina');


INSERT INTO transactions (id, user_id, amount, category, description, timestamp) VALUES
    (RANDOM_UUID(), '11111111-1111-1111-1111-111111111111', 150.00, 'EXPENSE', 'Groceries at Walmart', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '22222222-2222-2222-2222-222222222222', 2000.00, 'INCOME', 'Salary June', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '33333333-3333-3333-3333-333333333333', 50.75, 'EXPENSE', 'Internet Bill', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '44444444-4444-4444-4444-444444444444', 120.00, 'EXPENSE', 'Electricity Bill', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '55555555-5555-5555-5555-555555555555', 500.00, 'INCOME', 'Freelance Project', CURRENT_TIMESTAMP),

    (RANDOM_UUID(), '11111111-1111-1111-1111-111111111111', 35.50, 'EXPENSE', 'Coffee and snacks', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '22222222-2222-2222-2222-222222222222', 1800.00, 'INCOME', 'Bonus Payment', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '33333333-3333-3333-3333-333333333333', 75.00, 'EXPENSE', 'Gas refill', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '44444444-4444-4444-4444-444444444444', 300.00, 'INCOME', 'Consulting Fee', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '55555555-5555-5555-5555-555555555555', 90.00, 'EXPENSE', 'Dining out', CURRENT_TIMESTAMP),

    (RANDOM_UUID(), '11111111-1111-1111-1111-111111111111', 1200.00, 'INCOME', 'Stock Dividends', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '22222222-2222-2222-2222-222222222222', 45.00, 'EXPENSE', 'Movie tickets', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '33333333-3333-3333-3333-333333333333', 60.00, 'EXPENSE', 'Gym Membership', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '44444444-4444-4444-4444-444444444444', 250.00, 'INCOME', 'Rental Income', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '55555555-5555-5555-5555-555555555555', 20.00, 'EXPENSE', 'Books purchase', CURRENT_TIMESTAMP),

    (RANDOM_UUID(), '11111111-1111-1111-1111-111111111111', 110.00, 'EXPENSE', 'Pet supplies', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '22222222-2222-2222-2222-222222222222', 600.00, 'INCOME', 'Side gig', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '33333333-3333-3333-3333-333333333333', 80.00, 'EXPENSE', 'Car maintenance', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '44444444-4444-4444-4444-444444444444', 1500.00, 'INCOME', 'Project payout', CURRENT_TIMESTAMP),
    (RANDOM_UUID(), '55555555-5555-5555-5555-555555555555', 40.00, 'EXPENSE', 'Clothing purchase', CURRENT_TIMESTAMP);
