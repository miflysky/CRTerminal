package com.tieto.crterminal.model;

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

	public byte[] getByte() {
		try {
			mJSONObject = new JSONObject();

			mJSONObject.put(JsonCommadConstant.EVENTSTRING, mEvent);

			if (mEvent < JsonCommadConstant.FROM_SERVER_EVENT_NULL_END) {
				if ((mMsgType != null) && (mMsgString != null)) {
					mJSONObject.put(mMsgType, mMsgString);
				}
			}

			String jsonString = mJSONObject.toString();
			return jsonString.getBytes("UTF-8");
		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
		return null;
	}

}
