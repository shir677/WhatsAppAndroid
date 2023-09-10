# Our WhatsApp Web - Registration, Login and Chat screens
  
## Introduction:
This project consists of three React page - registration page, login page, and a chat page. 
The initial display will be of the login page, then if you don't have a user, you should go to the 
registration page. After registering, you will log in and get to the chat page, where you can talk 
with another users.

## Prerequisites:
To view the pages, you will need a web browser such as Chrome, Firefox or Safari.

## Instructions for use:
1. Open a local project on your computer using React (write 'npx create-app-react app' in terminal), and get into the 'app' folder it downloaded.  
2. Delete all files, except to 'node_modules' sub-folder.  
3. Copy or download this github project files to your local computer.  
4. move the whole project to 'app' folder.  
5. <b>Before starting the project, please install the next, using your terminal from the edited 'app' folder:  
  'npm install jquery'  
  'npm install moment'  
  'npm install react-icons --save'  
  'npm install @react-icons/all-files --save'</b> 
6. Open a local server of React from the terminal before (write 'npm start').    
7. You should see by now, an open local server which have been opened by React, and the project is shown there.  

## Registration page:
The registration page includes a form that allows users to enter their username, nickname, picture, password and password confirmation. The page includes a link to the login page for users who already have an account and also a button to send the form, so one can register and be added to a database.
  
![image](https://github.com/shir677/Ex1b/assets/83518959/7144a163-f33a-4d59-9309-6f87a76b0dde)
  
## Login page:
The login page includes a form for users to enter their name and password. The page includes a link to the registration page for users who need to create a new account and also a button to send the form to the chat screen.
Notice that users who haven't registered, will not be able to get to the chat screen.
  
![image](https://github.com/shir677/Ex1b/assets/83518959/26da3810-7f4c-40e2-9675-e15d10f3ab5b)
  
## Chat screen:
The chat screen displays messages from other users and allows users to send messages to each other. The page includes a chat box where messages are displayed and a box where users can enter their message and send it to the chat box. The page also includes a button for adding contacts.  
  
![image](https://github.com/shir677/Ex1b/assets/83518959/9b557ef5-a4dc-424e-a88d-de1794c388d8)
   
## CSS files:
The CSS files for each page include styling for the page elements such as fonts, colors and layout.

## Summary:
The registration, login and chat pages are designed to provide a simple and friendly interface for users to communicate with each other. There is already functionality for this project, but notice that there is no active server at the moment, and since then - there is no option for the other users to reply back a comment to the user who sent a message.
