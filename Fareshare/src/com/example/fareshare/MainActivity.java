package com.example.fareshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE_PASSWORD = "com.example.myfirstapp.MESSAGE_password";
	public final static String EXTRA_MESSAGE_USERID = "com.example.myfirstapp.MESSAGE_userid";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void searchtrip(View view) {
		//create a new intent
	    Intent intent = new Intent(this, Searchtrip.class);
	    
	    //get destination, origin, and radius from text block
	    EditText userid = (EditText) findViewById(R.id.userid);
	    EditText password = (EditText) findViewById(R.id.password);
	    
	    //transform to string
	    String message_userid = userid.getText().toString();
	    String message_password = password.getText().toString();
	    
	    //add data to intent
	    intent.putExtra(EXTRA_MESSAGE_USERID, message_userid);
	    intent.putExtra(EXTRA_MESSAGE_PASSWORD, message_password);
	    startActivity(intent);
	}

}
