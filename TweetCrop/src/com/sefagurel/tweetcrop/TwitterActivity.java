package com.sefagurel.tweetcrop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hintdesk.core.util.StringUtil;
import com.sefagurel.tweetcrop.asynctasks.TwitterGetAccessTokenTask;
import com.sefagurel.tweetcrop.asynctasks.TwitterUpdateStatusTask;
import com.sefagurel.tweetcrop.helpers.SlideHolder;
import com.sefagurel.tweetcrop.utils.ConstantValues;
import com.sefagurel.tweetcrop.utils.TwitterUtil;
import com.squareup.picasso.Picasso;

public class TwitterActivity extends ActionBarActivity implements OnClickListener, TextWatcher {

	private SlideHolder	mSlideHolder;

	EditText			editTextStatus;
	TextView			tvTextCounter;
	ImageView			img_profile_picture;
	WebView				wv_twitter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar ab = getSupportActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);

		Drawable d = getResources().getDrawable(R.drawable.back8);

		ab.setBackgroundDrawable(d);

		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowTitleEnabled(true);

		setContentView(R.layout.main2);

		mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);

		editTextStatus = (EditText) findViewById(R.id.editTextStatus);
		tvTextCounter = (TextView) findViewById(R.id.tv_text_counter);
		img_profile_picture = (ImageView) findViewById(R.id.img_profile_picture);

		wv_twitter = (WebView) findViewById(R.id.webView1);
		wv_twitter.getSettings().setJavaScriptEnabled(true);
		wv_twitter.setWebViewClient(new MyBrowser());
		wv_twitter.getSettings().setLoadsImagesAutomatically(true); 
		wv_twitter.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		wv_twitter.loadUrl("http://www.twitter.com");

		Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(img_profile_picture);

		editTextStatus.addTextChangedListener(this);

		Uri uri = getIntent().getData();

		if (uri != null && uri.toString().startsWith(ConstantValues.TWITTER_CALLBACK_URL)) {
			String verifier = uri.getQueryParameter(ConstantValues.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
			new TwitterGetAccessTokenTask(this).execute(verifier);
		}
		else {
			new TwitterGetAccessTokenTask(this).execute("");
		}

	}

 

	private class MyBrowser extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		tvTextCounter.setText(s.length() % 140 + "/" + ((s.length() / 140) + 1));
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
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
		else if (id == android.R.id.home) {
			View view = this.getCurrentFocus();
			if (view != null) {
				InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			mSlideHolder.toggle();
		}
		else if (id == R.id.action_logout) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
			editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
			editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, false);
			editor.commit();
			TwitterUtil.getInstance().reset();
			Intent intent = new Intent(TwitterActivity.this, MainActivity.class);
			startActivity(intent);
		}
		else if (id == R.id.action_send) {
			String status = editTextStatus.getText().toString();

			if (!StringUtil.isNullOrWhitespace(status)) {
				new TwitterUpdateStatusTask(this).execute(status);
			}
			else {
				Toast.makeText(getApplicationContext(), R.string.enter_status, Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}