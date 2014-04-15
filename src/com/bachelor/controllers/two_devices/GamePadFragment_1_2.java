package com.bachelor.controllers.two_devices;

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

		return view;
	}

	public void sendMessage(String msg) {
		new SendMessage((MainActivity)getActivity()).execute(msg);
		/*AsyncTask<String, String, String> asyncTsk = new AsyncTask<String, String, String>() {

			@SuppressWarnings("resource")
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.d("sending", params[0]);
				DatagramSocket socket = null;
				int port = getResources().getInteger(R.integer.PORT);
				try {
					socket = new DatagramSocket();
					byte[] buf = params[0].getBytes();
					DatagramPacket packet = new DatagramPacket(buf, buf.length,
							serverIP, port);// getResources().getInteger(R.integer.PORT)
					socket.send(packet);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		Log.d("send", msg);
		if (serverIP == null) {
			Toast.makeText(getActivity(), "Please make connection with server",
					Toast.LENGTH_SHORT).show();
		} else {
			asyncTsk.execute(msg);
		}*/
	}

}
