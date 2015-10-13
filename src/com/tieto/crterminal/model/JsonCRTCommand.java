package com.tieto.crterminal.model;

import org.json.JSONObject;

import android.util.Log;

public class JsonCRTCommand {

	private static final String TAG = JsonCRTCommand.class.getSimpleName();
	public String commandName;
	//TODO: change type to JsonCRTCommand can include another command
	public String value;

	public byte[] getByte() {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(commandName, value);
			String jsonString = jsonObject.toString();
			return jsonString.getBytes("UTF-8");
		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
		return null;
	}

}
