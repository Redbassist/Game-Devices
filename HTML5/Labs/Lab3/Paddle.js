function Paddle(x, y, posX, posY)
{
	this.x = posX;
	this.y = posY;
	this.xSize = 20;
	this.ySize = 110;
	this.screenWidth = x;
	this.screenHeight = y;
	this.moveUp = false;
	this.moveDown = false;
}

Paddle.prototype.Draw = function(ctx) 
{
	ctx.fillRect(this.x, this.y, this.xSize, this.ySize);
}

Paddle.prototype.Move = function(e, paddleNumber)
{
	if (paddleNumber == 1)
	{
		if (e.keyCode == 38)
		{
			this.moveUp = true;
		}
		else if (e.keyCode == 40)
		{
			this.moveDown = true;
		}
	}
	else
	{
		if (e.keyCode == 87)
		{
			this.moveUp = true;
		}
		else if (e.keyCode == 83)
		{
			this.moveDown = true;
		}
	}

	if (this.moveUp == true)
		this.y -= 8;
	else if (this.moveDown == true)
		this.y += 8;

	if (this.moveUp == false)
		console.log("false");
}

Paddle.prototype.StopMove = function() 
{
	this.moveUp = false;
	this.moveDown = false;
}