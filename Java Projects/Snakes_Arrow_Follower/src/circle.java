/////////////////////////////////////////////////////////////////////////////////
//////////////////////////// SNAKES//////////////////////////////////////////////
/////////////////////////AUTHOR : Aditya ////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////



import java.awt.*;
import java.util.Random;

import javax.swing.ImageIcon;

public class circle extends Panel implements Runnable{
	
	int x,y,X,Y,x_food,y_food,len=1,start=1,xbs_count=-1,ybs_count=-1,x_prev[],y_prev[];
	int x_movesave[];
	int y_movesave[];
	int dia=40,k=0;
	int score=0;
	Image head,body,food,tail,bg,over,border;
	
	boolean execution_status_flag=true;
	circle()
	{
		x=300;
		y=300;
		x_movesave=new int[150];
		y_movesave=new int[150];
		x_prev=new int[100];
		y_prev=new int [100];
		bg = new ImageIcon(".\\src\\bg.png").getImage();
		head = new ImageIcon(".\\src\\head.png").getImage();
		body=  new ImageIcon(".\\src\\bodyPart.png").getImage();
		food= new ImageIcon(".\\src\\apple.png").getImage();
		tail = new ImageIcon(".\\src\\tail.png").getImage();
		over = new ImageIcon(".\\src\\Default.png").getImage();
		border = new ImageIcon(".\\src\\border.png").getImage();
	}
	
	
	public void paint(Graphics g)
	{
		//super.paintComponents(g);
		System.out.println("Painting "+x+" "+y);
		 int prev_len=len;
		//OUT OF BONDS CHECK
		// g.drawImage(border,0,0,600,600,this);
		  if(checkxy())
		  {
			 // g.setColor(Color.red);
		     // g.fillRect(0,0,getWidth(),getHeight());
		     // g.setColor(Color.black);
			  g.drawImage(over,0,0,600,600,this);
			  //g.drawImage(border,0,0,600,600,this);
			  g.drawString("! GAME OVER !",250,150);
			  g.drawString("  SCORE : "+score,250,170);
			  game_terminate();
		  }
		  //Drawing body n food
		 else{
			 //Black Frame
			//g.setColor(Color.black);
			//g.fillRect(0,0,getWidth(),getHeight());
			 g.drawImage(bg, 0, 0, 600, 600, this);
			  //add food initially
			 if(start==1)
			 {
				 add_food(g);
			 }
			 //otherwise show the food with constant coordinates
			 else
			 {
				 //g.setColor(Color.WHITE);
				 //g.fillOval(x_food,y_food,dia-5,dia-5);
				 g.drawImage(food,x_food,y_food,this);
			 }
			 
		      g.setColor(Color.green);
		      //g.fillOval(x,y,dia,dia);
			 g.drawImage(head,x,y,this);
		     g.setColor(Color.red);
		     
		      if(get_food())
		      {
		    	 len++;
		    	 score=score+10;
		    	 add_food(g);
		      }  
		      
		      for(int i=0;i<k;i++)
			  {
			       x_prev[i]=x_prev[i+1];
			       y_prev[i]=y_prev[i+1];
			  }
		      
		      if(prev_len!=len)
				k++;
		      
				  x_prev[k]=x;
				  y_prev[k]=y;
				  
				  for(int i=0;i<k;i++)
				  move_saver(x_prev[i],y_prev[i]);
		      
		      for(int i=0;i<k;i++)
		      {
		    	  int x_temp=get_saved_x(),y_temp=get_saved_y();
		    	  System.out.println("Attempting to print body x="+x_temp+" and y "+y_temp);
		    	  //g.fillOval(x_temp ,y_temp, dia,dia);
		    	  if(i==k-1)
		    	  {
		    		  g.drawImage(tail,x_temp,y_temp,this);
		    		  break;
		    	  }
		    	  g.drawImage(body,x_temp,y_temp,this);
		      }
		     
		  }
		  
		  
		  
		  System.out.println("xbs n ybs"+xbs_count+" "+ybs_count);
		  
	      while((xbs_count!=-1)&&(ybs_count!=-1))
	      {
	        xbs_count--;
	  		ybs_count--;
	      }
		  
		  
	}
	
	public void move_saver(int xtemp,int ytemp)
	{
		
		if((xbs_count<len)&&(ybs_count<len))
		{
		xbs_count++;
		ybs_count++;
		System.out.println("Saving Move! x="+xtemp+" n y="+ytemp+"xbs n ybs "+xbs_count+" "+ybs_count);
		x_movesave[xbs_count]=xtemp;
		y_movesave[ybs_count]=ytemp;
		}
	}
	
	public int get_saved_x()
	{
		if(xbs_count>=0)
		{
			System.out.println("Removing x="+x_movesave[xbs_count]);
			return x_movesave[xbs_count--];
		}
		else
		    return 0;
	}
	
	public int get_saved_y()
	{
		if(ybs_count>=0)
		{
			System.out.println("Removing y="+y_movesave[ybs_count]);
			return y_movesave[ybs_count--];
		}
		else
		    return 0;
	}
	
	public void add_food(Graphics g)
	{
		Random r = new Random();
		g.setColor(Color.WHITE);
		x_food = r.nextInt(520);
		y_food = r.nextInt(520);
		//g.fillOval(x_food,y_food,dia-5,dia-5);
		g.drawImage(food,x_food,y_food,this);
		System.out.println("!!!!!!!Adding Food!!!!!!!");
	}
	//checking for the snake to get food
	public boolean get_food()
	{
		if(((x+7>=x_food)&&(x+7<=x_food+18))&&((y+7>=y_food)&&(y+7<=y_food+18)))
		{
			System.out.println("!!!!!!!!!!!EATING Food!!!!!!!!!!!!!!!");
			return true;
		}
		else
			return false;
	}
	//setting shifters
	public void set_shifters(int i,int j,int code)
	{
		X=i;
		Y=j;
	}
	// repositiong x and y
	public void reposition(int i,int j)
	{
		x=x+i;
		y=y+j;
	}
	
	public void game_terminate()
	{
		// while(true)
		      {
		    	  try{
		    		  // another way of stopping a thread...
		    		  Thread.currentThread().suspend();
		    		  //Thread.currentThread().stop();
		    	     }
		    	  catch(Exception ex)
		    	  {
		    		  ex.printStackTrace();
		    	  }
		      }	
	}
	
 public	void status_switcher()
	{
		if(execution_status_flag==false)
		{
			execution_status_flag=true;
			Thread.currentThread().resume();
		}
		else
			execution_status_flag=false;
		    //Thread.currentThread().interrupt();
		System.out.println("Status bit changed");
	}
	
	public void run()
	{
	// improve here....
    while(true)
	{
      while(execution_status_flag)
      {
      try{
    		  Thread.currentThread().sleep(50);
    		  reposition(X,Y);
    		  repaint();
    	  }
    	  catch(Exception ex)
    	  {
    		  ex.printStackTrace();
    	  }
      }}
	}
	
	public boolean coordscheck()
	{
		for(int i=0;i<=k;i++)
		{
			if(x==x_prev[i]&&(y==y_prev[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	//Checking for OUt of bonds
	public boolean checkxy()
	{
		if(((x+15/2<=0)||(x+15/2>=600))||((y+15/2<=0)||(y+15/2>=600)))
		{
			System.out.println("Out OF BONDS");
			return true;
		}
		return false;
	}
}

