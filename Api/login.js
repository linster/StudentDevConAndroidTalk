
var jwt = require('express-jwt');
var bcrypt = require('bcrypt');
var u = require('underscore')
// data class User(
//         val username : String = "",
//         val authToken : String? /* The server does not send this to other collaborators */
// )


//Simulates what you would store in the database for user credentials
var userStorage = [
    //{id: 0, username: "", password: "", authToken: ""}
]


app.post('/login', function(req, res){

    var authraw = new Buffer(req.headers['authorization'].replace('Basic ', ""), 'base64').toString()

    var auth = authraw.split(':')
    var requser = auth[0]
    var reqpass = auth[1]


    var userRecord = u.filter(userStorage, function(user) { return user.user === requser})[0] //Edge case!!! What if two users have the same username?


    if (!userRecord || userRecord == undefined){
        res.sendStatus(401);
        return;
    }

    bcrypt.compare(reqpass, userRecord.password, function(err, result){

        if (err){
            console.log(err)
            res.err()
        }

        if (result){
            res.json({username: userRecord.username, authToken: userRecord.authToken})
        } else {
            res.send(401)
        }

        

    })
})

app.post('/login/register', function(req, res){

    var authraw = new Buffer(req.headers['authorization'].replace('Basic ', ""), 'base64').toString()

    var auth = authraw.split(':')
    var user = auth[0]
    var pass = auth[1]


    var maxId = !u.isEmpty(userStorage) ? u.max(userStorage, function(user){return user.id}).id : -1

    bcrypt.hash(pass, 15, function(err, hash){

        if (err){
            res.json({})
        }

        var newUser = {id: maxId + 1, user: user, password: hash, authToken: user + hash + new Date().getTime() };

        userStorage.push(newUser);

        console.log("Current users:")
        console.log(userStorage)

        res.json({username: user, authToken: newUser.authToken});
    })

})


var isAuthenticatedMiddleware = function(req, res, next) {


    var rawAuthHeader = req.headers['authorization']
    if (!rawAuthHeader || rawAuthHeader == undefined){
        res.sendStatus(401)
        return;
    }


    var token = rawAuthHeader.replace('Basic ', "")

    var user = u.find(userStorage, function(u) { return u.authToken === token})

    if (user){
        next()
    } else {

        console.log("Authentication failed. Provided token: " + token);
        console.log("Existing user storage: " + us)

        res.send(401)
    }


}



module.exports = {
    //Export our isAuthenticatedMiddleware so that the main server module can
    //use it on requests to protected resources.
    authMiddleware : isAuthenticatedMiddleware
}