package com.hanyu.mobilesafe;

import com.hanyu.mobilesafe.adapter.MainAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridLayout;
import android.widget.GridView;

public class MainActivity extends Activity {
	
	private GridView gvMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		gvMain = (GridView) findViewById(R.id.gv_main);
		gvMain.setAdapter(new MainAdapter(this));	
		gvMain.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 8:
					Intent intent = new Intent(MainActivity.this, SettingCenterActivity.class);
					startActivity(intent);					
					break;
				default:
					break;
				}
			}
			
		});
	}

}
