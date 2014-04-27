package com.k2c.model;

import org.json.JSONArray;

public interface NetworkCallbackInterface {
	public void NetworkCallback(JSONArray data);
	public void NetworkPostExecute();
}
