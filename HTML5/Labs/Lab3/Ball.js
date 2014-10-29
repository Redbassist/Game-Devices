function Ball(x,y) 
{
	this.x = 10;
	this.y = 10;
	this.size = 20;
	this.xSpeed = 4;
	this.ySpeed = 4;
	this.screenWidth = x;
	this.screenHeight = y;
}

Ball.prototype.Move = function()
{
	/*
	if (e.keyCode == 38)
	{
		this.y -= 2;
	}
	else if (e.keyCode == 40)
	{
		this.y += 2;
	}
	else if (e.keyCode == 37)
	{
		this.x -= 2;
	}
	else if (e.keyCode == 39)
	{
		this.x += 2;
	}*/

	this.x += this.xSpeed;
	this.y += this.ySpeed;
}

Ball.prototype.Collisions = function(paddle1, paddle2) 
{
	if (this.y + this.size > this.screenHeight || this.y < 0)
	{
		this.ySpeed *= -1;
		console.log("y col")
	}
	/*
	if (this.x + this.size > this.screenWidth || this.x < 0)
	{
		this.xSpeed *= -1;
		console.log("x col")
	}*/

	//colliding with the paddles, SPEEDS UP
	if (this.y + this.size > paddle1.y && this.y < paddle1.y + paddle1.ySize
		&& this.x + this.size > paddle1.x && this.x < paddle1.x + paddle1.xSize) 
	{
		this.xSpeed *= -1.2;
		this.ySpeed *= 1.2;
	}
	else if (this.y + this.size > paddle2.y && this.y < paddle2.y + paddle2.ySize
		&& this.x + this.size > paddle2.x && this.x < paddle2.x + paddle2.xSize) 
	{
		this.xSpeed *= -1.2;
		this.ySpeed *= 1.2;
	}
}

Ball.prototype.Reset = function() 
{
	this.x = this.screenWidth / 2;
	this.y = this.screenHeight / 2;

	if (Math.random() > 0.5) 
	{
		this.xSpeed = Math.random() + 3;
		this.ySpeed = Math.random() + 3;
	}
	else 
	{
		this.xSpeed = Math.random() - 4;
		this.ySpeed = Math.random() - 4;
	}

}

Ball.prototype.Draw = function(ctx)
{
	ctx.fillRect(this.x, this.y, this.size, this.size);
}