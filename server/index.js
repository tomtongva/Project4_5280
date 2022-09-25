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
            res.status(401).send({error: "Invalid token", fullError: err});
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
    if (user) {
        let token = jwt.sign({uid: user._id, name: user.firstName + " " + user.lastName, 
                                exp:Math.floor(Date.now()/1000)+60*1,
                                currentTime:Date.now()}, jwtSecret);
        console.log("generating token for " + user.firstName);

        res.send({email: user.email, firstName: user.firstName, lastName: user.lastName, gender: user.gender,
            city: user.city, token: token});
    } else {
        res.status(401).send({error: "You're not found"});
    }
});

app.post('/api/user/update', jwtValidateUserMiddleware, async (req, res) => {
    let updated = await updateUser(req.body.email, req.body.firstName, req.body.lastName, req.body.gender, req.body.city);
    if (updated) {
        console.log("send successful updated response back");
        
        let decodedToken = req.decodedToken

        res.send({message:"user updated", data: {decoded: decodedToken}});
    } else {
        console.log("send failed update response back");
        res.status(401).send({error: "user update failed"});
    }
});

app.post('/api/signup', async (req, res) => {
    console.log("signup new user " + req.body.email);
    let user = await findUser(req.body.email)
    console.log("does user exist "  + user)
    let userId;
    if (null == user) {
        userId = await createUser(req.body.email, req.body.password, req.body.firstName, req.body.lastName, req.body.gender,
            req.body.city);
        console.log("got user id " + userId);
    } else {
        console.log("user exists " + user.email);
    }

    res.send({message: "You're registered ", id: userId, email: req.body.email, firstName: req.body.firstName, 
        lastName: req.body.lastName, gender: req.body.gender, city: req.body.city});
});



const { MongoClient } = require("mongodb");
// Connection URI
const uri =
  "mongodb://localhost:27017/?maxPoolSize=20&w=majority"; //mongodb+srv://localhost:27017/?maxPoolSize=20&w=majorit
// Create a new MongoClient
const client = new MongoClient(uri);

async function createUser(email, password, firstName, lastName, gender, city) {
    try {
        await client.connect();
        
        const doc = {
            email: email,
            password: password,
            firstName: firstName,
            lastName: lastName,
            gender: gender,
            city: city
        }
        const result = await client.db("users").collection("user").insertOne(doc);
        if (result) {
            console.log("user created with id " + result.insertedId);
            return result.insertedId;
        }
      } finally {
        await client.close();
      }
}

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

async function updateUser(email, firstName, lastName, gender, city) {
    try {
        await client.connect();
        
        const filter = {email: email};
        console.log("attemp to update " + email);
        const updateDoc = {$set: {firstName: firstName, lastName: lastName, gender: gender, city: city}};
        var updated = await client.db("users").collection("user").updateOne(filter, updateDoc);
        if (updated) {
            console.log("user was updated")
        }
        return updated;
      } finally {
        await client.close();
      }
}