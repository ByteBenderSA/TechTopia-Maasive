-- Insert Comments and Votes with Correct POST_IDs
-- Using actual POST_IDs: 17, 18, 19, 20, 21, 22, 23, 24

-- 1. Insert Comments with correct POST_IDs
INSERT INTO COMMENTS (POST_ID, STUDENT_NUMBER, STUDENT_COMMENT, VOTE_COUNT, STATUS) VALUES
-- Comments for Post 17 (Android Studio Setup)
(17, '2666605', 'I had the same issue! Try clearing the Android Studio cache and restarting. Go to File > Invalidate Caches and Restart.', 2, 1),
(17, '2548941', 'Also make sure you have enough RAM allocated to Android Studio. Check your studio.exe.vmoptions file.', 1, 1),
(17, '2729013', 'What operating system are you using? Sometimes compatibility issues can cause crashes.', 0, 1),

-- Comments for Post 18 (Study Group)
(18, '2688828', 'I would be interested in joining! I am also working on the database assignment. When and where should we meet?', 0, 1),
(18, '2548941', 'Count me in as well. I have some experience with database design from previous courses.', 1, 1),
(18, '2729013', 'Great idea! We could use the group study rooms on the 4th floor of the library.', 0, 1),

-- Comments for Post 19 (RecyclerView)
(19, '2688828', 'Check if you are calling notifyDataSetChanged() after adding items to your adapter.', 1, 1),
(19, '2666605', 'Also make sure your RecyclerView layout manager is properly set. LinearLayoutManager is most common.', 1, 1),
(19, '2729013', 'Can you share your adapter code? It might be easier to debug if we can see the implementation.', 0, 1),

-- Comments for Post 20 (UI Design)
(20, '2548941', 'Google Material Design documentation is excellent. Also check out the Material Design Components library.', 1, 1),
(20, '2688828', 'I recommend following Material You guidelines for the latest design principles.', 0, 1),
(20, '2666605', 'Figma has great Material Design templates that you can use as starting points.', 1, 1),

-- Comments for Post 21 (SQLite Error)
(21, '2729013', 'Make sure you are properly closing your database connections. Use try-with-resources or finally blocks.', 0, 1),
(21, '2666605', 'This usually happens when multiple threads try to access the database simultaneously. Consider using Room database library.', 0, 1),

-- Comments for Post 22 (App Prototype)
(22, '2548941', 'I would love to test your app! Can you share the APK file or TestFlight link?', 0, 1),
(22, '2729013', 'Sounds like a useful app. Have you considered adding gamification elements to increase engagement?', 0, 1),
(22, '2688828', 'Great concept! Make sure to gather feedback on both functionality and user experience.', 0, 1),

-- Comments for Post 23 (Java vs Kotlin)
(23, '2729013', 'I would recommend Kotlin. It is more concise and Google now prefers it for Android development.', 0, 1),
(23, '2666605', 'Kotlin has better null safety and more modern language features. The learning curve is not too steep.', 0, 1),
(23, '2688828', 'Java is still widely used and has more learning resources available online. Both are good choices.', 0, 1),

-- Comments for Post 24 (Group Project Partner)
(24, '2666605', 'I have good backend experience with Node.js and MySQL. Would love to collaborate with you!', 0, 1),
(24, '2548941', 'I am also looking for a project partner. I have worked with REST APIs and database design before.', 0, 1);

-- 2. Insert Votes with correct POST_IDs
INSERT INTO VOTES (STUDENT_NUMBER, POST_ID, COMMENT_ID, VOTE_TYPE) VALUES
-- Post votes (3 votes each for first 4 posts)
('2666605', 17, NULL, 1), -- Sarah upvotes John's Android Studio post
('2548941', 17, NULL, 1), -- Mike upvotes John's Android Studio post
('2729013', 17, NULL, 1), -- Emma upvotes John's Android Studio post

('2688828', 18, NULL, 1), -- John upvotes Sarah's study group post
('2548941', 18, NULL, 1), -- Mike upvotes Sarah's study group post
('2729013', 18, NULL, 1), -- Emma upvotes Sarah's study group post

('2688828', 19, NULL, 1), -- John upvotes Mike's RecyclerView post
('2666605', 19, NULL, 1), -- Sarah upvotes Mike's RecyclerView post
('2729013', 19, NULL, 1), -- Emma upvotes Mike's RecyclerView post

('2688828', 20, NULL, 1), -- John upvotes Emma's UI design post
('2666605', 20, NULL, 1), -- Sarah upvotes Emma's UI design post
('2548941', 20, NULL, 1); -- Mike upvotes Emma's UI design post

-- Note: Comment votes will need COMMENT_IDs after comments are inserted
-- First run the comments insert above, then check comment IDs with:
-- SELECT COMMENT_ID, POST_ID, STUDENT_NUMBER, LEFT(STUDENT_COMMENT, 30) FROM COMMENTS ORDER BY COMMENT_ID;

-- Verification
SELECT 'Comments Inserted' as Info, COUNT(*) as Count FROM COMMENTS
UNION ALL
SELECT 'Post Votes Inserted', COUNT(*) FROM VOTES WHERE POST_ID IS NOT NULL; 