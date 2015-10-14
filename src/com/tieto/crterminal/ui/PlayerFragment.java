package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.R.layout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
	 
	
	
	

}
