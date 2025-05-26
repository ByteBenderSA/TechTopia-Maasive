-- Check existing POST_IDs and generate correct INSERT statements

-- First, see what POST_IDs exist:
SELECT 
    POST_ID,
    STUDENT_NUMBER,
    LEFT(TITLE, 30) as TITLE_SHORT,
    VOTE_COUNT
FROM POSTS 
ORDER BY POST_ID;

-- Count existing data:
SELECT 'Posts' as Table_Name, COUNT(*) as Count FROM POSTS
UNION ALL
SELECT 'Comments', COUNT(*) FROM COMMENTS
UNION ALL
SELECT 'Votes', COUNT(*) FROM VOTES;

-- If you see POST_IDs like 15, 16, 17, 18... then use those numbers below:
-- Replace the XX, YY, ZZ with your actual POST_IDs

-- Example: If your Android Studio post has POST_ID = 15, replace XX with 15:
/*
INSERT INTO COMMENTS (POST_ID, STUDENT_NUMBER, STUDENT_COMMENT, VOTE_COUNT, STATUS) VALUES
(XX, '2666605', 'I had the same issue! Try clearing the Android Studio cache and restarting.', 2, 1),
(XX, '2548941', 'Also make sure you have enough RAM allocated to Android Studio.', 1, 1),
(XX, '2729013', 'What operating system are you using? Sometimes compatibility issues can cause crashes.', 0, 1);
*/ 