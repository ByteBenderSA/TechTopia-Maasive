# Simple PHP Voting System Setup Guide

## Overview
This guide explains how to set up the simple PHP backend for the MobileMind voting system. The code is written in a student-friendly style using basic mysqli, similar to your existing login.php file.

## Prerequisites
- XAMPP, WAMP, or similar local server with PHP 7.4+ and MySQL
- MySQL database set up with the MobileMind schema
- Android Studio for testing the app

## File Structure
```
php/
├── config/
│   └── database.php          # Simple database connection
├── api/
│   ├── vote.php              # Main voting endpoint (simple version)
│   └── get_vote_status.php   # Vote status checking (simple version)
└── README_PHP_SETUP.md       # This file
```

## Setup Instructions

### 1. Database Setup

Make sure your MySQL database is set up. Use the SQL files in the `database/` folder:

```sql
-- Create the database
CREATE DATABASE d268;
USE d268;

-- Run your existing schema files
SOURCE path/to/Database_Create.sql;
SOURCE path/to/setup_complete_database.sql;
```

### 2. Configure Database Connection

Edit `php/config/database.php` and update the database credentials to match your setup:

```php
// Database connection settings
$host = "127.0.0.1";
$db = "d268";           // Change to your database name
$user = "s268";         // Change to your MySQL username  
$pass = "s268";         // Change to your MySQL password
```

### 3. Place PHP Files

Copy the `php` folder to your web server directory:

**For XAMPP:**
- Copy to: `C:\xampp\htdocs\mobilemind\`
- Access via: `http://localhost/mobilemind/php/api/`

**For WAMP:**
- Copy to: `C:\wamp64\www\mobilemind\`
- Access via: `http://localhost/mobilemind/php/api/`

### 4. Test PHP Backend

Test the endpoints using Postman or your browser:

**Test Vote Endpoint (use Postman):**
```
POST: http://localhost/mobilemind/php/api/vote.php

Body (JSON):
{
  "student_number": "2688828",
  "vote_type": "post",
  "post_id": 1
}
```

**Test Vote Status (browser):**
```
http://localhost/mobilemind/php/api/get_vote_status.php?student_number=2688828&post_id=1
```

### 5. Update Android App

Update the base URL in `VoteManagerHTTP.java`:

```java
// For Android Emulator
private static final String BASE_URL = "http://10.0.2.2/mobilemind/php/api/";

// For real device (use your computer's IP)
// private static final String BASE_URL = "http://192.168.1.100/mobilemind/php/api/";
```

## Simple PHP Code Explained

### Database Connection (config/database.php)
```php
// Simple mysqli connection like your login.php
$host = "127.0.0.1";
$db = "d268";
$user = "s268";
$pass = "s268";

function getConnection() {
    global $host, $user, $pass, $db;
    $conn = new mysqli($host, $user, $pass, $db);
    
    if ($conn->connect_error) {
        echo json_encode(["success" => false, "message" => "Database connection failed"]);
        exit;
    }
    
    return $conn;
}
```

### Vote Handling (api/vote.php)
```php
// Get JSON data simply
$input = file_get_contents("php://input");
$data = json_decode($input, true);

// Simple validation
if (empty($data['student_number']) || empty($data['vote_type'])) {
    echo json_encode(["success" => false, "message" => "Missing fields"]);
    exit;
}

// Basic database queries
$conn = getConnection();
$sql = "SELECT VOTE_ID FROM VOTES WHERE STUDENT_NUMBER = '$student_number'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // User already voted - remove it
    $delete_sql = "DELETE FROM VOTES WHERE VOTE_ID = '$vote_id'";
    $conn->query($delete_sql);
} else {
    // Add new vote
    $insert_sql = "INSERT INTO VOTES (...) VALUES (...)";
    $conn->query($insert_sql);
}
```

## API Endpoints

### POST /vote.php
Simple voting with duplicate prevention.

**Send this JSON:**
```json
{
  "student_number": "2688828",
  "vote_type": "post",
  "post_id": 1
}
```

**Get this back:**
```json
{
  "success": true,
  "message": "Vote added successfully",
  "new_vote_count": 4,
  "has_voted": true
}
```

### GET /get_vote_status.php
Check if user voted on something.

**URL:** `get_vote_status.php?student_number=2688828&post_id=1`

**Response:**
```json
{
  "post_vote_status": {
    "has_voted": true,
    "vote_count": 4,
    "post_id": 1
  }
}
```

## Database Tables Needed

Your existing database should have these tables:

```sql
VOTES table:
- VOTE_ID (auto increment)
- STUDENT_NUMBER (varchar)
- POST_ID (int, can be null)
- COMMENT_ID (int, can be null)
- VOTE_TYPE (int, 1 = upvote)
- VOTED_DATE (datetime)

POSTS table:
- POST_ID (auto increment)
- STUDENT_NUMBER (varchar)
- TITLE (varchar)
- POST_QUESTION (text)
- VOTE_COUNT (int, default 0)

COMMENTS table:
- COMMENT_ID (auto increment)
- POST_ID (int)
- STUDENT_NUMBER (varchar)
- STUDENT_COMMENT (text)
- VOTE_COUNT (int, default 0)
```

## How It Prevents Duplicate Votes

The system checks if a user already voted:

```php
// Check existing vote
$check_sql = "SELECT VOTE_ID FROM VOTES 
              WHERE STUDENT_NUMBER = '$student_number' 
              AND POST_ID = '$post_id'";
$result = $conn->query($check_sql);

if ($result->num_rows > 0) {
    // User already voted - remove the vote (toggle off)
    // Delete the vote and decrease count
} else {
    // User hasn't voted - add new vote (toggle on)
    // Insert vote and increase count
}
```

## Troubleshooting

**Common Problems:**

1. **"Database connection failed"**
   - Check your database credentials in `config/database.php`
   - Make sure MySQL is running
   - Verify database name exists

2. **"Missing required fields"**
   - Check you're sending the JSON correctly
   - Use Postman to test the format

3. **Android can't connect**
   - Check the URL in `VoteManagerHTTP.java`
   - For emulator use `10.0.2.2` instead of `localhost`
   - For real device use your computer's IP address

4. **Vote not working**
   - Check if the tables exist in your database
   - Verify the vote count columns exist
   - Look at your web server error logs

## Testing Steps

1. **Test PHP first:** Use Postman to test the vote.php endpoint
2. **Check database:** Look at the VOTES table to see if votes are added/removed
3. **Test Android:** Run your app and try voting
4. **Check logs:** Look at Android logcat for any errors

## Simple vs Advanced

This is the **simple student version** that:
- ✅ Uses basic mysqli like your login.php
- ✅ Has simple error handling
- ✅ Uses straightforward SQL queries
- ✅ Easy to understand and modify
- ✅ Still prevents duplicate votes
- ✅ Works with your existing database

The code style matches your login.php approach, making it easier for you to understand and modify!

## Need Help?

If something doesn't work:
1. Check your web server error logs
2. Test the PHP files directly in your browser
3. Make sure your database tables match what the code expects
4. Verify your database connection settings 