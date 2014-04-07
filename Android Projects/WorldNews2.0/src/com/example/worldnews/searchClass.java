package com.example.worldnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class searchClass extends Activity implements OnClickListener{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	Button search,cancel;
	EditText query;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_dialog);
		search = (Button)findViewById(R.id.buttonSearch);
		cancel = (Button)findViewById(R.id.buttonCancel);
		query= (EditText)findViewById(R.id.searchtext);
		search.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		Log.d("SearhClass Initialised !!","!!!!!!!!!!");
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		switch (arg0.getId()) {
		case R.id.buttonCancel:
			finish();
			break;
		case R.id.buttonSearch:
			String querySring = query.getText().toString();
			if(querySring!=null)
			{
			Log.d("Valid Click","Query = "+querySring);
			Bundle basket = new Bundle();
			basket.putString("searchKey",querySring);
			Intent i = new Intent(this, MainActivity.class);
			i.putExtras(basket);
			setResult(RESULT_OK,i);
			Log.d("Finishing SearchClass and resuming..","!!!!!!!!");
			finish();
			}
			else
			{
				Toast.makeText(this, "Please Enter Text to Search",Toast.LENGTH_SHORT).show();
			}
			break;
		}
		
	}
		
	}

