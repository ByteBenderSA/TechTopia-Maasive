-- Test Data Bulk Insert for MobileMind Database
-- Student Numbers: 2688828, 2666605, 2548941, 2729013

-- 1. Insert Users
INSERT INTO USERS (STUDENT_NUMBER, STUDENT_FNAME, STUDENT_LNAME, STUDENT_CONTACT_NO, STUDENT_EMAIL, USER_ROLE) VALUES
('2688828', 'John', 'Doe', '0811234567', 'john.doe@students.wits.ac.za', 'STUDENT'),
('2666605', 'Sarah', 'Smith', '0827654321', 'sarah.smith@students.wits.ac.za', 'STUDENT'),
('2548941', 'Mike', 'Johnson', '0833456789', 'mike.johnson@students.wits.ac.za', 'STUDENT'),
('2729013', 'Emma', 'Wilson', '0849876543', 'emma.wilson@students.wits.ac.za', 'STUDENT');

-- 2. Insert Authentication (using simple password hashing for testing)
-- Note: In real application, these should be properly hashed passwords
INSERT INTO AUTHENTICATION (STUDENT_NUMBER, PASSWORD_HASH, VERIFICATION_TOKEN, VERIFIED) VALUES
('2688828', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'token123', 1),
('2666605', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'token456', 1),
('2548941', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'token789', 1),
('2729013', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'token012', 1);

-- 3. Insert Posts
INSERT INTO POSTS (STUDENT_NUMBER, TITLE, POST_QUESTION, VOTE_COUNT, STATUS) VALUES
('2688828', 'Help with Android Studio Setup', 'Hi everyone! I am having trouble setting up Android Studio on my laptop. It keeps crashing when I try to create a new project. Has anyone experienced this issue before? Any suggestions would be greatly appreciated!', 5, 1),

('2666605', 'Study Group for Database Assignment', 'Looking to form a study group for the upcoming database assignment. We need to design a complete database schema for a mobile app. Anyone interested in collaborating? We can meet at the library this weekend.', 8, 1),

('2548941', 'Question about RecyclerView Implementation', 'I am working on implementing a RecyclerView in my Android app but the data is not displaying correctly. The list appears empty even though I have added items to the adapter. Can someone help me debug this issue?', 12, 1),

('2729013', 'Mobile App UI Design Best Practices', 'What are some best practices for designing mobile app user interfaces? I want to make sure my app follows Material Design guidelines and provides a good user experience. Any resources or tips would be helpful!', 7, 1),

('2688828', 'Error with SQLite Database Connection', 'Getting a persistent error when trying to connect to my SQLite database in Android. The error message says "database is locked". I have checked my code multiple times but cannot find the issue. Please help!', 3, 1),

('2666605', 'Seeking Feedback on App Prototype', 'I have created a prototype for my mobile learning app and would love to get some feedback from fellow students. The app helps with organizing study materials and tracking progress. Would anyone be willing to test it?', 15, 1),

('2548941', 'Java vs Kotlin for Android Development', 'I am trying to decide between Java and Kotlin for my Android development project. What are the pros and cons of each? Which one would you recommend for a beginner? Any insights would be appreciated!', 9, 1),

('2729013', 'Group Project Partner Needed', 'Looking for a reliable partner for the final group project. The project involves creating a full-stack mobile application with backend integration. I have experience with frontend development and need someone good with backend/database work.', 4, 1);

-- 4. Insert Comments
INSERT INTO COMMENTS (POST_ID, STUDENT_NUMBER, STUDENT_COMMENT, VOTE_COUNT, STATUS) VALUES

(1, '2666605', 'I had the same issue! Try clearing the Android Studio cache and restarting. Go to File > Invalidate Caches and Restart.', 3, 1),
(1, '2548941', 'Also make sure you have enough RAM allocated to Android Studio. Check your studio.exe.vmoptions file.', 2, 1),
(1, '2729013', 'What operating system are you using? Sometimes compatibility issues can cause crashes.', 1, 1),

(2, '2688828', 'I would be interested in joining! I am also working on the database assignment. When and where should we meet?', 2, 1),
(2, '2548941', 'Count me in as well. I have some experience with database design from previous courses.', 4, 1),
(2, '2729013', 'Great idea! We could use the group study rooms on the 4th floor of the library.', 1, 1),


(3, '2688828', 'Check if you are calling notifyDataSetChanged() after adding items to your adapter.', 5, 1),
(3, '2666605', 'Also make sure your RecyclerView layout manager is properly set. LinearLayoutManager is most common.', 3, 1),
(3, '2729013', 'Can you share your adapter code? It might be easier to debug if we can see the implementation.', 2, 1),


(4, '2548941', 'Google Material Design documentation is excellent. Also check out the Material Design Components library.', 6, 1),
(4, '2688828', 'I recommend following Material You guidelines for the latest design principles.', 2, 1),
(4, '2666605', 'Figma has great Material Design templates that you can use as starting points.', 4, 1),

(5, '2729013', 'Make sure you are properly closing your database connections. Use try-with-resources or finally blocks.', 4, 1),
(5, '2666605', 'This usually happens when multiple threads try to access the database simultaneously. Consider using Room database library.', 3, 1),

(6, '2548941', 'I would love to test your app! Can you share the APK file or TestFlight link?', 2, 1),
(6, '2729013', 'Sounds like a useful app. Have you considered adding gamification elements to increase engagement?', 5, 1),
(6, '2688828', 'Great concept! Make sure to gather feedback on both functionality and user experience.', 1, 1),

(7, '2729013', 'I would recommend Kotlin. It is more concise and Google now prefers it for Android development.', 7, 1),
(7, '2666605', 'Kotlin has better null safety and more modern language features. The learning curve is not too steep.', 4, 1),
(7, '2688828', 'Java is still widely used and has more learning resources available online. Both are good choices.', 2, 1),

(8, '2666605', 'I have good backend experience with Node.js and MySQL. Would love to collaborate with you!', 3, 1),
(8, '2548941', 'I am also looking for a project partner. I have worked with REST APIs and database design before.', 2, 1);

-- 5. Insert Votes (Post Votes)
INSERT INTO VOTES (STUDENT_NUMBER, POST_ID, COMMENT_ID, VOTE_TYPE) VALUES
-- Post votes
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
('2666605', NULL, 4, 1), -- Sarah upvotes Mike's comment about joining study group
('2729013', NULL, 5, 1), -- Emma upvotes John's comment about notifyDataSetChanged
('2688828', NULL, 7, 1), -- John upvotes Emma's comment about Material You
('2548941', NULL, 9, 1), -- Mike upvotes Emma's comment about database connections
('2729013', NULL, 12, 1), -- Emma upvotes Mike's comment about Kotlin preference
('2666605', NULL, 14, 1); -- Sarah upvotes Emma's comment about backend collaboration 