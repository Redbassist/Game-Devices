//DONT USE GLOBALS AGAIN!!!
var ctx;
var canvas;
var screenWidth = window.innerWidth;
var screenHeight = window.innerHeight;

function main()
{
	console.log("Hello World");

	//call initCanvas function here
	initCanvas();
	for (var i = 0; i < 100; i++)
	{
		draw();
	}
}

function initCanvas()
{
	//create a canvas element
	canvas = document.createElement("canvas");
	//create a 2d content for drawing
	ctx = canvas.getContext('2d');

	//????
	document.body.appendChild(canvas);

	//setting the size of the canvas to the window size
	canvas.width = screenWidth;
	canvas.height = screenHeight;
}

function draw()
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
	ctx.fillStyle = rgb(r,g,b)
	ctx.fillRect(x,y,size,size);
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