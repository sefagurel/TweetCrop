package com.sefagurel.tweetlonger.asynctasks;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Html;
import android.widget.TextView;

import com.hintdesk.core.util.StringUtil;
import com.sefagurel.tweetlonger.R;
import com.sefagurel.tweetlonger.utils.ConstantValues;
import com.sefagurel.tweetlonger.utils.TwitterUtil;

public class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {

	private Activity	act;
	private TextView	textViewUserName;

	public TwitterGetAccessTokenTask(Activity activity) {
		act = activity;
		textViewUserName = (TextView) act.findViewById(R.id.textViewUserName);
	}

	@Override
	protected String doInBackground(String... params) {

		Twitter twitter = TwitterUtil.getInstance().getTwitter();
		RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
		if (!StringUtil.isNullOrWhitespace(params[0])) {
			try {

				AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(act);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
				editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
				System.out.println(accessToken.getToken() + " - " + accessToken.getTokenSecret());
				editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
				editor.commit();
				return twitter.showUser(accessToken.getUserId()).getName();
			}
			catch (TwitterException e) {
				e.printStackTrace();
			}
		}
		else {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(act);
			String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
			String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
			AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
			try {
				TwitterUtil.getInstance().setTwitterFactory(accessToken);
				return TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId()).getName();
			}
			catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	protected void onPostExecute(String userName) {
		textViewUserName.setText(Html.fromHtml("<b> " + act.getResources().getString(R.string.welcome) + " " + userName + "</b>"));
	}
}
