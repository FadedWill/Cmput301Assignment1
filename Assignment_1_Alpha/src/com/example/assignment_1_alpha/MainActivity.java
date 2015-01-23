package com.example.assignment_1_alpha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView claim_options;
	private ImageView add_claim_button;
	Claim_options_pop_window menuWindow;

	private void init() {

		claim_options = (ImageView) findViewById(R.id.claim_options);
		add_claim_button = (ImageView) findViewById(R.id.add_claim_button);
		

		claim_options.setAlpha(130);

		claim_options.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				uploadImage(MainActivity.this);
				/*
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ClaimActivity.class);
				MainActivity.this.startActivity(intent);
				*/
			}
		});

	}

	public void uploadImage(final Activity context) {
		menuWindow = new Claim_options_pop_window(MainActivity.this,
				itemsOnClick);
		menuWindow.showAtLocation(
				MainActivity.this.findViewById(R.id.claim_options), Gravity.TOP
						| Gravity.RIGHT, 0, 285);

	}

	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
