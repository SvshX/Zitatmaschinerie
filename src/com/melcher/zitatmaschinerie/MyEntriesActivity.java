package com.melcher.zitatmaschinerie;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MyEntriesActivity extends ListActivity {

	SQLAdapter sqladapter;

	private ListView lv;
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
				.setText("Meine Zitate");

		setContentView(R.layout.activity_mylist);
		FontHelper.applyFont(this, findViewById(R.id.entry_layout),
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

		// Fügt den Navigation Drawer zur ActionBar hinzu
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		sqladapter = new SQLAdapter(this);
		sqladapter.open();

		lv = (ListView) findViewById(android.R.id.list);

		displayQuote();
	}

	private void displayQuote() {
		Cursor c = sqladapter.readData();
		String[] from = new String[] { DBHelper.QUOTE_ID, DBHelper.SPEC_QUOTE };
		int[] to = new int[] { R.id.qu_id, R.id.qu_name };

		/*
		 * SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		 * R.layout.view_quote_entry, c, from, to);
		 * adapter.notifyDataSetChanged(); lv.setAdapter(adapter);
		 */

		final MyCursorAdapter adapter = new MyCursorAdapter(this,
				R.layout.view_quote_entry, c, from, to, 0);

		setListAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Cursor c = (Cursor) lv.getItemAtPosition(position);
				int quoteIndex = c.getColumnIndex(DBHelper.SPEC_QUOTE);
				int idIndex = c.getColumnIndex(DBHelper.QUOTE_ID);
				String pickedItem = c.getString(quoteIndex);
				String pickedID = c.getString(idIndex);

				Intent intent = new Intent(MyEntriesActivity.this,
						SingleQuoteActivity.class);
				intent.putExtra("ListEntry_Quote", pickedItem);
				intent.putExtra("ListEntry_Id", pickedID);
				view.setSelected(true);
				startActivity(intent);

			}

		});
	}

	// Öffnet und schließt den Navigation Drawer bei Klick auf den Titel/das
	// Icon in der ActionBar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Listener für die Navigation Drawer Einträge - Achtung: Zählung beginnt
	// bei 0!
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0) {

				startActivity(new Intent(MyEntriesActivity.this,
						ShowQuotesActivity.class));
			} else if (position == 1) {
				startActivity(new Intent(MyEntriesActivity.this,
						CreateQuoteActivity.class));
			} else if (position == 2) {
				startActivity(new Intent(MyEntriesActivity.this,
						QuoteOfTheDayActivity.class));
			} else if (position == 3) {
				mDrawerLayout.closeDrawer(mDrawerList);
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

	private class MyCursorAdapter extends SimpleCursorAdapter {

		public MyCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// get reference to the row
			View view = super.getView(position, convertView, parent);
			// check for odd or even to set alternate colors to the row
			// background
			if (position % 2 == 0) {
				view.setBackgroundColor(getResources().getColor(
						R.drawable.actionbar_bg));
			} else {
				view.setBackgroundColor(getResources().getColor(
						R.drawable.subtle_bluewhite));
			}

			Typeface tf = Typeface.createFromAsset(getAssets(),
					"fonts/Helvetica-Neue-25-Ultra-Light.ttf");
			// TextView v = (TextView) super.getView(position, convertView,
			// parent);
			TextView v = (TextView) view.findViewById(R.id.qu_name);
			v.setTypeface(tf);
			v.setTextColor(getResources().getColor(R.drawable.grey_color));
			v.setTextSize(20);

			return view;
		}

	}

}
