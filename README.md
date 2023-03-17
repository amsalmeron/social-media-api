Social Media API
===============================

## Overview

A RESTful API using Spring Boot, JPA, and Postgresql. 

A database schema with Spring services and controllers to handle requests, perform validation and business logic, and to transform data between the API and database models.

## Entity Relationship Diagram
![Spring Assessment ERD](https://user-images.githubusercontent.com/12191780/187276918-ccb2d373-be3b-42ff-a74d-5560ba806a10.png)


This ERD represents the database.

## API Endpoints

### `GET   validate/tag/exists/{label}`
Checks whether or not a given hashtag exists.

#### Response
```javascript
'boolean'
```

### `GET   validate/username/exists/@{username}`
Checks whether or not a given username exists.

#### Response
```javascript
'boolean'
```

### `GET   validate/username/available/@{username}`
Checks whether or not a given username is available.

#### Response
```javascript
'boolean'
```

### `GET     users`
Retrieves all active (non-deleted) users as an array.

#### Response
```javascript
['User']
```

### `POST    users`
Creates a new user. If any required fields are missing or the `username` provided is already taken, an error will be sent in lieu of a response.

If the given credentials match a previously-deleted user, re-activate the deleted user instead of creating a new one.

#### Request
```javascript
{
  credentials: 'Credentials',
  profile: 'Profile'
}
```

#### Response
```javascript
'User'
```

### `GET     users/@{username}`
Retrieves a user with the given username. If no such user exists or is deleted, an error will be sent in lieu of a response.

#### Response
```javascript
'User'
```


### `PATCH   users/@{username}`
Updates the profile of a user with the given username. If no such user exists, the user is deleted, or the provided credentials do not match the user, an error will be sent in lieu of a response.

#### Request
```javascript
{
  credentials: 'Credentials',
  profile: 'Profile'
}
```

#### Response
```javascript
'User'
```

### `DELETE  users/@{username}`
"Deletes" a user with the given username. If no such user exists or the provided credentials do not match the user, an error will be sent in lieu of a response. If a user is successfully "deleted", the response will contain the user data prior to deletion.

This action does not actually drop any records from the database. 

#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'User'
```

### `POST    users/@{username}/follow`
Subscribes the user whose credentials are provided by the request body to the user whose username is given in the url. If there is already a following relationship between the two users, no such followable user exists (deleted or never created), or the credentials provided do not match an active user in the database, an error will be sent as a response. If successful, no data is sent.

#### Request
```javascript
'Credentials'
```

### `POST    users/@{username}/unfollow`
Unsubscribes the user whose credentials are provided by the request body from the user whose username is given in the url. If there is no preexisting following relationship between the two users, no such followable user exists (deleted or never created), or the credentials provided do not match an active user in the database, an error will be sent as a response. If successful, no data is sent.

#### Request
```javascript
'Credentials'
```

### `GET     users/@{username}/feed`
Retrieves all (non-deleted) tweets authored by the user with the given username, as well as all (non-deleted) tweets authored by users the given user is following. This includes simple tweets, reposts, and replies. The tweets will appear in reverse-chronological order. If no active user with that username exists (deleted or never created), an error will be sent in lieu of a response.

#### Response
```javascript
['Tweet']
```

### `GET     users/@{username}/tweets`
Retrieves all (non-deleted) tweets authored by the user with the given username. This includes simple tweets, reposts, and replies. The tweets will appear in reverse-chronological order. If no active user with that username exists (deleted or never created), an error will be sent in lieu of a response.

#### Response
```javascript
['Tweet']
```

### `GET     users/@{username}/followers`
Retrieves the followers of the user with the given username. Only active users will be included in the response. If no active user with the given username exists, an error will be sent in lieu of a response.

#### Response
```javascript
['User']
```

### `GET     users/@{username}/following`
Retrieves the users followed by the user with the given username. Only active users will be included in the response. If no active user with the given username exists, an error will be sent in lieu of a response.

#### Response
```javascript
['User']
```

### `GET     tweets`
Retrieves all (non-deleted) tweets. The tweets will appear in reverse-chronological order.

#### Response
```javascript
['Tweet']
```

### `POST    tweets`
Creates a new simple tweet, with the author set to the user identified by the credentials in the request body. If the given credentials do not match an active user in the database, an error will be sent in lieu of a response.

The response will contain the newly-created tweet.

#### Request
```javascript
{
  content: 'string',
  credentials: 'Credentials'
}
```

#### Response
```javascript
'Tweet'
```

### `GET     tweets/{id}`
Retrieves a tweet with a given id. If no such tweet exists, or the given tweet is deleted, an error will be sent in lieu of a response.

#### Response
```javascript
'Tweet'
```

### `DELETE  tweets/{id}`
"Deletes" the tweet with the given id. If no such tweet exists or the provided credentials do not match author of the tweet, an error will be sent in lieu of a response. If a tweet is successfully "deleted", the response will contain the tweet data prior to deletion.


#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'Tweet'
```

### `POST    tweets/{id}/like`
Creates a "like" relationship between the tweet with the given id and the user whose credentials are provided by the request body. If the tweet is deleted or otherwise doesn't exist, or if the given credentials do not match an active user in the database, an error will be sent. Following successful completion of the operation, no response body is sent.

#### Request
```javascript
'Credentials'
```

### `POST    tweets/{id}/reply`
Creates a reply tweet to the tweet with the given id. The author of the newly-created tweet will match the credentials provided by the request body. If the given tweet is deleted or otherwise doesn't exist, or if the given credentials do not match an active user in the database, an error will be sent in lieu of a response.

The response will contain the newly-created tweet.

#### Request
```javascript
{
  content: 'string',
  credentials: 'Credentials'
}
```

#### Response
```javascript
'Tweet'
```

### `POST    tweets/{id}/repost`
Creates a repost of the tweet with the given id. The author of the repost will match the credentials provided in the request body. If the given tweet is deleted or otherwise doesn't exist, or the given credentials do not match an active user in the database, an error will be sent in lieu of a response.

Because this creates a repost tweet, content is not allowed. Additionally, notice that the `repostOf` property is not provided by the request. The server must create that relationship.

The response will contain the newly-created tweet.

#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'Tweet'
```

### `GET     tweets/{id}/likes`
Retrieves the active users who have liked the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error will be sent in lieu of a response.

Deleted users will be excluded from the response.

#### Response
```javascript
['User']
```

### `GET     tweets/{id}/context`
Retrieves the context of the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error will be sent in lieu of a response.

#### Response
```javascript
'Context'
```

### `GET     tweets/{id}/replies`
Retrieves the direct replies to the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error will be sent in lieu of a response.

Deleted replies to the tweet will be excluded from the response.

#### Response
```javascript
['Tweet']
```

### `GET     tweets/{id}/reposts`
Retrieves the direct reposts of the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error will be sent in lieu of a response.

Deleted reposts of the tweet will be excluded from the response.

#### Response
```javascript
['Tweet']
```
