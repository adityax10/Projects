/////////////////////////////////////////////////////////////////////////////////
//////////////////////////// SNAKES//////////////////////////////////////////////
/////////////////////////AUTHOR : Aditya ////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////



import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


public class gui extends JFrame{
	
	circle c;
	JTextField f;
	int SHIFT = 15;
	Thread thread;
	JButton button;
	JPanel j;
	
	public gui()
	{
		super("<< ~ SNAKE ~ >>");
	}

	public void start()
	{
		
		c=new circle();
	   setResizable(false);
	   //setUndecorated(true);
		f=new JTextField(20);
		thread=new Thread(c);
		f.addKeyListener(new mover());
		button=new JButton("Pause");
		button.addActionListener(new actionclass());
		add(BorderLayout.NORTH,button);
		add(c);
		add(BorderLayout.SOUTH,f);
		
//		addWindowListener(new wlclass());
		
		setVisible(true);
		setSize(600,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
//	
//	public void wlclass implements WindowListener
//	{
//		
//	}
	
	public class actionclass implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			c.status_switcher();
			if(button.getText()=="Pause")
			button.setText("Resume");
			else
			button.setText("Pause");
		}
	}
	
	public class mover implements KeyListener{
		
	    public void keyPressed(KeyEvent e)
	    {
	    	if(e.getKeyCode()==KeyEvent.VK_UP)
	    	    c.set_shifters(0,-SHIFT,1);
	    	else if(e.getKeyCode()==KeyEvent.VK_DOWN)
		    	c.set_shifters(0,SHIFT,2);
	    	else if(e.getKeyCode()==KeyEvent.VK_LEFT)
		    	c.set_shifters(-SHIFT,0,3);
	    	else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		    	c.set_shifters(SHIFT,0,4);
	    	else
	    		f.setText("Press only arrows..! Not ");
	    	if(c.start==1)
	    	{
	    	   thread.start();
	    	   c. start=0;
	    	}
	    }
	    
	    public void keyReleased(KeyEvent e)
	    {
	    	f.setText("");
	    }
	    
	    public void keyTyped(KeyEvent e)
	    {
	    	//
	    }
	}
}
