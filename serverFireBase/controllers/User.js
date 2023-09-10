const jwt = require("jsonwebtoken")
const key = "Some super secret key shhhhhhhhhhhhhhhhh!!!!!"
const User = require('../services/User')
//const { default: userFire } = require("../userFire")

let userFire = new Map();




const getToken = async (req,res) => { 
    const token = await User.getToken(req.body.username,req.body.password)
    if(token){
        const data = { username: req.body.username }
        // Generate the token.
        const token = jwt.sign(data, key)
        // Return the token to the browser
        //console.log("token "+token+ " data "+data)
        res.status(201).json(token);
        }
    else{
        // Incorrect username/password. The user should try again.
        res.status(404).send('Invalid username and/or password')
    }
}

const getUser = async (req,res) => { 
    if (req.headers.authorization) {
        const data = getDataFromToken(req.headers.authorization)

        if (req.headers.fire) {
            console.log("fireeeeee")
           
           
            // Update client details
            userFire.set(data.username, {
                fire: req.headers.fire
            });
  

            //userFire[data.username]=req.headers.fire
        }


        res.json(await User.getUser(data.username))
    }
    else res.status(401).send('Invalid token')
}

const addUser = async(req,res) =>{
    const add = await User.addUser(req.body)
    if(add){
        res.status(200).json({"message":"add"});
    }
   else res.status(409).json({"message":'faild'});
}

const isLoggedIn = (req, res, next) => {
    // If the request has an authorization header
    if (req.headers.authorization) {
        if(getDataFromToken(req.headers.authorization)){
            return next()
        }
        // Extract the token from that header
        /*const token = req.headers.authorization.split(" ")[1];
        const modifiedToken = token.substring(1, token.length - 1);
        try {
            // Verify the token is valid

            const data = jwt.verify(modifiedToken, key);
            // Token validation was successful. Continue to the actual function (index)
            return next()
        } catch (err) {
            return res.status(401).send("Invalid Token");   
            }*/
        }
    else
        return res.status(403).send('Token required');
}

const getDataFromToken = (reqToken) => {
    // Extract the token from that header
    const token = reqToken.split(" ")[1];
    const modifiedToken = token.substring(1, token.length - 1);
    try {
    //return modifiedToken;
    const data = jwt.verify(modifiedToken, key);
    // Token validation was successful. Continue to the actual function (index)
        return data;
    } catch (err) {
        return false;   
        }
    }

module.exports = {
    getToken,
    getUser,
    addUser,
    isLoggedIn,
    getDataFromToken,
    userFire
}