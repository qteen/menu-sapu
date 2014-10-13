package com.qteen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HelloAndroidActivity extends Activity {
	private TextView txt;
	public Handler handler = new Handler(new Handler.Callback() {
		 
	    @Override
	    public boolean handleMessage(Message msg) {
	    	Bundle data = msg.getData();
	    	String celsius = data.getString("celsius");
	    	String farenheit = data.getString("farenheit");
	        txt.setText(farenheit+"`F ==> "+celsius+"`C");
	        return false;
	    }
	});

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText edt = (EditText) findViewById(R.id.value_to_convert);
		Button btn = (Button) findViewById(R.id.convert);
		txt = (TextView) findViewById(R.id.answer);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edt.length() > 0) {
					getCelsius(edt.getText().toString());
				} else {
					txt.setText("Fahrenheit value can not be empty.");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(com.qteen.R.menu.main, menu);
		return true;
	}

	private final void getCelsius(final String toConvert) {
		new Thread(new Runnable() {			

			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				SoapRequests ex = new SoapRequests();
				String celsius = ex.getCelsiusConversion(toConvert);
				Bundle bundle = new Bundle();
				bundle.putString("celsius", celsius);
				bundle.putString("farenheit", toConvert);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}).start();
	}
}
