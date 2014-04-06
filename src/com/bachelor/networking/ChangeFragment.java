package com.bachelor.networking;

import com.bachelor.controllers.one_device.GamePadFragment_1_1;
import com.bachelor.controllers.one_device.JoystickFragment_1_1;
import com.bachelor.controllers.two_devices.GamePadFragment_1_2;
import com.bachelor.controllers.two_devices.GamePadFragment_2_2;
import com.bachelor.controllers.two_devices.JoystickFragment_1_2;
import com.bachelor.controllers.two_devices.JoystickFragment_2_2;
import com.bachelor.unity_remote_control.HomeFragment;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class ChangeFragment {

	MainActivity mainActivity;
	int number;
	
	public ChangeFragment(int number, MainActivity mainActivity){
		this.mainActivity=mainActivity;
		this.number=number;
	}
	
	public void change(){
		Fragment fr = null;
		mainActivity.lastSelectedItemActionBar=number;
		Bundle bundle=new Bundle(1);
		if (mainActivity.serverIP!=null){
			bundle.putString(mainActivity.getResources().getString(R.string.IP_ADRESS), mainActivity.serverIP.toString());
		} else {
			bundle.putString(mainActivity.getResources().getString(R.string.IP_ADRESS), null);
		}
		
		int my_number = mainActivity.prefs.getInt(
				mainActivity.getResources().getString(R.string.DEV_NUMBER), 1);
		int count_of_dev = mainActivity.prefs.getInt(
				mainActivity.getResources().getString(R.string.COUNT_OF_DEVICES), 1);
		
		switch (number) {
		case 3:
			if (count_of_dev==1)
			{
				if (my_number==1){
					fr = new GamePadFragment_1_1();
				} 
			} 
			else 
			{
				if (my_number==1){
					fr = new GamePadFragment_1_2();
				} else {
					fr = new GamePadFragment_2_2();
				}
			}
			
			fr.setArguments(bundle);
			break;
		case 0:
			fr = new HomeFragment();
			fr.setArguments(bundle);
			break;
		case 2:
			if (count_of_dev==1)
			{
				if (my_number==1){
					fr = new JoystickFragment_1_1();
				}
			} 
			else 
			{
				if (my_number==1){
					fr = new JoystickFragment_1_2();
				} else {
					fr = new JoystickFragment_2_2();
				}
			}
			fr.setArguments(bundle);
			break;	
		default:
			return;
		}
		// fr = new GamePadFragment();
		if (fr != null) {
			Log.d("Change Fragment", String.valueOf(number));
			FragmentManager fm = mainActivity.getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, fr);
			fragmentTransaction.commit();
		}
	}
}
