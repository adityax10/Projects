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
	static int slowRate;
	//booleans
	public boolean isDead;
	
	
	//CONSTRUTOR
	public Enemy(int big_x,int big_y,int rank)
	{
		//System.out.println("Initialiosing Enemy");
		
		switch(rank)
		{
		case 1 :
			//top side enemies
			if(big_x == -1 && big_y==-1)
			{
			x= (int) (Math.random() * GamePanel.WIDTH);
			y=0;
			}
			else
			{
				x=big_x;
				y=big_y;
			}
			
			this.rank=rank;
			speed=6;
			this.rank=rank;
			health=1;
			r=15;
			break;
		case 2:
			//left and right side enemies
			if(big_x == -1 && big_y==-1){
			int p= (int) (Math.random() * 10);
			if(p%2==0)
			    x=0;
			else
				x=GamePanel.WIDTH;
			}
			else
			{
				x=big_x;
				y=big_y;
			}
			
			y=(int) (Math.random() * GamePanel.HEIGHT);
			this.rank=rank;
			speed=9;
	        this.rank=rank;
			health=2;
			r=13;
			break;
		case 3:
			//More Health and lazy enemies
			x= (int) (Math.random() * GamePanel.WIDTH);
			y=0;
			this.rank=rank;
			health=10;
			speed=4;
			r=20;
			break;
		case 4:
			//Big Boss Enemy
			x= (int) (Math.random() * GamePanel.WIDTH);
			y= (int) (Math.random() * GamePanel.WIDTH);
			this.rank=rank;
			health=30;
			speed=5;
			r=30;
			break;
		}
		
		int angle = (int)(Math.random() *135);
		double rad = Math.toRadians(angle);
		
		dx=Math.cos(rad)*speed;
		dy=Math.sin(rad)*speed;
		slowRate=0;
		isDead=false;
		
	}
	
	//FUNCTIONS
	
	int getX(){return x;};
	int getY(){return y;};
	int getHealth(){return health;};
	
	public void Update()
	{
		
		x+= dx-slowRate;
		y+= dy-slowRate;
		
		if(x<0 || x>GamePanel.WIDTH-2*r) {dx=-dx;};
		if(y<0 || y>GamePanel.HEIGHT-2*r){dy=-dy;};
		
		if(x<0)
			x=0;
		if(y<0)
			y=0;
		if(x>GamePanel.WIDTH-2*r)
			x=GamePanel.WIDTH-2*r;
		if(y>GamePanel.HEIGHT-2*r)
			y=GamePanel.HEIGHT-2*r;  
		//System.out.println("Updating x:"+x+" y : "+y);
	}
	
	public static void enemySlowDown()
	{
		slowRate = 4;
	}
	
	public static void resumeOrignalSpeed()
	{
		slowRate = 0;
	}
	
	public void hit(){
		health--;
		if(health==0)
		kill();	
	}
	
	public void kill()
	{
		isDead=true;
		if(rank==4)
		{
			for(int i=0;i<20;i++)
			GamePanel.enemies.add(new Enemy(x,y,2));
		}
		else if(rank==3)
		{
			for(int i=0;i<15;i++)
				GamePanel.enemies.add(new Enemy(x,y,1));
		}
	}
	
	public void draw(Graphics2D g)
	{
		if(!isDead)
		{
			//drawing According to the types
			switch(rank)
			{
			case 1:
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.GREEN.darker());
					g.drawOval(x-3, y-3, r*2+3, r*2+3);
					g.setColor(Color.GREEN);
					g.fillOval(x, y, 2*r, 2*r);
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.YELLOW);
					g.fillOval(x+r/2,y+r/2,r,r);
					break;
			case 2:
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.BLACK);
					g.drawOval(x, y, r * 2, r * 2);
					g.setColor(Color.GRAY);
					g.fillOval(x, y, 2 * r, 2 * r);
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.BLUE);
					g.fillOval(x+r/2,y+r/2,r,r);
					break;
			case 3:
					g.setStroke(new BasicStroke(5));
					g.setColor(Color.RED);
					g.drawOval(x, y, 2*r, 2*r);
					g.setColor(Color.yellow);
					g.fillOval(x, y,2*r,2*r);
					break;
			case 4:
					g.setStroke(new BasicStroke(5));
					g.setColor(Color.WHITE);
					g.drawOval(x, y, 2*r, 2*r);
					g.setColor(Color.BLACK);
					g.fillOval(x, y,2*r,2*r);
					break;
			}
		}
	}
	
}
