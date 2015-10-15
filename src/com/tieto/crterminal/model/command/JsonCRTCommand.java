package com.tieto.crterminal.model.command;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonCRTCommand {

	private static final String TAG = JsonCRTCommand.class.getSimpleName();

	private int mEvent;

	// Type could be int or string
	private String mMsgType;

	// Store the int or string data
	private String mMsgString;
	private int mMsgInt;

	public JSONObject mJSONObject;

	public JsonCRTCommand(int event) {
		mEvent = event;
	}

	public JsonCRTCommand(String data) {
		try {
			mJSONObject = new JSONObject(data);
		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
	}

	public int getEvent() {

		try {
			mEvent = mJSONObject.getInt(JsonCommadConstant.INTDATA);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mEvent;
	}

	public String getStrData() {

		try {
			mMsgString = mJSONObject.getString(JsonCommadConstant.STRDATA);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mMsgString;
	}

	public void setValue(int value) {
		mMsgType = JsonCommadConstant.INTDATA;
		mMsgInt = value;

	}

	public void setValue(String str) {
		mMsgType = JsonCommadConstant.STRDATA;
		mMsgString = str;
	}

	public String toString() {
		try {
			mJSONObject = new JSONObject();

			mJSONObject.put(JsonCommadConstant.EVENTSTRING, mEvent);

			if ((mMsgType != null) && (mMsgType == JsonCommadConstant.STRDATA)) {
				mJSONObject.put(JsonCommadConstant.STRDATA, mMsgString);
			}else if ((mMsgType != null) && (mMsgType == JsonCommadConstant.INTDATA)) {
				mJSONObject.put(JsonCommadConstant.INTDATA, mMsgInt);
			}

			// if get string
			// String jsonString = mJSONObject.toString()
			// return jsonString.getBytes("UTF-8");

			return mJSONObject.toString();

		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
		return null;
	}
}
