package uk.ac.abdn.csd.tesco;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ListItems extends Activity{
	//this is the local server ip address for server running on same host
	// localhost or 127.0.0.1 per android is its own emulator
//	String URL = "http://10.0.2.2:8080/jersey/rest/hello";
//	String URL = "http://www.csd.abdn.ac.uk/~bscharla/helloworld.xml";
	
	// for tesco this url gets you logged in and returns sessionkey to use 
	// in followup requests
	String loginURL = "https://secure.techfortesco.com/groceryapi/RESTService.aspx?COMMAND=LOGIN&EMAIL=&PASSWORD=&DEVELOPERKEY=YXxH1y07AvFUB4tNccNo&APPLICATIONKEY=E7EFAF0D8F6B1F858491";
	String startURL = "http://www.techfortesco.com/groceryapi/RESTService.aspx?COMMAND=PRODUCTSEARCH&SESSIONKEY=";
		
	
	String result = "";
	String deviceId = "xxxxx";
	final String tag = "your logcat tag: ";
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_items);
		
		String key="empty";
		try {
			key = login();
		} catch (JSONException e1) {
			
			e1.printStackTrace();
		}
		Bundle b = this.getIntent().getExtras();
		String q = "nothing here";
		if (b!=null){
		q = b.getString("param1");
		}
		 EditText foundThis = (EditText) findViewById(R.id.EditText02);
		// foundThis.setText(q);
		 Button backButton = (Button) findViewById(R.id.Button02);
		 HttpClient httpclient = new DefaultHttpClient();
		 
		 String URL = startURL+key+ "&SEARCHTEXT=" + q +"&EXTENDEDINFO=N&QUICKSEARCH=Y&FAST=Y";
	    	HttpGet request = new HttpGet(URL);
	    
	    	request.addHeader("deviceId", deviceId);
	    	ResponseHandler<String> handler = new BasicResponseHandler();
	    	try {
	    		result = httpclient.execute(request, handler);
	    		foundThis.setText(result);
	    	}catch (ClientProtocolException e) {
	    		e.printStackTrace();
	    	}catch(IOException e) {
	    		e.printStackTrace();
	    	}
	    	httpclient.getConnectionManager().shutdown();
	    	Log.i(tag, result);
	    	
		 backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handleBackButton();
			}
		});
	}

	private void handleBackButton(){
		finish();
	}
	
	private String login() throws JSONException{
		 HttpClient httpclient = new DefaultHttpClient();
		  //  	HttpGet request = new HttpGet(URL+q);
		    	HttpGet request = new HttpGet(loginURL);
		    	request.addHeader("deviceId", deviceId);
		    	ResponseHandler<String> handler = new BasicResponseHandler();
		    	try {
		    		result = httpclient.execute(request, handler);
		    		JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
			    	result = object.getString("SessionKey");
		    		
		    	}catch (ClientProtocolException e) {
		    		e.printStackTrace();
		    	}catch(IOException e) {
		    		e.printStackTrace();
		    	}
		    	httpclient.getConnectionManager().shutdown();
		    	Log.i(tag, result);
		    	
		    	return result;
	}
}
