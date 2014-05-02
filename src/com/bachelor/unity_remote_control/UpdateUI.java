package com.bachelor.unity_remote_control;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bachelor.networking.DataStorage;
import com.example.resultrecdemo.R;


/**
 * 
 * Trieda ktora po spracovani spravy od receiveru updatne pouzivatelske
 * prostredie
 * 
 */
class UpdateUI implements Runnable {
	String msgFromServer;
	int my_number;
	int count_of_dev;
	int numControl = -1;
	MainActivity mainActivity;
	

	public UpdateUI(String msgFromServer, MainActivity mainActivity) {
		this.msgFromServer = msgFromServer;
		this.mainActivity=mainActivity;
		
		// message for setting number of clients and number of my device
		if (msgFromServer.contains(mainActivity.getResources().getString(
				R.string.SET_NUMBER_OF_DEVICES))) {
			try {
				String[] data = msgFromServer.split(" ");

				// save data

				my_number = Integer.parseInt(data[2]);
				count_of_dev = Integer.parseInt(data[1]);

				Log.d("My number from server", data.toString());
				Log.d("Count of devices", Integer.toString(count_of_dev));
				Editor editor = mainActivity.prefs.edit();
				editor.putInt(
						mainActivity.getResources().getString(R.string.DEV_NUMBER),
						my_number);
				editor.putInt(
						mainActivity.getResources().getString(R.string.COUNT_OF_DEVICES),
						count_of_dev);
				editor.commit();
				Toast.makeText(mainActivity,
						"Server set my number of dev to " + my_number,
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Log.d("ERROR", "SETTING NUM OF DEV");
				e.printStackTrace();
			}
		}

		// message with request for change a control
		if (this.msgFromServer.contains(mainActivity.getResources().getString(
				R.string.TYPE_OF_CONTROL))) {
			try {
				String[] typeOfControls = mainActivity.getResources().getStringArray(
						R.array.typesOfControl);
				String[] data = this.msgFromServer.split(" ");
				for (int i = 0; i < typeOfControls.length; i++) {
					if (data[1].equals(typeOfControls[i])) {
						Log.d("TypeOfControl", typeOfControls[i]);
						mainActivity.changeControlFragment(i);
						numControl = i;
					}
				}

			} catch (Exception e) {
				Log.d("ERROR", "PARSE MESSAGE FROM SERVER");
				e.printStackTrace();
			}

		}
		
		//in case service which receive messages from server was killed
		if (this.msgFromServer.equals(mainActivity.getResources().getString(R.string.service_killed))){
			mainActivity.startService();
		}
		
		
		String[] aData=this.msgFromServer.split(";");
		
		// zobrazenie textViewActivity
		if (aData[0]!=null && aData[0].equals(mainActivity.getResources().getString(R.string.TEXT_FROM_SERVER))){
			if (aData[1]!=null){
				Log.d("Before Start Text", aData[0]);
				Intent i = new Intent(mainActivity, TextViewActivity.class);
				Bundle b = new Bundle();
				//b.putString("text", aData[1]);
				i.putExtra("DataStorage", new DataStorage(this.msgFromServer));
				b.putInt("active_fragment", mainActivity.lastSelectedItemActionBar);
				i.putExtras(b);
				mainActivity.startActivityForResult(i, mainActivity.TEXT_VIEW_ACTIVITY);
			}
		}
		
		//zobrazenie menu zo serveru
		if (aData[0]!=null && aData[0].equals(mainActivity.getResources().getString(R.string.MENU_FROM_SERVER))){
			if (aData[1]!=null){
				Log.d("Before Start Menu", aData[0]);
				Intent i = new Intent(mainActivity, MenuViewActivity.class);
				Bundle b = new Bundle();
				b.putString("text", this.msgFromServer);
				b.putString("serverIP", mainActivity.serverIP.toString());
				b.putInt("active_fragment", mainActivity.lastSelectedItemActionBar);
				i.putExtras(b);
				mainActivity.startActivityForResult(i, mainActivity.MENU_VIEW_ACTIVITY);
			}
		}
		
	}

	public void run() {
		if (msgFromServer.contains(mainActivity.getResources().getString(
				R.string.SET_NUMBER_OF_DEVICES))) {
			Toast.makeText(
					mainActivity,
					"Server set my number of dev to " + my_number + " "
							+ count_of_dev, Toast.LENGTH_SHORT).show();
		}
		// zmenim aktivnu polozku v spinner menu
		if (numControl != -1) {
			mainActivity.serverRequest = true;
			mainActivity.actionBar.setSelectedNavigationItem(numControl);
		}
	}
}