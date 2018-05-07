'use strict';

var socket = require('socket.io')

var io = socket.listen(8081)

var users = []

io.sockets.on('connection', (socket) => {
    // GET NICKNAME FROM NEW USER    
    var data = socket.request;
    var nickname = data._query['nickname']

    
    if (!nickname || nickname==undefined || nickname==null) return socket.disconnect();
    if(users.indexOf(nickname)>-1) {
        return socket.disconnect();
    } else {
        users.push(nickname)
    }

    // GET TIME AND NOTIFY ALL USERS ABOUT NEW USER
    var time = (new Date).toLocaleTimeString();
    socket.json.send({'event': 'connected',
                      'nickname': nickname,
                      'time': time});
    console.log(time + " new user! he'r name is " + nickname)
    
    socket.broadcast.json.send({'event': 'userJoin', 'nickname': nickname, 'time': time, 'text': 'User '+nickname+' joined'});  
    
    // ON NEW MESSAGE + NOTIFY ALL USERS ABOUT IT
    socket.on('message', function(msg) {
        var time = (new Date).toLocaleTimeString();
        
        socket.json.send({'event': 'messageSent', 'name': nickname, 'text': msg, 'time': time});
        console.log(time+" user " + nickname + " sent new message: " + msg)
		
		socket.broadcast.json.send({'event': 'messageNewReceived', 'name': nickname, 'text': msg, 'time': time})
	});
    
    // ON USER LEAVE FROM CHAT
	socket.on('disconnect', function() {
		var time = (new Date).toLocaleTimeString();
        io.sockets.json.send({'event': 'userLeave', 'nickname': nickname, 'time': time, 'text': 'User '+nickname+' leave from chat'});
        var user_index = users.indexOf(nickname)
        if(user_index>-1) {
            users.splice(user_index, 1)
        }
        console.log(time + " user " + nickname + " leave from chat")
	});
})



