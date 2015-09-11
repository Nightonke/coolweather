package com.coolweather.app.activity;

import java.util.ArrayList;

import com.coolweather.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NListViewAdatper extends BaseAdapter {

	private ArrayList<String> list = null;
	private Context context = null;
	
	public NListViewAdatper(ArrayList<String> list, Context context) {
		this.list = list;
		this.context = context;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (list == null) {
			return null;
		} else {
			return list.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(this.context);
		View view = layoutInflater.inflate(R.layout.list_item, null);
		TextView textView = (TextView)view.findViewById(R.id.text_view);
		textView.setText(getItem(position).toString());
		return view;
		
	}

	
	
}
