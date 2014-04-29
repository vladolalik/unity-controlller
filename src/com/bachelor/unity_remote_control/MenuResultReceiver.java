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
	class MenuResultReceiver extends ResultReceiver {
		
		MenuViewActivity menuActivity;
		
		public MenuResultReceiver(Handler handler, MenuViewActivity menuActivity) {
			super(handler);
			this.menuActivity=menuActivity;
			
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {

			if (resultCode == 100) {
				menuActivity.runOnUiThread(new UpdateUIMenu(resultData.getString("start"),menuActivity));
			} else if (resultCode == 200) {
				menuActivity.runOnUiThread(new UpdateUIMenu(resultData.getString("end"),menuActivity));
			} else if (resultCode == 10) {
				menuActivity.runOnUiThread(new UpdateUIMenu(resultData.getString("msg"),menuActivity));
			} else {
				menuActivity.runOnUiThread(new UpdateUIMenu("Message from server > "
						+ resultData.getString("msg"),menuActivity));
			}
		}
	}