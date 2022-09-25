# Project 4: ITIS 5280
## UNC Charlotte | Advanced Mobile Application Development
### Members:
- Alex Miller
- Tom Va
- Jared Tamulynas

## Implementatoin
APIs:
	- Registration: /api/signup <br />
		- If the registering email is found in the database, user is presented with an error toast message. <br />
	- Auth: /api/auth <br />
		- Both email and password must match. <br />
	- Protected APIS: <br />
		- User Profile Update: /api/user/update <br />
Creating a new user uses the Registration API. If API returns a successful user then go to user profile screen. <br />
Login uses the Auth API. API returns a user JSON if email and password match database entries. If no match a toast message is displayed. <br />
A successful login through either Registration or Auth APIs will return a token back to the client. <br />
If token expires or there is any exception during an update then user is presented with a toast message that something went wrong and user must logout. <br />
Logout uses the User Profile Update API, with the token removed. This works because of JWT Token validation middleware for every update call. <br />
After user logs out, user is not able to go "back" to previous screens. <br />


## Project Purpose

In this in class assignment you should work on creating a simple authentication API for mobile application, the requirements are as follows :

You are not allowed to use frameworks such as Firebase. 
You should use NodeJS and Express framework to create this app.
You should use an online provisioning provider, such as Heroku, Amazon AWS, or Microsoft Azure, or others.
Your api should provide sign in, login, and logout features. (Use JWT tokens!!)
Your api should provide a mechanism to allow the user to access protected resources after a successful login. For example, the protected api could return the user's profile information (name, age, weight, address). This api should only work for logged-in users.
Your api should provide an error reporting mechanism. All data returned by the API should be in JSON.
The API can be implemented using NodeJS and Express Framework.
The API should be deployed on a remote server, which means local host is not acceptable for your submission. The user data profile information should be stored on a remote database, you can use MongoDB or any database of your choice.
Mobile App
The API should be connected to the mobile application, and should also demonstrate the protected API.
Sign Up (Registration), Login, Profile Screen.
The profile screen should allow the user to view and edit their profile information.
Submission should include:

Create a Github or Bitbucket repo for the assignment.
Push your code to the created repo. Should contain both the mobile and web code. 
On the same repo create a wiki page describing your api design and implementation. The wiki page should describe the API routes, DB Schema and all the assumptions required to provide authentication. In addition describe any data that is stored on the device or on the server.
Include the Postman file in the repo.
The API should be demonstrated using Postman, you should create an api component in Postman for each of your created APIs.
Demo your API using a mobile app that uses your implemented api.