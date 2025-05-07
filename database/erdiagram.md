erDiagram
    USERS {
        VARCHAR STUDENT_NUMBER PK "Primary Key, Unique, Not Null"
        VARCHAR STUDENT_FNAME
        VARCHAR STUDENT_LNAME
        VARCHAR STUDENT_CONTACT_NO "Unique"
        VARCHAR STUDENT_EMAIL "Unique, Not Null"
        ENUM USER_ROLE "Default: STUDENT, Values: STUDENT, ADMIN"
    }

    AUTHENTICATION {
        VARCHAR STUDENT_NUMBER PK "Primary Key"
        VARCHAR STUDENT_NUMBER FK "Foreign Key to USERS(STUDENT_NUMBER)"
        VARCHAR PASSWORD_HASH "Not Null"
        VARCHAR VERIFICATION_TOKEN "Not Null"
        TINYINT VERIFIED "Default: 0, Not Null"
        
    }

    POSTS {
        INT POST_ID PK "Primary Key, Auto Increment"
        VARCHAR STUDENT_NUMBER FK "Foreign Key to USERS(STUDENT_NUMBER)"
        VARCHAR TITLE "Not Null"
        TEXT POST_QUESTION "Not Null"
        DATETIME POST_DATE "Default: CURRENT_TIMESTAMP"
        INT VOTE_COUNT "Default: 0"
        TINYINT STATUS "Default: 1 (1 = ACTIVE, 0 = DELETED)"
    }

    COMMENTS {
        INT COMMENT_ID PK "Primary Key, Auto Increment"
        INT POST_ID FK "Foreign Key to POSTS(POST_ID)"
        VARCHAR STUDENT_NUMBER FK "Foreign Key to USERS(STUDENT_NUMBER)"
        TEXT STUDENT_COMMENT "Not Null"
        DATETIME COMMENT_DATE "Default: CURRENT_TIMESTAMP"
        INT VOTE_COUNT "Default: 0"
        TINYINT STATUS "Default: 1 (1 = ACTIVE, 0 = DELETED)"
    }

    VOTES {
        INT VOTE_ID PK "Primary Key, Auto Increment"
        VARCHAR STUDENT_NUMBER FK "Foreign Key to USERS(STUDENT_NUMBER)"
        INT POST_ID FK "Foreign Key to POSTS(POST_ID)"
        INT COMMENT_ID FK "Foreign Key to COMMENTS(COMMENT_ID)"
        TINYINT VOTE_TYPE "Default: 1 (1 = UPVOTE, 0 = NO_VOTE)"
        DATETIME VOTED_DATE "Default: CURRENT_TIMESTAMP"
        UNIQUE UNIQUE_POST_VOTE "(STUDENT_NUMBER, POST_ID)"
        UNIQUE UNIQUE_COMMENT_VOTE "(STUDENT_NUMBER, COMMENT_ID)"
    }

    USERS ||--|| AUTHENTICATION : "has"
    USERS ||--o{ POSTS : "creates"
    USERS ||--o{ COMMENTS : "writes"
    USERS ||--o{ VOTES : "casts"
    POSTS ||--o{ COMMENTS : "has"
    POSTS ||--o{ VOTES : "receives"
    COMMENTS ||--o{ VOTES : "receives"

