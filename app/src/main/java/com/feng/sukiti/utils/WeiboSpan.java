/* 
 * Copyright (C) 2014 Peter Cai
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.feng.sukiti.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.feng.sukiti.R;
import com.feng.sukiti.SuKiTiApplication;
import com.feng.sukiti.module.home.WebviewActivity;

//import com.shaweibo.biu.R;
//import com.shaweibo.biu.dao.user.UserDao;
//import com.shaweibo.biu.global.MyApplication;
//import com.shaweibo.biu.model.UserModel;
//import com.shaweibo.biu.ui.timeline.UserHomeActivity;
//
//import static com.shaweibo.biu.BuildConfig.DEBUG;


public class WeiboSpan extends ClickableSpan {
	private static String TAG = WeiboSpan.class.getSimpleName();
	public static final String URL = "url";

	private String mUrl;
	private Uri mUri;
	private boolean isLight;

	public WeiboSpan(String url) {
		mUrl = url;
		mUri = Uri.parse(mUrl);
		isLight=false;
	}

	public WeiboSpan(String url,boolean isLight) {
		mUrl = url;
		mUri = Uri.parse(mUrl);
		this.isLight=isLight;
	}
	public String getURL() {
		return mUrl;
	}

	@Override
	public void onClick(View v) {
		Context context = v.getContext();



		if (mUrl.startsWith(SpannableStringUtils.HTTP_SCHEME)) {
			// TODO View some weibo pages inside app
//			Intent i = new Intent();
//			i.setAction(Intent.ACTION_VIEW);
//			i.setData(mUri);
			Intent intent = new Intent(context, WebviewActivity.class);
			intent.putExtra(URL, mUrl);
			context.startActivity(intent);
		} else {
			if (mUrl.startsWith(SpannableStringUtils.MENTION_SCHEME)) {
				String name = mUrl.substring(mUrl.lastIndexOf("@") + 1, mUrl.length());

//				if (DEBUG) {
//					Log.d(TAG, "Mention user link detected: " + name);
//				}

//				new UserInfoTask().execute(context, name);
			} else if (mUrl.startsWith(SpannableStringUtils.TOPIC_SCHEME)) {
				String name = mUrl.substring(mUrl.indexOf("#") + 1, mUrl.lastIndexOf("#"));

				// Start Activity
//				Intent i = new Intent();
//				i.setAction(Intent.ACTION_MAIN);
//				i.setClass(context, TopicsActivity.class);
//				i.putExtra("topic", name);
//				context.startActivity(i);
			}
		}
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		if (isLight){
			ds.setColor(SuKiTiApplication.getInstance().getResources().getColor(R.color.white));
		}
		else {
			ds.setColor(SuKiTiApplication.getInstance().getResources().getColor(R.color.mark));
		}

		ds.setUnderlineText(false);
	}

//	private class UserInfoTask extends AsyncTask<Object, Void, Object[]> {
//
//		@Override
//		protected Object[] doInBackground(Object... params) {
//			// Detect user info in background
//			return new Object[]{params[0],
//					new UserDao((Context) params[0]).getUserByName((String) params[1])};
//
//		}
//
//		@Override
//		protected void onPostExecute(Object[] result) {
//			super.onPostExecute(result);
//
//			Context context = (Context) result[0];
//			UserModel usr = (UserModel) result[1];
//
//			if (usr != null && usr.id != null & !usr.id.trim().equals("")) {
//
//				UserHomeActivity.start(context, usr);
//			}
//		}
//	}
}


