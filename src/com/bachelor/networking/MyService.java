package com.bachelor.networking;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Timer;

import com.example.resultrecdemo.R;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
				byte[] buf = new byte[65535];
				DatagramPacket receivePacket = new DatagramPacket(buf,
						buf.length);
				socket.receive(receivePacket);
				byte[] flag=new byte[3];
				
				System.arraycopy(buf, 0, flag, 0, 3);
				if (new String(flag).equals("IMG")){
					byte[] imgData=new byte[65535];
					System.arraycopy(buf, 3, imgData, 0, buf.length-3);
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
			} catch (Exception e) {
				Log.d("SERVICE", "exception");
				e.printStackTrace();
			}
		}
	}
	
	class SavePhotoTask extends AsyncTask<byte[], String, String> {
	    @Override
	    protected String doInBackground(byte[]... jpeg) {
	      File dir= new File(Environment.getExternalStorageDirectory()+"/unity-controller/");
	      dir.mkdirs();
	      File photo=new File(dir, "photo.jpg");
	      Log.d("photo", photo.getAbsolutePath());
	      if (photo.exists()) {
	            photo.delete();
	      }

	      try {
	        FileOutputStream fos=new FileOutputStream(photo.getPath());

	        fos.write(jpeg[0]);
	        fos.close();
	      }
	      catch (java.io.IOException e) {
	        Log.e("PictureDemo", "Exception in photoCallback", e);
	      }

	      return(null);
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