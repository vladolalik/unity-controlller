package com.bachelor.networking;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.resultrecdemo.R;

public class MyService extends IntentService {

	public MyService() {
		super("MyService");
		// TODO Auto-generated constructor stub
	}

	// Timer timer = new Timer();
	// MyTimerTask timerTask;
	ResultReceiver resultReceiver;
	DatagramSocket socket;

	public boolean run = true;

	public class StopReceiver extends BroadcastReceiver {

		public static final String ACTION_STOP = "stop";

		@Override
		public void onReceive(Context context, Intent intent) {
			run = false;
			Log.d("STOP RECIEVE", "STAHP");
		}
	}

	public void receiveMessg() {
		Log.d("START", "SERVICeE");
		while (run) {
			try {
				if (socket == null) {
					socket = new DatagramSocket(null);
					socket.setReuseAddress(true);
					socket.setBroadcast(true);
					socket.bind(new InetSocketAddress(getResources()
							.getInteger(R.integer.PORT)));
				}
				// prijatie odpovede od servera
				byte[] buf = new byte[65535];
				DatagramPacket receivePacket = new DatagramPacket(buf,
						buf.length);
				socket.setSoTimeout(10);
				socket.receive(receivePacket);
				byte[] flag = new byte[3];

				System.arraycopy(buf, 0, flag, 0, 3);
				if (new String(flag).equals("IMG")) {
					byte[] imgData = new byte[65535];
					System.arraycopy(buf, 3, imgData, 0, buf.length - 3);
					new SavePhotoTask().execute(imgData);
				}
				String sentence = new String(receivePacket.getData()).trim();

				Log.d("Message", sentence.trim());
				// publishProgress(sentence);
				// serverIP = receivePacket.getAddress();
				Bundle bundle = new Bundle();
				bundle.putString("msg", sentence);
				resultReceiver.send(10, bundle);
				Log.d("MSG SERVER", sentence);
				// socket.close();
			} catch (Exception e) {
				// Log.d("SERVICE", "exception");
				// e.printStackTrace();
			}
		}
		unregisterReceiver(receiver);
		Log.d("MyService", "STAHP");
		stopSelf();

	}

	class SavePhotoTask extends AsyncTask<byte[], String, String> {
		@Override
		protected String doInBackground(byte[]... jpeg) {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ "/unity-controller/");
			dir.mkdirs();
			File photo = new File(dir, "photo.jpg");
			Log.d("photo", photo.getAbsolutePath());
			if (photo.exists()) {
				photo.delete();
			}

			try {
				FileOutputStream fos = new FileOutputStream(photo.getPath());

				fos.write(jpeg[0]);
				fos.close();
			} catch (java.io.IOException e) {
				Log.e("PictureDemo", "Exception in photoCallback", e);
			}

			return (null);
		}

		@Override
		protected void onPostExecute(String result) {
			Bundle bundle = new Bundle();
			bundle.putString("msg", "IMAGE");
			resultReceiver.send(10, bundle);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	StopReceiver receiver;

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		resultReceiver = intent.getParcelableExtra("receiver");

		try {
			if (socket == null) {
				socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.setBroadcast(true);
				socket.bind(new InetSocketAddress(getResources().getInteger(
						R.integer.PORT)));
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IntentFilter filter = new IntentFilter(StopReceiver.ACTION_STOP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new StopReceiver();
		registerReceiver(receiver, filter);

		// Do stuff ....

		// In the work you are doing

		receiveMessg();
	}

}