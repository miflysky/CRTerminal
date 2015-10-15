package com.tieto.crterminal.model.command;

import org.json.JSONObject;

import android.util.Log;

public class JsonCRTCommand {

	private static final String TAG = JsonCRTCommand.class.getSimpleName();

	private int mEvent;


	// Store the int or string data
	private String mMsgString;

	public JSONObject mJSONObject;

	public JsonCRTCommand(int event) {
		mEvent = event;
	}

	public JsonCRTCommand(String data) {
		try {
			mJSONObject = new JSONObject(data);
			mEvent = mJSONObject.getInt(JsonCommadConstant.EVENTSTRING);
			mMsgString = mJSONObject.getString(JsonCommadConstant.STRDATA);
		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
	}

	public int getEvent() {
		return mEvent;
	}


	public void setValue(String str) {
		mMsgString = str;
	}

	public String getValue() {
		return mMsgString;
	}

	public String toString() {
		try {
			mJSONObject = new JSONObject();

			mJSONObject.put(JsonCommadConstant.EVENTSTRING, mEvent);
        	mJSONObject.put(JsonCommadConstant.STRDATA, mMsgString);

			return mJSONObject.toString();

		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
		return null;
	}

}
