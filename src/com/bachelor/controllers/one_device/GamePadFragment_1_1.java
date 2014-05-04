package com.bachelor.controllers.one_device;

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
import android.widget.ImageView;

import com.bachelor.networking.SendMessageMain;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;


public class GamePadFragment_1_1 extends Fragment {

	
	Vibrator vibrator;
	ImageView imgView;
	ImageView joystickMiddle;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	
		// Inflate the layout for this fragment
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		View view = inflater.inflate(R.layout.gamepad_fragment_1_1, container,
				false);
		vibrator = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		Button mvLeft = (Button) view.findViewById(R.id.buttonMovLeft);
		mvLeft.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_LEFT), (MainActivity)getActivity()));

			

		final Button mvRight = (Button) view.findViewById(R.id.buttonMovRight);
		mvRight.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_RIGHT), (MainActivity)getActivity()));

		Button mvUp = (Button) view.findViewById(R.id.buttonMovFV);
		mvUp.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_FV), (MainActivity)getActivity()));
			
			
		Button mvDown = (Button) view.findViewById(R.id.buttonMovBack);
		mvDown.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources().getString(R.string.JOYSTICK_MV_BACK), (MainActivity)getActivity()));
		
		Button action = (Button) view.findViewById(R.id.action_button);
		action.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.ACTION_MESSAGE));
			}
		});
		
		Button quit=(Button)view.findViewById(R.id.quit);
		quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.MSG_QUIT));
			}
		});

		imgView = (ImageView) view.findViewById(R.id.imgViewJoyStick);
		joystickMiddle = (ImageView) view
				.findViewById(R.id.imageViewJoyStickMiddle);
		imgView.setOnTouchListener(new MyOnTouchListener(imgView, joystickMiddle, getResources().getString(R.string.JOYSTICK_ROTATE), (MainActivity)getActivity()));
		return view;
	}
	
	public void sendMessage(String msg) {
		new SendMessageMain((MainActivity)getActivity()).execute(msg);
	}

}
