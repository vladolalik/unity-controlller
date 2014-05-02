package com.bachelor.networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Timer;

import com.example.resultrecdemo.R;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class MyService extends IntentService {

	public MyService() {
		super("MyService");
		// TODO Auto-generated constructor stub
	}

	Timer timer = new Timer();
	// MyTimerTask timerTask;
	ResultReceiver resultReceiver;
	DatagramSocket socket;


	public void receiveMessg() {
		Log.d("START", "SERVICeE");
		while (true) {
			try {
				//if (socket == null) {
					socket = new DatagramSocket(null);
					socket.setReuseAddress(true);
					socket.setBroadcast(true);
					socket.bind(new InetSocketAddress(getResources().getInteger(R.integer.PORT)));
				//}

				// prijatie odpovede od servera

				byte[] buf = new byte[4096];
				DatagramPacket receivePacket = new DatagramPacket(buf,
						buf.length);
				socket.receive(receivePacket);
				String sentence = new String(receivePacket.getData()).trim();
				
				Log.d("Message", sentence.trim());
				// publishProgress(sentence);
				// serverIP = receivePacket.getAddress();
				Bundle bundle = new Bundle();
				bundle.putString("msg", sentence);
				resultReceiver.send(10, bundle);
				Log.d("MSG SERVER", sentence);
			} catch (Exception e) {
				Log.d("SERVICE", "exception");
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		resultReceiver = intent.getParcelableExtra("receiver");

		try {
			if (socket == null) {
				socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.setBroadcast(true);
				socket.bind(new InetSocketAddress(getResources().getInteger(R.integer.PORT)));
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		receiveMessg();
	}

}