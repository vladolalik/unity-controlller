package com.bachelor.controllers.one_device;

import com.bachelor.networking.SendMessageMain;
import com.bachelor.unity_remote_control.MainActivity;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ButtonHoldingOnTouchListener implements View.OnTouchListener  {

	private Handler mHandler;
	private String msg;
	private MainActivity mainActivity;
	
	public ButtonHoldingOnTouchListener(String msg, MainActivity mainActivity){
		this.msg=msg;
		this.mainActivity=mainActivity;
	}
	
    @Override 
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (mHandler != null) return true;
            mHandler = new Handler();
            mHandler.postDelayed(mAction, 10);
            break;
        case MotionEvent.ACTION_UP:
            if (mHandler == null) return true;
            mHandler.removeCallbacks(mAction);
            mHandler = null;
            break;
        }
        return false;
    }

    Runnable mAction = new Runnable() {
        @Override public void run() {
            System.out.println("Performing action...");
            Log.d("Click", "mvRight");
			sendMessage(msg);
            mHandler.postDelayed(this, 10);
        }
    };
    
    public void sendMessage(String msg) {
		new SendMessageMain(mainActivity).execute(msg);
	}
}
