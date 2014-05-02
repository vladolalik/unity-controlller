package com.bachelor.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

import android.os.AsyncTask;
import android.util.Log;

public class SendMessageMain extends AsyncTask<String, String, String> {

	MainActivity mainActivity;
		
	public SendMessageMain(MainActivity mainActivity){
		this.mainActivity=mainActivity;
	}
		
		
		@SuppressWarnings("resource")
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.d("sending", params[0]);
			Log.d("ServerIP when sending", mainActivity.serverIP.toString());
			DatagramSocket socket = null;
			
			int port = mainActivity.getResources().getInteger(R.integer.PORT);
			try {
				socket = new DatagramSocket(null);
				byte[] buf = params[0].getBytes("UTF-8");
				DatagramPacket packet = new DatagramPacket(buf, buf.length,
						mainActivity.serverIP, port);
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
}

