package com.example.resultrecdemo;

import info.androidhive.actionbar.model.SpinnerNavItem;
import info.androidhive.info.actionbar.adapter.TitleNavigationAdapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener, OnClickListener,
		ServersDialogFragment.NoticeDialogListener {

	Intent intent;
	MyResultReceiver resultReceiver;
	InetAddress serverIP;
	Boolean stop = false;
	Button btn;
	DatagramSocket socket;
	final Integer ACTION_SWITCH_ON_WIFI = 1;
	final Integer SETTING_RESULT = 2;
	final Integer NUM_OF_SERVER = 3;
	CharSequence[] aServerIP; // list of servers
	// final String NUMBER_OF_DEVICES = "SetNoDev";
	SharedPreferences prefs;
	// action bar
	private ActionBar actionBar;
	private Integer lastSelectedItemActionBar;
	private boolean serverRequest = false;
	// Title navigation Spinner data
	private ArrayList<SpinnerNavItem> navSpinner;
	// Navigation adapter
	private TitleNavigationAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("MainActivity", "onCreate");
		// action bar
		actionBar = getActionBar();

		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(false);
		
		// Enabling Spinner dropdown navigation
		// nastavenie menu, priradenie obrazkov k jednotlivym polozkam
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		String[] actionMenuItems = getResources().getStringArray(
				R.array.action_list);
		navSpinner = new ArrayList<SpinnerNavItem>();
		navSpinner.add(new SpinnerNavItem(actionMenuItems[0],
				R.drawable.ic_action_dock));
		navSpinner.add(new SpinnerNavItem(actionMenuItems[1],
				R.drawable.touchpad_icon));
		navSpinner.add(new SpinnerNavItem(actionMenuItems[2],
				R.drawable.circle_icon));
		navSpinner.add(new SpinnerNavItem(actionMenuItems[3],
				R.drawable.gamepad));
		navSpinner.add(new SpinnerNavItem(actionMenuItems[4],
				R.drawable.gyroscope_icon));

		// title drop down adapter
		adapter = new TitleNavigationAdapter(getApplicationContext(),
				navSpinner);

		// assigning the spinner navigation
		actionBar.setListNavigationCallbacks(adapter, this);
		// ////////////
		lastSelectedItemActionBar = actionBar.getSelectedNavigationIndex();
		
		// getServerIP(); // make connection with server

		// shared preferences
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		//addDynamicFragment(); // add frament

	}

	public void showNoticeDialog(CharSequence[] s) {
		// Create an instance of the dialog fragment and show it
		DialogFragment dialog = new ServersDialogFragment(s);
		dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
	}

	// The dialog fragment receives a reference to this Activity through the
	// Fragment.onAttach() callback, which it uses to call the following methods
	// defined by the NoticeDialogFragment.NoticeDialogListener interface
	/**
	 * Spracovanie udalosti pri vybere ip adresy serveru z dialogoveho okna
	 */
	@Override
	public void onDialogItemClick(DialogFragment dialog, int id) {
		// User touched the dialog's positive button
		startService();
		Log.d("Dialog", "Positive button");
		if (serverIP != null) {
			try {
				if (serverIP.toString().equals(
						InetAddress.getByName((String) aServerIP[id])
								.toString())) {
					Log.d("Changeserver", "the same ip");
					Toast.makeText(this,
							"You are already connected to this server",
							Toast.LENGTH_SHORT).show();
				} else {
					serverIP = InetAddress.getByName((String) aServerIP[id]);
				}
			} catch (UnknownHostException e) {
				Toast.makeText(this, "Unexpected error occured",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		} else {
			try {
				serverIP = InetAddress.getByName((String) aServerIP[id]);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Unable to make connection with server",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
		invalidateOptionsMenu();
		Toast.makeText(this, "Connected to server " + aServerIP[id],
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Spracovanie udalosti pri kliknuti na polozku v menu
	 * */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		// pripojenie k serveru prvy krat
		case R.id.action_connect:
			if (serverIP == null)
				getServerIP();
			else
				Toast.makeText(this, "You are already connected to server",
						Toast.LENGTH_SHORT).show();
			return true;
		// spustenie aktivity s nastaveniami
		case R.id.action_settings:
			Intent i = new Intent(this, SettingsActivity.class);
			Log.d("Starting", "setting activity");
			startActivityForResult(i, SETTING_RESULT);
			return true;
		// vyhladanie novych serverov
		case R.id.action_change_server:
			Log.d("ACTION", "Change server");
			// serverIP = null;
			if (intent != null) {
				stopService(intent);
			}
			intent = null;
			getServerIP();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Spustenie servisu, ktory bezi na samostanom vlakne a spracovava spravy od
	 * serveru
	 */
	public void startService() {
		if (intent == null) {
			Log.d("Intent", "Started");
			resultReceiver = new MyResultReceiver(null);
			intent = new Intent(getApplicationContext(), MyService.class);
			intent.putExtra("receiver", resultReceiver);
			// intent.putExtra("serverIP", serverIP);
			startService(intent);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("MainActivity", "onRestart");
		changeControlFragment(0);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		//po navrate sa zobrazi home fragment
		actionBar.setSelectedNavigationItem(0);
		changeControlFragment(0);
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("onDestroy", "executed");
		serverIP = null;
		if (intent != null) {
			stopService(intent);
		}
	}

	/**
	 * 
	 * Trieda ktora po spracovani spravy od receiveru updatne pouzivatelske
	 * prostredie
	 * 
	 */
	class UpdateUI implements Runnable {
		String msgFromServer;
		int my_number;
		int count_of_dev;
		int numControl = -1;

		public UpdateUI(String msgFromServer) {
			this.msgFromServer = msgFromServer;

			// message for setting number of clients and number of my device
			if (msgFromServer.contains(getResources().getString(
					R.string.SET_NUMBER_OF_DEVICES))) {
				try {
					String[] data = msgFromServer.split(" ");

					// save data

					my_number = Integer.parseInt(data[2]);
					count_of_dev = Integer.parseInt(data[1]);

					Log.d("My number from server", data.toString());
					Log.d("Count of devices", Integer.toString(count_of_dev));
					Editor editor = prefs.edit();
					editor.putInt(
							getResources().getString(R.string.DEV_NUMBER),
							my_number);
					editor.putInt(
							getResources().getString(R.string.COUNT_OF_DEVICES),
							count_of_dev);
					editor.commit();
					Toast.makeText(MainActivity.this,
							"Server set my number of dev to " + my_number,
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Log.d("ERROR", "SETTING NUM OF DEV");
					e.printStackTrace();
				}
			}

			// message with request for change a control
			if (this.msgFromServer.contains(getResources().getString(
					R.string.TYPE_OF_CONTROL))) {
				try {
					String[] typeOfControls = getResources().getStringArray(
							R.array.typesOfControl);
					String[] data = this.msgFromServer.split(" ");
					for (int i = 0; i < typeOfControls.length; i++) {
						if (data[1].equals(typeOfControls[i])) {
							Log.d("TypeOfControl", typeOfControls[i]);
							changeControlFragment(i);
							numControl = i;
						}
					}

				} catch (Exception e) {
					Log.d("ERROR", "PARSE MESSAGE FROM SERVER");
					e.printStackTrace();
				}

			}
		}

		public void run() {
			if (msgFromServer.contains(getResources().getString(
					R.string.SET_NUMBER_OF_DEVICES))) {
				Toast.makeText(
						MainActivity.this,
						"Server set my number of dev to " + my_number + " "
								+ count_of_dev, Toast.LENGTH_SHORT).show();
			}
			// zmenim aktivnu polozku v spinner menu
			if (numControl != -1) {
				serverRequest = true;
				actionBar.setSelectedNavigationItem(numControl);
			}
		}
	}

	/**
	 * 
	 * @author Brutal
	 * 
	 *         ResultReceiver spracovava spravy od servisu ktory bezi na
	 *         samostatnom vlakne a prijima spravy od serveru
	 */
	class MyResultReceiver extends ResultReceiver {
		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {

			if (resultCode == 100) {
				runOnUiThread(new UpdateUI(resultData.getString("start")));
			} else if (resultCode == 200) {
				runOnUiThread(new UpdateUI(resultData.getString("end")));
			} else if (resultCode == 10) {
				runOnUiThread(new UpdateUI(resultData.getString("msg")));
			} else {
				runOnUiThread(new UpdateUI("Message from server > "
						+ resultData.getString("msg")));
			}
		}
	}

	/**
	 * Funkcia ktora zachytava result zo SettingActivity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ACTION_SWITCH_ON_WIFI) {
			if (resultCode == RESULT_OK) {
				Log.d("Intent", "Calling getServerIP");
				getServerIP();
				// tvResult.setText(data.getIntExtra("result",-1)+"");
			}
		}
		if (requestCode == SETTING_RESULT) {
			if (resultCode == RESULT_OK) {
				Log.d("Settings", "Change");
				int my_number = prefs.getInt(
						getResources().getString(R.string.DEV_NUMBER), 1);
				int count_of_dev = prefs.getInt(
						getResources().getString(R.string.COUNT_OF_DEVICES), 1);
				sendMessage(getResources().getString(R.string.MSG_DEV_NUMBER)
						+ " " + count_of_dev + " " + my_number);
			}
		}

	}

	@Override
	/**
	 * Funkcia ktora akualizuje menu (zmeni ikonku ked sa podari spojit so serverom
	 * 
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem switchButton = menu.findItem(R.id.action_connect);
		if (serverIP != null) {
			switchButton.setIcon(R.drawable.hardware_server);
		} else {
			switchButton.setIcon(R.drawable.hardware_server_uncon);
		}
		return super.onPrepareOptionsMenu(menu);

	}

	/**
	 * Funckia ktora pouzije AsyncTask na ziskanie IP adries dostupnych serverov
	 * v danej wifi sieti. Ak wifi nie je zapnute tak sa spusti intent ktory
	 * vyzve pouzivatela aby sa pripojil k wifi, inak nebude pokracovat v
	 * hladani serverov.
	 */
	public void getServerIP() {

		AsyncTask<String, Integer, String> ascTask = new AsyncTask<String, Integer, String>() {

			private ProgressDialog pDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// progress dialog initialize
				pDialog = ProgressDialog.show(MainActivity.this, "",
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
					Toast.makeText(MainActivity.this, "No internet connection",
							Toast.LENGTH_SHORT).show();
				} else if (result.equals("broadcastAddress")) {
					Toast.makeText(MainActivity.this,
							"Unable to get a broadcast address",
							Toast.LENGTH_SHORT).show();
				} else if (result.equals("sendBroadcast")) {
					Toast.makeText(MainActivity.this,
							"Unable to send broadcast message",
							Toast.LENGTH_SHORT).show();
				} else if (result.equals("receive")) {
					Toast.makeText(MainActivity.this,
							"Unable to receive message from server",
							Toast.LENGTH_SHORT).show();
				} else if (result.equals("response")) {
					Toast.makeText(MainActivity.this,
							"Bad response from server", Toast.LENGTH_SHORT)
							.show();
				} else {
					aServerIP = result.split(";");
					showNoticeDialog(aServerIP);
				}
				if (serverIP != null) {
					startService();
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
				WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
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
				int port = getResources().getInteger(R.integer.PORT);
				socket = null;
				if (socket == null) {
					try {
						socket = new DatagramSocket(null);
						socket.setReuseAddress(true);
						socket.setBroadcast(true);
						socket.bind(new InetSocketAddress(port));
					} catch (SocketException e) {
						e.printStackTrace();
						return "connect";
					}

				}
				// posielanie broadcast pre zistenie servera
				String data = getResources().getString(
						R.string.CONNECT_TO_SERVER);
				DatagramPacket packet;
				try {
					packet = new DatagramPacket(data.getBytes("UTF-8"), data.length(),
							getBroadcastAddress(), getResources().getInteger(
									R.integer.PORT));
				} catch (IOException e) {
					e.printStackTrace();
					return "broadcastAddress";
				}
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
					return "sendBroadcast";
				}

				try {
					socket = new DatagramSocket(null);
					socket.setReuseAddress(true);
					socket.setBroadcast(true);
					socket.bind(new InetSocketAddress(port));
				} catch (IOException e) {
					e.printStackTrace();
					return "receive";
				}
				String addresses = "";
				for (int i = 0; i < NUM_OF_SERVER; i++) {
					byte[] buf = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(buf,
							buf.length);
					try {
						socket.setSoTimeout(2000);
						socket.receive(receivePacket);
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
					if (response.equals(getResources().getString(
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
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(service);
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
			startActivityForResult(intent1, ACTION_SWITCH_ON_WIFI);
			Log.d("WIFI", "Show must go on!");
		}
	}

	/**
	 * Funkcia spracuje spravu od servera
	 * 
	 * @param msg
	 */
	public void proccessMessage(String msg) {
		Log.d("proccessMessage", msg);
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

					byte[] buf = params[0].getBytes("UTF-8");
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
			Toast.makeText(MainActivity.this,
					"Please make connection with server", Toast.LENGTH_SHORT)
					.show();
		} else {
			asyncTsk.execute(msg);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		// String[]
		// actionMenuItems=getResources().getStringArray(R.array.action_list);
		if(itemPosition==lastSelectedItemActionBar && !serverRequest){
			return true;
		}
		
		String[] typeOfControls = getResources().getStringArray(
				R.array.typesOfControl);
		
		// home screen moze byt zobrazeny aj bez schvalenia serverom
		if (itemPosition==0){
			actionBar.setIcon(R.drawable.ic_action_dock);
			changeControlFragment(0);
			return true;
		}
		
		
		// ak to nie je sprava od servera tak len poslem poziadavku serveru
		if (!serverRequest) {
			actionBar.setSelectedNavigationItem(lastSelectedItemActionBar);
			//serverRequest=true; // nastavim spravu od servera na true aby som sa vyhol posielaniu druhej spravy s uz zvolenym typom ovladania
			///
			
			Log.d("SELECTED ITEM",
					String.valueOf(actionBar.getSelectedNavigationIndex()));
			// Log.d("ACTION menu", actionMenuItems[itemPosition]);

			String changeControl = getResources()
					.getString(R.string.CHANGE_CONTROL);
			switch (itemPosition) {
			case 0:
				return true;

			case 1:
				sendMessage(changeControl + " " + typeOfControls[1]);
				break;

			case 2:
				sendMessage(changeControl + " " + typeOfControls[2]);
				Log.d("pokus", "2");
				return true;

			case 3:
				sendMessage(changeControl + " " + typeOfControls[3]);
				break;

			case 4:
				sendMessage(changeControl + " " + typeOfControls[4]);
				break;

			default:
				return true;

			}
			return false;
			///
		} else {
			Log.d("Spinner nav", "change selected");
			serverRequest = false;
			
			// zmenit obrazok pri navigacii aby reprezentoval prave zvoleny typ ovladania
			switch (itemPosition) {
				case 1: break;
				case 2: break;
				case 3:	actionBar.setIcon(R.drawable.gamepad);
						break;
			}
		}

		return true;
	}

	private void changeControlFragment(int i) {
		Fragment fr = null;
		lastSelectedItemActionBar = i;
		Bundle bundle=new Bundle(1);
		if (serverIP!=null){
			bundle.putString(getResources().getString(R.string.IP_ADRESS), serverIP.toString());
		} else {
			bundle.putString(getResources().getString(R.string.IP_ADRESS), null);
		}
		switch (i) {
		case 3:
			fr = new GamePadFragment1();
			fr.setArguments(bundle);
			break;
		case 0:
			fr = new HomeFragment();
			fr.setArguments(bundle);
			break;
		default:
			return;
		}
		// fr = new GamePadFragment();
		if (fr != null) {
			Log.d("Change Fragment", String.valueOf(i + 1));
			FragmentManager fm = getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, fr);
			fragmentTransaction.commit();
		}
	}

	private void addDynamicFragment() {
		// TODO Auto-generated method stub
		// creating instance of the HelloWorldFragment.
		Fragment fg = new HomeFragment();

		// adding fragment to relative layout by using layout id
		getFragmentManager().beginTransaction()
				.add(R.id.fragment_container, fg).commit();
	}

	protected void setOrientation() {
		int current = getRequestedOrientation();
		// only switch the orientation if not in portrait
		if (current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

}