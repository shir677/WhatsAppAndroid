const { Server } = require('socket.io')

const express = require('express');
var app = express();
const http = require('http')
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json({limit:'10mb'}));

const cors = require('cors');
app.use(cors());



require('custom-env').env(process.env.NODE_ENV, './config');
console.log(process.env.CONNECTION);
console.log(process.env.PORT);

const mongoose = require('mongoose');
mongoose.connect(process.env.CONNECTION, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
  serverSelectionTimeoutMS: 30000, // 30 seconds
})
  .then(() => console.log('Connected to MongoDB'))
  .catch((error) => console.error('Failed to connect to MongoDB:', error));

app.use(express.static('public'))

const user = require('./routes/User');
const { getAnotherUser } = require('./services/Chat');
app.use('/api', user);


const httpServer = http.createServer(app, {
  maxHttpBufferSize: 10 * 1024 * 1024,});


const io = new Server(httpServer, {
  maxHttpBufferSize: 10 * 1024 * 1024, // Set a higher value to increase the payload size limit
  transports: ["websocket"],
  cors: {
    origin: "http://localhost:3000",
  },
});


io.on("connection", (socket) => {

  console.log(`user ${socket.id} has connected`)

  socket.on("joinUser",(username)=>{
    socket.join(username)
  })


  socket.on("joinRoom", async(data) => {
    socket.join(data.roomId)
    console.log(`user ${socket.id} joined room ${data.roomId}`)
    if (data.contact){
      const user = await getAnotherUser(data.roomId,data.contact)
      io.to(data.contact).emit("newRoom",{id:data.roomId,user})

    }
  })

  socket.on("message", async(data) => {
    const { roomId, message } = data;
    console.log({ roomId, message })
    io.to(roomId).emit("message", { ...message, chatId: roomId, id: message._id });
    
    console.log(message,roomId,data.roomId,data.chatId)
    const user = await getAnotherUser(message.chatId,message.sender.username)

  
  })

  socket.on("leaveRoom", (roomId) => {
    socket.leave(roomId);
  })
});


httpServer.listen(process.env.PORT, () =>
  console.log(`Server started on ${process.env.PORT}`)
);