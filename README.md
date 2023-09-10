# WhatsAppAndroid App  

### In case you don't have a 'config' directory inside the 'serverFireBase' directory, create one - containing an 'env.local' file with the following content:  
CONNECTION="mongodb://127.0.0.1:27017/test"  
PORT="12345"  
CLIENT="http://localhost:3000"  

looks like that:  
  
![image](https://github.com/SlowlyFire/WhatsAppAndroid/assets/83518959/0cb8f554-4b1d-4e86-8c88-557607dc9750)  
  
### Now you should add a 'node_modules' directory to the 'serverFireBase' directory (using node.js commands - 'npm init' first and then 'npm install')

### Introduction:  
<b>This project consists of 5 activities (pages)</b> - Registration page, Login page, Contacts page (the page with all the contacts), Chat page (page with a unique chat with one of the contacts) and a Setting activity, to change the IP and the theme screen.   
<b>The initial display will be of the Login page</b>, then if you don't have a user, you should go to the 
Registration page and create one.  
<b>Notice you can upload a profile picture limitied to 10MB</b>, but it's better to have a smaller picture, so the app will move faster.  
<b>After registering</b>, you will log in and get to the Contacts page, where you can add and talk with another users.  

### Prerequisites:  
To activate the app, you need to connect to the server first, and just then, to open the app via your android (and only android) phone.  

### Instructions for use:  
1. Download the zip code from github, then extract.
2. Inside the 'serverFireBase' directory, add 'config' and 'node_modules' directories as described earlier.  
3.a. From the 'serverFireBase' directory, use the 'npm start' command from the terminal (notice that the 'package.json' file fits to a windows user).  
3.b. If you are a linux user, you should change the 'start' command (inside the 'package.json') to:  
"start": "NODE_ENV=local node app.js",  
In windows:  
![image](https://github.com/SlowlyFire/WhatsAppAndroid/assets/83518959/8b43f8ce-a646-4b18-a485-9ed05549ed68)
  
In linux:  
![image](https://github.com/SlowlyFire/WhatsAppAndroid/assets/83518959/0a102c70-03b9-4885-bfea-400167f77719)  
  

4. In order to activate the server, install the next, using your terminal, from the 'serverFireBase' folder:  
  <b>'npm install jquery'  
  'npm install moment'  
  'npm install react-icons --save'  
  'npm install @react-icons/all-files --save'  
  'npm install cors'  
  'npm install custom-env'  
  'npm install jsonwebtoken'  
  'npm install body-parser'  
  'npm install socket.io-client'  
  'npm install firebase-admin'  
   etc..</b>

   ![image](https://github.com/SlowlyFire/WhatsAppAndroid/assets/83518959/37c2f1c8-7594-47b6-98b6-dc9f2df97bf2)  
  
5. Notice that in order to have notifications and a 'real-time' messages, you may need to install 'fireBase' and connect the server to fireBase.  
6. Activate the client, via your android studio (activate by openening in android studio only the 'client' directory, without the server directory).    
7. Once you have opened the app, make sure you entered your IP using the setting button. <b>that's super critical!!!!</b> 
Have fun!  
  
### Registration page:  
The Registration page includes a form that allows users to enter their username, nickname, picture, password and password confirmation. The page includes a link to the Login page for users who already have an account and also a button to send the form, so one can register and be added to a database.
    
### Login page:  
The Login page includes a form for users to enter their name and password. The page includes a link to the Registration page for users who need to create a new account and also a button to send the form to the Contacts page.  
Notice that users who haven't registered, will not be able to get to the Contacts page.  
Also notice you should change your IP to your IP, via the setting button (in order to know your ip on youe computer you can run 'ip a' on terminal.  
  
### Contacts page:  
The Contacts page displays the contacts of a user, their last messages, and more information related to them.  
The page also includes a button for adding contacts.  


### Chat page:   
The Chat page displays messages from other users and allows users to send messages to each other.  
The page includes a chat box where messages are displayed and a box where users can enter their message and send it to the chat box.  




### Setting page:  
The Setting page displays an option to change IP, and theme of the app.  


### Summary:  
The Registration, Login, Contacts, Chat and Setting pages are designed to provide a simple and friendly interface
for users to communicate with each other. Using 'socket.io' and 'fireBase', a user can get messages and notifications in 'real-time'.  
Have fun!  
