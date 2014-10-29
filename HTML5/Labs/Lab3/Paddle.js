function Paddle(x, y, posX, posY)
{
	this.x = posX;
	this.y = posY;
	this.xSize = 20;
	this.ySize = 110;
	this.screenWidth = x;
	this.screenHeight = y;
	this.moveUp = 0;
	this.moveDown = 0;
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
			this.moveUp = 1;

		else if (e.keyCode == 40)
			this.moveDown = 1;
	}
	else
	{
		if (e.keyCode == 87)
			this.moveUp = 1;

		else if (e.keyCode == 83)
			this.moveDown = 1;
	}
}

Paddle.prototype.MovePaddle = function()
{	

	if (this.moveUp == 1 && this.y > 0)
		this.y -= 8;
	else if (this.moveDown == 1 && this.y < this.screenHeight - this.ySize)
		this.y += 8;
}

Paddle.prototype.StopMove = function(e, paddleNumber) 
{
	if (paddleNumber == 1)
	{
		if (e.keyCode == 38)
			this.moveUp = 0;

		else if (e.keyCode == 40)
			this.moveDown = 0;
	}
	else
	{
		if (e.keyCode == 87)
			this.moveUp = 0;

		else if (e.keyCode == 83)
			this.moveDown = 0;
	}
}