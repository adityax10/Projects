import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.w3c.dom.css.Counter;

public class Explosions {

	// Variables
	int x;
	int y;
	int r;
	int type;
	int hitsCounter;

	// CONSTRUCTOR
	public Explosions(int x, int y) {
		this.x = x;
		this.y = y;
		this.r = 0;
		type=1;
	}
	
	public Explosions(int x,int y,int r)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		type=2;
		hitsCounter=0;
	}

	// Functions
	public boolean Update() {
		if(type==1)
		{
		r += 5;
		if (r > 35)
			return true;
		return false;
		}
		else if(type ==2)
		{
			hitsCounter++;
			if(hitsCounter >1)
				return true;
			
		}
		return false;
	}

	public void draw(Graphics2D g) {
		if(type == 1)
		{
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.WHITE);
		g.drawOval(x, y, 2 * r, 2 * r);
		}
		else if(type == 2)
		{
			g.setColor(Color.WHITE);
			g.fillOval(x, y, 2*r, 2*r);
		}
	}

}
