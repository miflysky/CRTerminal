package com.tieto.crterminal.controler;

import java.util.ArrayList;
import java.util.List;

import com.tieto.crterminal.R;
import com.tieto.crterminal.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

public class APSelectDialogFragment extends DialogFragment implements
		DialogInterface.OnClickListener {

	private static final String TAG = "CRTerminal";

	private ArrayAdapter<String> mArrayAdapter;
	private List<String> mApList;

	private ScanResultsReceiver mScanResultsReceiver;

	public static APSelectDialogFragment newInstance(Activity mainActivity) {
		APSelectDialogFragment framgment = new APSelectDialogFragment();

		// TODO: Need activity pass parameter?

		return framgment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.APSelectorDialog);

		mApList = new ArrayList<String>();

		mArrayAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_expandable_list_item_1, mApList);

		builder.setSingleChoiceItems(mArrayAdapter, -1, this);

		// register the ScanResultsReceiver to get the wifi scan results
		Activity mainActiviy = getActivity();
		mScanResultsReceiver = new ScanResultsReceiver(mArrayAdapter);
		mainActiviy.registerReceiver(mScanResultsReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		return builder.create();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {

		Activity mainActiviy = getActivity();
		mainActiviy.unregisterReceiver(mScanResultsReceiver);

	}

	//For get the wifi scan results
	public class ScanResultsReceiver extends BroadcastReceiver {

		ArrayAdapter<String> mArrayAdapterRec;

		public ScanResultsReceiver(ArrayAdapter<String> adapter) {
			mArrayAdapterRec = adapter;

		}

		@Override
		public void onReceive(Context context, Intent intent) {

			String apName;

			mArrayAdapterRec.clear();

			Activity mainActiviy = getActivity();

			WifiManager wifiManager = (WifiManager) mainActiviy
					.getSystemService(Context.WIFI_SERVICE);

			List<ScanResult> wifiList = wifiManager.getScanResults();
			for (int i = 0; i < wifiList.size(); i++) {
				apName = wifiList.get(i).SSID;

				if (apName == null)
					continue;

				Log.i(TAG, "Searched ap: " + apName);

				// List host name in dialogFragment
				if (apName.startsWith(MainActivity.APPREFIX)) {
					mArrayAdapterRec.add(apName.substring(MainActivity.APPREFIX
							.length()));
				}

			}

		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		String apname = MainActivity.APPREFIX + mArrayAdapter.getItem(which);
		Log.i(TAG, "User select AP: " + apname);

		// Send back the selected ap name
		MainActivity mainActiviy = (MainActivity) getActivity();

		Handler handler = mainActiviy.getNoneUIEventHandler();

		Message msg = handler.obtainMessage(MainActivity.GOTAPNAME);
		msg.obj = apname;
		handler.sendMessage(msg);

		dismiss();
	}

}
