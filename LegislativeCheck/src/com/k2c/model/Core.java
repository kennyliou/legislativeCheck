package com.k2c.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

/*
 * This is the core that are capable to do anything that is needed for this app.
 * Anything that requires the data connection needs to go through core
 * HTTP fetching
 * 
 */
public class Core extends AsyncTask<Void, Void, Void> {

	public NetworkCallbackInterface callback;

	private final String hearingURL = "http://api.ly.g0v.tw/v0/collections/calendar?q=";
	private final String hearingURL_qp = "{\"type\":\"hearing\"}";

	public void getData() {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(hearingURL + URLEncoder.encode(hearingURL_qp, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			HttpResponse response = httpclient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			JSONObject json = new JSONObject(builder.toString());

			JSONArray finalResult = json.getJSONArray("entries");

			callback.NetworkCallback(finalResult);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected Void doInBackground(Void... params) {
		getData();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		callback.NetworkPostExecute();
		return;
	}

	public void setCallback(NetworkCallbackInterface callback) {
		this.callback = callback;
	}
}
