const mongoose = require("mongoose");

const userSchema = new mongoose.Schema({
    username : {
        type: String,
        required: true,
        unique: true
        },
    password: {
        type: String,
        required: true,
        min: 8,
        },
    displayName : {
        type: String,
        required: true
        },
    profilePic: {
        type: String,
        required: true
        },
});

module.exports = mongoose.model("User", userSchema);