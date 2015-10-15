package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;

public class StartupActivity extends Activity implements View.OnClickListener{

	private Button btnHost;
	private Button btnGuest;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		
		btnHost = (Button) findViewById(R.id.btn_host);
		btnGuest = (Button) findViewById(R.id.btn_guest);

		btnHost.setOnClickListener(this);
		btnGuest.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent;
		switch(id){
		case R.id.btn_host:
			intent = new Intent(this,HostGameActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_guest:
			intent = new Intent(this,GuestGameActivity.class);
			startActivity(intent);
			break;

		}
		
	}
}
