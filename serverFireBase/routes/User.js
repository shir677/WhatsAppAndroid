const express =  require('express');
const Chat = require('../controllers/Chat');
const User = require('../controllers/User');
const router = express.Router();

router.route('/Chats')
    .get(Chat.getChats)
    .post(User.isLoggedIn, Chat.addChat);

router.get('/Users/:id', User.getUser)
router.post('/Users', User.addUser)

router.post('/Tokens', User.getToken)

router.route('/Chats/:id/Messages')
    .get(User.isLoggedIn, Chat.getChatMessages)
    .post(User.isLoggedIn, Chat.addMessage)

router.delete('/Chats/:id', User.isLoggedIn,Chat.deleteChat)

module.exports = router;