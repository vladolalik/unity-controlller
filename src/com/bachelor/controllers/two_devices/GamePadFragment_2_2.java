package com.bachelor.controllers.two_devices;


import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bachelor.controllers.one_device.MyOnTouchListener;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

public class GamePadFragment_2_2 extends Fragment {

	DatagramSocket socket;
	InetAddress serverIP;
	Vibrator vibrator;
	ImageView imgView, joystickMiddle;

	/*
	 * public GamePadFragment(InetAddress serverIP, FragmentActivity
	 * mainActivity){ this.serverIP=serverIP; this.mainActivity=mainActivity; }
	 */

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			String ip = getArguments().getString(
					getResources().getString(R.string.IP_ADRESS));
			if (ip != null) {
				ip = ip.replace("/", "");
				Log.d("GAMEPAD ip", ip);
				serverIP = InetAddress.getByName(ip);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Log.d("GAMEPAD", "bad format of ip");
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("GAMEPAD", "parameter ip not found");
			e.printStackTrace();
		}
		// Inflate the layout for this fragment
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		View view = inflater.inflate(R.layout.gamepad_fragment_2_2, container,
				false);

		imgView = (ImageView) view.findViewById(R.id.imgViewJoyStick);
		joystickMiddle = (ImageView) view
				.findViewById(R.id.imageViewJoyStickMiddle);
		imgView.setOnTouchListener(new MyOnTouchListener(imgView,
				joystickMiddle, getResources().getString(R.string.rotate),
				(MainActivity) getActivity()));
		return view;
	}

}