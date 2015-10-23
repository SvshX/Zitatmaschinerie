package com.melcher.zitatmaschinerie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuListAdapter extends BaseAdapter {
	private Context context;
	private String[] mTitle;
	private LayoutInflater inflater;

	public MenuListAdapter(Context pContext, String[] pTitle,
			String[] pSubtitle, int[] pIcon) {
		context = pContext;
		mTitle = pTitle;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.drawer_list_item, parent,
				false);

		TextView txtTitle = (TextView) itemView.findViewById(R.id.title);

		txtTitle.setText(mTitle[position]);

		return itemView;
	}

	@Override
	public int getCount() {
		return mTitle.length;
	}

	@Override
	public Object getItem(int position) {
		return mTitle[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}