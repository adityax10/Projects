import javax.sound.midi.*;
import javax.swing.*;
//import javax.swing.event.*;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

////NOTE : I have not been able to add graphics animation on each column


public class Final_Beat_boX extends JFrame implements ActionListener,Serializable{
	
	
	//for GUI Stuff
	JFrame frame;
	ArrayList<JCheckBox> boxes;
	JButton start,stop,temp_up,temp_down;
	JPanel left_panel,right_panel;
	JPanel cbox_panel;
	JButton clr;
	JButton save;
	JButton load;
	JTextField bpm;
	//For tone codes and their corresponding meanings..
	String instruments[]={"Bass Drum","Closed Hi-Hat","Open Hi-Hat","Acoustic Snare","Crash Cymbal","Hand Clap","High Clap","Hight Tom","Hi Bongo","Marcas","Whistle","Low Congo","Cowbell","Vibraslap","Low-mid Tom","High Agogo","Open High Congo"};
	//pitch code for instruments..
	int inst_pitch[]={35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	
	//for Tones stuff
	MidiEvent e=null;
	Sequencer Seq;
	Sequence s ;
	Track t;
	float tempo=120;
	
	//for saving stuff
	Seq_save state_saver=new Seq_save();
	
	public void makeGUI()
	{
		
		// INITIATNIG FRAME!!!!
		frame = new JFrame(" * * Beat BoX * * ");
		//frame.set
		frame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		//JPanel main = new JPanel();
		//main.setBorder(BorderFactory.createEmptyBorder(10,10,10.10));
		
		
		
		//adding the left
		left_panel = new JPanel();
		left_panel.setLayout(new BoxLayout(left_panel,BoxLayout.Y_AXIS));//NOTE.>!!
		left_panel.setBorder(BorderFactory.createTitledBorder("Beats"));
		for(int i=0;i<16;i++)
		{
			left_panel.add(new JLabel(instruments[i]));
		}
		
	
		// Adding check boxes
		GridLayout grid = new GridLayout(16,16);
		grid.setHgap(2);
		grid.setVgap(2);
	    cbox_panel = new JPanel(grid);
	    cbox_panel.setBorder(BorderFactory.createTitledBorder("Selections"));
	    //cbox_panel.setBackground(Color.DARK_GRAY);
	    boxes = new ArrayList<JCheckBox>();
	    
	for(int i=0;i<256;i++)
	{
		JCheckBox check_box = new JCheckBox();
		check_box.setSelected(false);
		boxes.add(check_box);
	    cbox_panel.add(check_box);
	}
		
		
		//Initiating buttons
		start= new JButton(" Start ");
		stop = new JButton(" Stop  ");
		temp_up = new JButton("Tempo Up");
		temp_down = new JButton("Tempo Down");
		clr = new JButton("Clear All");
		save=new JButton("Save");
		load=new JButton("Load");
		
		//Creating filed for bpm
		bpm = new JTextField();
		bpm.setText(" BPM : "+tempo);
		bpm.setEditable(false);

		
		//adding buttons
		start.addActionListener(this);
		stop.addActionListener(this);
		temp_down.addActionListener(this);
		temp_up.addActionListener(this);
		clr.addActionListener(this);
		save.addActionListener(this);
		load.addActionListener(this);
		
		
		 //Making right_panel..!!!
		 right_panel = new JPanel();
		 right_panel.setLayout(new BoxLayout(right_panel,BoxLayout.Y_AXIS));
		 right_panel.setBorder(BorderFactory.createTitledBorder("Options"));
		 right_panel.add(start);
		 right_panel.add(stop);
		 right_panel.add(temp_up);
		 right_panel.add(bpm);
		 right_panel.add(temp_down);
		 right_panel.add(clr);
		// right_panel.setBackground(Color.DARK_GRAY);
		 right_panel.add(save);
		 right_panel.add(load);
		 
		 //Making the frame
		 frame.add(BorderLayout.WEST,left_panel);
		 //
		 
		 frame.add(BorderLayout.CENTER,cbox_panel);
		 
		 frame.getContentPane().add(BorderLayout.EAST,right_panel);
		 
//        Adding animating circle
//			circles c = new circles();
//			frame.add(c);
		 
		 //frame.pack();
		 frame.setSize(600,290);
		 frame.setVisible(true);
		 
		 this.initialise_player();
	}
	
	
	
	public void actionPerformed(ActionEvent e)
	{
	    if(e.getSource()==start)
	    {
	    	this.player_setTrack();
	    }
	    else if(e.getSource()==stop)
	    {
	    	Seq.stop();
	    }
	    else if(e.getSource()==temp_up)
	    {
	    	tempo = (float) (tempo*1.1);
	        Seq.setTempoInBPM(tempo);
	        refresh_tempo();
	    }
	    else if(e.getSource()==temp_down)
	    { 
	    	tempo = (float) (tempo*0.1);
	        Seq.setTempoInBPM(tempo);
	        refresh_tempo();
	    }
	    else if(e.getSource()==clr)
	    {
	    	for(int i=0;i<256;i++)
	    	{
	    	JCheckBox jc = (JCheckBox) boxes.get(i);
	    	jc.setSelected(false);
	    	}
	    }
	    else if(e.getSource()==save)
	    {
	       state_saver.serialise();
	    }
	    else
	    {
	    	state_saver.deserialise();
	    }
	}
	
	    public void  refresh_tempo()
	    {
	    	bpm.setText("BPM : "+String.valueOf(tempo));
	    }
	
		
		public void initialise_player()
		{
		try{
			Seq = MidiSystem.getSequencer();
			s = new Sequence(Sequence.PPQ,4);
			Seq.open();
			Track t = s.createTrack();
			}
		catch(Exception e)
		{
			System.out.println("Exception Thrown..!!");
		}}
		
		public void player_setTrack()
		{
			//for a single instrument;
			int inst_key_list[];
			
			s.deleteTrack(t);//delete previous
			t = s.createTrack();//add new
			tempo=120;
			for(int j=0;j<16;j++)
			{
				inst_key_list = new int[16];

				int key = inst_pitch[j];
				for(int i=0;i<16;i++)
				{
				JCheckBox j_temp = (JCheckBox) boxes.get(16*j+i);
				if(j_temp.isSelected()==true)
				{
					inst_key_list[i]=key;
				}
				else
					inst_key_list[i]=0;
				}
				player_addTrack(inst_key_list);
			}
			
			try{
				Seq.setSequence(s);
				Seq.start();
				Seq.setTempoInBPM(tempo);
				refresh_tempo();
				//Continously play the given track in a loop
				Seq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			}
			catch(Exception e){
				
			}
		}
		
		public void player_addTrack(int a[])
		{
			for(int i=0;i<16;i++)
			{
				//tone on
				t.add(addMidievent(144, 9,a[i], 100, i));
				//tone off
				t.add(addMidievent(128, 9, a[i], 100,i+1));
			}
		}
		
		public MidiEvent addMidievent(int msg_mode,int channel,int pitch,int velocity,int t)
		{
			try{
			ShortMessage msg = new ShortMessage();
			msg.setMessage(msg_mode,channel,pitch,velocity);
			e = new MidiEvent(msg,t);
			}
			catch(Exception e)
			{
				System.out.println("Error.>!!");
				JOptionPane j = new JOptionPane();
				//j.showMessageDialog("Error");
			}
			return e;
			
		}
		
		public static void main(String args[])
		{
		    Final_Beat_boX player = new Final_Beat_boX();
			player.makeGUI();
		}
	    
		public class Seq_save extends JOptionPane{//Saving
			int[] tones_temp;
			public void serialise()
			{
				try{
					String filename=JOptionPane.showInputDialog("Save File As ..");
					filename.concat(".adi");
					FileOutputStream fo = new FileOutputStream(filename);
					ObjectOutputStream ob =new ObjectOutputStream(fo);
					tones_temp=new int[256];
					for(int i=0;i<256;i++)
					{
						if(boxes.get(i).isSelected()==true)
						{
							tones_temp[i]=1;
						}
						else
						{
							tones_temp[i]=0;
						}
					}
					
					ob.writeObject(tones_temp);
					
					ob.close();
					System.out.println("Sequence Saved!!");
				}
				catch(FileNotFoundException e)
				{
					System.out.println("fILE NOT FOUND FOR SAVING ");
				}
				catch(IOException e)
				{
					System.out.println("I/O exception while saving..");
				}
				catch(Exception e)
				{
					System.out.println("Error while Saving..! ");
				}
			}
			
			
			public void deserialise()
			{
				try{
					String filename=JOptionPane.showInputDialog("Enter the file name to Open..");
					filename.concat(".adi");
					FileInputStream fo = new FileInputStream(filename);
					ObjectInputStream ob = new ObjectInputStream(fo);
					tones_temp=(int[])ob.readObject();
					for(int i=0;i<256;i++)
					{
						if(tones_temp[i]==1)
						{
							boxes.get(i).setSelected(true);
						}
						else
						{
							boxes.get(i).setSelected(false);
						}
					}
					System.out.println("Sequence Loaded.!!");
					
				}
				catch(Exception e){
					System.out.println("Error.! Sequence Wasn't Loaded.!!");
				}
			}
		}

}
