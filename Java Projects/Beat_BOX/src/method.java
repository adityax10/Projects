//Creating a mini music player..! :P

import javax.sound.midi.*;


public class method {
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
		 
		 
		 
		 //creating a instruction to be added to the Midievent in form of shortmessage
		 ShortMessage msg = new ShortMessage();
		 //giving (message type,channel,note,velocity) to the messsage.!
		 msg.setMessage(192,1,102,0);
		 //creating the midi event and adding the instruction msg & time
		 //144 for tone on
		 //128 for tone off
		 //192 for instrument change
		 MidiEvent noteON= new MidiEvent(msg,1);
		 //adding MindiEvent to the track.>!
     	 t.add(noteON);             
    	 //msg.setMessage(192,2,102,0);
    	 msg.setMessage(144,1,77,100);
    	 t.add(new MidiEvent(msg,4));
		 
		 ShortMessage msg2 = new ShortMessage();
		 msg2.setMessage(144,9,55,100);
		 MidiEvent noteOFF = new MidiEvent(msg2,5);
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
		 method m = new method();//object of class
		 m.play();
		 
	 }

}
