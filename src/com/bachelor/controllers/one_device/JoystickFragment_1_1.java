package com.bachelor.controllers.one_device;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

public class JoystickFragment_1_1 extends Fragment  {

	Vibrator vibrator;
	ImageView imgView;
	ImageView joystickMiddle;
	ImageView imgViewMov;
	ImageView joystickMidMov;
	float dx, dy, _xDelta, _yDelta;


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
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		View view = inflater.inflate(R.layout.joystick_fragment_1_1, container,
				false);
		vibrator = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);

		imgView = (ImageView) view.findViewById(R.id.imgViewJoyStick);
		joystickMiddle = (ImageView) view
				.findViewById(R.id.imageViewJoyStickMiddle);
		imgViewMov = (ImageView) view.findViewById(R.id.imgViewJoystickMov);
		joystickMidMov = (ImageView) view
				.findViewById(R.id.imgViewJoystickMidMov);
		imgView.setOnTouchListener(new MyOnTouchListener(imgView, joystickMiddle, getActivity().getResources().getString(R.string.JOYSTICK_ROTATE), (MainActivity)getActivity()));
		imgViewMov.setOnTouchListener(new MyOnTouchListener(imgViewMov, joystickMidMov, getActivity().getResources().getString(R.string.JOYSTICK_MV), (MainActivity)getActivity()));
		return view;
	}

	



}
