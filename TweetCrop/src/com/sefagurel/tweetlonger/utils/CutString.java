package com.sefagurel.tweetlonger.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;

import com.sefagurel.tweetlonger.R;

/**
 * @author Sefa Gürel
 * 
 */

public class CutString {

	private List<String>	Tweets			= new ArrayList<String>();
	private List<String>	TweetsForSend	= new ArrayList<String>();
	StringBuilder			tmpBuilder		= new StringBuilder("");

	public List<String> TweetBol(String yazi, Activity act) {

		if (yazi.length() > 12000) {
			Tweets.add(act.getResources().getString(R.string.error_long_text));
			return Tweets;
		}

		if (yazi.length() <= 140) {
			Tweets.add(yazi);
			return Tweets;
		}

		List<String> Words = new ArrayList<String>(Arrays.asList(yazi.split("\\s+")));

		Tweets = getAllTweets(Words);

		if (Tweets.size() > 1) {

			for (int i = 0; i < Tweets.size(); i++) {

				if (i == 0) {
					TweetsForSend.add(Tweets.get(i) + " (" + (i + 1) + "/" + Tweets.size() + ")");
				}
				else {
					TweetsForSend.add("+ " + Tweets.get(i) + " (" + (i + 1) + "/" + Tweets.size() + ")");
				}
			}

			Tweets = TweetsForSend;

		}

		return Tweets;

	}

	private List<String> getStringList(String strWord) {

		List<String> Tweets1 = new ArrayList<String>();

		int rangeCounter = 0;
		int oldStringLenght = tmpBuilder.length();

		tmpBuilder.append(strWord);
		String strWord2 = tmpBuilder.toString();

		int newStringLenght = strWord2.length();

		while (rangeCounter < newStringLenght) {

			int tmp = newStringLenght - rangeCounter;

			if (tmp < 130) {
				String str = strWord2.substring(rangeCounter, rangeCounter + tmp);
				tmpBuilder = new StringBuilder(str + " ");
				// Tweets1.add(str);
				// tmpBuilder = new StringBuilder("");
			}
			else {
				String str = strWord2.substring(rangeCounter, rangeCounter + 130);
				Tweets1.add(str);
			}

			rangeCounter = rangeCounter + 130;
		}

		return Tweets1;
	}

	public List<String> getAllTweets(List<String> words) {
		List<String> Words = words;
		List<String> Tweets = new ArrayList<String>();

		int counter = 1;

		for (String string : Words) {

			if (tmpBuilder.length() + string.length() <= 130) {
				tmpBuilder.append(string + " ");

				if (counter == Words.size()) {
					Tweets.add(tmpBuilder.toString());
				}
			}
			else {

				if (string.length() > 130) {
					Tweets.addAll(getStringList(string));
				}
				else {
					Tweets.add(tmpBuilder.toString());
					tmpBuilder = new StringBuilder(string + " ");
				}

			}
			counter++;
		}

		return Tweets;
	}
}
