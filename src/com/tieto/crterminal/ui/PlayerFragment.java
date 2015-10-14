package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.R.layout;
import com.tieto.crterminal.model.command.JsonCommadConstant;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayerFragment extends Fragment {


	private Context mActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		Intent intent = new Intent();
		intent.setAction(GamePadFragment.GAME_READY_ACTION);
		mActivity.sendBroadcast(intent);
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
        View view = inflater.inflate(R.layout.fragment_all_player, container, false);  
        
        
        
        
        
        return view;  
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

	
	public Handler getHandler(){
		
		return mGamePlayerHandler;
	}
	
	
	public void playerAdd(String name){
		
	}
	
	public void playerLeave(String name){
		
	}
	
	public void playerFailed(String name){
		
	}
	
	public void playMakeChoice(String name){
		
	}
	
	
	

}
