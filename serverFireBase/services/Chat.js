const userServer = require('./User');
const Chat = require('../models/Chat');
const Message = require('../models/Message');












var admin = require("firebase-admin");
var serviceAccount = require("../whatsappproject-38500-firebase-adminsdk-41zcf-de25f27f72.json");
const { userFire } = require('../controllers/User');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://whatsappproject-38500-default-rtdb.firebaseio.com"
});




const fireBaseSender = (username, id, newMsg)=>{
    console.log('in fireBaseSender func ',username);
    console.log(userFire)
   let token = null;
   const clientDetails = userFire.get(username);
    if(clientDetails){
        console.log(username)
        token = clientDetails.fire;
    }
    if (token!=null){

      const msg = {
        // Specify the message data
        data: {
          chatId:id,
          message:newMsg
          // Add your custom key-value pairs here
          // For example, 'chatId': '123456'
        },
        notification: {
          // Specify the notification payload
          title: username,
          body: newMsg,
        },
      };

      admin.messaging().sendToDevice(token,msg)
    }
   // return token;
  }


















function sort(results){
  results.sort((a, b) => {
    if (a.lastMessage && b.lastMessage) {
      return b.lastMessage.created - a.lastMessage.created;
    } else if (a.lastMessage) {
      return -1;
    } else if (b.lastMessage) {
      return 1;
    } else {
      return 0;
    }
  });
}


const getChats = async (username) => { 
    try {

      const chats = await Chat.find({ getChat: { $in: [username]  } })

        const results = []
        const chatData = await Promise.all(
            chats.map(async (chat) => {
              const otherUser = chat.username1 === username ? chat.username2 : chat.username1;
              console.log(otherUser)
              const user = await userServer.getUser(otherUser);
              if(user){
                let lastMessage = null;
                const messages = await Message.find({ chatId: chat._id })
                  .sort({ created: -1 })
                  .limit(1);
        
                if (messages.length > 0) {
                  lastMessage = messages[0];
                }

                const result = {
                  id: chat._id,
                  user: user,
                  lastMessage: lastMessage
                }
                results.push(result)
              }
            }));
            console.log(results)
            sort(results)
            return results;
      } catch (error) {
        // Handle error
        console.error("Error finding chats:", error);
      }
}
//cheack if user exists with username
const getChatMessages = async (username,id) => { 
  try {
    
    const messages = await Message.find( { $and: [{chatId: id , getMessage: { $in: [username] } }] });

    const formattedMessages = messages.map(message => {
      return {
        id: message._id,
        sender: message.sender,
        content: message.content,
        created: message.created,
      };
    });

      if (messages.length > 0) {
        ans = formattedMessages
        console.log(ans)
        
        return ans;
      } else {
        console.log('messages not found: '+ id+" "+username);
        return false;
      }
  } catch (error) {
  console.error('Failed to retrieve messages:', error);
  }
}

const getAnotherUser = async(id,username)=>{
 try {
  const chat = await Chat.findById(id)
  const otherUser = chat.username1 === username ? chat.username2 : chat.username1;
  const user2 = await userServer.getUser(otherUser);
  if (user2 ){
      return user2
  }
 } catch (error) {
    console.error(error);  
 }
}

const addChat = async(username1,username2) =>{
  try {
    const user2 = await userServer.getUser(username2);
    if (user2 ){
      const newChat = {username1: username1, username2: user2.username}
      const isChat = await Chat.find({
        getChat: { $all: [username1, username2] }
      });
      //const isChat = await Chat.find({getChat: { $in: [username1, user2.username]}})

      //fireBaseSender(username2, isChat[0]._id,"newChat" )

        if (isChat.length > 0){
          const result = {
            id:  isChat[0]._id,
            user: user2,
            lastMessage: null
          }
        console.log(result)
        return result;
      }
      const chat = new Chat(newChat);
      await chat.save();

      //const chat = await Chat.create(newChat);
      console.log('chat added successfully');
      const result = {
        id: chat._id,
        user: user2,
        lastMessage: null
      }
      return result
    }
  } catch (error) {
  console.error('Failed to add chat:', error);
  }
}

const addMessage = async(username,message,id) =>{
    try {
      const chat = await Chat.findById(id)
      console.log(chat.username1, chat.username2)

      buildMsg ={
        chatId:id,
        sender:{username:username},
        content:message.msg,
        getMessage: [chat.username1, chat.username2]
      }
        const newMessage = new Message(buildMsg);
        await newMessage.save();

        await Chat.findByIdAndUpdate(id, { getChat: [chat.username1, chat.username2] });
       
        console.log('message added successfully');

        const name = username == chat.username1 ? chat.username2 : chat.username1;

        fireBaseSender(name,id,message.msg )

        return newMessage
        
    } catch (error) {
    console.error('Failed to add message:', error);
    }
  }

  const deleteChat = async (username, id) => {
    try {
      console.log("del hiii")
      const chat = await Chat.findById(id);
  
      if (!chat) {
        console.log('Chat not found.');
        return;
      }
  
      //const delChat = await Chat.find({ getChat: { $in: [username] } });
      console.log( chat.getChat)
        if (chat.getChat.length <= 1 || chat.getChat==[username,username]) {
          await Chat.findByIdAndRemove(id);
          await Message.renoveMany({ chatId: id });
          console.log('Chat deleted.');
        } else {
          await Chat.findByIdAndUpdate(id, { $pull: { getChat: username } });
          console.log('Username removed from chat.');
        }
        await Message.updateMany({ chatId: id }, { $pull: { getMessage: username } });
        console.log(username+ ' removed from messeges.');
            return
    } catch (error) {
      console.error('Failed to remove chat:', error);
    }
  };

module.exports = { getChats, getChatMessages, addChat, addMessage, deleteChat, getAnotherUser }
