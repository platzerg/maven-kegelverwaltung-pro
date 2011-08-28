package platzerworld.kegeln.common.remote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import platzerworld.kegeln.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RemoteActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kegelverwaltung_remote);

		TextView v = (TextView) findViewById(R.id.remoteTextView);
		
		String mUrlString = "http://" + "85.181.123.45" + ":" + 
        8080 + 
        "/DynamicWebProject/GeoPositionsService";
		
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(mUrlString);


	    final List<NameValuePair> postParameters = 
	        new ArrayList<NameValuePair>();
	      postParameters.add(
	          new BasicNameValuePair("mobilnummer","123245"));
	      postParameters.add(
	          new BasicNameValuePair("laengengrad",String.valueOf(1234)));
	      postParameters.add(
	          new BasicNameValuePair("breitengrad",String.valueOf(345)));
	      postParameters.add(
	          new BasicNameValuePair("hoehe",String.valueOf(56)));
	      postParameters.add(
	          new BasicNameValuePair("zeitstempel",
	          String.valueOf(1233445567)));
	      try {
	    	  httppost.setEntity(new UrlEncodedFormEntity(postParameters));
	      } catch (UnsupportedEncodingException e2) {
	        Log.e("RemoteActivity", e2.getMessage());
	      }

	      try {
	        Log.d("RemoteActivity", "_sendePosition(): Request abschicken...");
	        httpclient.execute(httppost);      
	      } catch (ClientProtocolException e1) {
	        Log.e("RemoteActivity", e1.getMessage());
	        v.setText(e1.getMessage());
	      } catch (IOException e1) {
	        Log.e("RemoteActivity", e1.getMessage());
	        v.setText(e1.getMessage());
	      }

	}
}