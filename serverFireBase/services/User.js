const User = require('../models/User');

const getToken = async (username, password) => { 
    try {
        console.log(username, password)
        const user = await User.findOne({ username, password });

        if (user) {
            console.log('Valid credentials');
            return true;
        } else {
          console.log('User not found');
          return false;
        }
    } catch (error) {
    console.error('Failed to retrieve user:', error);
    }
}

const getUser = async (username) => { 
    try {
        const user = await User.findOne({ username});
        
        if (user) {
            console.log('found user');

            const userObject = user.toObject({ getters: true });
            const {username, displayName, profilePic } = userObject;
          
            const jsonObject = {username, displayName, profilePic};
            //console.log(jsonObject);
            return jsonObject;
        } else {
          console.log('User not found');
          return false;
        }
    } catch (error) {
    console.error('Failed to retrieve user:', error);
    }
}

const addUser = async(newUser) =>{
    try {
        const user = new User(newUser);
        await user.save();
        console.log('User added successfully');
        return true

    } catch (error) {
    console.error('Failed to add user:', error);
    return false
    }
}

module.exports = {getToken, getUser,addUser }