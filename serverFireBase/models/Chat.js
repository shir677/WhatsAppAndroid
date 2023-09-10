const mongoose = require("mongoose");

const chatSchema = new mongoose.Schema({
    username1:  {
        type: String,
        required: true
        },
    username2: {
        type: String,
        required: true
    },
    getChat: {
        type: [String],
        required: true,
        default: function () {
            return [this.username1, this.username2];
          }
      },
});

module.exports = mongoose.model("Chat", chatSchema);