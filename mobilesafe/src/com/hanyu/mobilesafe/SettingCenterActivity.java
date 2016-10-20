package com.hanyu.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.text.Editable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SettingCenterActivity extends Activity {
	
	private SharedPreferences sp;

	private CheckBox cbSetAutoUpdateBox;

	private TextView tvSetAutoUpdateStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_center);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		cbSetAutoUpdateBox = (CheckBox) findViewById(R.id.cb_set_autoupdate);
		tvSetAutoUpdateStatus = (TextView) findViewById(R.id.tv_set_autoupdate_status);
		
		boolean update = sp.getBoolean("autoupdate", true);
		if (update)
		{
			tvSetAutoUpdateStatus.setText("自动更新已经开启");
			cbSetAutoUpdateBox.setChecked(true);			
		}
		else
		{
			tvSetAutoUpdateStatus.setText("自动更新已关闭");
			cbSetAutoUpdateBox.setChecked(false);						
		}		
		cbSetAutoUpdateBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor editor = sp.edit();
				editor.putBoolean("autoupdate", isChecked);
				editor.commit();
				if (isChecked)
				{
					tvSetAutoUpdateStatus.setText("自动更新已经开启");
					cbSetAutoUpdateBox.setChecked(true);			
				}
				else
				{
					tvSetAutoUpdateStatus.setText("自动更新已关闭");
					cbSetAutoUpdateBox.setChecked(false);						
				}		

			}
		});
	}
	
	
}
