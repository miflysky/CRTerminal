package com.tieto.crterminal.model.command;

import org.json.JSONObject;

import android.util.Log;

public class JsonCRTCommand {

	private static final String TAG = JsonCRTCommand.class.getSimpleName();

	public int mEvent;
	public String mMsgType;
	public String mMsgString;
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

	public void setValue(int value) {
		mMsgType = JsonCommadConstant.INTDATA;
		mMsgString = Integer.toString(value);

	}

	public void setValue(String value) {
		mMsgType = JsonCommadConstant.STRDATA;
		mMsgString = value;
	}

	public String toString() {
		try {
			mJSONObject = new JSONObject();

			mJSONObject.put(JsonCommadConstant.EVENTSTRING, mEvent);

			if ((mMsgType != null) && (mMsgString != null)) {
				mJSONObject.put(mMsgType, mMsgString);
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
