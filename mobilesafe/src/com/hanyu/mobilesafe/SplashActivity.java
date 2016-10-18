package com.hanyu.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.params.HttpAbstractParamBean;
import org.xmlpull.v1.XmlPullParserException;

import com.hanyu.mobilesafe.domain.UpdateInfo;
import com.hanyu.mobilesafe.engine.UpdateInfoParser;
import com.hanyu.mobilesafe.utils.DownLoadUtil;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private TextView mTvSpalshVersion;
	
	private RelativeLayout mRlSplash;
	
	private static final int GET_INFO_SUCCESS = 10;
	private static final int SERVER_ERROR = 11;
	private static final int SERVER_URL_ERROR = 12;
	private static final int PROTOCOL_ERROR = 13;
	private static final int IO_ERROR = 14;
	private static final int XML_PARSE_ERROR = 15;
	private static final int DOWNLOAD_SUCCESS = 16;
	private static final int DOWNLOAD_ERROR = 17;
	
	private long startTime;

	private long endTime;
	
	private UpdateInfo info;
	
	private static final String TAG = "SplashActivity";
	
	private ProgressDialog pd;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case XML_PARSE_ERROR:
				Toast.makeText(getApplicationContext(), "xml解析错误", 1).show();
			case IO_ERROR:
				Toast.makeText(getApplicationContext(), "I/O错误", 1).show();
				loadMainUI();
				break;
			case PROTOCOL_ERROR:
				Toast.makeText(getApplicationContext(), "协议不支持", 1).show();
				loadMainUI();
				break;
			case SERVER_URL_ERROR:
				Toast.makeText(getApplicationContext(), "服务器路径不正确", 1).show();
				loadMainUI();
				break;
			case SERVER_ERROR:
				Toast.makeText(getApplicationContext(), "服务器内部异常", 1).show();
				loadMainUI();
				break;
			case GET_INFO_SUCCESS:
				String serverVersion = info.getVersion();
				String currenntVersion = getVersion();
				if (currenntVersion.equals(serverVersion))
				{
					Log.d(TAG, "版本号相同，进入主页面");
					loadMainUI();
				}
				else {
					Log.d(TAG, "版本号不相同");
					showUpdateDialoog();
				}
				break;
			case DOWNLOAD_SUCCESS:
				Log.i(TAG, "文件下载成功");
				File file = (File) msg.obj;
				installApk(file);
				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "下载数据异常", 1).show();
				loadMainUI();
				break;
			}
		}
	};
	
	/**
	 * 安装一个apk文件
	 * 
	 * @param file
	 *            要安装的完整文件名
	 */
	protected void installApk(File file) {
		// 隐式意图
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");// 设置意图的动作
		intent.addCategory("android.intent.category.DEFAULT");// 为意图添加额外的数据
		// intent.setType("application/vnd.android.package-archive");
		// intent.setData(Uri.fromFile(file));
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");// 设置意图的数据与类型
		startActivity(intent);// 激活该意图
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		mTvSpalshVersion = (TextView) findViewById(R.id.tv_splash_version);
		mTvSpalshVersion.setText("veision:" + getVersion());
		
		mRlSplash = (RelativeLayout) findViewById(R.id.rl_splash);
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		mRlSplash.setAnimation(aa);
		new Thread(new CheckVersionTask()).start();
	}

	private String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}	
	}
	
	
	private class CheckVersionTask implements Runnable
	{
		public void run() {
			startTime = System.currentTimeMillis();
			Message msg = Message.obtain();
			String serverurl = getResources().getString(R.string.serverurl);
			try {
				URL url = new URL(serverurl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(5000);
				int code = connection.getResponseCode();
				
				if (code == 200)
				{
					Log.d(TAG, "code=200");
					InputStream is = connection.getInputStream();
					info = UpdateInfoParser.getUpdateInfo(is);
					endTime = System.currentTimeMillis();
					long resultTime = endTime- startTime;
					if (resultTime < 2000)
					{
						try {
							Thread.sleep(2000 - resultTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					msg.what = GET_INFO_SUCCESS;
					handler.sendMessage(msg);
				}
				else 
				{
					Log.d(TAG, "code=error");
					msg.what = SERVER_ERROR;
					handler.sendMessage(msg);
					endTime = System.currentTimeMillis();
					long resultTime = endTime- startTime;
					if (resultTime < 2000)
					{
						try {
							Thread.sleep(2000 - resultTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
				msg.what = SERVER_URL_ERROR;
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				msg.what = IO_ERROR;
				handler.sendMessage(msg);
			} catch (XmlPullParserException e1) {
				e1.printStackTrace();
				msg.what = XML_PARSE_ERROR;
				handler.sendMessage(msg);
			}
		}		
	}

	protected void showUpdateDialoog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(getResources().getDrawable(R.drawable.notification));
		builder.setTitle("升级提示");
		builder.setMessage(info.getDescription());
		
		pd = new ProgressDialog(this);
		pd.setMessage("正在下载");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
				{
					pd.show();
					new Thread(){
						public void run() {
							String path = info.getApkurl();
							String filename = DownLoadUtil.getFilename(path);
							File file = new File(Environment.getExternalStorageDirectory(), filename);
							file = DownLoadUtil.getFile(path, file.getAbsolutePath(), pd);			
							if (file != null)
							{
								Message msg = Message.obtain();
								msg.what = DOWNLOAD_SUCCESS;
								msg.obj = file;
								handler.sendMessage(msg);
							}
							else
							{
								Message msg = Message.obtain();
								msg.what = DOWNLOAD_ERROR;
								handler.sendMessage(msg);
							}	
							pd.dismiss();
						};
					}.start();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "sd卡不可用", 1).show();
					loadMainUI();// 进入程序主界面
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {		
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}
		});
		builder.create().show();
	}
	/**
	 * 加载主界面
	 */
	private void loadMainUI() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();// 把当前的Activity从任务栈里面移除
	}
}
