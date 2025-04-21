# Influencer Management System Manual

## Table of Contents
1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
    - [Login](#login)
    - [Registration](#registration)
3. [User Profile](#user-profile)
    - [Premium Subscription](#premium-subscription)
4. [Influencer List](#influencer-list)
    - [Viewing Influencers](#viewing-influencers)
    - [Searching Influencers](#searching-influencers)
    - [Filtering Influencers](#filtering-influencers)
    - [Sorting Influencers](#sorting-influencers)
    - [Adding to Favorites](#adding-to-favorites)
5. [Managing Favorites](#managing-favorites)
    - [Viewing Favorites](#viewing-favorites)
    - [Searching Favorites](#searching-favorites)
    - [Removing Favorites](#removing-favorites)
6. [Data Import/Export](#data-importexport)
    - [Exporting Data](#exporting-data)
    - [Importing Data](#importing-data)
7. [Logging Out](#logging-out)
8. [Troubleshooting](#troubleshooting)

## Introduction

The Influencer Management System is a Java console application designed to help users discover, track, and manage social media influencers across various platforms. This application allows you to search, filter, and sort influencers based on different criteria, maintain a personalized favorites list, and import/export data for external use.

With the Influencer Management System, users can:

- Register and log in with a username and password
- Browse the information of influencers, including platform, category, follower count, and country
- Subscribe to premium features to access influencer ad rate information
- Search through influencers by name, presenting results that match full or partial names
- Sort influencers by name, follower count, or ad rate (premium only)
- Filter influencers by platform, category, follower range, or country
- Add or remove influencers to/from their personalized favorites list
- Import influencer data from external files (CSV or JSON formats)
- Export influencer data or favorites to external files (CSV or JSON formats)

All changes made to favorites are automatically saved and persist from session to session

## Getting Started

When you launch the Influencer Management System, you'll be presented with the login screen:

```
==== Influencer Management System ====

=== Login ===
1. Enter Username and Password
2. Register a New Account
3. Exit
Select an option: 
```
### Registration

To create a new account:

1. On the login screen, type `2` and press Enter
2. Enter a unique username when prompted
3. Enter a password when prompted
```
=== Register a New Account ===
Please enter your details:
Username: Allen
Password: 12345
```

If successful, you'll be returned to the login screen where you can log in with your new account.


### Login

To log in to the system:

1. On the login screen, type `1` and press Enter
2. Enter your username when prompted
3. Enter your password when prompted
```
=== Login ===
1. Enter Username and Password
2. Register a New Account
3. Exit
Select an option: 1
Username: Allen
Password: 12345
```
If your credentials are correct, you'll be taken to your user profile.


## User Profile

After logging in, you'll see your user profile:

```
==== Influencer Management System ====

=== User Profile ===
Username: [your_username]
Subscription Status: Free

Options:
1. Subscribe to Premium
2. View Influencers
3. View Favorites
4. Logout
Select an option: 
```

From this screen, you can:
- Manage your subscription status
- Access the influencer list
- View your favorites
- Log out of the system

### Premium Subscription

Premium subscription gives you access to influencer ad rate information, which is valuable for planning promotional campaigns.

To subscribe to premium:
1. From the user profile, type `1` and press Enter
2. Your status will change to "Premium"
```
=== User Profile ===
Username: [your_username]
Subscription Status: Premium

Options:
1. Cancel Premium
2. View Influencers
3. View Favorites
4. Logout
```

To cancel your premium subscription:
1. From the user profile, type `1` and press Enter when you already have Premium status
2. Your status will change to "Free"

## Influencer List
The influencer list is the main interface for exploring available influencers:

```
==== Influencer Management System ====

=== Influencer List ===

ID | Name               | Platform   | Category        | Followers  | Country    | Ad Rate
--------------------------------------------------------------------------------
1  | John Smith         | Instagram  | Fitness         | 500000     | USA          | $2500.00
2  | Emma Johnson       | YouTube    | Beauty          | 2000000    | UK           | $5000.00
3  | David Lee          | TikTok     | Comedy          | 1500000    | Canada       | $3000.00
...

Options:
1. Search by Name
2. Filter by Platform
3. Filter by Category
4. Filter by Follower Range
5. Filter by Country
6. Sort by Name
7. Sort by Followers
8. Sort by Ad Rate
9. Add to Favorites
10. Export Data
11. Import Data
12. Back to User Profile
13. Reset to All Influencers
Select an option: 
```

Note: Ad Rate information is only visible to Premium subscribers.

### Viewing Influencers
To view the full list of influencers, type `2` and press Enter

The influencer list displays detailed information about each influencer, including:
- Name
- Platform (Instagram, YouTube, TikTok, etc.)
- Content category
- Follower count
- Country
- Ad rate (Premium only)

### Searching Influencers

To search for influencers by name:
1. Type `1` and press Enter
2. Enter the name or partial name to search
3. The results will show only influencers matching your search term
```
Select an option: 1
Enter name to search (in current results): Wei

==== Search Results ====

ID | Name | Platform | Category | Followers | Country | Ad Rate
--------------------------------------------------------------------------------
1  | Li Wei               | Twitch     | Gaming          | 1150000    | China      | $2800.00
2  | Wei Chen             | Instagram  | Fitness         | 700000     | Taiwan     | $1850.00
3  | Wei Zhang            | Instagram  | Technology      | 730000     | China      | $1900.00
4  | Wei Liu              | Instagram  | Travel          | 860000     | China      | $2200.00
5  | Chen Wei             | Instagram  | Photography     | 670000     | Taiwan     | $1800.00

Press Enter to continue...
```

### Filtering Influencers

The system offers several filtering options:

**Filter by Platform:**
1. Type `2` and press Enter
2. Enter the platform name (e.g., "Instagram", "YouTube", "TikTok")
3. The list will display only influencers on that platform
```
Select an option: 2
Enter platform to filter by (in current results): Instagram

==== Influencer Management System ====

=== Influencer List ===

ID | Name               | Platform   | Category        | Followers  | Country      | Ad Rate
------------------------------------------------------------------------------------
1  | John Smith         | Instagram  | Fitness         | 500000     | USA          | $2500.00
4  | Sophia Chen        | Instagram  | Fashion         | 800000     | China        | $1800.00
7  | William Kim        | Instagram  | Travel          | 600000     | South Korea  | $1500.00
...

[Showing 20 of 152 influencers]
```

**Filter by Category:**
1. Type `3` and press Enter
2. Enter the category name (e.g., "Fashion", "Beauty", "Gaming")
3. The list will display only influencers in that category

**Filter by Follower Range:**
1. Type `4` and press Enter
2. Enter the minimum follower count
3. Enter the maximum follower count (or 0 for no upper limit)
4. The list will display influencers within that follower range
```
Select an option: 4
Enter minimum followers (for current results): 1000000
Enter maximum followers (0 for no limit): 1100000

==== Influencer Management System ====

=== Influencer List ===

ID | Name | Platform | Category | Followers | Country | Ad Rate
--------------------------------------------------------------------------------
1  | Isabella Rossi       | YouTube    | DIY             | 1100000    | Italy      | $2700.00
2  | Matthew Nguyen       | Twitch     | Gaming          | 1050000    | Vietnam    | $2500.00
3  | Henry Wilson         | TikTok     | Comedy          | 1050000    | Australia  | $2600.00
4  | Felix Garcia         | Twitch     | Gaming          | 1100000    | Mexico     | $2700.00
...
[Showing 11 of influencers]
```
**Filter by Country:**
1. Type `5` and press Enter
2. Enter the country name (e.g., "USA", "UK", "Canada")
3. The list will display only influencers from that country
```
```
### Sorting Influencers

You can sort the influencer list in different ways:

**Sort by Name:**
1. Type `6` and press Enter
2. The list will be sorted alphabetically by name (A-Z)
```
=== Influencer List ===

ID | Name | Platform | Category | Followers | Country | Ad Rate
--------------------------------------------------------------------------------
1  | Abigail Kim          | TikTok     | Dance           | 1300000    | South Korea | $3000.00
2  | Adonis Chen          | TikTok     | Music           | 1600000    | China      | $3800.00
3  | Adrian Chen          | Instagram  | Photography     | 680000     | Taiwan     | $1800.00
...

[Showing current influencers list in alphabetical order ]
```
**Sort by Followers:**
1. Type `7` and press Enter
2. The list will be sorted by follower count (high to low)

**Sort by Ad Rate:**
1. Type `8` and press Enter
2. The list will be sorted by ad rate (high to low)
   Note: This option requires Premium subscription

### Adding to Favorites

To add an influencer to your favorites:
1. Type `9` and press Enter
2. Enter the ID number of the influencer you want to add
3. The influencer will be added to your favorites list

## Managing Favorites

The favorites section lets you manage your saved influencers:

```
==== Influencer Management System ====

=== Your Favorites ===

ID | Name               | Platform   | Category        | Followers  | Country    | Ad Rate
--------------------------------------------------------------------------------
1  | Jane Smith         | YouTube    | Beauty          | 500000     | UK         | $2000.00
2  | Mike Johnson       | TikTok     | Gaming          | 200000     | Canada     | $1500.00
...

Options:
1. Search by Name
2. Remove from Favorites
3. Export Favorites
4. Back to User Profile
Select an option: 
```

### Viewing Favorites

To access your favorites:
1. From the user profile, type `3` and press Enter
2. Your saved influencers will be displayed

### Searching Favorites

To search within your favorites:
1. Type `1` and press Enter
2. Enter the name or partial name to search
3. The results will show only favorites matching your search term

### Removing Favorites

To remove an influencer from your favorites:
1. Type `2` and press Enter
2. Enter the ID number of the influencer you want to remove
3. The influencer will be removed from your favorites list

## Data Import/Export

The system allows you to import and export influencer data in CSV or JSON formats.

### Exporting Data

To export the current influencer list:
1. From the influencer list, type `10` and press Enter
2. Select the export format:
   ```
   === Export Data ===
   1. Export as CSV
   2. Export as JSON
   3. Back
   Select an option: 
   ```
3. Enter a path to save the file or accept the default path
4. The data will be exported to the specified location

To export your favorites:
1. From the favorites view, type `3` and press Enter
2. Select the export format:
   ```
   === Export Data ===
   1. Export as CSV
   2. Export as JSON
   3. Back
   Select an option: 
   ```
3. Enter a path to save the file or accept the default path
4. Your favorites will be exported to the specified location

### Importing Data

To import influencer data:
1. From the influencer list, type `11` and press Enter
2. Select the import format:
   ```
   === Import Data ===
   1. Import from CSV
   2. Import from JSON
   3. Back
   Select an option: 
   ```
3. Enter the path to the file you want to import or accept the default path
4. The data will be imported into the system

CSV Format Example:
```
Name,Platform,Category,FollowerCount,Country,AdRate
John Doe,Instagram,Fashion,100000,USA,1000.0
Jane Smith,YouTube,Beauty,500000,UK,2000.0
```

JSON Format Example:
```json
[
  {
    "name": "John Doe",
    "platform": "Instagram",
    "category": "Fashion",
    "followers": 100000,
    "adRate": 1000.0,
    "country": "USA"
  },
  {
    "name": "Jane Smith",
    "platform": "YouTube",
    "category": "Beauty",
    "followers": 500000,
    "adRate": 2000.0,
    "country": "UK"
  }
]
```

## Logging Out

To log out of the system:
1. From the user profile, type `4` and press Enter
2. You'll be returned to the login screen

## Troubleshooting

**Issue: Invalid username or password**
- Make sure you're entering the correct username and password
- Usernames and passwords are case-sensitive

**Issue: Error when importing data**
- Ensure your file is in the correct format (CSV or JSON)
- Check that the file path is correct
- Verify that the file has the required columns/fields

**Issue: Cannot see ad rates**
- Ad rates are only visible to Premium subscribers
- Subscribe to Premium from the user profile screen

**Issue: Changes not saved**
- The system automatically saves changes to your favorites
- If you're experiencing issues, try logging out and back in
