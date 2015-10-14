package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;

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

public class GamePadFragment extends Fragment implements View.OnClickListener{

	private Button btnStart;
	private Button btnStop;
	private Button btnConfirm;
	private ImageButton btnPaper;
	private ImageButton btnRock;
	private ImageButton btnScissors;
	private boolean mIsOwner;
	
	public static String GAME_READY_ACTION = "com.tieto.crterminal.game_ready";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(GAME_READY_ACTION);
		GameReadyBroadcastReceiver gameReadyBroadcastReceiver = new GameReadyBroadcastReceiver();
		getActivity().registerReceiver(gameReadyBroadcastReceiver, intentFilter);
	}

    class GameReadyBroadcastReceiver extends BroadcastReceiver{

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
            Bundle savedInstanceState)  
    {  
        View view = inflater.inflate(R.layout.fragment_gamepad, container, false);  
       
        
        btnStart = (Button) view.findViewById(R.id.start_Btn);
        btnStop = (Button) view.findViewById(R.id.stop_Btn);
        btnConfirm = (Button) view.findViewById(R.id.confirm_Btn);
        btnPaper = (ImageButton) view.findViewById(R.id.gamepad_paper);
        btnRock = (ImageButton) view.findViewById(R.id.gamepad_rock);
        btnScissors = (ImageButton) view.findViewById(R.id.gamepad_scissors);
        
        if (!mIsOwner) {
        	btnStart.setVisibility(View.GONE);
        	btnPaper.setVisibility(View.INVISIBLE);
        	btnRock.setVisibility(View.INVISIBLE);
        	btnScissors.setVisibility(View.INVISIBLE);
        }
        
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnPaper.setOnClickListener(this);
        btnRock.setOnClickListener(this);
        btnScissors.setOnClickListener(this);
        
        return view;  
    } 
	
	
	 
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mIsOwner = activity.getIntent().getBooleanExtra("OWNER", false);
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent;
		switch(id){
		case R.id.btn_host:
			//TODO: start the game
			break;
		case R.id.stop_Btn:
			//TODO: stop the game
			break;
		case R.id.confirm_Btn:
			
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
	
	private void btnPaperOnClick(){
		//TODO: update UI event
	}
	
	private void btnRockOnClick(){
		//TODO: update UI event
	}
	
	private void btnScissorsOnClick(){
		//TODO: update UI event
	}

}
