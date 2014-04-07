import java.io.*;

//socket lies here
import java.net.*;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Server extends JFrame {
 
	JFrame frame;
	JTextArea chat_field;
	JTextField send_area;
	JButton send;
	JButton close;
	JButton refresh;
	JButton disconnect;
	JScrollPane scroller;
	InputStreamReader ip_stream;
	PrintWriter w;
	Socket s;
	ServerSocket ss;
	BufferedReader reader;
	String ID ="Me";
	Thread t ;
	public void make_gui()
	{
		frame = new JFrame(" * *CHAT SERVER * * ");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		chat_field = new JTextArea(20,25);
	    chat_field.setEditable(false);
	    chat_field.setLineWrap(false);
	    
		scroller = new JScrollPane(chat_field);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setAutoscrolls(true);
		refresh = new JButton("Refresh");
	    refresh.addActionListener(new conn_listner());
	    
	    disconnect = new JButton("Disconnect");
	    disconnect.addActionListener(new d_listner());
	    
	    JPanel panel = new JPanel();
	    send = new JButton("Send !");
	    s_listner l = new s_listner();
	    send.addActionListener(l);
	    
//	    c_listner c = new c_listner();
//	    close = new JButton("End Chat !");
//	    close.addActionListener(c);
	    
	    send_area= new JTextField(20);
	    send_area.addActionListener(l);
	    panel.add(scroller);
	    panel.add(send_area);
	    panel.add(send);
	    panel.add(refresh);
	    panel.add(BorderLayout.SOUTH,disconnect);
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
			try{
			t.stop();//!!
			reader.close();
			w.close();
		    s.close();
		    ss.close();
			go();
			}
			catch(Exception exp)
			{
				System.out.println("Error during Refresh");
			}
			refresh_chatfield("Refreshed Connections...");
		}
	}
	
	
//public class c_listner implements ActionListener{
//		
//		public void actionPerformed(ActionEvent e)
//		{
//		
//		}
//}
	
	
	
	public class s_listner implements ActionListener{
		
		public void actionPerformed(ActionEvent e)
		{
			try{
		    w.write("Server : "+send_area.getText()+"\n");
		    refresh_chatfield(ID+" : "+send_area.getText());
		    System.out.println("Writing Data !");
		    w.flush();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				System.out.println("IO Exception Caught during writing!!!");
			}
			
			send_area.setText("");
		}
	}
	
	public class d_listner implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			try{
				Thread.interrupted();
				reader.close();
				w.close();
			    s.close();
			    ss.close();
			
			    refresh_chatfield("Disconnected.!!");
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.out.println("IO Exception Caught during closing!");
			}	
		}
	}

	public void go(){
		try{
		     refresh_chatfield("Making Server Socket....");
			//starts listening on a port you specify
			ss=new ServerSocket(4343,100);//100 people cant wait to connect to the server...
			refresh_chatfield("Server Socket Made...at PORT"+ss.getLocalPort());
			//Server goes into a continuous loop waiting for a client to connect
			
			//REMOVE THIS LATER!!!!!!!!
			while(true)
			{
				//ss was listening on a port 4242...  when a connection is made ss accepts it
				//accept method blocks until a request comes in and then method returns a Socket
				//to communicate with  the client
				refresh_chatfield("Waiting for Client......");
				Socket s= ss.accept();
				refresh_chatfield("Client Request Accepted..! PORT : "+s.getPort()+"\n\n");
				//thus s is the new socket created on the server for connecting to the client
				
				//now we use the socket s to to create a printwriter and send it to the client
				w=new PrintWriter(s.getOutputStream());
				ip_stream = new InputStreamReader(s.getInputStream());
				reader=new BufferedReader(ip_stream);
				
				//authenticating client now
				refresh_chatfield("Authenticating Client...");
				client_authenticate();
				
				//now reading
		    t = new Thread(new write());
		    t.start();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void client_authenticate()
	{
	try{
		//reading creds from file
	  File f = new File("password_list.txt");
	   FileReader fr = new FileReader(f);
	   
	   BufferedReader br = new BufferedReader(fr);
	   String Creds = br.readLine();
	   System.out.println("Creds read from file are : "+Creds);
	   //String password[] = usrname.split("~");
	   //reading creads from inputstream
	   String Creds_got = reader.readLine();
	   
	   while(!Creds.equals(Creds_got))
	   {
	   Creds_got = reader.readLine();
	   System.out.println("Read Credentials : "+Creds_got);
	   }
	   
	   if(Creds.equals(Creds_got)){
		   w.write("#*#*#*#\n");
	   }
	   else
	   {
		   w.write("~~~~~~~~\n");
	   }
	   w.flush();
	   
		}
		catch(FileNotFoundException fe)
		{
			System.out.println("Password File not Found ");
			fe.printStackTrace();
		}
		catch(IOException fe)
		{
			System.out.println("Can't read credentials from Stream");
		}
		
	   
	}
	

	public class write implements Runnable{
		
		public void run()
		{
			try{
				while(true)
				{
				   System.out.println("Attempting to read InputSteam");
			       String str = reader.readLine();
			       if(str!=null)
			       {
			    	  System.out.println("String READ : "+str);
				      this.broadcast(str);
			       }
			       Thread.sleep(1000);
			    }}
			catch(Exception ex)
			{
				ex.printStackTrace();
				System.out.println("Error While Reading");
			}
		}
		//Adding !!!!!Synchronized!!!! here..!!
		public  synchronized void broadcast(String msg)
		{
			refresh_chatfield("Server : "+msg);
			System.out.println("Broadcasting Message..");
			w.write("Me : "+msg+"\n");
			w.flush();
		}
	}
	
	public static void main(String args[])
	{
		Server s = new Server();
	    s.make_gui();
	    s.go(); 
	}

}
