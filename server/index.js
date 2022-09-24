const express = require("express")
const app = express()
const port = 300

app.listen(port, () => {
    console.log(`Listening at http://localhost:${port}`)
});

app.get('/', (req, res) => { //can declare get our put route, first param is the route, second param is the function that is executed
    res.send("Hello world");
});

app.get('/api/users', (req, res) => {
    res.send("Hello world users query");
    console.log(req.query);
    console.log(req.query.id);
});

app.get('/api/users/:uid', (req, res) => {
    res.send("Hello world users route ");
    console.log(req.params);
});

app.use(express.urlencoded());
app.use(express.json());
app.post('/api/users', (req, res) => {
    res.send("Hello world users post");
    console.log(req.body);
    console.log(req.body.name);
});

const myFirestMiddleWare = (req, res, next) => {
    console.log("my first middle ware");
    next();
}
app.use(myFirestMiddleWare) //global middlware

console.log("Hello World");

function printHello() {
    console.log("printing hello");
};

printHello();

const otherPrintHello = () => {
    console.log("printing other hello");
};

otherPrintHello();

const { MongoClient } = require("mongodb");
// Connection URI
const uri =
  "mongodb://localhost:27017/?maxPoolSize=20&w=majority"; //mongodb+srv://localhost:27017/?maxPoolSize=20&w=majorit
// Create a new MongoClient
const client = new MongoClient(uri);
async function run() {
  try {
    // Connect the client to the server (optional starting in v4.7)
    await client.connect();
    // Establish and verify connection
    await client.db("admin").command({ ping: 1 });
    console.log("Connected successfully to server");
    var user = await client.db("users").collection("user").findOne({name: "John D"});
    console.log("user is " + user.name);
  } finally {
    // Ensures that the client will close when you finish/error
    await client.close();
  }
}
run().catch(console.dir);