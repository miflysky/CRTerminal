package com.tieto.crterminal.model;

import org.json.JSONObject;

import android.util.Log;

public class JsonCRTCommand {

	private static final String TAG = JsonCRTCommand.class.getSimpleName();

	private static final String EVENTSTRING = "event";

	public int mEvent;
	public String mMsgType;
	
	public String mMsgString;
	public String mMsgInt;
	
	
	public JsonCRTCommand() {
		// initialize default value?
	}

	public JsonCRTCommand(int event) {
		mEvent = event;
	}

	public byte[] getByte() {
		try {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put(EVENTSTRING, mEvent);

			if ((mMsgType != null) && (mMsgString != null)) {
				jsonObject.put(mMsgType, mMsgString);
			}

			String jsonString = jsonObject.toString();
			return jsonString.getBytes("UTF-8");
		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
		return null;
	}

}
