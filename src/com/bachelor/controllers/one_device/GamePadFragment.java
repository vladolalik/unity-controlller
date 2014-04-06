package com.bachelor.controllers.one_device;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.example.resultrecdemo.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


@SuppressLint("ValidFragment") public class GamePadFragment extends Fragment{
	
	DatagramSocket socket;
	InetAddress serverIP;
	
	/*public GamePadFragment(InetAddress serverIP, FragmentActivity mainActivity){
		this.serverIP=serverIP;
		this.mainActivity=mainActivity;	
	}*/
	
	@Override
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
	      
		 try {
			String ip = getArguments().getString(getResources().getString(R.string.IP_ADRESS));
			if (ip!=null){
				ip=ip.replace("/", "");
				Log.d("GAMEPAD ip", ip);
				serverIP=InetAddress.getByName(ip);
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
	       //Inflate the layout for this fragment
		 getActivity().setRequestedOrientation(
		            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 View view = inflater.inflate(R.layout.gamepad_fragment, container, false);
		 Button mvLeft = (Button) view.findViewById(R.id.buttonMovLeft);
		 mvLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Click", "mvLeft");
				sendMessage(getResources().getString(R.string.JOYSTICK_MV_LEFT));
			}
		 });
		 
		 Button mvRight = (Button) view.findViewById(R.id.buttonMovRight);
		 mvRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Click", "mvRight");
				sendMessage(getResources().getString(R.string.JOYSTICK_MV_RIGHT));
			}
		 });

		 Button mvUp = (Button) view.findViewById(R.id.buttonMovFV);
		 mvUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Click", "mvUp");
				sendMessage(getResources().getString(R.string.JOYSTICK_MV_FV));
			}
		 });
		 
		 Button mvDown = (Button) view.findViewById(R.id.buttonMovBack);
		 mvDown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Click", "mvDown");
				sendMessage(getResources().getString(R.string.JOYSTICK_MV_BACK));
			}
		 });
		 
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
		AsyncTask<String, String, String> asyncTsk = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.d("sending", params[0]);
				int port = getResources().getInteger(R.integer.PORT);
				try {
					if (socket == null) {
						socket = new DatagramSocket(null);
						socket.setReuseAddress(true);
						socket.setBroadcast(true);
						socket.bind(new InetSocketAddress(port));
					}

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
			Toast.makeText(getActivity(),
					"Please make connection with server", Toast.LENGTH_SHORT)
					.show();
		} else {
			asyncTsk.execute(msg);
		}
	}
	
	

}
