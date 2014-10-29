//only one global variable, everything will be available from here! This is the exception
var game;


function Game() 
{
	this.screenWidth = window.innerWidth;
	this.screenHeight = window.innerHeight;
	//this.ctx;
	this.ball = new Ball(this.screenWidth, this.screenHeight);
	this.paddle1 = new Paddle(this.screenWidth, this.screenHeight, this.screenWidth / 20, this.screenHeight / 2);
	this.paddle2 = new Paddle(this.screenWidth, this.screenHeight, this.screenWidth - this.screenWidth / 20, this.screenHeight / 2);

	this.score1 = 0;
	this.score2 = 0;

	//setting up key movement!
	this.Key = {
		_pressed = {},

		UP : 38,
		DOWN : 40,
		UP2 : 87,
		DOWN2 : 83,

		isDown: function(keyCode) {
			return this._pressed[keyCode]
		},

		onKeyDown: function(event) {
			this._pressed[event.keyCode] = true;
		},

		onKeyup: function(event) {
			delete this._pressed[event.keyCode];
		}
	};

	document.addEventListener("keydown", move); //this means that function move will be called
	document.addEventListener("keyup", stopMove)
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
	game.ball.Move();
	game.ball.Collisions(game.paddle1, game.paddle2);
	game.CalculateScore();
	game.clearScreen();
	game.draw();

	window.requestAnimationFrame(game.gameLoop);
}

Game.prototype.update = function() 
{
	//to get input from the player
}

//takes e as the event
function move(e) 
{
	/*outputting the key pressed
	console.log(e.keyCode);
	*/
	game.paddle1.Move(e, 2);
	game.paddle2.Move(e, 1);

}

function stopMove()
{
	game.paddle1.StopMove();
	game.paddle2.StopMove();
}

Game.prototype.clearScreen = function() {
    this.ctx.clearRect(0, 0, this.screenWidth, this.screenHeight);
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
/*
	var r = Math.random() * 255;
	var g = Math.random() * 255;
	var b = Math.random() * 255;
*/
	var r = 255;
	var g = 255;
	var b = 255;
	this.ctx.fillStyle = rgb(r,g,b);
}

Game.prototype.draw = function()
{
	game.ball.Draw(this.ctx);
	game.paddle1.Draw(this.ctx);
	game.paddle2.Draw(this.ctx);

	//drawing the score on the screen
	this.ctx.font = "60px Verdana";
	this.ctx.fillText(this.score1, this.screenWidth / 3, this.screenHeight / 15);
	this.ctx.fillText("-", this.screenWidth / 2, this.screenHeight / 15 )
	this.ctx.fillText(this.score2, (this.screenWidth / 3) * 2, this.screenHeight / 15);
}

Game.prototype.CalculateScore = function() 
{
	if (game.ball.x + game.ball.size > this.screenWidth)
	{
		this.score1 += 1;
		game.ball.Reset();
	}
	if (game.ball.x < 0)
	{
		this.score2 += 1;
		game.ball.Reset();
	}
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