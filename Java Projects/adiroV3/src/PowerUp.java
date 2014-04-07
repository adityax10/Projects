import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class PowerUp {
	
	//varibles
	int x;
	int y;
	int w;
	int dy;
	int type;
	boolean isUsed;
	
	//CONSTRUCTOR
	public PowerUp(int x,int y,int type)
	{
		
		this.x=x;
		this.y=y;
		this.type=type;
		isUsed=false;
		
		//SLOW DOWN
		if(type==0)
		{
			dy=5;
		}
		//BULLET UP
		else if(type ==1)
		{
			dy=5;
		}
		else if(type ==2)
			dy=5;
		w=3;
	}
	
	public void setPowerAction()
	{
		if(type==0)
		{
			GamePanel.setSlowDownMode();
		}
		else if(type ==1)
		{
			//playerSpeed up
			GamePanel.playerSpeedUp();
		}
		else if(type == 2)
		{
			//increase health
			GamePanel.p.lives++;
			GamePanel.setPrintString("1  U P !", x, y);
		}
		isUsed=true;
	}
	
	public boolean Update()
	{
		y+=dy;
		if(y > GamePanel.WIDTH+w || isUsed)
		{
			return true;
		}
		return false;
	}
	
	public void draw(Graphics2D g)
	{
		switch(type)
		{
		case 0:
			//SLOW DOWN
			g.setColor(Color.RED);
			g.fillRect(x, y, 3*w,3*w);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE);
			g.drawRect(x, y, 3*w, 3*w);
			break;
		case 1:
			//PlayerSpeed Up
			g.setColor(Color.GREEN);
			g.fillRect(x, y, 3*w,3* w);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE);
			g.drawRect(x , y, 3*w,3*w);
			break;
		case 2:
			//Extra life
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.RED);
			g.fillOval(x, y, 3*w, 3*w);
			g.setStroke(new BasicStroke(4));
			g.setColor(Color.WHITE);
			g.fillOval(x+w/3, y+w/3, w, w);
		}
	}
	

}
