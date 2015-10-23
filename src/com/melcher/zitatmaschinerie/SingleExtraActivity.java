package com.melcher.zitatmaschinerie;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SingleExtraActivity extends Activity {

	private TextView tv;
	SQLAdapter sqladapter;
	
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
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setCustomView(
				getLayoutInflater().inflate(R.layout.action_bar_title, null),
				new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
						ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

		((TextView) actionBar.getCustomView().findViewById(R.id.ab_title))
				.setText("Zitat");

		setContentView(R.layout.activity_singleextra_old);
		FontHelper.applyFont(this, findViewById(R.id.extra_layout),
				"fonts/Helvetica-Neue-25-Ultra-Light.ttf");
		
	/*	Typeface tf = Typeface.createFromAsset(getAssets(),
		        "fonts/Helvetica-Neue-25-Ultra-Light.ttf");
		TextView menutitle = (TextView) findViewById(R.id.title);
		menutitle.setTypeface(tf);     */
		
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
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		sqladapter = new SQLAdapter(this);
		sqladapter.open();

		tv = (TextView) findViewById(R.id.extra_authorquote);

		Intent intent = getIntent();

		String quoteName = intent.getStringExtra("ListEntry_Quote");

		tv.setText("\"" + quoteName + "\"");
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Chantelli_Antiqua.ttf");
		tv.setTypeface(face);
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
					startActivity(new Intent(SingleExtraActivity.this,
							ShowQuotesActivity.class));
				} else if (position == 1) {
					startActivity(new Intent(SingleExtraActivity.this,
							CreateQuoteActivity.class));
				} else if (position == 2) {
					startActivity(new Intent(SingleExtraActivity.this,
							QuoteOfTheDayActivity.class));
				} else if (position == 3) {
					startActivity(new Intent(SingleExtraActivity.this,
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
		
}
