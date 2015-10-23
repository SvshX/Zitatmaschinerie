package com.melcher.zitatmaschinerie;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

	private final Context context;
	  private final ArrayList<String> values;
	  private Typeface tf;
	  
	  public CustomListAdapter(Context context, ArrayList<String> values, Typeface tf) {
		    super(context, R.layout.view_quote_entry, values);
		    this.context = context;
		    this.values = values;
		    this.tf = tf;
		  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.view_quote_entry, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.qu_name);
	    textView.setText((CharSequence) values);
	    tf = Typeface.createFromAsset(context.getAssets(),"fonts/Helvetica-Neue-25-Ultra-Light.ttf");
	    textView = (TextView) super.getView(position, convertView, parent);
	    textView.setTypeface(tf);
	    textView.setTextColor(context.getResources().getColor(R.drawable.grey_color));
	    textView.setTextSize(18);

	    return rowView;
	  }
	  
}
