package com.example.fareshare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Searchtrip_origin extends Activity {
	@SuppressLint("NewApi")
    InputStream is = null;
    String origin;
    String destination;
    String radius;
	@Override
	public void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);	  
		Intent intent = getIntent();
	    origin = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_ORIGIN);
	    destination = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_DESTINATION);
	    radius = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_RADIUS);
	    accessWebService();
	    
	    //read the data from is
	    String rLine = "";
	    StringBuilder answer = new StringBuilder();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));	  
	    try {
	     while ((rLine = rd.readLine()) != null) {
	      answer.append(rLine);
	     }
	    }	  
	    catch (IOException e) {
	     // e.printStackTrace();
	     Toast.makeText(getApplicationContext(),
	       "Error..." + e.toString(), Toast.LENGTH_LONG).show();
	    }
	    
	    
        //paring data
        try{
        JSONArray jArray = new JSONArray(answer.toString());
        JSONObject json_data=null;
        String PosterID;
        String TripID;
        String DriverID;
        String fd_name;
        for(int i=0;i<jArray.length();i++){
                json_data = jArray.getJSONObject(i);
                PosterID=json_data.getString("Success");
                TripID=json_data.getString("TripID");
                DriverID=json_data.getString("DriverID");
                TextView textView = new TextView(this);
        	    textView.setTextSize(40);
        	    textView.setText(PosterID);
                setContentView(textView);
        }
         
        }catch(JSONException e1){
            Toast.makeText(getBaseContext(), "No Food Found", Toast.LENGTH_LONG).show();
        }catch (ParseException e1){
            e1.printStackTrace();
        }
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void accessWebService() {
		  ConnectServer task = new ConnectServer();
		  // passes values for the urls string array
		  task.execute();
	}
	private class ConnectServer extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String ...strings ) {
		        //StringBuilder NameValuePair;      
		        List<NameValuePair> postParameters;
		        postParameters = new ArrayList<NameValuePair>();
		        postParameters.add(new BasicNameValuePair("Origin", "champagin"));
		        postParameters.add(new BasicNameValuePair("Destination", "chicago"));
		        //postParameters.add(new BasicNameValuePair("Radius",radius));
			    //http post
		        try{
		            HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost("http://ec2-54-214-246-100.us-west-2.compute.amazonaws.com/Controllers/TripController.php");
		            httppost.setEntity(new UrlEncodedFormEntity(postParameters));
		            HttpResponse response = httpclient.execute(httppost);
		            HttpEntity entity = response.getEntity();
		            is = entity.getContent();
		        }catch(Exception e){
		            Log.e("log_tag", "Error in http connection"+e.toString());
		        }
		     return null;
		        
		}
	}
}
