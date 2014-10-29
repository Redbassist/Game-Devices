function Ball(x,y) 
{
	this.x = 0;
	this.y = 0;
	this.size = 50;
}

Ball.prototype.Move = function(e)
{
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
	}
}

Ball.prototype.Draw = function(ctx)
{
	ctx.fillRect(this.x, this.y, this.size, this.size);
}