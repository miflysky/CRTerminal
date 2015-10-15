package com.tieto.crterminal.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.wifi.WifiUtils;

public class SearchFragment extends Fragment {

	private static final String TAG = "CRTerminal";

	private ListView mGameListView;
	private TextView mSearchingText;
	private GroupAdapter mGroupAdapter;
	private List<ScanResult> mWifiList;

	private ScanResultsReceiver mScanResultsReceiver;

	private WifiUtils mWifiUtils;

	private List<Group> mGroups = new ArrayList<Group>();
	private int[] groupImageList = new int[] { R.drawable.guest1, R.drawable.guest7, R.drawable.guest8 };

	public static final int UIEVENT1 = 1;

	private UIEventHandler mUIEventHandler = new UIEventHandler(this);

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, container, false);
		mGameListView = (ListView) view.findViewById(R.id.game_listview);
		mSearchingText = (TextView) view.findViewById(R.id.searching);

		mGroupAdapter = new GroupAdapter();
		mGameListView.setAdapter(mGroupAdapter);

		mGameListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				mWifiUtils.connectToSSID(BaseGameActivity.APPREFIX + mGroups.get(position).getGroupName());

				// TODO: join game

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				PlayerFragment pf = new PlayerFragment();
				ft.replace(R.id.main_fragment, pf);
				ft.commit();

			}
		});

		WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		mWifiUtils = new WifiUtils(wifiManager);

		mScanResultsReceiver = new ScanResultsReceiver(this);

		getActivity().registerReceiver(mScanResultsReceiver,
				new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		
		mWifiUtils.startWifiScan();
		
		return view;
	}
	
	@Override
	public void onDestroyView() {
		
		// TODO Auto-generated method stub
		super.onDestroyView();

		getActivity().unregisterReceiver(mScanResultsReceiver);
	}
	


	class GroupAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public GroupAdapter() {
			inflater = LayoutInflater.from(SearchFragment.this.getActivity());
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return (mGroups != null) ? mGroups.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mGroups.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.game_list_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.groupImageView = (ImageView) convertView.findViewById(R.id.group_icn);
				viewHolder.groupName = (TextView) convertView.findViewById(R.id.group_name);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.groupImageView.setImageResource(mGroups.get(position).getGroupImageId());
			viewHolder.groupName.setText(mGroups.get(position).getGroupName());

			return convertView;
		}

		class ViewHolder {
			public ImageView groupImageView;
			public TextView groupName;
		}
	}

	static class Group {
		private int groupImageId;
		private String groupName;

		public Group(int groupImageId, String groupName) {
			super();
			this.setGroupImageId(groupImageId);
			this.setGroupName(groupName);
		}

		public int getGroupImageId() {
			return groupImageId;
		}

		public void setGroupImageId(int groupImageId) {
			this.groupImageId = groupImageId;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}
	}

	// For get the wifi scan results
	public class ScanResultsReceiver extends BroadcastReceiver {

		WeakReference<SearchFragment> mWeakReference;

		public ScanResultsReceiver(SearchFragment searchFragment) {
			mWeakReference = new WeakReference<SearchFragment>(searchFragment);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			SearchFragment searchFragment = mWeakReference.get();
			if (searchFragment == null) {
				return;
			}
			if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

				mSearchingText.setVisibility(View.GONE);
				mGroups.clear();

				String apName;

				WifiManager wifiManager = (WifiManager) searchFragment.getActivity()
						.getSystemService(Context.WIFI_SERVICE);

				mWifiList = wifiManager.getScanResults();
				for (int i = 0; i < mWifiList.size(); i++) {
					apName = mWifiList.get(i).SSID;
					if (apName == null)
						continue;

					Log.i(TAG, "Searched ap: " + apName);

					// List host name in dialogFragment
					if (apName.startsWith(BaseGameActivity.APPREFIX)) {
						Group group = new SearchFragment.Group(searchFragment.groupImageList[i % 3],
								apName.substring(BaseGameActivity.APPREFIX.length()));
						mGroups.add(group);
					}
				}
				Message msg = new Message();
				msg.what = UIEVENT1;
				searchFragment.mUIEventHandler.sendMessage(msg);
			}
		}
	}

	private static class UIEventHandler extends Handler {

		private WeakReference<SearchFragment> mWeakReference;

		public UIEventHandler(SearchFragment searchFragment) {
			mWeakReference = new WeakReference<SearchFragment>(searchFragment);
		}

		@Override
		public void handleMessage(Message msg) {
			SearchFragment searchFragment = mWeakReference.get();
			if (searchFragment == null) {
				return;
			}
			switch (msg.what) {
			case UIEVENT1:
				searchFragment.mGroupAdapter.notifyDataSetChanged();
				break;
			}
		}
	}

}
