-- Insert Posts, Comments, and Votes for Existing Users
-- Assumes users 2688828, 2666605, 2548941, 2729013 already exist

-- 1. Insert Posts
INSERT INTO POSTS (STUDENT_NUMBER, TITLE, POST_QUESTION, VOTE_COUNT, STATUS) VALUES
('2688828', 'Help with Android Studio Setup', 'Hi everyone! I am having trouble setting up Android Studio on my laptop. It keeps crashing when I try to create a new project. Has anyone experienced this issue before? Any suggestions would be greatly appreciated!', 3, 1),

('2666605', 'Study Group for Database Assignment', 'Looking to form a study group for the upcoming database assignment. We need to design a complete database schema for a mobile app. Anyone interested in collaborating? We can meet at the library this weekend.', 3, 1),

('2548941', 'Question about RecyclerView Implementation', 'I am working on implementing a RecyclerView in my Android app but the data is not displaying correctly. The list appears empty even though I have added items to the adapter. Can someone help me debug this issue?', 3, 1),

('2729013', 'Mobile App UI Design Best Practices', 'What are some best practices for designing mobile app user interfaces? I want to make sure my app follows Material Design guidelines and provides a good user experience. Any resources or tips would be helpful!', 3, 1),

('2688828', 'Error with SQLite Database Connection', 'Getting a persistent error when trying to connect to my SQLite database in Android. The error message says "database is locked". I have checked my code multiple times but cannot find the issue. Please help!', 0, 1),

('2666605', 'Seeking Feedback on App Prototype', 'I have created a prototype for my mobile learning app and would love to get some feedback from fellow students. The app helps with organizing study materials and tracking progress. Would anyone be willing to test it?', 0, 1),

('2548941', 'Java vs Kotlin for Android Development', 'I am trying to decide between Java and Kotlin for my Android development project. What are the pros and cons of each? Which one would you recommend for a beginner? Any insights would be appreciated!', 0, 1),

('2729013', 'Group Project Partner Needed', 'Looking for a reliable partner for the final group project. The project involves creating a full-stack mobile application with backend integration. I have experience with frontend development and need someone good with backend/database work.', 0, 1);

-- 2. Insert Comments
INSERT INTO COMMENTS (POST_ID, STUDENT_NUMBER, STUDENT_COMMENT, VOTE_COUNT, STATUS) VALUES
-- Comments for Post 1 (Android Studio Setup)
(1, '2666605', 'I had the same issue! Try clearing the Android Studio cache and restarting. Go to File > Invalidate Caches and Restart.', 2, 1),
(1, '2548941', 'Also make sure you have enough RAM allocated to Android Studio. Check your studio.exe.vmoptions file.', 1, 1),
(1, '2729013', 'What operating system are you using? Sometimes compatibility issues can cause crashes.', 0, 1),

-- Comments for Post 2 (Study Group)
(2, '2688828', 'I would be interested in joining! I am also working on the database assignment. When and where should we meet?', 0, 1),
(2, '2548941', 'Count me in as well. I have some experience with database design from previous courses.', 1, 1),
(2, '2729013', 'Great idea! We could use the group study rooms on the 4th floor of the library.', 0, 1),

-- Comments for Post 3 (RecyclerView)
(3, '2688828', 'Check if you are calling notifyDataSetChanged() after adding items to your adapter.', 1, 1),
(3, '2666605', 'Also make sure your RecyclerView layout manager is properly set. LinearLayoutManager is most common.', 1, 1),
(3, '2729013', 'Can you share your adapter code? It might be easier to debug if we can see the implementation.', 0, 1),

-- Comments for Post 4 (UI Design)
(4, '2548941', 'Google Material Design documentation is excellent. Also check out the Material Design Components library.', 1, 1),
(4, '2688828', 'I recommend following Material You guidelines for the latest design principles.', 0, 1),
(4, '2666605', 'Figma has great Material Design templates that you can use as starting points.', 1, 1),

-- Comments for Post 5 (SQLite Error)
(5, '2729013', 'Make sure you are properly closing your database connections. Use try-with-resources or finally blocks.', 0, 1),
(5, '2666605', 'This usually happens when multiple threads try to access the database simultaneously. Consider using Room database library.', 0, 1),

-- Comments for Post 6 (App Prototype)
(6, '2548941', 'I would love to test your app! Can you share the APK file or TestFlight link?', 0, 1),
(6, '2729013', 'Sounds like a useful app. Have you considered adding gamification elements to increase engagement?', 0, 1),
(6, '2688828', 'Great concept! Make sure to gather feedback on both functionality and user experience.', 0, 1),

-- Comments for Post 7 (Java vs Kotlin)
(7, '2729013', 'I would recommend Kotlin. It is more concise and Google now prefers it for Android development.', 0, 1),
(7, '2666605', 'Kotlin has better null safety and more modern language features. The learning curve is not too steep.', 0, 1),
(7, '2688828', 'Java is still widely used and has more learning resources available online. Both are good choices.', 0, 1),

-- Comments for Post 8 (Group Project Partner)
(8, '2666605', 'I have good backend experience with Node.js and MySQL. Would love to collaborate with you!', 0, 1),
(8, '2548941', 'I am also looking for a project partner. I have worked with REST APIs and database design before.', 0, 1);

-- 3. Insert Votes
INSERT INTO VOTES (STUDENT_NUMBER, POST_ID, COMMENT_ID, VOTE_TYPE) VALUES
-- Post votes (3 votes each for first 4 posts)
('2666605', 1, NULL, 1), -- Sarah upvotes John's Android Studio post
('2548941', 1, NULL, 1), -- Mike upvotes John's Android Studio post
('2729013', 1, NULL, 1), -- Emma upvotes John's Android Studio post

('2688828', 2, NULL, 1), -- John upvotes Sarah's study group post
('2548941', 2, NULL, 1), -- Mike upvotes Sarah's study group post
('2729013', 2, NULL, 1), -- Emma upvotes Sarah's study group post

('2688828', 3, NULL, 1), -- John upvotes Mike's RecyclerView post
('2666605', 3, NULL, 1), -- Sarah upvotes Mike's RecyclerView post
('2729013', 3, NULL, 1), -- Emma upvotes Mike's RecyclerView post

('2688828', 4, NULL, 1), -- John upvotes Emma's UI design post
('2666605', 4, NULL, 1), -- Sarah upvotes Emma's UI design post
('2548941', 4, NULL, 1), -- Mike upvotes Emma's UI design post

-- Comment votes
('2548941', NULL, 1, 1), -- Mike upvotes Sarah's comment about cache clearing
('2729013', NULL, 1, 1), -- Emma upvotes Sarah's comment about cache clearing
('2688828', NULL, 2, 1), -- John upvotes Mike's comment about RAM
('2666605', NULL, 5, 1), -- Sarah upvotes Mike's comment about joining study group
('2729013', NULL, 7, 1), -- Emma upvotes John's comment about notifyDataSetChanged
('2688828', NULL, 8, 1), -- John upvotes Sarah's comment about RecyclerView
('2548941', NULL, 10, 1), -- Mike upvotes Emma's comment about Material Design
('2729013', NULL, 12, 1); -- Emma upvotes Sarah's comment about Figma templates

-- Verification query
SELECT 'Posts Created' as Info, COUNT(*) as Count FROM POSTS
UNION ALL
SELECT 'Comments Created', COUNT(*) FROM COMMENTS
UNION ALL  
SELECT 'Votes Created', COUNT(*) FROM VOTES; 