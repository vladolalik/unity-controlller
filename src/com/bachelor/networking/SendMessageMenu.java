package com.bachelor.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.example.resultrecdemo.R;

import android.os.AsyncTask;
import android.util.Log;

public class SendMessageMenu extends AsyncTask<String, String, String> {

	InetAddress ip;
	Integer port;
	DatagramSocket socket = null;
		
	public SendMessageMenu(InetAddress ip, Integer port){
		this.ip=ip;
		this.port=port;
	}
		
		
		@SuppressWarnings("resource")
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.d("sending", params[0]);
			//Log.d("ServerIP when sending", mainActivity.serverIP.toString());
			
			
			//int port = mainActivity.getResources().getInteger(R.integer.PORT);
			try {
				socket = null;
				/*if (mainActivity.socket == null) {
					
						mainActivity.socket = new DatagramSocket(null);
						mainActivity.socket.setReuseAddress(true);
						//mainActivity.socket.setBroadcast(true);
						mainActivity.socket.bind(new InetSocketAddress(port));
					
				}*/
				socket=new DatagramSocket();
				byte[] buf = params[0].getBytes("UTF-8");
				DatagramPacket packet = new DatagramPacket(buf, buf.length,
						ip, port);
				socket.send(packet);
				//mainActivity.socket.close();
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


