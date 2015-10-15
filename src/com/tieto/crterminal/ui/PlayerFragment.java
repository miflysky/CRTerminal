package com.tieto.crterminal.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.player.GamePlayer;
import com.tieto.crterminal.model.player.GamePlayerGuest;
import com.tieto.crterminal.model.player.JanKenPonValue;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerFragment extends Fragment {

	private BaseGameActivity mActivity;

	private List<GamePlayer> mPlayers = new ArrayList<GamePlayer>();

	private Map<String, GamePlayer> mNameMap = new HashMap<String, GamePlayer>();

	private GridView players_grid;

	private PlayersGridAdapter mAdapter;

	private boolean showResult = false;

	public PlayerFragment(GamePlayer player) {
		mNameMap.put(player.mName, player);
		mPlayers.add(player);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = (BaseGameActivity) getActivity();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_players, container, false);
		initView(view);
		return view;
	}

	private void initView(View parentView) {
		players_grid = (GridView) parentView.findViewById(R.id.players_grid);
		players_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new PlayersGridAdapter(mActivity);
		players_grid.setAdapter(mAdapter);
	}

	public int getJanKenPonImageResourceId(int value) {
		switch (value) {
		case JanKenPonValue.Scissors:
			return R.drawable.scissors;
		case JanKenPonValue.Rock:
			return R.drawable.rock;
		case JanKenPonValue.Paper:
			return R.drawable.paper;
		default:
			return 0;
		}
	}

	class PlayersGridAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public PlayersGridAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mPlayers.size();
		}

		@Override
		public Object getItem(int position) {
			return mPlayers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			ImageView player_icon;
			TextView player_name;
			ImageView player_card;
			TextView player_status;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.player, null);
				holder.player_icon = (ImageView) convertView.findViewById(R.id.player_icon);
				holder.player_name = (TextView) convertView.findViewById(R.id.player_name);
				holder.player_card = (ImageView) convertView.findViewById(R.id.player_card);
				holder.player_status = (TextView) convertView.findViewById(R.id.player_status);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			GamePlayer player = mPlayers.get(position);

			holder.player_name.setText(player.mName);

			int resId = getJanKenPonImageResourceId(player.mValue);

			if (player.mValue != 0 && (showResult || player.mName.equals(mActivity.mMyName)) && resId != 0) {
				holder.player_card.setVisibility(View.VISIBLE);
				holder.player_card.setImageResource(resId);
				if (mActivity.isConfirm) {
					holder.player_status.setVisibility(View.VISIBLE);
				}
			} else if (player.mValue != 0 && !showResult) {
				if (player.status == GamePlayer.READY) {
					holder.player_status.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}
	}

		public void playerAdd(String name) {
			GamePlayerGuest player = new GamePlayerGuest(name, null);
			mNameMap.put(player.mName, player);
			mPlayers.add(player);
			mAdapter.notifyDataSetChanged();
		}

		public void playerLeave(String name) {
			mPlayers.remove(mNameMap.remove(name));
			mAdapter.notifyDataSetChanged();
		}

		public void playerFailed(String name) {
			mAdapter.notifyDataSetChanged();
		}

		public void showResult() {
			showResult = true;
		}

		public void playerReady(String name) {
			mNameMap.get(name).status = GamePlayer.READY;
			mAdapter.notifyDataSetChanged();
		}

		public void confirmChoose() {
			mAdapter.notifyDataSetChanged();
		}

		public void playMakeChoice(String userName, int value) {
			mNameMap.get(userName).mValue = value;
			mAdapter.notifyDataSetChanged();
		}
		
		public void notifyUIChange() {
			mAdapter.notifyDataSetChanged();
		}
		
		public void notifyResult(String result){
			//TODO 
		}

	
}
