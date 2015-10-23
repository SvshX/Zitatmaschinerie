package com.melcher.zitatmaschinerie;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ShowQuotesActivity extends Activity {

	SQLAdapter sqladapter;
	private Spinner spinner;
	int check = 0;
	Typeface tf;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private String[] drawerTitles;
	private String[] drawerSubtitles;
	private int[] drawerIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setCustomView(
				getLayoutInflater().inflate(R.layout.action_bar_title, null),
				new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
						ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

		((TextView) actionBar.getCustomView().findViewById(R.id.ab_title))
				.setText("Zitatauswahl");

		setContentView(R.layout.activity_show);

		FontHelper.applyFont(this, findViewById(R.id.show_layout),
				"fonts/Helvetica-Neue-25-Ultra-Light.ttf");

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Hole die Titel aus einem Array aus der strings.xml
		drawerTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// Erstellt den neuen MenuAdapter aus der Klasse MenuListAdapter
		MenuListAdapter mMenuAdapter = new MenuListAdapter(this, drawerTitles,
				drawerSubtitles, drawerIcons);
		mDrawerList.setAdapter(mMenuAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// F�gt den Navigation Drawer zur ActionBar hinzu
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		clickOnItem();
		// spinnermeth();

	}

	// �ffnet und schlie�t den Navigation Drawer bei Klick auf den Titel/das
	// Icon in der ActionBar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Listener f�r die Navigation Drawer Eintr�ge - Achtung: Z�hlung beginnt
	// bei 0!
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else if (position == 1) {
				startActivity(new Intent(ShowQuotesActivity.this,
						CreateQuoteActivity.class));
			} else if (position == 2) {
				startActivity(new Intent(ShowQuotesActivity.this,
						QuoteOfTheDayActivity.class));
			} else if (position == 3) {
				startActivity(new Intent(ShowQuotesActivity.this,
						MyEntriesActivity.class));
			}

			mDrawerList.setItemChecked(position, true);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public <ViewGroup> void clickOnItem() {
		sqladapter = new SQLAdapter(this);
		sqladapter.open();

		spinner = (Spinner) findViewById(R.id.spinner);

		// Loading spinner data from database
		Cursor c = sqladapter.loadSpinnerData();

		String[] from = new String[] { DBHelper.AUTHOR };
		int[] to = new int[] { android.R.id.text1 };

		SimpleCursorAdapter spinadapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, c, from, to) {

			public View getView(int position, View convertView,
					android.view.ViewGroup parent) {
				tf = Typeface.createFromAsset(getAssets(),
						"fonts/Helvetica-Neue-25-Ultra-Light.ttf");
				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(tf);
				v.setTextColor(getResources().getColor(R.drawable.grey_color));
				v.setTextSize(20);
				return v;
			}

			public View getDropDownView(int position, View convertView,
					android.view.ViewGroup parent) {
				tf = Typeface.createFromAsset(getAssets(),
						"fonts/Helvetica-Neue-25-Ultra-Light.ttf");
				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(tf);
				v.setTextColor(getResources().getColor(R.drawable.grey_color));
				v.setTextSize(20);
				return v;
			}
		};
		spinadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(new NothingSelectedSpinnerAdapter(spinadapter,
				R.layout.spinner_nothing_selected, this) {
			public View getView(int position, View convertView,
					android.view.ViewGroup parent) {
				tf = Typeface.createFromAsset(getAssets(),
						"fonts/Helvetica-Neue-25-Ultra-Light.ttf");
				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(tf);
				v.setTextColor(getResources().getColor(R.drawable.grey_color));
				v.setTextSize(18);
				return v;
			}
		});

		FontHelper.applyFont(this, findViewById(android.R.id.text1),
				"fonts/Helvetica-Neue-25-Ultra-Light.ttf");

		spinner.setOnItemSelectedListener(new OnClickSpinnerItem());

	}

	public class OnClickSpinnerItem implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			check = check + 1;
			if (check > 1) {

				Intent spinner_intent = new Intent(ShowQuotesActivity.this,
						AuthorQuotesSummaryActivity.class);
				spinner_intent.putExtra("NameofAuthor",
						((TextView) findViewById(android.R.id.text1)).getText()
								.toString());
				startActivity(spinner_intent);

			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	public void onButtonClick(View view) {

		startActivity(new Intent(this, RandomQuoteActivity.class));

	}

}
