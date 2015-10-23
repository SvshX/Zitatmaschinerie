package com.melcher.zitatmaschinerie;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SingleQuoteActivity extends Activity {

	private EditText et;
	Button edit_btn, delete_btn;
	long qu_id;
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
				.setText("Mein Zitat");

		setContentView(R.layout.activity_singlequote);

		FontHelper.applyFont(this, findViewById(R.id.singlequote_layout),
				"fonts/Helvetica-UltraLigCond.otf");
		
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

		et = (EditText) findViewById(R.id.singlequote);

		edit_btn = (Button) findViewById(R.id.btn_update);
		delete_btn = (Button) findViewById(R.id.btn_delete);

		Intent i = getIntent();

		String quoteID = i.getStringExtra("ListEntry_Id");
		String quoteName = i.getStringExtra("ListEntry_Quote");

		qu_id = Long.parseLong(quoteID);

		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Chantelli_Antiqua.ttf");
		et.setTypeface(face);

		et.setText(quoteName);
	
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
					startActivity(new Intent(SingleQuoteActivity.this,
							ShowQuotesActivity.class));
				} else if (position == 1) {
					startActivity(new Intent(SingleQuoteActivity.this,
							CreateQuoteActivity.class));
				} else if (position == 2) {
					startActivity(new Intent(SingleQuoteActivity.this,
							QuoteOfTheDayActivity.class));
				} else if (position == 3) {
					startActivity(new Intent(SingleQuoteActivity.this,
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
	
	public void onButtonClick(View view) {

		switch (view.getId()) {
		case R.id.btn_update:
			et.setEnabled(true);
			if (hasContent(et)) {
				String new_quote = et.getText().toString();
				sqladapter.updateData(qu_id, new_quote);
				this.stepback();
			} else {
				Builder builder = new Builder(this);
				builder.setTitle(R.string.alert)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setMessage(R.string.alert_message)
						.setPositiveButton(android.R.string.ok, null).show();
			}
			break;

		case R.id.btn_delete:
			sqladapter.deleteData(qu_id);
			this.stepback();
			break;
		}

	}

	public void stepback() {

		Intent back = new Intent(SingleQuoteActivity.this,
				MyEntriesActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(back);
	}

	private boolean hasContent(EditText et) {
		// Always assume false until proven otherwise
		boolean bHasContent = false;

		if (et.getText().toString().trim().length() > 0) {
			bHasContent = true;
		}
		return bHasContent;
	}

}
