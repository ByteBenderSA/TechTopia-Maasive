-- Add Comment Votes (run this AFTER inserting comments)

-- First, check what COMMENT_IDs exist:
SELECT COMMENT_ID, POST_ID, STUDENT_NUMBER, LEFT(STUDENT_COMMENT, 40) as COMMENT_PREVIEW 
FROM COMMENTS 
ORDER BY COMMENT_ID;

-- Add comment votes using the actual COMMENT_IDs from above

INSERT INTO VOTES (STUDENT_NUMBER, POST_ID, COMMENT_ID, VOTE_TYPE) VALUES
-- Comment votes with actual COMMENT_IDs
('2548941', NULL, 45, 1), -- Mike upvotes Sarah's comment about cache clearing
('2729013', NULL, 45, 1), -- Emma upvotes Sarah's comment about cache clearing  
('2688828', NULL, 46, 1), -- John upvotes Mike's comment about RAM
('2666605', NULL, 49, 1), -- Sarah upvotes Mike's comment about joining study group
('2729013', NULL, 51, 1), -- Emma upvotes John's comment about notifyDataSetChanged
('2688828', NULL, 52, 1), -- John upvotes Sarah's comment about RecyclerView
('2548941', NULL, 54, 1), -- Mike upvotes Mike's comment about Material Design
('2729013', NULL, 56, 1); -- Emma upvotes Sarah's comment about Figma templates

-- Mapping used:
-- 45 = "I had the same issue! Try clearing the A" (Sarah's cache clearing comment)
-- 46 = "Also make sure you have enough RAM alloc" (Mike's RAM comment)
-- 49 = "Count me in as well. I have some experie" (Mike's study group comment)
-- 51 = "Check if you are calling notifyDataSetCh" (John's notifyDataSetChanged comment)
-- 52 = "Also make sure your RecyclerView layout" (Sarah's RecyclerView comment)
-- 54 = "Google Material Design documentation is" (Mike's Material Design comment)
-- 56 = "Figma has great Material Design template" (Sarah's Figma comment)

-- Verification
SELECT 'Total Votes' as Info, COUNT(*) as Count FROM VOTES; 