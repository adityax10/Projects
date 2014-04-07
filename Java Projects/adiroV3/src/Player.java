import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;

public class Player {

	// Variables
	static int x;
	static int y;
	static int r;
	int dx;
	int dy;
	int lives;
	int speed;
	boolean setFiring;
	long firingDelay;
	long firingTimer;
	int health;
	
	boolean isRecovering;
	int recoveryDelay;
	long recoveryTimer;

	boolean left, right, top, bottom;

	// CONSTRUCTOR
	public Player() {
		
		x = GamePanel.WIDTH/2;
		y = GamePanel.HEIGHT/2;
		r=20;
		dx=dy=0;
		speed=8;
		lives=3;
		setFiring = false;
		firingDelay = 150;
		firingTimer = System.currentTimeMillis();
		isRecovering = false;
		recoveryDelay = 2000;
		recoveryTimer = 0;
	}
	
	//FUNCTIONS
	public void Update()
	{
		if(left)
			dx=-speed;
		if(right)
			dx=+speed;
		if(top)
			dy=+speed;
		if(bottom)
			dy=-speed;
		
		x+=dx;
		y+=dy;
		if(x<0)
		 x=0;
		if(x>GamePanel.WIDTH-2*r)
	     x=GamePanel.WIDTH-2*r;
		if(y<0)
			y=0;
		if(y>GamePanel.HEIGHT-2*r)
		 	y=GamePanel.HEIGHT-2*r;
		dx=dy=0;
		
		if(setFiring)
		{
			long elapsedTime = System.currentTimeMillis() - firingTimer;
			if(elapsedTime > firingDelay)
			{
				GamePanel.bullets.add(new Bullet(x+r, y, 270));
				firingTimer = System.currentTimeMillis();
			}
		}
		if(isRecovering)
		{
			long waitTime = System.currentTimeMillis() - recoveryTimer;
			if(waitTime > recoveryDelay)
			{
				isRecovering = false;
			}
		}
	}
	
   public void speedUp()
   {
	   speed += 5;
   }
   public void speedDown()
   {
	   speed -= 5;
   }
	
	public void recovery()
	{
		isRecovering = true;
		recoveryTimer = System.currentTimeMillis();
	}
	
	public void draw(Graphics2D g)
	{
		if(!isRecovering)
		{
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.GRAY.brighter());
			g.fillOval(x, y, 2*r, 2*r);
			g.setStroke(new BasicStroke(4));
			g.setColor(Color.black);
			g.drawOval(x, y, 2*r, 2*r);
			g.setStroke(new BasicStroke(4));
			g.setColor(Color.white);
			g.fillOval(x+r/2, y+r/2, r, r);
			if(setFiring)
			{
				g.setColor(Color.RED);
				g.fillOval(x+r-8/2, y-1,8,10);
			}
		}
		else
		{
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.RED);
			g.fillOval(x, y, 2*r, 2*r);
			g.setStroke(new BasicStroke(4));
			g.setColor(Color.WHITE);
			g.fillOval(x+r/2, y+r/2, r, r);
		}
	}

	public void setLeft(boolean b){left = b;};
	public void setRight(boolean b){right = b;};
	public void setTop(boolean b){top = b;};
	public void setBottom(boolean b){bottom = b;};
	
}
