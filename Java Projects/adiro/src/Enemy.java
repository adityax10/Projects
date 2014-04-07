import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class Enemy {
	
	//VARIABLES
	//Coordinates
	int x;
	int y;
	double dx;
	double dy;
    int r;
	//Parts
	int health;
	int speed;
	int rank;
	int type;
	
	//booleans
	public boolean isDead;
	
	
	//CONSTRUTOR
	public Enemy(int rank)
	{
		//System.out.println("Initialiosing Enemy");
		x= (int) (Math.random() * GamePanel.WIDTH);
		y=0;
		
		switch(rank)
		{
		case 1 :
			this.rank=rank;
			speed=6;
			type=1;
			health=5;
			r=40;
			break;
		}
		
		int angle = (int)(Math.random() *135);
		double rad = Math.toRadians(angle);
		
		dx=Math.cos(rad)*speed;
		dy=Math.sin(rad)*speed;
		
		isDead=false;
		
	}
	
	//FUNCTIONS
	
	int getX(){return x;};
	int getY(){return y;};
	int getHealth(){return health;};
	
	public void Update()
	{
		x+=dx;
		y+=dy;
		
		if(x<0 || x>GamePanel.WIDTH-r) {dx=-dx;};
		if(y<0 || y>GamePanel.HEIGHT-r){dy=-dy;};
		
		if(x<0)
			x=0;
		if(y<0)
			y=0;
		if(x>GamePanel.WIDTH-r)
			x=GamePanel.WIDTH-r;
		if(y>GamePanel.HEIGHT-r)
			y=GamePanel.HEIGHT-r;  
		//System.out.println("Updating x:"+x+" y : "+y);
	}
	
	public void kill()
	{
		isDead=true;
	}
	
	public void draw(Graphics2D g)
	{
		if(!isDead)
		{
		g.setStroke(new BasicStroke(5));
		g.setColor(Color.ORANGE);
		g.drawOval(x, y, r, r);
		}
	}
	
}
