const mongoose = require("mongoose");

const MessageSchema = mongoose.Schema({    
  chatId: {
    type: mongoose.Schema.Types.ObjectId,
    required: true
  },
    created: {
        type: Date,
        required: true,
        default: Date.now
    },
    sender: {
      username: {
        type: String,
        required: true
      }
    },
    content: {
        type: String, 
        required: true
      },
    getMessage: {
      type: [String],
      required: true,
    },
  }
  );

module.exports = mongoose.model("Message", MessageSchema);