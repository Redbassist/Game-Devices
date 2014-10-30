//only one global variable, everything will be available from here! This is the exception
var game;


function Game() 
{
	this.screenWidth = window.innerWidth;
	this.screenHeight = window.innerHeight;
	//this.ctx;
	this.ball = new Ball(this.screenWidth / 2, this.screenHeight / 2);
	document.addEventListener("keydown", move); //this means that function move will be called
}

function main()
{
	game = new Game();
	
	//call initCanvas function here
	game.initCanvas();
	
	/*
	for (var i = 0; i < 1; i++)
	{
		game.draw();
	}
	*/
	game.gameLoop();
}

//creating a game loop
Game.prototype.gameLoop = function()
{	
	game.update();
	game.draw();
	window.requestAnimationFrame(game.gameLoop);
}

Game.prototype.update = function() 
{
	//to get input from the player
}

//takes e as the event
function move(e) {
	/*outputting the key pressed
	console.log(e.keyCode);
	*/
	game.ball.Move(e);

}

Game.prototype.initCanvas = function()  //prototype is a way of making object aware of another
{
	//create a canvas element
	this.canvas = document.createElement("canvas");
	//create a 2d content for drawing
	this.ctx = this.canvas.getContext('2d');  //canvas and this.canvas are DIFFERENT!!!

	//????
	document.body.appendChild(this.canvas);

	//setting the size of the canvas to the window size
	this.canvas.width = this.screenWidth;
	this.canvas.height = this.screenHeight;

	var r = Math.random() * 255;
	var g = Math.random() * 255;
	var b = Math.random() * 255;

	this.ctx.fillStyle = rgb(r,g,b);
}

Game.prototype.draw = function()
{
	game.ball.Draw(this.ctx);
}


/*function for rgb for convenience*/
function rgb(r, g, b) 
{ return 'rgb('+clamp(Math.round(r),0,255)+', '+clamp(Math.round(g),0,255)+', '+clamp(Math.round(b),0,255)+')';};

/*helper function*/
function clamp(value, min, max) { 
	if(max<min) { 
		var temp = min; 
		min = max; 
		max = temp; 
	}
	return Math.max(min, Math.min(value, max)); 
};