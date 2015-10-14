package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GamePadFragment extends Fragment implements View.OnClickListener{

	private Button btnStart;
	private Button btnStop;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
        View view = inflater.inflate(R.layout.fragment_gamepad, container, false);  
       
        
        btnStart = (Button) view.findViewById(R.id.start_Btn);
        btnStop = (Button) view.findViewById(R.id.stop_Btn);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        
        
        return view;  
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

		}
		
	}

}
