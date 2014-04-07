import javax.sound.midi.*;

public class Improved_Midi {
	
	public void go()
	{
		try{
		Sequencer player = MidiSystem.getSequencer();
		Sequence s = new Sequence(Sequence.PPQ,4);
		player.open();
		Track t = s.createTrack();
		
		for(int i=5;i<100;i+=4)
		{
		t.add(addMidiEvent(144,1,i,100,i));
		t.add(addMidiEvent(128,1,i,100,i+2));
		}
		player.setSequence(s);
		
		player.start();
		}
		catch(Exception e)
		{
			System.out.println ("Exception caught");
		}
		
	}
	
	public MidiEvent  addMidiEvent(int msg_mode,int channel, int pitch, int velocity,int tym)
	{
		MidiEvent event = null;//note : delcared out side try cuz inside try it will be a instance variable
		try{
		ShortMessage s = new ShortMessage();
		s.setMessage(msg_mode,channel,pitch,velocity);
		event = new MidiEvent(s,tym);
		}
		catch(Exception e)
		{//
		}
		return event;
		
		
	}
	
	public static void main(String args[])
	{
		Improved_Midi p = new Improved_Midi();
		p.go();
		
	}

}
