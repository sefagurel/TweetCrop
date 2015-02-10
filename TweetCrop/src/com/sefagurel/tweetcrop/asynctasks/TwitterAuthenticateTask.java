package com.sefagurel.tweetcrop.asynctasks;

import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.sefagurel.tweetcrop.OAuthActivity;
import com.sefagurel.tweetcrop.utils.ConstantValues;
import com.sefagurel.tweetcrop.utils.TwitterUtil;

public class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

	private Activity	act;

	public TwitterAuthenticateTask(Activity activity) {
		act = activity;
	}

	@Override
	protected RequestToken doInBackground(String... params) {
		return TwitterUtil.getInstance().getRequestToken();
	}

	@Override
	protected void onPostExecute(RequestToken requestToken) {
		if (requestToken != null) {
			if (!ConstantValues.isUseWebViewForAuthentication) {

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
				act.startActivity(intent);
			}
			else {
				Intent intent = new Intent(act, OAuthActivity.class);
				intent.putExtra(ConstantValues.STRING_EXTRA_AUTHENCATION_URL, requestToken.getAuthenticationURL());
				act.startActivity(intent);
			}
		}
	}

}
