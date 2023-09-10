const getData =  require('./User')
const Chat = require('../services/Chat')





const getDataFromToken = (token) => {
    try{
        const data = getData.getDataFromToken(token)
        return data.username
    }
    catch (error) {
        console.error('Failed to getDataFromToken :', error);
    }
}

const getChats = async(req,res) => {
    if (req.headers.authorization) {
        try {
            const username = getDataFromToken(req.headers.authorization)
            res.json( await Chat.getChats(username))
        } catch (error) {
            res.status(404).send(' cant get Chats ')
        }
    }
    else res.status(401).send('Invalid token')
}

const getChatMessages =  async(req,res) => {
    console.log("req", req.get("host"), req.get("origin"))
    try {
        const username = getDataFromToken(req.headers.authorization)
        const result = await Chat.getChatMessages(username,req.params.id)
        if (result)
            res.json( result)
        else
            res.json([])
        
    } catch (error) {
        res.status(401).send(error)
    }

}

const addChat = async(req,res) =>{
    try {
        const username = getDataFromToken(req.headers.authorization)
        const results = await Chat.addChat(username,req.body.username)
        console.log("newChat?", results)
        res.json(results)

    } catch (error) {
        res.status(401).send(error)
    }
}

const addMessage = async(req,res) =>{
    try {
        const username = getDataFromToken(req.headers.authorization)

        res.json(await Chat.addMessage(username,req.body,req.params.id))
        
        
    } catch (error) {
        res.status(401).send(error)    
    }
}

const deleteChat = async(req,res) => { 
    try {
        const username = getDataFromToken(req.headers.authorization)
        await Chat.deleteChat(username, req.params.id)
        res.status(200).send(req.params.id)
    } catch (error) {
        res.status(401).send(error)    
    }

}

module.exports = {
    getChats, 
    getChatMessages, 
    addChat, 
    addMessage, 
    deleteChat,
    getDataFromToken
}