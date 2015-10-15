package com.tieto.crterminal.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.player.JanKenPonValue;

public class PlayerFragment extends Fragment {

	private BaseGameActivity mActivity;

	private List<Player> players = new ArrayList<Player>();

	private Map<String, Player> nameMap = new HashMap<String, Player>();

	private GridView players_grid;

	private PlayersGridAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = (BaseGameActivity) getActivity();
		Intent intent = new Intent();
		intent.setAction(GamePadFragment.GAME_READY_ACTION);
		mActivity.sendBroadcast(intent);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_players, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View parentView) {
		players_grid = (GridView) parentView.findViewById(R.id.players_grid);
		players_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new PlayersGridAdapter(mActivity);
		players_grid.setAdapter(mAdapter);

		playerAdd(mActivity.getUserName());
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
			return players.size();
		}

		@Override
		public Object getItem(int position) {
			return players.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.player, null);
				holder.player_icon = (ImageView) convertView
						.findViewById(R.id.player_icon);
				holder.player_name = (TextView) convertView
						.findViewById(R.id.player_name);
				holder.player_card = (ImageView) convertView
						.findViewById(R.id.player_card);
				holder.player_status = (TextView) convertView
						.findViewById(R.id.player_status);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Player player = players.get(position);

			holder.player_name.setText(player.name);

			int resId = getJanKenPonImageResourceId(player.value);
			if (resId != 0) {
				holder.player_card.setVisibility(View.VISIBLE);
				holder.player_card.setImageResource(resId);
			}

			if (player.status == Player.READY) {
				holder.player_status.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView player_icon;
			TextView player_name;
			ImageView player_card;
			TextView player_status;
		}
	}

	public Handler mGamePlayerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case JsonCommadConstant.FROM_SERVER_EVENT_NULL_STARTGAME:

				break;

			case JsonCommadConstant.FROM_SERVER_EVENT_NULL_GAMERESULT:

				break;

			case JsonCommadConstant.FROM_SERVER_EVENT_INT_NEWROUND:

				break;

			case JsonCommadConstant.FROM_SERVER_EVENT_INT_ENDROUND:

				break;

			case JsonCommadConstant.FROM_SERVER_EVENT_STR_USERLIST:

				break;
			}
		}

	};

	public Handler getHandler() {

		return mGamePlayerHandler;
	}

	public void playerAdd(String name) {
		Player player = new Player(name);
		nameMap.put(name, player);
		players.add(player);
		mAdapter.notifyDataSetChanged();
	}

	public void playerLeave(String name) {
		players.remove(nameMap.remove(name));
		mAdapter.notifyDataSetChanged();
	}

	public void playerFailed(String name) {
		mAdapter.notifyDataSetChanged();
	}

	public void playMakeChoice(String name, int value) {
		nameMap.get(name).value = value;
		mAdapter.notifyDataSetChanged();
	}

	public void playerReady(String name) {
		nameMap.get(name).status = Player.READY;
		mAdapter.notifyDataSetChanged();
	}

	public class Player {
		public Player(String name) {
			this.name = name;
		}

		public Player(String name, int value) {
			this.name = name;
			this.value = value;
		}

		public String name = "";
		public int value = 0;
		public int status = NOT_READY;

		public final static int NOT_READY = 0;
		public final static int READY = 1;
	}

}
