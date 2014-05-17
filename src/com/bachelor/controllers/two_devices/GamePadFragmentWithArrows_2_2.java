package com.bachelor.controllers.two_devices;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bachelor.controllers.one_device.ButtonHoldingOnTouchListener;
import com.bachelor.networking.SendMessageMain;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;


public class GamePadFragmentWithArrows_2_2 extends Fragment{
	
	
	@Override
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
	      
		
	       //Inflate the layout for this fragment
		 getActivity().setRequestedOrientation(
		            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		 View view = inflater.inflate(R.layout.gamepad_fragment_arrows_2_2, container, false);

		 
		 Button rtRight = (Button) view.findViewById(R.id.buttonRotRight);
		 rtRight.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_ROTATE_Y) + " 1", (MainActivity)getActivity()));
			
		 
		 Button rtLeft = (Button) view.findViewById(R.id.buttonRotLeft);
		 rtLeft.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_ROTATE_Y) + " 0", (MainActivity)getActivity()));
		 
		 Button rtUP = (Button) view.findViewById(R.id.buttonRotUP);
		 rtUP.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_ROTATE_X) + " 1", (MainActivity)getActivity()));
			
			

		 Button rtDown = (Button) view.findViewById(R.id.buttonRotDown);
		 rtDown.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_ROTATE_X) + " 0", (MainActivity)getActivity()));
			
	     return view;
	   }
	
	public void sendMessage(String msg) {
		new SendMessageMain((MainActivity)getActivity()).execute(msg);
	}
	
	

}
