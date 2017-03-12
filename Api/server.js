
u = require('underscore');
express = require('express');
app = express();

/* Set up global logging middleware */
app.use(require('morgan')('dev'));


var bodyParser = require('body-parser');
/* Global body parser */
app.use(bodyParser.json());


login = require('./login.js');

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

app.post('/todo/new', login.authMiddleware,  function(req, res){

    //We need to assign an ID number to the new todo item.

    var item = req.body; //Don't actually do this in real life, this is very type-unsafe.

    var maxId = !u.isEmpty(todoList) ? u.max(todoList, function(todoItem){return todoItem.id}).id : -1

    if (!item.id || item.id == 0 || item.id < maxId){
        item.id = maxId + 1
    }

    todoList.push(item)
    console.log(todoList)
    res.json({items: todoList})

})

app.get('/todo/', login.authMiddleware, function(req, res){
    res.json(todoList)
})

app.get('/todo/:id', login.authMiddleware, function(req, res){
    var id = req.params.id;

    res.json(u.find(todoList, login.authMiddleware, function(item){return item.id == id}))
})

app.put('/todo/:id', login.authMiddleware, function(req, res){
    var id = req.params.id;

    var index = u.indexOf(todoList, u.find(todoList, function(item){return item.id == id}))
    todoList[index] = req.body

    res.json({items: todoList})
})

app.delete('/todo/:id', login.authMiddleware, function(req, res){
    var id = req.params.id;

    var index = u.indexOf(todoList, u.find(todoList, function(item){return item.id == id}))

    todoList[index] = null

    todoList = u.compact(todoList)

    res.json({items: todoList})
    
})




var server = app.listen(8000, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Demo server listening at http://%s:%s', host, port);
});




