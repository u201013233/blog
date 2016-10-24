package com.hanyu.mobilesafe;

import com.hanyu.mobilesafe.utils.Md5Encoder;

import android.animation.AnimatorSet.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LostProtectedActivity extends Activity implements View.OnClickListener{

	private SharedPreferences sp;

	private EditText et_first_dialog_pwd;

	private EditText et_first_dialog_pwd_confirm;
	private Button bt_first_dialog_ok;	
	private Button bt_first_dialog_cancel;	
	
	private EditText et_normal_dialog_pwd;	
	private Button bt_normal_dialog_ok;	
	private Button bt_normal_dialog_cancel;	

	private AlertDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		if (setUpPwd())
		{
			showNormalEntryDialog();
		}
		else
		{
			showFirstEntryDialog();
		}
	}
	
	private void showNormalEntryDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.normal_entry_dialog, null);
		
		et_normal_dialog_pwd = (EditText) view.findViewById(R.id.et_normal_dialog_pwd); 
		bt_normal_dialog_ok = (Button) view.findViewById(R.id.bt_normal_dialog_ok);
		bt_normal_dialog_cancel = (Button) view.findViewById(R.id.bt_normal_dialog_cancle);
		bt_normal_dialog_ok.setOnClickListener(this);
		bt_normal_dialog_cancel.setOnClickListener(this);
		
		builder.setView(view);
		dialog = builder.create();
		dialog.show();	
		
	}

	/**
	 * 第一次防盗的对话框
	 */
	private void showFirstEntryDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.first_entry_dialog, null);
		
		et_first_dialog_pwd = (EditText) view.findViewById(R.id.et_first_dialog_pwd); 
		et_first_dialog_pwd_confirm = (EditText) view.findViewById(R.id.et_first_dialog_pwd_confirm); 
		bt_first_dialog_ok = (Button) view.findViewById(R.id.bt_first_dialog_ok);
		bt_first_dialog_cancel = (Button) view.findViewById(R.id.bt_first_dialog_cancel);
		bt_first_dialog_ok.setOnClickListener(this);
		bt_first_dialog_cancel.setOnClickListener(this);
		
		builder.setView(view);
		dialog = builder.create();
		dialog.show();	
	}


	private boolean setUpPwd() {
		String pwd = sp.getString("password", "");
		if (TextUtils.isEmpty(pwd))
			return false;
		else 
			return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_first_dialog_ok:
			String pwd = et_first_dialog_pwd.getText().toString().trim();
			String pwdConfirm = et_first_dialog_pwd_confirm.getText().toString().trim();
			if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdConfirm))
			{
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (pwd.equals(pwdConfirm))
			{
				Editor editor = sp.edit();
				editor.putString("password", Md5Encoder.encode(pwd));
				editor.commit();
				dialog.dismiss();
				finish();
			}
			else {
				Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
				return;				
			}
			break;
		case R.id.bt_first_dialog_cancel:
			dialog.cancel();
			finish();
			break;
		case R.id.bt_normal_dialog_ok:
			String userPwd = et_normal_dialog_pwd.getText().toString().trim();			
			if (TextUtils.isEmpty(userPwd))
			{
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			String savePwd =  sp.getString("password", "");
			if (Md5Encoder.encode(userPwd).equals(savePwd))
			{
				Toast.makeText(this, "密码正确", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				return;				
			}
			else
			{
				Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT).show();
				return;				
			}
		case R.id.bt_normal_dialog_cancle:
			dialog.cancel();
			finish();			
			break;
		default:
			break;
		}
	}
	

}
