package com.bachelor.unity_remote_control;

import info.androidhive.actionbar.model.SpinnerNavItem;
import info.androidhive.info.actionbar.adapter.TitleNavigationAdapter;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Toast;

import com.bachelor.networking.ChangeFragment;
import com.bachelor.networking.GetServerIP;
import com.bachelor.networking.MyService;
import com.bachelor.networking.MyService.StopReceiver;
import com.bachelor.networking.SendMessageMain;
import com.bachelor.networking.ServersDialogFragment;
import com.example.resultrecdemo.R;

@SuppressLint("NewApi") public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener, OnClickListener,
		ServersDialogFragment.NoticeDialogListener {

	public boolean stopService=false;
	Intent intent;
	MyResultReceiver resultReceiver;
	public InetAddress serverIP;
	Boolean stop = false;
	Button btn;
	public DatagramSocket socket;
	public final Integer ACTION_SWITCH_ON_WIFI = 1;
	final Integer SETTING_RESULT = 2;
	final Integer TEXT_VIEW_ACTIVITY=3;
	final Integer MENU_VIEW_ACTIVITY=4;
	final Integer IMAGE_VIEW_ACTIVITY=5;
	final Integer MAP_VIEW_ACTIVITY=6;
	boolean activity_result=false;
	public final Integer NUM_OF_SERVER = 3;
	public CharSequence[] aServerIP; // list of servers
	// final String NUMBER_OF_DEVICES = "SetNoDev";
	public SharedPreferences prefs;
	// action bar
	ActionBar actionBar;
	public Integer lastSelectedItemActionBar=0;
	boolean serverRequest = false;
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
		navSpinner.add(new SpinnerNavItem(actionMenuItems[5],
				R.drawable.gyroscope_icon));
		navSpinner.add(new SpinnerNavItem(actionMenuItems[6],
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
		if (intent==null){
			startServiceMy();
		}
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
		
		case R.id.action_map:
			sendMessage(getResources().getString(R.string.MAP));
			Log.d("klik", "map");
			return true;
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
			/*if (intent != null) {
				stopService(intent);
			}*/
			Intent sIntent = new Intent();
			sIntent.setAction(StopReceiver.ACTION_STOP);
			sendBroadcast(sIntent);
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
	public void startServiceMy() {
	
			if (!isMyServiceRunning()){
				Log.d("Intent", "Started");
				resultReceiver = new MyResultReceiver(null, this);
				intent = new Intent(getApplicationContext(), MyService.class);
				intent.putExtra("receiver", resultReceiver);
				startService(intent);
			}
	}
	
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (MyService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("MainActivity", "onPause");
		if (!isMyServiceRunning()){
			Log.d("servis", "was turned off");
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("MainActivity", "onRestart");
		changeControlFragment(lastSelectedItemActionBar);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		//po navrate sa zobrazi home fragment
		actionBar.setSelectedNavigationItem(0);
		Log.d("MainActivity", "onResume");
		Log.d("lastItem", String.valueOf(lastSelectedItemActionBar));
		changeControlFragment(lastSelectedItemActionBar);
		if(serverIP!=null){
				Log.d("MAIN_RES", "START SERVICE");
				startServiceMy();
		}
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("onDestroy", "executed");
		
		Intent sIntent = new Intent();
		sIntent.setAction(StopReceiver.ACTION_STOP);
		sendBroadcast(sIntent);
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
		if (requestCode == TEXT_VIEW_ACTIVITY){
			if (resultCode == RESULT_OK){
				int active_fragment=data.getIntExtra("active_fragment", 2);
				Log.d("active_fragment", String.valueOf(active_fragment));
				changeControlFragment(active_fragment);
				lastSelectedItemActionBar=active_fragment;
				activity_result=true;
			}
		}
		if (requestCode == MENU_VIEW_ACTIVITY){
			if (resultCode == RESULT_OK){
				int active_fragment=data.getIntExtra("active_fragment", 2);
				String msg=data.getStringExtra(getResources().getString(R.string.CLIENT_SEND_MENU_ITEM));
				sendMessage(getResources().getString(R.string.CLIENT_SEND_MENU_ITEM) + " " + msg);
				Log.d("menu msg", getResources().getString(R.string.CLIENT_SEND_MENU_ITEM) + " " + msg);
				changeControlFragment(active_fragment);
				lastSelectedItemActionBar=active_fragment;
				activity_result=true;
			}
			if (resultCode == RESULT_CANCELED){
				if (data!=null){
					int active_fragment=data.getIntExtra("active_fragment", 2);
					changeControlFragment(active_fragment);
					lastSelectedItemActionBar=active_fragment;
					activity_result=true;
					Log.d("MENUVIEW", "RESULT");
				} else {
					lastSelectedItemActionBar=2;
				}
			}
		}
		
		if (requestCode == IMAGE_VIEW_ACTIVITY){
			if (data!=null){
				int active_fragment=data.getIntExtra("active_fragment", 2);
				changeControlFragment(active_fragment);
				lastSelectedItemActionBar=active_fragment;
				activity_result=true;
			} else {
				lastSelectedItemActionBar=2;
			}
			activity_result=true;
		}
		
		if (requestCode == MAP_VIEW_ACTIVITY){
			if (data!=null){
				int active_fragment=data.getIntExtra("active_fragment", 2);
				changeControlFragment(active_fragment);
				lastSelectedItemActionBar=active_fragment;
				activity_result=true;
			} else {
				lastSelectedItemActionBar=2;
			}
			activity_result=true;
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
		GetServerIP getIP=new GetServerIP(this);
		getIP.getAdress();
	}

	/**
	 * Funkcia spracuje spravu od servera
	 * 
	 * @param msg
	 */
	/*public void proccessMessage(String msg) {
		Log.d("proccessMessage", msg);
	}*/

	public void sendMessage(String msg) {

		Log.d("send", msg);
		if (serverIP == null) {
			Toast.makeText(MainActivity.this,
					"Please make connection with server", Toast.LENGTH_SHORT)
					.show();
		} else {
			new SendMessageMain(this).execute(msg);
			//asyncTsk.execute(msg);
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
		if (itemPosition==0 && !activity_result){
			actionBar.setIcon(R.drawable.ic_action_dock);
			changeControlFragment(0);
			return true;
		} else {
			activity_result=false;
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
			case 5:
				sendMessage(changeControl + " " + typeOfControls[5]);
				break;	
			case 6:
				sendMessage(changeControl + " " + typeOfControls[6]);
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
				case 2: actionBar.setIcon(R.drawable.circle_icon);
						break;
				case 3:	actionBar.setIcon(R.drawable.gamepad);
						break;
			}
		}

		return true;
	}

	public void changeControlFragment(int i) {
		
		new ChangeFragment(i, this).change();
		
	}



/*	protected void setOrientation() {
		int current = getRequestedOrientation();
		// only switch the orientation if not in portrait
		if (current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
*/
}