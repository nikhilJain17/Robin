var robot = require("robotjs"); // mouse mover, screen interacter
var app = require("express")();
var http = require("http").Server(app);
var io = require("socket.io")(http);

app.get('/', function (req, res) {
	res.send("I hear you");
	console.log("Somebody connected, guess who!\n" + req);
});

io.on("connection", function(socket) {

	console.log('connected');

	// mouse move event
	socket.on('mouse_move', function(x,y) {

		var currentPos = robot.getMousePos();

		var newX = currentPos.x + x;
		var newY = currentPos.y + y;

		robot.moveMouse(newX, newY);

	});

});


http.listen(8080, function() {
	console.log("Listening on *8080");
});