package com.bachelor.controllers.one_device;

import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bachelor.networking.SendMessageMain;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

public class MyAccelerometerListener implements SensorEventListener {
	
	ImageView[] intensityLeft;
	ImageView[] intensityRight;
	Fragment parent;
	
	
	public MyAccelerometerListener(ImageView[] intensityLeft, ImageView[] intensityRight, Fragment parent){
		this.intensityLeft=intensityLeft;
		this.intensityRight=intensityRight;
		this.parent=parent;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			
			//float headingAngle=event.values[0];
			float pitchValue=event.values[1];
			//float moveValue=event.values[0];
			
			for (int i=0; i<intensityLeft.length; i++){
				intensityLeft[i].setVisibility(View.INVISIBLE);
				intensityRight[i].setVisibility(View.INVISIBLE);
				
				if (pitchValue>0){
					if (pitchValue>i*1.5+0.2){
						intensityRight[i].setVisibility(View.VISIBLE);
					}
				} else {	
					if (Math.abs(pitchValue)>i*1.5+0.2){
						intensityLeft[i].setVisibility(View.VISIBLE);
					}
				}
				
			
			}
			
			if (parent.isAdded()){
				if (Math.abs(pitchValue)>0.5){
					sendMessage(parent.getResources().getString(R.string.ORIENTATION_ROTATE) + " " + pitchValue);
				} 
			} else {
				Log.d("OrientationFragment", "not attached to activity");
			}
			
			/*
			Log.d("headingAngle", String.valueOf(headingAngle));
			Log.d("pitchAngle", String.valueOf(pitchAngle));
			Log.d("rollAngle", String.valueOf(rollAngle));
			*/
		}
	}
	
	private void sendMessage(String msg){
		new SendMessageMain((MainActivity)parent.getActivity()).execute(msg);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}
