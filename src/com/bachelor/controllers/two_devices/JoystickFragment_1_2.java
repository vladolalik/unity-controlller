package com.bachelor.controllers.two_devices;

import java.net.DatagramSocket;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.bachelor.controllers.one_device.MyOnTouchListener;
import com.bachelor.networking.SendMessageMain;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;


public class JoystickFragment_1_2 extends Fragment {

	DatagramSocket socket;
	//InetAddress serverIP;
	Vibrator vibrator;
	ImageView imgViewMov;
	ImageView joystickMidMov;
	float dx, dy, _xDelta, _yDelta;




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/*try {
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
		}*/
		// Inflate the layout for this fragment
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		View view = inflater.inflate(R.layout.joystick_fragment_1_2, container,
				false);
		vibrator = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);

		imgViewMov = (ImageView) view.findViewById(R.id.imgViewJoystickMov);
		joystickMidMov = (ImageView) view
				.findViewById(R.id.imgViewJoystickMidMov);
		imgViewMov.setOnTouchListener(new MyOnTouchListener(imgViewMov, joystickMidMov, getActivity().getResources().getString(R.string.JOYSTICK_MV), (MainActivity)getActivity()));
		Button action = (Button) view.findViewById(R.id.ac_button);
		action.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.ACTION_MESSAGE));
			}
		});
		
		Button quit=(Button)view.findViewById(R.id.quit_4);
		quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage(getResources().getString(R.string.MSG_QUIT));
			}
		});

		return view;
	}
	
	private void sendMessage(String msg){
		new SendMessageMain((MainActivity)getActivity()).execute(msg);
	}

}
