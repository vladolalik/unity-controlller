package com.bachelor.controllers.one_device;

import com.bachelor.networking.SendMessage;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class GamePadFragmentWithArrows_2_2 extends Fragment{
	
	
	@Override
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
	      
		
	       //Inflate the layout for this fragment
		 getActivity().setRequestedOrientation(
		            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 View view = inflater.inflate(R.layout.gamepad_fragment_arrows_2_2, container, false);

		 
		 Button rtRight = (Button) view.findViewById(R.id.buttonRotRight);
		 rtRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Click", "mvDown");
				sendMessage(getResources().getString(R.string.JOYSTICK_ROTATE_Y) + " 1");
			}
		 });
		 
		 Button rtLeft = (Button) view.findViewById(R.id.buttonRotLeft);
		 rtLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Click", "mvDown");
				sendMessage(getResources().getString(R.string.JOYSTICK_ROTATE_Y) + " 0");
			}
		 });
		 
		 Button rtUP = (Button) view.findViewById(R.id.buttonRotUP);
		 rtUP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Click", "mvDown");
				sendMessage(getResources().getString(R.string.JOYSTICK_ROTATE_X) + " 1");
			}
		 });

		 Button rtDown = (Button) view.findViewById(R.id.buttonRotDown);
		 rtDown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Click", "mvDown");
				sendMessage(getResources().getString(R.string.JOYSTICK_ROTATE_X) + " 0");
			}
		 });



	     return view;
	   }
	
	public void sendMessage(String msg) {
		new SendMessage((MainActivity)getActivity()).execute(msg);
	}
	
	

}
