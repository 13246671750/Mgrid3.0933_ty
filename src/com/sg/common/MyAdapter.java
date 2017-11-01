package com.sg.common;

import java.util.ArrayList;

import com.mgrid.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	private  LayoutInflater inflater;
	private  ArrayList<String> list;
	
	public MyAdapter(Context context,ArrayList<String> list){
		
		inflater=LayoutInflater.from(context);
		this.list=list;
		
	}
	
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.lv_items, null);
		}
		TextView text=(TextView) convertView.findViewById(R.id.text);
		text.setText(list.get(position));
		
		return convertView;
	}

}
