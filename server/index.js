const express = require("express");
const app = express();
const port = 3000;
const jwt  = require("jsonwebtoken");
const jwtSecret = "Group3KeyForJWT";

app.listen(port, () => {
    console.log(`Listening at http://localhost:${port}`)
});

const jwtValidateUserMiddleware = (req, res, next) => {
    console.log("validate user");

    let token = req.header("x-jwt-token");
    if (token) {
        try {
            console.log("verify " + token)
            let decoded = jwt.verify(token, jwtSecret);
            req.decodedToken = decoded;
            console.log("user validated " + decoded);
            next();
        } catch (err) {
            res.status(401).status({error: "Invalid token", fullError: err});
        }
    } else {
        res.status(401).send({error: "Token is required"});
    }
}

app.use(express.urlencoded());
app.use(express.json());
app.post("/api/auth", async (req, res) => {

    let user = await findUser(req.body.email);
    console.log("found " + user);
    if (user._id) {
        let token = jwt.sign({uid: user._id, name: user.name}, jwtSecret);
        console.log("generating token for " + user.name);

        res.send({email: user.email, name: user.name, token: token});
    } else {
        res.status(401).send({error: "You're not found"});
    }
});

app.post('/api/users', jwtValidateUserMiddleware, (req, res) => {
    console.log("send response back");
    
    let decodedToken = req.decodedToken

    res.send({message:"Hello world users post ", data: {decoded: decodedToken}});
    console.log(req.body);
    console.log(req.body.name);
});



const { MongoClient } = require("mongodb");
// Connection URI
const uri =
  "mongodb://localhost:27017/?maxPoolSize=20&w=majority"; //mongodb+srv://localhost:27017/?maxPoolSize=20&w=majorit
// Create a new MongoClient
const client = new MongoClient(uri);
async function run() {
  try {
    await client.connect();
    
    var user = await client.db("users").collection("user").findOne({name: "John D"});
    console.log("user is " + user.name);
  } finally {
    await client.close();
  }
}
run().catch(console.dir);

async function findUser(email) {
    try {
        await client.connect();
        
        var user = await client.db("users").collection("user").findOne({email: email});
        if (user) {
            console.log("user is " + user.name);
            return user;
        }
      } finally {
        await client.close();
      }
}