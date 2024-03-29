package com.bachelor.controllers.one_device;

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

public class OrientationSensor_1_1 extends Fragment {


	public SensorManager sm;
	ImageView intensity_1;
	ImageView[] intensityLeft=new ImageView[5];
	ImageView[] intensityRight=new ImageView[5];
	ImageView[] intensityForward=new ImageView[5];
	ImageView[] intensityBack=new ImageView[5];
	MyAccelerometerListener myAccelerometerListener;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myAccelerometerListener = new MyAccelerometerListener(intensityLeft, intensityRight, this);
		// Inflate the layout for this fragment
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		View view = inflater.inflate(R.layout.orientation_sensor_1_1, container,
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
		Button actionButon=(Button)view.findViewById(R.id.action_button_orient);
		actionButon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.ACTION_MESSAGE));
			}
		});
		
		Button mvFV=(Button) view.findViewById(R.id.buttonMovFV);
		mvFV.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_FV), (MainActivity)getActivity()));
		
		Button mvBACK=(Button) view.findViewById(R.id.buttonMovBack);
		mvBACK.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_BACK), (MainActivity)getActivity()));
		
		Button quit=(Button)view.findViewById(R.id.quit_5);
		quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.MSG_QUIT));
			}
		});
		
		Button rotateUP=(Button)view.findViewById(R.id.rotate_up_orient);
		rotateUP.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_ROTATE_X) + " 0", (MainActivity)getActivity()));

		Button rotateDown=(Button)view.findViewById(R.id.rotate_down_orient);
		rotateDown.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_ROTATE_X) + " 1", (MainActivity)getActivity()));
		
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
    
   

	/*final  SensorEventListener myAccelerometerListener = new SensorEventListener() {

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
				
				if (isAdded()){
					if (Math.abs(pitchValue)>0.5){
						sendMessage(getResources().getString(R.string.ORIENTATION_ROTATE) + " " + pitchValue);
					} 
				} else {
					Log.d("OrientationFragment", "not attached to activity");
				}
				
				/*
				Log.d("headingAngle", String.valueOf(headingAngle));
				Log.d("pitchAngle", String.valueOf(pitchAngle));
				Log.d("rollAngle", String.valueOf(rollAngle));
				
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	};*/
	
	private void sendMessage(String msg){
		new SendMessageMain((MainActivity)getActivity()).execute(msg);
	}

	

}
