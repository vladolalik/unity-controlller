package com.bachelor.controllers.two_devices;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bachelor.networking.SendMessageMain;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

public class OrientationSensor_2_2 extends Fragment {


	public SensorManager sm;
	ImageView intensity_1;
	ImageView[] intensityLeft=new ImageView[5];
	ImageView[] intensityRight=new ImageView[5];

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		View view = inflater.inflate(R.layout.orientation_2_2, container,
				false);
		sm = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		
		Sensor accSensor=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (accSensor==null){
			Toast.makeText(getActivity(), "Accelerometer is not working", Toast.LENGTH_LONG).show();
		}
		sm.registerListener(myAccelerometerListener, accSensor,
				SensorManager.SENSOR_DELAY_GAME);
		setIntensityArrays(view);
		Button actionButon=(Button)view.findViewById(R.id.action_button_or);
		actionButon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.ACTION_MESSAGE));
			}
		});
		
		Button quit=(Button)view.findViewById(R.id.quit_6);
		quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.MSG_QUIT));
			}
		});


		
		return view;
	}
	
	private void setIntensityArrays(View view){
		intensityLeft[0]=(ImageView)view.findViewById(R.id.intensity_left_1);
		intensityLeft[1]=(ImageView)view.findViewById(R.id.intensity_left_2);
		intensityLeft[2]=(ImageView)view.findViewById(R.id.intensity_left_3);
		intensityLeft[3]=(ImageView)view.findViewById(R.id.intensity_left_4);
		intensityLeft[4]=(ImageView)view.findViewById(R.id.intensity_left_5);
		intensityRight[0]=(ImageView)view.findViewById(R.id.intensity_right_1);
		intensityRight[1]=(ImageView)view.findViewById(R.id.intensity_right_2);
		intensityRight[2]=(ImageView)view.findViewById(R.id.intensity_right_3);
		intensityRight[3]=(ImageView)view.findViewById(R.id.intensity_right_4);
		intensityRight[4]=(ImageView)view.findViewById(R.id.intensity_right_5);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		sm.unregisterListener(myAccelerometerListener);
	}

    public void onStop(){
    	super.onStop();
    	sm.unregisterListener(myAccelerometerListener);
    }

    public void onDestroy(){
    	super.onDestroy();
    	sm.unregisterListener(myAccelerometerListener);
    }

	final  SensorEventListener myAccelerometerListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				
				//float headingAngle=event.values[0];
				float pitchValue=event.values[0];		
				for (int i=0; i<intensityLeft.length; i++){
					intensityLeft[i].setVisibility(View.INVISIBLE);
					intensityRight[i].setVisibility(View.INVISIBLE);
					
					if (pitchValue>0){
						if (pitchValue>i*1.5+0.2){
							intensityLeft[i].setVisibility(View.VISIBLE);
						}
					} else {	
						if (Math.abs(pitchValue)>i*1.5+0.2){
							intensityRight[i].setVisibility(View.VISIBLE);
						}
					}
				}
				//float rollAngle=event.values[2];
				if (isAdded()){
					if (Math.abs(pitchValue)>0.5){
						sendMessage(getResources().getString(R.string.ROTATE_MSG) + " " + (-1)*pitchValue);
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

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	};
	
	private void sendMessage(String msg){
		new SendMessageMain((MainActivity)getActivity()).execute(msg);
	}

	

}
