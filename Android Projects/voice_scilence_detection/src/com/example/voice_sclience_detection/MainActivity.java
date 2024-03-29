package com.example.voice_sclience_detection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	AudioRecord audioRecorder ;
	Button b;
	Boolean isRecording=false; 
	private static final int RECORDER_BPP = 16;
	private static int RECORDER_SAMPLERATE = 8000; 
	private static int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	 
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b =(Button)findViewById(R.id.button1);
        b.setOnClickListener(this);
        b.setText("Start");
    }
	
	public void startIntelligentRecording()
	{
		
		Toast.makeText(this,"Recording Starting...",Toast.LENGTH_SHORT).show();
		  Log.d("Initialising..","!!!!!!!!");
		    // Get the minimum buffer size required for the successful creation of an AudioRecord object. 
		    int bufferSizeInBytes = AudioRecord.getMinBufferSize( RECORDER_SAMPLERATE,
		                                                          RECORDER_CHANNELS,
		                                                          RECORDER_AUDIO_ENCODING
		                                                         ); 
		    
		    Log.d("Initialising Recorder","!!!!!!!!");
		    // Initialize Audio Recorder.
		    audioRecorder = new AudioRecord( MediaRecorder.AudioSource.MIC,
		                                                 RECORDER_SAMPLERATE,
		                                                 RECORDER_CHANNELS,
		                                                 RECORDER_AUDIO_ENCODING,
		                                                 bufferSizeInBytes
		                                                );
		
		    Log.d("Starting Recording","!!!!!!!!");
		    // Start Recording.
		    audioRecorder.startRecording();

		    int numberOfReadBytes   = 0; 
		    byte audioBuffer[]      = new  byte[bufferSizeInBytes];
		    boolean recording       = false;
		    float tempFloatBuffer[] = new float[3];
		    int tempIndex           = 0;
		    int totalReadBytes      = 0;
		    byte totalByteBuffer[]  = new byte[60 * 44100 * 2];

		    Log.d("Getting Data From MIC","!!!!!!!!");
		    
		    // While data come from microphone. 
		    while( true )
		    {
		        float totalAbsValue = 0.0f;
		        short sample        = 0; 

		        numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );

		        // Analyze Sound.
		        for( int i=0; i<bufferSizeInBytes; i+=2 ) 
		        {
		            sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
		            totalAbsValue += Math.abs( sample ) / (numberOfReadBytes/2);
		        }

		        // Analyze temp buffer.
		        tempFloatBuffer[tempIndex%3] = totalAbsValue;
		        float temp                   = 0.0f;
		        for( int i=0; i<3; ++i )
		            temp += tempFloatBuffer[i];

		        if( (temp >=0 && temp <= 350) && recording == false )
		        {
		            Log.i("TAG", "1");
		            tempIndex++;
		            continue;
		        }

		        if( temp > 350 && recording == false )
		        {
		            Log.i("TAG", "2");
		            recording = true;
		        }

		        if( (temp >= 0 && temp <= 350) && recording == true )
		        {
		            Log.i("TAG", "Save audio to file.");

		            // Save audio to file.
		            String filepath = Environment.getExternalStorageDirectory().getPath();
		            File file = new File(filepath,"AudioRecorder");
		            if( !file.exists() )
		                file.mkdirs();

		            String fn = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".wav";

		            long totalAudioLen  = 0;
		            long totalDataLen   = totalAudioLen + 36;
		            long longSampleRate = RECORDER_SAMPLERATE;
		            int channels        = 1;
		            long byteRate       = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;
		            totalAudioLen       = totalReadBytes;
		            totalDataLen        = totalAudioLen + 36;
		            byte finalBuffer[]  = new byte[totalReadBytes + 44];

		            finalBuffer[0] = 'R';  // RIFF/WAVE header
		            finalBuffer[1] = 'I';
		            finalBuffer[2] = 'F';
		            finalBuffer[3] = 'F';
		            finalBuffer[4] = (byte) (totalDataLen & 0xff);
		            finalBuffer[5] = (byte) ((totalDataLen >> 8) & 0xff);
		            finalBuffer[6] = (byte) ((totalDataLen >> 16) & 0xff);
		            finalBuffer[7] = (byte) ((totalDataLen >> 24) & 0xff);
		            finalBuffer[8] = 'W';
		            finalBuffer[9] = 'A';
		            finalBuffer[10] = 'V';
		            finalBuffer[11] = 'E';
		            finalBuffer[12] = 'f';  // 'fmt ' chunk
		            finalBuffer[13] = 'm';
		            finalBuffer[14] = 't';
		            finalBuffer[15] = ' ';
		            finalBuffer[16] = 16;  // 4 bytes: size of 'fmt ' chunk
		            finalBuffer[17] = 0;
		            finalBuffer[18] = 0;
		            finalBuffer[19] = 0;
		            finalBuffer[20] = 1;  // format = 1
		            finalBuffer[21] = 0;
		            finalBuffer[22] = (byte) channels;
		            finalBuffer[23] = 0;
		            finalBuffer[24] = (byte) (longSampleRate & 0xff);
		            finalBuffer[25] = (byte) ((longSampleRate >> 8) & 0xff);
		            finalBuffer[26] = (byte) ((longSampleRate >> 16) & 0xff);
		            finalBuffer[27] = (byte) ((longSampleRate >> 24) & 0xff);
		            finalBuffer[28] = (byte) (byteRate & 0xff);
		            finalBuffer[29] = (byte) ((byteRate >> 8) & 0xff);
		            finalBuffer[30] = (byte) ((byteRate >> 16) & 0xff);
		            finalBuffer[31] = (byte) ((byteRate >> 24) & 0xff);
		            finalBuffer[32] = (byte) (2 * 16 / 8);  // block align
		            finalBuffer[33] = 0;
		            finalBuffer[34] = RECORDER_BPP;  // bits per sample
		            finalBuffer[35] = 0;
		            finalBuffer[36] = 'd';
		            finalBuffer[37] = 'a';
		            finalBuffer[38] = 't';
		            finalBuffer[39] = 'a';
		            finalBuffer[40] = (byte) (totalAudioLen & 0xff);
		            finalBuffer[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		            finalBuffer[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		            finalBuffer[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		            for( int i=0; i<totalReadBytes; ++i )
		                finalBuffer[44+i] = totalByteBuffer[i];

		            FileOutputStream out;
		            try {
		                out = new FileOutputStream(fn);
		                 try {
		                        out.write(finalBuffer);
		                        out.close();
		                    } catch (IOException e) {
		                        // TODO Auto-generated catch block
		                        e.printStackTrace();
		                    }

		            } catch (FileNotFoundException e1) {
		                // TODO Auto-generated catch block
		                e1.printStackTrace();
		            }

		            //*/
		            tempIndex++;
		            
		           
		           // break;
		            //Rinintailaising Eveything
		            
		            
		        }

		        // -> Recording sound here.
		        Log.i( "TAG", "Recording Sound." );
		        for( int i=0; i<numberOfReadBytes; i++ )
		            totalByteBuffer[totalReadBytes + i] = audioBuffer[i];
		        totalReadBytes += numberOfReadBytes;
		     

		        tempIndex++;

		    }
		    //audioRecorder.stop();
		    //audioRecorder.release();
		    //Toast.makeText(this,"Closing Recorder...",Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(!isRecording)
		{
			b.setText("Stop");
			startIntelligentRecording();
			isRecording=true;
		}
		else
		{
			b.setText("Start");
			audioRecorder.stop();
		    audioRecorder.release();
		    Toast.makeText(this,"Closing Recorder...",Toast.LENGTH_SHORT).show();
		    isRecording=false;
		}
	}

}
