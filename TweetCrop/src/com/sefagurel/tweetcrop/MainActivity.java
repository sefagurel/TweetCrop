package com.sefagurel.tweetcrop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hintdesk.core.activities.AlertMessageBox;
import com.hintdesk.core.util.OSUtil;
import com.hintdesk.core.util.StringUtil;
import com.sefagurel.tweetcrop.asynctasks.TwitterAuthenticateTask;
import com.sefagurel.tweetcrop.utils.ConstantValues;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private Button	buttonLogin;
	private boolean	isUseStoredTokenKey	= false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (!OSUtil.IsNetworkAvailable(getApplicationContext())) {
			AlertMessageBox.Show(MainActivity.this, getResources().getString(R.string.internet_connection), getResources().getString(R.string.valid_connection),
					AlertMessageBox.AlertMessageBoxIcon.Info);
			return;
		}

		if (StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_KEY) || StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_SECRET)) {
			AlertMessageBox.Show(MainActivity.this, getResources().getString(R.string.twitter_oauth_infos), getResources().getString(R.string.set_consumer), AlertMessageBox.AlertMessageBoxIcon.Info);
			return;
		}

		buttonLogin = (Button) findViewById(R.id.buttonLogin);

		buttonLogin.setOnClickListener(this);

		if (isUseStoredTokenKey) {
			logIn();
		}
	}

	private void logIn() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, false)) {
			new TwitterAuthenticateTask(this).execute();
		}
		else {
			Intent intent = new Intent(this, TwitterActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonLogin) {
			logIn();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
