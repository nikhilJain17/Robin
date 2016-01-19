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

	console.log("Screen: " + robot.getScreenSize().width + ", " + robot.getScreenSize().height);

	// mouse move event
	socket.on('mouse_move', function(x,y) {


		var currentPos = robot.getMousePos();

		var amplifier = -8;

		var newX = Math.round(currentPos.x + (amplifier * x)); 
		var newY = Math.round(currentPos.y + (amplifier * y));

		console.log("\nSent through server: " + x + ", " + y);
		console.log("\nCurrent coordinates: " + currentPos.x + ", " + currentPos.y);
		console.log("New mouse coordinates: " + newX + ", " + newY);

		// dont move past edge of screen
		// if (newX < robot.getScreenSize().width && newY < robot.getScreenSize.height)
			robot.moveMouseSmooth(newX, newY);
		// else
		// 	console.log("Too far")

	});


	// left click
	socket.on('left_click', function() {
		console.log('left click');
		robot.mouseClick("left");
	});


	// right click
	socket.on('right_click', function() {
		console.log('right click');
		robot.mouseClick("right");
	})


	// scroll
	socket.on('scroll', function(scrollY) {

		console.log('scroll');

		var directionY;

		if (scrollY < 0)
			directionY = "down";
		else
			directionY = "up";

		robot.scrollMouse(scrollY * 0.3, directionY);

	});

});


http.listen(8080, function() {
	console.log("Listening on *8080");
});