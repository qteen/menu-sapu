package com.qteen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class MainActivity extends Activity {
	public String[] cities = new String[] { "Berita", "Pembelian", "Transfer",
			"Pembayaran", "Rekap", "Tiket", "Cek", "Komplain", "Administrasi" };
	// Within which the entire activity is enclosed
	DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	ExpandableListView mDrawerList;

	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the
	// action bar
	ActionBarDrawerToggle mDrawerToggle;

	// Title of the action bar
	String mTitle = "";
	private ArrayList<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	private ExpandableListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = (String) getTitle();

		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ExpandableListView) findViewById(R.id.drawer_list);

		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Welcome");
				invalidateOptionsMenu();
			}
		};

		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		prepareListData();

		// Creating an expandablelistadapter to add items to the listview
		// mDrawerList
		adapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

		// Setting the adapter on mDrawerList
		mDrawerList.setAdapter(adapter);

		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				checkDrawerClick(groupPosition, -1);
				return false;
			}
		});

		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				checkDrawerClick(groupPosition, childPosition);
				return false;
			}
		});
	}

	protected void checkDrawerClick(int groupPosition, int childPosition) {
		List<String> list = listDataChild
				.get(listDataHeader.get(groupPosition));
		if (list == null || list.isEmpty()) {
			mTitle = listDataHeader.get(groupPosition);
		} else if (childPosition >= 0) {
			mTitle = listDataHeader.get(groupPosition)
					+ " - "
					+ listDataChild.get(listDataHeader.get(groupPosition)).get(
							childPosition);
		} else {
			return;
		}
		int layoutid = R.layout.fragment_layout;
		switch (groupPosition) {
		case 1:
			switch (childPosition) {
			case 0:
				layoutid = R.layout.layout_stok;
				break;
			case 1:
				layoutid = R.layout.layout_saldo;
				break;
			case 2:
				layoutid = R.layout.layout_pulsa;
				break;
			case 3:
				layoutid = R.layout.layout_paket;
				break;
			default:
				break;
			}
			break;
		case 2:
			switch (childPosition) {
			case 0:
				layoutid = R.layout.layout_stok_t;
				break;
			case 1:
				layoutid = R.layout.layout_saldo_t;
				break;
			default:
				break;
			}
			break;
		default:
			layoutid = R.layout.fragment_layout;
			break;
		}

		// Creating a fragment object
		ListFragment rFragment = new ListFragment(layoutid);

		// Creating a Bundle object
		Bundle data = new Bundle();

		// Setting the index of the currently selected item of
		// mDrawerList
		data.putString("menu", mTitle);

		// Setting the position to the fragment
		rFragment.setArguments(data);

		// Getting reference to the FragmentManager
		FragmentManager fragmentManager = getFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();

		// Adding a fragment to the fragment transaction
		ft.replace(R.id.content_frame, rFragment);

		// Committing the transaction
		ft.commit();

		// Closing the drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/** Handling the touch event of app icon */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Berita");
		listDataHeader.add("Pembelian");
		listDataHeader.add("Transfer");
		listDataHeader.add("Pembayaran");
		listDataHeader.add("Rekap");
		listDataHeader.add("Tiket");
		listDataHeader.add("Cek");
		listDataHeader.add("Komplain");
		listDataHeader.add("Administrasi");

		// Adding child data
		List<String> pembelian = new ArrayList<String>();
		pembelian.add("Stok");
		pembelian.add("Saldo");
		pembelian.add("Pulsa");
		pembelian.add("Paket");

		List<String> transfer = new ArrayList<String>();
		transfer.add("Stok");
		transfer.add("Saldo");

		List<String> rekap = new ArrayList<String>();
		rekap.add("Stok");
		rekap.add("Saldo");
		rekap.add("Pulsa");

		List<String> cek = new ArrayList<String>();
		cek.add("Stok");
		cek.add("Saldo");
		cek.add("Harga");

		listDataChild.put("Pembelian", pembelian); // Header, Child data
		listDataChild.put("Transfer", transfer);
		listDataChild.put("Rekap", rekap);
		listDataChild.put("Cek", cek);
	}
}
