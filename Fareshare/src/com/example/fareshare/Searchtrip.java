package com.example.fareshare;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class Searchtrip extends Activity {
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);

	    // Get the message from the intent
	    Intent intent = getIntent();
	    String origin = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_ORIGIN);
	    String destination = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_DESTINATION);
	    String radius = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_RADIUS);
	    String result = null;
        InputStream is = null;
        StringBuilder sb=null;
	    //http post
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://127.0.0.1/food.php");
           // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection"+e.toString());
        }
     
        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
          
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
             
            is.close();
            result=sb.toString();
             
        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
 
        //paring data
        int fd_id;
        String fd_name;
        try{
        jArray = new JSONArray(result);
        JSONObject json_data=null;
         
        for(int i=0;i<jArray.length();i++){
                json_data = jArray.getJSONObject(i);
                fd_id=json_data.getInt("FOOD_ID");
                fd_name=json_data.getString("FOOD_NAME");
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

}
