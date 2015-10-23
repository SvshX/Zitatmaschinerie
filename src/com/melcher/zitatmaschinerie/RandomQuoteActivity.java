package com.melcher.zitatmaschinerie;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
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

public class RandomQuoteActivity extends Activity implements
		SensorEventListener {

	private final float SHAKE_THRESHOLD_VALUE = 3.5f;

	private final int X_AXIS = 0;
	private final int Y_AXIS = 1;
	private final int Z_AXIS = 2;

	private boolean restarted = true;
	private boolean moved = false;
	private boolean shaked = false;

	private float xfirstAcceleration;
	private float yfirstAcceleration;
	private float zfirstAcceleration;

	private float xlastAcceleration;
	private float ylastAcceleration;
	private float zlastAcceleration;

	SQLAdapter sqladapter;
	private TextView tv, tv2;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private String[] drawerTitles;
	private String[] drawerSubtitles;
	private int[] drawerIcons;

	private SensorManager sensorManager;

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
				.setText("Zufallszitat");

		setContentView(R.layout.activity_randomquote);
		FontHelper.applyFont(this, findViewById(R.id.random_layout),
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
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		sqladapter = new SQLAdapter(this);
		sqladapter.open();

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		createRandomQuote();

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
				startActivity(new Intent(RandomQuoteActivity.this,
						ShowQuotesActivity.class));
			} else if (position == 1) {
				startActivity(new Intent(RandomQuoteActivity.this,
						CreateQuoteActivity.class));
			} else if (position == 2) {
				startActivity(new Intent(RandomQuoteActivity.this,
						QuoteOfTheDayActivity.class));
			} else if (position == 3) {
				startActivity(new Intent(RandomQuoteActivity.this,
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

	private void createRandomQuote() {

		Cursor c = sqladapter.getRandomQuote();

		tv = (TextView) findViewById(R.id.authorquote);
		tv2 = (TextView) findViewById(R.id.authorname);

		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Chantelli_Antiqua.ttf");
		tv.setTypeface(face);

		tv.setText("\"" + c.getString(2) + "\"");

		tv2.setText(c.getString(1));
		c.close();
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void onButtonClick(View view) {

		switch (view.getId()) {
		case R.id.btn_furtherquotes:

			Intent summary_intent = new Intent(RandomQuoteActivity.this,
					AuthorQuotesSummaryActivity.class);
			summary_intent.putExtra("NameofAuthor",
					((TextView) findViewById(R.id.authorname)).getText()
							.toString());
			startActivity(summary_intent);
			break;

		case R.id.btn_infoauthor:
			String authorName = tv2.getText().toString();
			Cursor c = sqladapter.readWiki(authorName);

			Intent info_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(c
					.getString(1)));
			startActivity(info_intent);
			break;

		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		updateAccelerationValues(event.values[X_AXIS], event.values[Y_AXIS],
				event.values[Z_AXIS]);

		moved = moveRecognised();

		if (moved && !shaked) {

			shaked = true;
		} else if (moved && shaked) {

			createRandomQuote();

		}

		else if (!moved && shaked) {

			shaked = false;
		}
	}

	protected void updateAccelerationValues(float xcurrentAcceleration,
			float ycurrentAcceleration, float zcurrentAcceleration) {

		if (restarted) {

			xfirstAcceleration = xcurrentAcceleration;
			yfirstAcceleration = ycurrentAcceleration;
			zfirstAcceleration = zcurrentAcceleration;

			restarted = false;
		} else {
			xfirstAcceleration = xlastAcceleration;
			yfirstAcceleration = ylastAcceleration;
			zfirstAcceleration = zlastAcceleration;

		}

		xlastAcceleration = xcurrentAcceleration;
		ylastAcceleration = ycurrentAcceleration;
		zlastAcceleration = zcurrentAcceleration;

	}

	protected boolean moveRecognised() {

		final float xDifference = Math.abs(xfirstAcceleration
				- xlastAcceleration);
		final float yDifference = Math.abs(yfirstAcceleration
				- ylastAcceleration);
		final float zDifference = Math.abs(zfirstAcceleration
				- zlastAcceleration);

		return (xDifference > SHAKE_THRESHOLD_VALUE
				|| yDifference > SHAKE_THRESHOLD_VALUE || zDifference > SHAKE_THRESHOLD_VALUE);

	}

}
