package com.gtracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class TransmitCoordinatesTask extends
		AsyncTask<String, Integer, HttpEntity> {

	
	@Override
	protected HttpEntity doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			return transmitCoordinates(params[0],params[1]);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	private HttpEntity transmitCoordinates(String lat, String lon) throws ClientProtocolException, IOException, IllegalStateException, JSONException
	{
		
		@SuppressWarnings("deprecation")
		HttpParams httpParams = new BasicHttpParams();
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		ThreadSafeClientConnManager connMgr = new ThreadSafeClientConnManager(httpParams, registry); 
		HttpClient client = new DefaultHttpClient(connMgr, httpParams);
		
		HttpGet get = new HttpGet(String.format("http://gpsrest.azurewebsites.net/GPSRestImpl.svc/json?lat=%s&lon=%s", lat, lon));
		
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();	
		
		return entity;
		
	}
	
	@Override
	protected void onPostExecute(HttpEntity result)
	{
		JSONObject jsonObj;
		try {
			if (result != null) {
				jsonObj = new JSONObject(
						convertStreamToString(result.getContent()));
				Log.d("ME", jsonObj.getString("JSONDataResult"));
			} else {
				com.gtracker.Main.setNetworkState(false);
				Log.d("ME", "No internet connection onPostExecute [TransmitCoordinates]");
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("ME", e.getLocalizedMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("ME", e.getLocalizedMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("ME", e.getLocalizedMessage());
		}
		
	}
	
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
