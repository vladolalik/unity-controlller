package com.bachelor.controllers.one_device;

import com.bachelor.networking.SendMessage;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


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
		mvLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vibrator.vibrate(50);
				Log.d("Click", "mvLeft");
				sendMessage(getResources().getString(R.string.JOYSTICK_MV_LEFT));

			}
		});

		Button mvRight = (Button) view.findViewById(R.id.buttonMovRight);
		mvRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vibrator.vibrate(50);
				Log.d("Click", "mvRight");
				sendMessage(getResources()
						.getString(R.string.JOYSTICK_MV_RIGHT));

			}
		});

		Button mvUp = (Button) view.findViewById(R.id.buttonMovFV);
		mvUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vibrator.vibrate(50);
				Log.d("Click", "mvUp");
				sendMessage(getResources().getString(R.string.JOYSTICK_MV_FV));

			}
		});

		Button mvDown = (Button) view.findViewById(R.id.buttonMovBack);
		mvDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vibrator.vibrate(50);
				Log.d("Click", "mvDown");
				sendMessage(getResources().getString(R.string.JOYSTICK_MV_BACK));
			}
		});

		imgView = (ImageView) view.findViewById(R.id.imgViewJoyStick);
		joystickMiddle = (ImageView) view
				.findViewById(R.id.imageViewJoyStickMiddle);
		imgView.setOnTouchListener(new MyOnTouchListener(imgView, joystickMiddle, getResources().getString(R.string.rotate), (MainActivity)getActivity()));
		return view;
	}

	public void sendMessage(String msg) {
		new SendMessage((MainActivity)getActivity()).execute(msg);
	}

}
