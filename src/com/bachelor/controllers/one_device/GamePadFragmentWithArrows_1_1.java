package com.bachelor.controllers.one_device;

import com.bachelor.networking.SendMessageMain;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class GamePadFragmentWithArrows_1_1 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		View view = inflater.inflate(R.layout.gamepad_fragment_arrows_1_1,
				container, false);
		Button mvLeft = (Button) view.findViewById(R.id.buttonMovLeft);
		mvLeft.setOnTouchListener(new ButtonHoldingOnTouchListener(
				getResources().getString(R.string.JOYSTICK_MV_LEFT),
				(MainActivity) getActivity()));

		final Button mvRight = (Button) view.findViewById(R.id.buttonMovRight);
		mvRight.setOnTouchListener(new ButtonHoldingOnTouchListener(
				getResources().getString(R.string.JOYSTICK_MV_RIGHT),
				(MainActivity) getActivity()));

		Button mvUp = (Button) view.findViewById(R.id.buttonMovFV);
		mvUp.setOnTouchListener(new ButtonHoldingOnTouchListener(getResources()
				.getString(R.string.JOYSTICK_MV_FV),
				(MainActivity) getActivity()));

		Button mvDown = (Button) view.findViewById(R.id.buttonMovBack);
		mvDown.setOnTouchListener(new ButtonHoldingOnTouchListener(
				getResources().getString(R.string.JOYSTICK_MV_BACK),
				(MainActivity) getActivity()));

		Button rtRight = (Button) view.findViewById(R.id.buttonRotRight);
		rtRight.setOnTouchListener(new ButtonHoldingOnTouchListener(
				getResources().getString(
						R.string.JOYSTICK_ROTATE_Y)
						+ " 1",
				(MainActivity) getActivity()));

		
		Button rtLeft = (Button) view.findViewById(R.id.buttonRotLeft);
		rtLeft.setOnTouchListener(new ButtonHoldingOnTouchListener(
				getResources().getString(
						R.string.JOYSTICK_ROTATE_Y)
						+ " 0",
				(MainActivity) getActivity()));

		
		Button rtUP = (Button) view.findViewById(R.id.buttonRotUP);
		rtUP.setOnTouchListener(new ButtonHoldingOnTouchListener(
				getResources().getString(
						R.string.JOYSTICK_ROTATE_X)
						+ " 1",
				(MainActivity) getActivity()));

		Button rtDown = (Button) view.findViewById(R.id.buttonRotDown);
		rtDown.setOnTouchListener(new ButtonHoldingOnTouchListener(
				getResources().getString(
						R.string.JOYSTICK_ROTATE_X)
						+ " 0",
				(MainActivity) getActivity()));
		Button action = (Button) view.findViewById(R.id.action_button);
		action.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.ACTION_MESSAGE));
			}
		});
		return view;
	}

	public void sendMessage(String msg) {
		new SendMessageMain((MainActivity) getActivity()).execute(msg);
	}

}
