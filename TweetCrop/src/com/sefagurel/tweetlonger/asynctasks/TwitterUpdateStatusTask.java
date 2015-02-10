package com.sefagurel.tweetlonger.asynctasks;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.hintdesk.core.util.StringUtil;
import com.sefagurel.tweetlonger.R;
import com.sefagurel.tweetlonger.utils.ConstantValues;
import com.sefagurel.tweetlonger.utils.CutString;
import com.sefagurel.tweetlonger.utils.TwitterUtil;

public class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {

	private Activity	act;

	public TwitterUpdateStatusTask(Activity activity) {
		act = activity;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {

			List<String> tweets = new CutString().TweetBol(params[0], act);

			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(act);
			String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
			String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
			AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
			Twitter status = TwitterUtil.getInstance().getTwitterFactory().getInstance(accessToken);

			for (String tweet : tweets) {

				if (!StringUtil.isNullOrWhitespace(accessTokenString) && !StringUtil.isNullOrWhitespace(accessTokenSecret)) {
					status.updateStatus(tweet);

				}
			}
			return true;
		}
		catch (TwitterException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}
		return false; // To change body of implemented methods use File | Settings | File Templates.

	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result)
			Toast.makeText(act, R.string.tweet_successfully, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(act, R.string.tweet_failed, Toast.LENGTH_SHORT).show();
	}
}
