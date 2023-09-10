var admin = require("firebase-admin");
var serviceAccount = require("../whatsappproject-38500-firebase-adminsdk-41zcf-de25f27f72.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://whatsappproject-38500-default-rtdb.firebaseio.com"
});

module.exports = admin;