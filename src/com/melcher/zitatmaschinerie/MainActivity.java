package com.melcher.zitatmaschinerie;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	SQLAdapter sqladapter;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(
				getLayoutInflater().inflate(R.layout.action_bar_title, null),
				new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
						ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

		FontHelper.applyFont(this, findViewById(R.id.main_layout),
				"fonts/Helvetica-Neue-25-Ultra-Light.ttf");

		sqladapter = new SQLAdapter(this);
		sqladapter.open();

		tv = (TextView) findViewById(R.id.home);
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Dosis-ExtraLight.ttf");
		tv.setTypeface(face);

		StateListDrawable slDraw = new StateListDrawable();
		slDraw.addState(new int[] { android.R.attr.state_focused },
				getResources().getDrawable(R.drawable.ghost_button_focused));
		slDraw.addState(new int[] { android.R.attr.state_selected },
				getResources().getDrawable(R.drawable.ghost_button_pressed));

		slDraw.addState(new int[] { android.R.attr.state_pressed },
				getResources().getDrawable(R.drawable.ghost_button_pressed));

		slDraw.addState(new int[] {},
				getResources().getDrawable(R.drawable.ghost_button));
		Button btn_myList = (Button) findViewById(R.id.btn_myList);
		btn_myList.setBackgroundDrawable(slDraw);
	}

	public void onButtonClick(View view) {

		switch (view.getId()) {
		case R.id.btn_show:
			startActivity(new Intent(this, ShowQuotesActivity.class));
			break;

		case R.id.btn_create:

			startActivity(new Intent(this, CreateQuoteActivity.class));
			break;

		case R.id.btn_myList:
			startActivity(new Intent(this, MyEntriesActivity.class));
			break;

		case R.id.btn_quoteoftheday:
			startActivity(new Intent(this, QuoteOfTheDayActivity.class));
			break;

		}
	}

}
