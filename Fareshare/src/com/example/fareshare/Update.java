package com.example.fareshare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class Update extends Activity {

	 private String url = "http://ec2-54-214-246-100.us-west-2.compute.amazonaws.com/Controllers/update_trip.php?";
	 private String jsonResult;
	 private String tripid = "156";//need to get from somewhere, I just fix it here for it to function first
	 
	 private String destination = "";
	 private String origin = "";
	 private String radius = "";
	 private String tripdatetime = "";
	 private String driverid = "";
	 private String seats = "";
	 private String tripcost = "";
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Intent intent = getIntent();
		String dest = intent.getStringExtra(Updatepage.EXTRA_MESSAGE_DESTINATION);
		String orig = intent.getStringExtra(Updatepage.EXTRA_MESSAGE_ORIGIN);
		String rad = intent.getStringExtra(Updatepage.EXTRA_MESSAGE_RADIUS);
		String time = intent.getStringExtra(Updatepage.EXTRA_MESSAGE_TRIPDATETIME);
		String did = intent.getStringExtra(Updatepage.EXTRA_MESSAGE_DRIVERID);
		String seat = intent.getStringExtra(Updatepage.EXTRA_MESSAGE_SEATS);
		String cost = intent.getStringExtra(Updatepage.EXTRA_MESSAGE_TRIPCOST);
		setContentView(R.layout.activity_update);
		destination = dest;
		origin = orig;
		radius = rad;
		tripdatetime = time;
		driverid = did;
		seats = seat;
		tripcost = cost;
		// Show the Up button in the action bar.
		setupActionBar();
		accessWebService();
		PrintTrip();
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
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
		  JsonReadTask task = new JsonReadTask();
		  // passes values for the urls string array
		  task.execute(new String[] { url });
	}
	// Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		  @Override
		  protected String doInBackground(String... params) {
		   HttpClient httpclient = new DefaultHttpClient();
		   params[0]+="TripID="+tripid
				   +"&Origin="+origin
				   +"&Destination="+destination
				   +"&Radius="+radius
				   +"&TripDateTime="+tripdatetime
				   +"&DriverID="+driverid
				   +"&Seats="+seats
				   +"&TripCost="+tripcost;
		   
		   HttpPost post = new HttpPost(params[0]);
		   try {
		    HttpResponse response = httpclient.execute(post);
		    jsonResult = inputStreamToString(
		      response.getEntity().getContent()).toString();
		   }
		 
		   catch (ClientProtocolException e) {
		    e.printStackTrace();
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		   return null;
		  }
		 
		  private StringBuilder inputStreamToString(InputStream is) {
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
		   return answer;
		  }
		 }// end async task
	
	private void PrintTrip(){
		try {
			  JSONObject jOb = new JSONObject(jsonResult);
		        String text = "";
		        text += jOb.get("Destnation");
		        text += "\n"+jOb.get("Origin");
		        text += "\n"+jOb.get("Radius");
		        text += "\n"+jOb.get("TripDateTime");
		        text += "\n"+jOb.get("DriverID");
		        text += "\n"+jOb.get("Seats");
		        text += "\n"+jOb.get("Tripcost");
		  	  TextView textView=new TextView(this);
			  textView.setTextSize(20);
			  textView.setText(text);
			  setContentView(textView);
		        }catch(JSONException e1){
					Toast.makeText(getBaseContext(), "No PosterID", Toast.LENGTH_LONG).show();
		        }catch (ParseException e1){
		            e1.printStackTrace();
		        }
	}
}
