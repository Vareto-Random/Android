package uk.ac.abdn.csd.tesco;

//import java.io.IOException;
//
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class main extends Activity {
	
	

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText txtSearch = (EditText)findViewById(R.id.EditText01);
        txtSearch.setOnClickListener(new EditText.OnClickListener() {
        	public void onClick(View v){txtSearch.setText("");}
        });
        	final Button btnSearch = (Button)findViewById(R.id.Button01);
        	btnSearch.setOnClickListener(new Button.OnClickListener() {
        		public void onClick(View v) {
        			String query = txtSearch.getText().toString();
        			callWebService(query);
        		}
        	});
    }
    private void callWebService(String q) {
//    	Bundle b = new Bundle();
//    	b.putString("param1", q);
    	Intent newIntent = new Intent(this, ListItems.class);
    	newIntent.putExtra("param1", q);
    	startActivity(newIntent);
    	
    	
    	
    }
}
