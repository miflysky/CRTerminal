package com.tieto.crterminal.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.player.JanKenPonValue;

public class GamePadFragment extends Fragment implements View.OnClickListener {

	private Button btnStart;
	private Button btnStop;
	private Button btnConfirm;
	private ImageButton btnPaper;
	private ImageButton btnRock;
	private ImageButton btnScissors;

	private BaseGameActivity mActivity;
	private Fragment mPlayerFragment;
	
	private int mJanKenPonValue = 0;

	public static String GAME_READY_ACTION = "com.tieto.crterminal.game_ready";

	GameReadyBroadcastReceiver mGameReadyBroadcastReceiver;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mActivity = (BaseGameActivity)getActivity();
		mPlayerFragment = mActivity.getFragmentManager().findFragmentById(
				R.id.main_fragment);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(GAME_READY_ACTION);
		mGameReadyBroadcastReceiver = new GameReadyBroadcastReceiver();
		getActivity().registerReceiver(mGameReadyBroadcastReceiver, intentFilter);

	}

	
	@Override
	public void onDestroy(){
		
		super.onDestroy();
		getActivity().unregisterReceiver(mGameReadyBroadcastReceiver);
		
	}
	
	
	class GameReadyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(GAME_READY_ACTION)) {

				btnPaper.setVisibility(View.VISIBLE);
				btnRock.setVisibility(View.VISIBLE);
				btnScissors.setVisibility(View.VISIBLE);
			}
		}

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gamepad, container,
				false);

		initView(view, BaseGameActivity.isGameHost());

		return view;
	}
	
	private void initView(View view, boolean isHost) {
		
		btnStart = (Button) view.findViewById(R.id.start_Btn);
		btnStop = (Button) view.findViewById(R.id.stop_Btn);
		btnConfirm = (Button) view.findViewById(R.id.confirm_Btn);
		btnPaper = (ImageButton) view.findViewById(R.id.gamepad_paper);
		btnRock = (ImageButton) view.findViewById(R.id.gamepad_rock);
		btnScissors = (ImageButton) view.findViewById(R.id.gamepad_scissors);
		
		btnPaper.setVisibility(View.INVISIBLE);
		btnRock.setVisibility(View.INVISIBLE);
		btnScissors.setVisibility(View.INVISIBLE);

		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);

		btnConfirm.setOnClickListener(this);
		btnPaper.setOnClickListener(this);
		btnRock.setOnClickListener(this);
		btnScissors.setOnClickListener(this);
		
		if (!isHost) {
			btnStart.setVisibility(View.GONE);
		}
			
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		//Intent intent;
		switch (id) {
		case R.id.btn_host:
			// TODO: start the game
			// send broadcast

			break;
		case R.id.stop_Btn:
			getActivity().finish();
			break;
		case R.id.confirm_Btn:
			btnConfirmOnClick();
			break;
		case R.id.gamepad_paper:
			btnPaperOnClick();
			break;
		case R.id.gamepad_rock:
			btnRockOnClick();
			break;
		case R.id.gamepad_scissors:
			btnScissorsOnClick();
			break;

		}

	}

	private void btnConfirmOnClick() {
		if (mJanKenPonValue != 0) {
			mActivity.isReady = true;
			if (mPlayerFragment instanceof PlayerFragment) {
				((PlayerFragment) mPlayerFragment).confirmChoose();
				if (BaseGameActivity.isGameHost()) {
					((GuestGameActivity)getActivity()).mGuestPlayer.sendJanKenPonValue(mJanKenPonValue);
				} else {
					((HostGameActivity)getActivity()).mHostPlayer.sendJanKenPonValue(mJanKenPonValue);
				}
			}
		}
	}

	private void btnPaperOnClick() {
		setMyChoice(JanKenPonValue.Paper);
	}

	private void btnRockOnClick() {
		setMyChoice(JanKenPonValue.Rock);
	}

	private void btnScissorsOnClick() {
		setMyChoice(JanKenPonValue.Scissors);
	}

	private void setMyChoice(int value) {
		
		if (mPlayerFragment instanceof PlayerFragment) {
			((PlayerFragment) mPlayerFragment).playMakeChoice(mActivity.getUserName(),
					value);
		}
		mJanKenPonValue = value;
	}
}
