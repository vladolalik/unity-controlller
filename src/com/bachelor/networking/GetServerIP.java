package com.bachelor.networking;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GetServerIP {

	MainActivity mainActivity;
	
	public GetServerIP(MainActivity mainActivity){
		this.mainActivity=mainActivity;
	}
	
	public void getAdress(){
		
	AsyncTask<String, Integer, String> ascTask = new AsyncTask<String, Integer, String>() {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progress dialog initialize
			pDialog = ProgressDialog.show(mainActivity, "",
					"Searching for servers...");
		};

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.d("ASYNC", "WORK1");
			return sendSocket();// result;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();

			if (result.equals("connect")) {
				Toast.makeText(mainActivity, "No internet connection",
						Toast.LENGTH_SHORT).show();
			} else if (result.equals("broadcastAddress")) {
				Toast.makeText(mainActivity,
						"Unable to get a broadcast address",
						Toast.LENGTH_SHORT).show();
			} else if (result.equals("sendBroadcast")) {
				Toast.makeText(mainActivity,
						"Unable to send broadcast message",
						Toast.LENGTH_SHORT).show();
			} else if (result.equals("receive")) {
				Toast.makeText(mainActivity,
						"Unable to receive message from server",
						Toast.LENGTH_SHORT).show();
			} else if (result.equals("response")) {
				Toast.makeText(mainActivity,
						"Bad response from server", Toast.LENGTH_SHORT)
						.show();
			} else {
				mainActivity.aServerIP = result.split(";");
				mainActivity.showNoticeDialog(mainActivity.aServerIP);
			}
			if (mainActivity.serverIP != null) {
				mainActivity.startService();
			}

		}

		/**
		 * Funkcia zisti broadcast adresu siete, v ktorej sa nachádza
		 * telefon
		 * 
		 * @return InetAddress
		 * @throws IOException
		 */
		InetAddress getBroadcastAddress() throws IOException {
			WifiManager wifi = (WifiManager) mainActivity.getSystemService(Context.WIFI_SERVICE);
			DhcpInfo dhcp = wifi.getDhcpInfo();

			int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
			byte[] quads = new byte[4];
			for (int i = 0; i < 4; i++) {
				quads[i] = (byte) ((broadcast >> i * 8) & 0xFF);
			}
			Log.d("broadcast adress is", InetAddress.getByAddress(quads)
					.toString());
			return InetAddress.getByAddress(quads);

		}

		/**
		 * Funkcia posle broadcast packet, ked dostane odpoved od servera,
		 * vrati jeho IP adresu
		 * 
		 * @return InetAddress
		 */
		String sendSocket() {
			int port = mainActivity.getResources().getInteger(R.integer.PORT);
			mainActivity.socket = null;
			if (mainActivity.socket == null) {
				try {
					mainActivity.socket = new DatagramSocket(null);
					mainActivity.socket.setReuseAddress(true);
					mainActivity.socket.setBroadcast(true);
					mainActivity.socket.bind(new InetSocketAddress(port));
				} catch (SocketException e) {
					e.printStackTrace();
					return "connect";
				}

			}
			// posielanie broadcast pre zistenie servera
			String data = mainActivity.getResources().getString(
					R.string.CONNECT_TO_SERVER);
			DatagramPacket packet;
			try {
				packet = new DatagramPacket(data.getBytes("UTF-8"), data.length(),
						getBroadcastAddress(), mainActivity.getResources().getInteger(
								R.integer.PORT));
			} catch (IOException e) {
				e.printStackTrace();
				return "broadcastAddress";
			}
			try {
				mainActivity.socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
				return "sendBroadcast";
			}

			try {
				mainActivity.socket = new DatagramSocket(null);
				mainActivity.socket.setReuseAddress(true);
				mainActivity.socket.setBroadcast(true);
				mainActivity.socket.bind(new InetSocketAddress(port));
			} catch (IOException e) {
				e.printStackTrace();
				return "receive";
			}
			String addresses = "";
			for (int i = 0; i < mainActivity.NUM_OF_SERVER; i++) {
				byte[] buf = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(buf,
						buf.length);
				try {
					mainActivity.socket.setSoTimeout(2000);
					mainActivity.socket.receive(receivePacket);
				} catch (IOException e) {
					// e.printStackTrace();
					// return "receive";
					Log.d("receive", "unable to receive response");
					continue;
				}

				String response = "";
				try {
					response = new String(buf, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response = response.substring(0, receivePacket.getLength());
				Log.d("Message", response);

				InetAddress IP = receivePacket.getAddress();
				// receivePacket.
				// Log.d("serverIP",serverIP.toString());

				Log.d("serverIPPP", IP.toString());
				if (response.equals(mainActivity.getResources().getString(
						R.string.CONNECT_ACCEPT))) {
					addresses += ";" + IP.toString().replace("/", "");
				}
			}
			try {
				if (addresses.equals("")) {
					throw (new UnsupportedEncodingException());
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "receive";
			}
			addresses = addresses.substring(1);
			Log.d("ADRESY", addresses);
			return addresses;

		}
	};

	checkWifiState(ascTask);

}

void checkWifiState(AsyncTask<String, Integer, String> ascTask) {
	String service = Context.CONNECTIVITY_SERVICE;
	ConnectivityManager connectivity = (ConnectivityManager) mainActivity.getSystemService(service);
	NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
	boolean isWifi = false;
	if (activeNetwork != null)
		isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
	if (isWifi) {
		Log.d("WIFI", "fungujeee");
		ascTask.execute();
	} else {
		Log.d("WIFI", "nefunguje");
		Intent intent1 = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
		intent1.putExtra("only_access_points", true);
		intent1.putExtra("extra_prefs_show_button_bar", true);
		intent1.putExtra("wifi_enable_next_on_connect", true);
		mainActivity.startActivityForResult(intent1, mainActivity.ACTION_SWITCH_ON_WIFI);
		Log.d("WIFI", "Show must go on!");
	}
}
}
