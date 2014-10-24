package com.qteen;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ListFragment extends Fragment {
	public Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			Bundle data = msg.getData();
			// tv.setText(data.getString("weather"));
			Toast.makeText(getActivity(), data.getString("weather"),
					Toast.LENGTH_LONG).show();
			return false;
		}

	});
	private int layoutid;

	public ListFragment(int layoutid) {
		this.layoutid = layoutid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final String menu = getArguments().getString("menu");
		SharedPreferences preferences = getActivity().getPreferences(
				Activity.MODE_PRIVATE);
		final String urlStr = preferences.getString("url", getActivity()
				.getString(R.string.pref_default_url));

		// Creating view correspoding to the fragment
		View v = inflater.inflate(layoutid, container, false);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				SoapRequestsCoba ex = new SoapRequestsCoba(urlStr);
				String weather = ex.getWeatherFromZipCode(menu);
				Bundle bundle = new Bundle();
				bundle.putString("weather", weather);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}

		});

		// Updating the action bar title
		getActivity().getActionBar().setTitle(menu);
		thread.start();

		return v;
	}
}