import javax.sound.midi.*;


public class practice {
	
	 public void play()
	 {
		 try
		 {
	     // creating the sequencer.>!		 
	    Sequencer player = MidiSystem.getSequencer();
		 //opening it to add a track
		 player.open();
		 // creating  a sequence..
		 Sequence seq = new Sequence(Sequence.PPQ,5);
		 //creating a track in the sequence
		 Track t =seq.createTrack();
		 
		 ShortMessage msg= new ShortMessage();
		 
		for(int j=1;j<10;j++)
		{
		for(int i=1;i<10;i++)
		{
		    //System.out.println("i = "+i+"j = "+j);
			msg.setMessage(144,1,(11*i),110);
			MidiEvent event =  new MidiEvent(msg,4*j*i);
			t.add(event);
		}
		}
	     
		
		ShortMessage msg2 = new ShortMessage();
		 msg2.setMessage(128,1,55,100);
		 MidiEvent noteOFF = new MidiEvent(msg2,400);
		 t.add(noteOFF);
		 //giving sequence to the sequencer to  play
		 player.setSequence(seq);
		 
		 //starting the sequencer now... !
		 player.start();
		 
		
		 
		 }
		 catch(Exception e)
		 {
			 System.out.println("MidiUnavailableException Caught");
		 }
		 
		 System.out.println("Sequencer Made..!!");
		 
	 }
	 
	 public static void main(String args[])
	 {
		 practice m = new practice();//object of class
		 m.play();
		 
	 }


}
