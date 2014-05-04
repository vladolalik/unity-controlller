package com.bachelor.unity_remote_control;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.bachelor.networking.DataStorage;
import com.bachelor.networking.ImageStorage;
import com.example.resultrecdemo.R;



/**
 * 
 * Trieda ktora po spracovani spravy od receiveru updatne pouzivatelske
 * prostredie
 * 
 */
class UpdateUIMenu implements Runnable {
	String msgFromServer;
	int my_number;
	int count_of_dev;
	int numControl = -1;
	MenuViewActivity menuActivity;
	

	public UpdateUIMenu(String msgFromServer, MenuViewActivity menuActivity) {
		this.msgFromServer = msgFromServer;
		this.menuActivity=menuActivity;
		
		String[] aData=this.msgFromServer.split(";");
		Log.d("UPDATEMENU",aData[0]);
		// zobrazenie textViewActivity
		if (aData[0]!=null && aData[0].equals(menuActivity.getResources().getString(R.string.TEXT_FROM_SERVER))){
			if (aData[1]!=null){
				Log.d("Before Start Text", aData[0]);
				Intent i = new Intent(menuActivity, TextViewActivity.class);
				Bundle b = new Bundle();
				//b.putString("text", aData[1]);
				i.putExtra("DataStorage", new DataStorage(this.msgFromServer));
				b.putInt("active_fragment", menuActivity.mainActivityActiveFragment);
				i.putExtras(b);
				menuActivity.startActivityForResult(i, menuActivity.TEXT_VIEW_ACTIVITY);
			}
		}
		
		if (this.msgFromServer.equals(menuActivity.getResources().getString(R.string.IMG_SERVER))){
			
			Intent i = new Intent(menuActivity, ImageViewActivity.class);
			Bundle b = new Bundle();
			b.putInt("active_fragment", menuActivity.mainActivityActiveFragment);
			i.putExtras(b);
			menuActivity.startActivityForResult(i, menuActivity.IMAGE_VIEW_ACTIVITY);
		}
	}

	public void run() {
	
	}
}