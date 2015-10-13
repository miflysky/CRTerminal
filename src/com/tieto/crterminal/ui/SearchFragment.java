package com.tieto.crterminal.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.tieto.crterminal.R;
import com.tieto.crterminal.R.drawable;
import com.tieto.crterminal.R.id;
import com.tieto.crterminal.R.layout;
import com.tieto.crterminal.ui.GameActivity;
import com.tieto.crterminal.ui.SearchFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchFragment extends Fragment {

	private ListView mGameListView;

	private int[] groupImageList = new int[] { R.drawable.guest1,
			R.drawable.guest7, R.drawable.guest8 };

	private String[] groupNameList = new String[] { "group one", "group two",
			"group three", "group four" };

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_search, container, false);
		mGameListView = (ListView) view.findViewById(R.id.game_listview);

		GroupAdapter groupAdapter = new GroupAdapter(getActivity(),
				groupImageList, groupNameList);
		mGameListView.setAdapter(groupAdapter);

		mGameListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				PlayerFragment pf = new PlayerFragment();
				ft.replace(R.id.main_fragment, pf);
				ft.commit();

			}
		});

		return view;
	}

	class GroupAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<Group> groups;

		public GroupAdapter(Context context, int[] groupImage,
				String[] groupName) {

			inflater = LayoutInflater.from(context);
			groups = new ArrayList<Group>();

			for (int i = 0; i < groupImage.length; i++) {
				Group group = new Group(groupImage[i], groupName[i]);
				groups.add(group);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return (groups != null) ? groups.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return groups.get(position);
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
				viewHolder.groupImageView = (ImageView) convertView
						.findViewById(R.id.group_icn);
				viewHolder.groupName = (TextView) convertView
						.findViewById(R.id.group_name);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.groupImageView.setImageResource(groups.get(position)
					.getGroupImageId());
			viewHolder.groupName.setText(groups.get(position).getGroupName());

			return convertView;
		}

		class ViewHolder {
			public ImageView groupImageView;
			public TextView groupName;
		}

		class Group {
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

	}

	// For get the wifi scan results
	public static class ScanResultsReceiver extends BroadcastReceiver {

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

			String apName;

			WifiManager wifiManager = (WifiManager) searchFragment
					.getActivity().getSystemService(Context.WIFI_SERVICE);

			List<ScanResult> wifiList = wifiManager.getScanResults();
			//String[] apNameList = new 
			for (int i = 0; i < wifiList.size(); i++) {
				apName = wifiList.get(i).SSID;

				if (apName == null)
					continue;

				// List host name in dialogFragment
				if (apName.startsWith(GameActivity.APPREFIX)) {
					//searchFragment.groupNameList[i]
				}

			}

		}
	}

}
