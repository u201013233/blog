package com.hanyu.mobilesafe.adapter;

import com.hanyu.mobilesafe.R;

import android.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter{

	private static final int[] icons = {
		R.drawable.widget01,
		R.drawable.widget02,
		R.drawable.widget03,
		R.drawable.widget04,
		R.drawable.widget05,
		R.drawable.widget06,
		R.drawable.widget07,
		R.drawable.widget08,
		R.drawable.widget09,
	};
	private static final String[] names = {
		"手机防盗",
		"通信卫士",
		"软件管家",
		"进程管理",
		"流量统计",
		"手机杀毒",
		"系统优化",
		"高级工具",
		"设置中心"
	};
	
	private Context context;
	
	private LayoutInflater inflater;
	
	public MainAdapter(Context context)
	{
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return names.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView ==  null)
			view = inflater.inflate(R.layout.main_item, null);
		else
			view = convertView;
		TextView tv_name = (TextView) view.findViewById(R.id.tv_main_item);
		ImageView iv_name = (ImageView) view.findViewById(R.id.iv_main_item);
		tv_name.setText(names[position]);
		iv_name.setImageResource(icons[position]);
		
		return view;
	}

}
