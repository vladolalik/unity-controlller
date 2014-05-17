package com.bachelor.controllers.two_devices;

import com.bachelor.controllers.one_device.ButtonHoldingOnTouchListener;
import com.bachelor.networking.SendMessageMain;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class GamePadFragment_1_2 extends Fragment {


	Vibrator vibrator;

	/*
	 * public GamePadFragment(InetAddress serverIP, FragmentActivity
	 * mainActivity){ this.serverIP=serverIP; this.mainActivity=mainActivity; }
	 */

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		// Inflate the layout for this fragment
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		View view = inflater.inflate(R.layout.gamepad_fragment_1_2, container,
				false);
		vibrator = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		Button mvLeft = (Button) view.findViewById(R.id.buttonMovLeft);
		mvLeft.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_LEFT), (MainActivity)getActivity()));

			

		Button mvRight = (Button) view.findViewById(R.id.buttonMovRight);
		mvRight.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_RIGHT), (MainActivity)getActivity()));

		Button mvUp = (Button) view.findViewById(R.id.buttonMovFV);
		mvUp.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_FV), (MainActivity)getActivity()));

		Button mvDown = (Button) view.findViewById(R.id.buttonMovBack);
		mvDown.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_BACK), (MainActivity)getActivity()));
		
		Button action = (Button) view.findViewById(R.id.action_button_1_2);
		action.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.ACTION_MESSAGE));
			}
		});
		
		Button quit=(Button)view.findViewById(R.id.quit_1);
		quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.MSG_QUIT));
			}
		});


		return view;
	}

	public void sendMessage(String msg) {
		new SendMessageMain((MainActivity)getActivity()).execute(msg);
		
	}

}
