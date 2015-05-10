package com.ims.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ims.R;
import com.ims.entity.Ask;

public class AskAdapter extends BaseAdapter{
	
	private List<Ask> asks;
	private Context ctx;
	 private LayoutInflater mInflater;
	
	  public AskAdapter(Context context, List<Ask> asks) {
	        ctx = context;
	        this.asks = asks;
	        mInflater = LayoutInflater.from(ctx);
	    }
	
	  public AskAdapter(Context context) {
	        ctx = context;
	        this.asks = new ArrayList<Ask>();
	        mInflater = LayoutInflater.from(ctx);
	    }
	  
	@Override
	public int getCount() {
		return asks.size();
	}

	@Override
	public Object getItem(int position) {
		return asks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Ask entity = asks.get(position);
		convertView = mInflater.inflate(R.layout.item_ask, null);
		TextView pro = (TextView) convertView.findViewById(R.id.item_ask_tv_ask);
		pro.setText(entity.getPor());
	    return convertView;
	}
	
	public void addAsk(Ask ask){
		asks.add(ask);
	}

}
