
u = require('underscore');
express = require('express');
app = express();

/* Set up global logging middleware */
app.use(require('morgan')('dev'));


var bodyParser = require('body-parser');
/* Global body parser */
var jsonParser = bodyParser.json();
app.use(bodyParser.urlencoded({ extended: true }));


console.log('Setting up API server.')

/**
 * This is an MVP server that holds the todolist data in memory.
 */

function TodoItem(
        id,
        completed,
        contents,
        modifiedOn,
        creator)
{
    this.id = id
    this.completed = completed
    this.contents = contents
    this.modifiedOn = modifiedOn
    this.creator = creator
}

todoList = []

app.post('/todo/new', function(req, res){
    console.log(req.body)
})

app.get('todo/', function(req, res){

})

app.get('todo/:id', function(req, res){
    var id = req.params.id;
})

app.put('todo/:id', function(req, res){
    var id = req.params.id;
})

app.delete('todo/:id', function(req, res){
    var id = req.params.id;
})




var server = app.listen(8080, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('SayHi server listening at http://%s:%s', host, port);
});




