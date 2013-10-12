package com.example.fareshare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Searchtrip extends Activity {
	 @SuppressLint("NewApi")
	 private String jsonResult;
	 private String url = "http://ec2-54-214-246-100.us-west-2.compute.amazonaws.com/Controllers/TripController.php?";
	 private ListView listView;
	 private String origin;
	 private String destination;
	 private String radius;
	 private String PosterID_whole;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  Intent intent = getIntent();
	  origin = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_ORIGIN);
	  destination = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_DESTINATION);
	  radius = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_RADIUS);
	  setContentView(R.layout.activity_main);
	  listView = (ListView) findViewById(R.id.listView1);
	  accessWebService();
	  //TextView textView=new TextView(this);
	  //textView.setTextSize(40);
	  //textView.setText(PosterID_whole);
	 // setContentView(textView);
	 }
	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) { 
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.main, menu);
	  return true;
	 }
	 
	 // Async Task to access the web
	 private class JsonReadTask extends AsyncTask<String, Void, String> {
	  @Override
	  protected String doInBackground(String... params) {
	   HttpClient httpclient = new DefaultHttpClient();
	   params[0]+="Origin=champaign&Destination=chicago";
	   HttpGet httpget = new HttpGet(params[0]);
	   try {
	    HttpResponse response = httpclient.execute(httpget);
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
	 
	  @Override
	  protected void onPostExecute(String result) {
	   ListDrwaer();
	  }
	 }// end async task
	 
	 public void accessWebService() {
	  JsonReadTask task = new JsonReadTask();
	  // passes values for the urls string array
	  task.execute(new String[] { url });
	 }
	 
	 // build hash set for list view
	 public void ListDrwaer() {
	  List<Map<String, String>> PosterIDList = new ArrayList<Map<String, String>>();
	 
	  try {
		  JSONArray jArray = new JSONArray(jsonResult);
	        JSONObject json_data=null;
	        String PosterID;
	        String TripID;
	        String DriverID;
	        String fd_name;
	        json_data = jArray.getJSONObject(0);
	        PosterID_whole=json_data.getString("PosterID");
	        for(int i=0;i<jArray.length();i++){
	                json_data = jArray.getJSONObject(i);
	                PosterID=json_data.getString("PosterID");
	                TripID=json_data.getString("TripID");
	                DriverID=json_data.getString("DriverID");
	                PosterIDList.add(createPoster("PosterID", PosterID));
	        }	         
	        }catch(JSONException e1){
				Toast.makeText(getBaseContext(), "No PosterID", Toast.LENGTH_LONG).show();
	        }catch (ParseException e1){
	            e1.printStackTrace();
	        }
	 
	  SimpleAdapter simpleAdapter = new SimpleAdapter(this, PosterIDList,
	    android.R.layout.simple_list_item_1,
	    new String[] { "PosterID" }, new int[] { android.R.id.text1 });
	  listView.setAdapter(simpleAdapter);
	 }
	 
	 private HashMap<String, String> createPoster(String name, String number) {
	  HashMap<String, String> employeeNameNo = new HashMap<String, String>();
	  employeeNameNo.put(name, number);
	  return employeeNameNo;
	 }
	}