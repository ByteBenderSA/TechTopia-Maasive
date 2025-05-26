-- Safe Insert for Comments and Votes
-- Fix for foreign key constraint errors

-- Step 1: First, check what POST_IDs actually exist in your database
SELECT POST_ID, STUDENT_NUMBER, TITLE 
FROM POSTS 
ORDER BY POST_ID;

-- Step 2: Based on the results above, manually replace the POST_IDs below
-- Example: If your posts have IDs 15, 16, 17, 18... then use those numbers

-- IMPORTANT: Run the SELECT query above first, then replace the numbers below!

-- Template for inserting comments (replace POST_ID numbers with actual values):
-- INSERT INTO COMMENTS (POST_ID, STUDENT_NUMBER, STUDENT_COMMENT, VOTE_COUNT, STATUS) VALUES

-- Example if your first post has POST_ID = 15:
-- (15, '2666605', 'I had the same issue! Try clearing the Android Studio cache and restarting.', 2, 1),
-- (15, '2548941', 'Also make sure you have enough RAM allocated to Android Studio.', 1, 1),

-- Safer approach: Insert comments one post at a time
-- Replace 'XX' with the actual POST_ID of your Android Studio post

/*
-- For Android Studio Setup post (replace XX with actual POST_ID):
INSERT INTO COMMENTS (POST_ID, STUDENT_NUMBER, STUDENT_COMMENT, VOTE_COUNT, STATUS) VALUES
(XX, '2666605', 'I had the same issue! Try clearing the Android Studio cache and restarting.', 2, 1),
(XX, '2548941', 'Also make sure you have enough RAM allocated to Android Studio.', 1, 1),
(XX, '2729013', 'What operating system are you using? Sometimes compatibility issues can cause crashes.', 0, 1);

-- For Study Group post (replace YY with actual POST_ID):
INSERT INTO COMMENTS (POST_ID, STUDENT_NUMBER, STUDENT_COMMENT, VOTE_COUNT, STATUS) VALUES
(YY, '2688828', 'I would be interested in joining! When and where should we meet?', 0, 1),
(YY, '2548941', 'Count me in as well. I have experience with database design.', 1, 1),
(YY, '2729013', 'Great idea! We could use the group study rooms.', 0, 1);
*/

-- Alternative: If you want to clear existing posts and start fresh with ID=1:
-- DELETE FROM POSTS;
-- ALTER TABLE POSTS AUTO_INCREMENT = 1;
-- Then run the original insert_posts_comments_votes_only.sql

-- Check current state:
SELECT 'Existing Posts' as Info, COUNT(*) as Count FROM POSTS
UNION ALL
SELECT 'Existing Comments', COUNT(*) FROM COMMENTS; 