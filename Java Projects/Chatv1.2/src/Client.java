//input stream reader
import java.io.*;
//socket lies here
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

//NOTE : Encrypt Password!!!here Sent in String form!!! :(\
//how to close a frame when no longer needed..!! /???


public class Client extends JFrame{
	
	JFrame frame;
	JTextArea chat_field;
	JTextField send_area;
	JButton send;
	JButton close;
	JButton connect;
	JScrollPane scroller;
	Socket s;
	PrintWriter writer;
	BufferedReader reader;
	String ID ="Me";
	
	public void make_gui()
	{
		frame = new JFrame("CHAT CLIENT");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		chat_field = new JTextArea(20,25);
	    chat_field.setEditable(false);
	    chat_field.setLineWrap(false);
	    
		scroller = new JScrollPane(chat_field);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
   
		connect = new JButton("Connect");
	    connect.addActionListener(new conn_listner());
	    
	    JPanel panel = new JPanel();
	    send = new JButton("Send !");
	    s_listner l = new s_listner();
	    send.addActionListener(l);
	    
	    c_listner c = new c_listner();
	    close = new JButton("End Chat !");
	    close.addActionListener(c);
	    
	    send_area= new JTextField(20);
	    send_area.addActionListener(l);
	    send_area.setEditable(false);
	    panel.add(scroller);
	    panel.add(send_area);
	    panel.add(send);
	    panel.add(connect);
	    panel.add(BorderLayout.SOUTH,close);
	    panel.setBackground(Color.lightGray);
	    
	    frame.add(panel);
	    frame.setSize(400,450);
	    //- |
	    //frame.pack();
	    frame.setVisible(true);
	    
		
	}
	
	public void refresh_chatfield(String msg)
	{
		chat_field.append(msg+"\n");
	}
	
	public class conn_listner implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			start_network();
		}
	}
	
	
public class c_listner implements ActionListener{
		
		public void actionPerformed(ActionEvent e)
		{
			try{
			s.close();
			reader.close();
			writer.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.out.println("IO Exception Caught during closing!");
				
			}
		}
}
	
	
	
	public class s_listner implements ActionListener{
		
		
		public void actionPerformed(ActionEvent e)
		{
			//try{
		    writer.write(send_area.getText()+"\n");
		    System.out.println("Writing Data !");
		    writer.flush();
			//}
			/*catch(IOException ex)
			{
				ex.printStackTrace();
				System.out.println("IO Exception Caught during writing!!!");
			}*/
			
			send_area.setText("");
		}
	}
	
	
	
	public class read implements Runnable{
		
	public void run(){
		refresh_chatfield("Authenticated..!!");
		try{
			while(true)
			{
		
		System.out.println("Reading InputStream..");
		String text=reader.readLine();
		if(text!=null)
		{
	    System.out.println("Got Something...!");
		refresh_chatfield(text);
//		//Also returning to the Server..!! 
//		writer.write(text+"\n");
//		writer.flush();
		System.out.println("Recieved: "+text );
		}}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.out.println("IO Exception Caught during Reading!!!");
		}
	}
	}
	
	
	
	public void start_network(){
		try{
			//Reader Part
			//Server->Socket->InputstreamReader->BufferedReader->client
			//Creating a Socket
			s=new Socket("127.0.0.1",4343);
			
			refresh_chatfield("Connected at PORT:"+s.getLocalPort()+"\n\n");
			//getinputstream gives the low level input
			//InputStreamReader is a bridge between the lowlevel byte stream and high level character stream
			InputStreamReader r = new InputStreamReader(s.getInputStream());
			
			
			writer = new PrintWriter(s.getOutputStream());
			
			
			//Stream having the high level characters is sent to the buffer where
			//it joins and is prepared to be sent all at once
			reader = new BufferedReader(r); 
			
			//Now asking foe authentication
			client_utility c= new client_utility();
			//while(true)
			{
			c.ask_state();
			}
			
			
//			String text=reader.readLine();
//			refresh_chatfield(text);
//			System.out.println("Recieved: "+text )
			
			
			
			//Now the stream has been used.. close it..
			
		}catch(Exception ex){
			JOptionPane.showMessageDialog(new JButton("OK"),"Error ! Cant Connect the Server");
			ex.printStackTrace();
			}
		
	}
	
	public void Start_Thread()
	{
		Thread t = new Thread(new read());
		t.start();
		
	}
	
	public class client_utility{
		String usrname=null;
		String passwrd=null;
		JTextField username;
		JTextField password;

		JPanel j_main;
		CardLayout c;
		JFrame j;
		
	public void ask_client_authentication()
	{
		writer.write(usrname+"~"+passwrd+"\n");
		writer.flush();
		System.out.println("Credentials Sent");
	}
	public int validating()
	{
		try{
		if(reader.readLine().equals("#*#*#*#"))
		{
			send_area.setEditable(true);
			return 1;
		}
		else{
		send_area.setEditable(false);
		send_area.setText("Enter valid username and password");
		return 0;
		}
		}
		catch(IOException ex)
		{
			System.out.println("Error while authenticating IN Client");
		}
		return 0;
	}
	public int verify(){
	//while(true)
	{
	ask_client_authentication();
	if(validating()==1)
		{return 1;}
	else
	{
       JOptionPane.showConfirmDialog(new JButton("OK"),"Bad Username OR Password.! Try Again..");
        return 0;
	}}
	}
	
	public void ask_state()
	{
		j= new JFrame("Select Option");
		j.setLayout(new FlowLayout());
		JPanel panel = new JPanel();
		panel.add(new JLabel("Are you a new User ?"),BorderLayout.NORTH);
		JButton oldie = new JButton("Yes,I Have an Account !");
		JButton newie = new JButton("No,I am a new user !");
		
		        
				
				j_main= new JPanel();
				c = new CardLayout();
		        j_main.setLayout(c);
		        
		        JPanel j0 = new JPanel();
		        j_main.add(j0);
		        
		        JPanel j1 = new JPanel();
		        j1.setLayout(new BoxLayout(j1, BoxLayout.PAGE_AXIS));
		        username = new JTextField(20);
		        username.setText("Enter Username");
		        username.addActionListener(new ActionListener(){
		        	public void actionPerformed(ActionEvent ev)
		        	{
		                 username.setText("");
		        	}
		        }
		        );
		        
		        password = new JTextField(20);
		        password.setText("Enter Password");
		        password.addActionListener(new ActionListener(){
		        	public void actionPerformed(ActionEvent ev)
		        	{
		        		password.setText("");
		        	}
		        }
		        );
		        
		        JButton auth = new JButton("Authenticate !");
		        auth.addActionListener(new ActionListener(){
		        	public void actionPerformed(ActionEvent ev)
		        	{
		        		
		        		passwrd=password.getText();
		        		usrname=username.getText();
		        		c.next(j_main);
		        		password.setText("");
		        		username.setText("");
		        		if(verify()==1)
		        		{
		        		    JOptionPane.showMessageDialog(new JButton("OK"),"Authenticated!!");
		        		    Start_Thread();
		        		    j.dispose();
		        		}
		        		else
		        		{JOptionPane.showMessageDialog(new JButton("OK"),"Authentication Failed... Try Again");
		        		 c.next(j_main);
		        		}
		        			
		        	}
		        }
		        );
		        j1.add(username);
		        j1.add(password);
		        j1.add(auth);
		        
		        j_main.add(j1);
		        
		        JPanel j2 = new JPanel();
		        //j2.setLayout(new BoxLayout(j2,BoxLayout.PAGE_AXIS));
		        Icon icon = new ImageIcon(getClass().getResource("loading51.gif"));
		        JLabel Loading = new JLabel("Loading...");
		        j2.add(Loading);
		        JButton ld = new JButton("",icon);
		        ld.setSize(50,50);
		        j2.add(ld);
		        
		        j_main.add(j2);
		   
		        oldie.addActionListener(
						new ActionListener(){
							public void actionPerformed(ActionEvent ev)
							{
								c.next(j_main);
							}
						});
				newie.addActionListener(
						new ActionListener()
						{
							public void actionPerformed(ActionEvent ev)
							{
								//
							}
						}
						);
		        
		        panel.add(oldie);
		        panel.add(newie);
		        
		      //This will center the JFrame in the middle of the screen
		        j.setLocationRelativeTo(null);
		        j.add(panel);
		        j.add(j_main);
				j.setSize(550,300);
				j.setVisible(true);
		        
	}
	}
	
	public static void main(String args[])
	{	
		Client cl = new Client();
		 cl.make_gui();
		cl.start_network(); 
	   
	    
	}

}
