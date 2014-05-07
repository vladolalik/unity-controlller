package com.bachelor.unity_remote_control;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
	 * 
	 * @author Brutal
	 * 
	 *         ResultReceiver spracovava spravy od servisu ktory bezi na
	 *         samostatnom vlakne a prijima spravy od serveru
	 */
	class MyResultReceiver extends ResultReceiver {
		
		public MainActivity mainActivity;
		
		public MyResultReceiver(Handler handler, MainActivity mainActivity) {
			super(handler);
			this.mainActivity=mainActivity;
			
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {

			if (resultCode == 100) {
				mainActivity.runOnUiThread(new UpdateUI(resultData.getString("start"),mainActivity));
			} else if (resultCode == 200) {
				mainActivity.runOnUiThread(new UpdateUI(resultData.getString("end"),mainActivity));
			} else if (resultCode == 10) {
				mainActivity.runOnUiThread(new UpdateUI(resultData.getString("msg"),mainActivity));
			} else {
				mainActivity.runOnUiThread(new UpdateUI("Message from server > "
						+ resultData.getString("msg"),mainActivity));
			}
		}
	}