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

		// CONTROLS SENSITIVITY
		var amplifier = -10;

		var newX = Math.round(currentPos.x + (amplifier * x)); 
		var newY = Math.round(currentPos.y + (amplifier * y));

		console.log("\nSent through server: " + x + ", " + y);
		console.log("\nCurrent coordinates: " + currentPos.x + ", " + currentPos.y);
		console.log("New mouse coordinates: " + newX + ", " + newY);

		// handle negatives
		if (newX < 0)
			newX = 0;

		if (newY < 0)
			newY = 0;

		// dont move past edge of screen
		// if (newX < robot.getScreenSize().width - 10 && newY < robot.getScreenSize().height - 10)
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

	// keyboard
	socket.on('key', function(keyPressed, isCapsLock) {

		

		var chr;
		// convert to corresponding ascii depending on if capslock or not
		if (isCapsLock)
			chr = String.fromCharCode(keyPressed + 36); // 36 is offset from android codes to ascii
		
		else
			chr = String.fromCharCode(keyPressed + 36 + 32); // 32 is uppercase to lowercase offset

		// handle special case space
		if (keyPressed == -1) {
			chr = "space";
		}

		console.log('Key Pressed: ' + chr);
		robot.keyTap(chr);

	});

});


http.listen(8080, function() {
	console.log("Listening on *8080");
});