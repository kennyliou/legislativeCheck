package com.k2c.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

/*
 * This is the core that are capable to do anything that is needed for this app.
 * Anything that requires the data connection needs to go through core
 * HTTP fetching
 * 
 */
public class Core implements CoreInterface {

	private final String hearingURL = "http://api.ly.g0v.tw/v0/collections/calendar?q={\"type\":\"hearing\"}";

	@Override
	public JSONArray getData() {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(hearingURL);

		try {
			HttpResponse response = httpclient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			JSONTokener tokener = new JSONTokener(builder.toString());
			JSONArray finalResult = new JSONArray(tokener);

			return finalResult;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
