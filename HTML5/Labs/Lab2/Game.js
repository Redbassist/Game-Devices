function Game() 
{
	this.ctx;
	this.screenWidth = window.innerWidth;
	this.screenHeight = window.innerHeight;
}

function main()
{
	var game = new Game();

	console.log("Hello World");

	//call initCanvas function here
	game.initCanvas();
	for (var i = 0; i < 100; i++)
	{
		game.draw();
	}
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
}

Game.prototype.draw = function()
{
	//random values for the square color
	var r = Math.random() * 255;
	var g = Math.random() * 255;
	var b = Math.random() * 255;

	//random location for the square
	var x = Math.random() * window.innerWidth;
	var y = Math.random() * window.innerHeight;

	//size random
	var size = Math.random() * 200;

	//parameters for rect are x, y, width, height
	//creating 100 squares
	this.ctx.fillStyle = rgb(r,g,b)
	this.ctx.fillRect(x,y,size,size);
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