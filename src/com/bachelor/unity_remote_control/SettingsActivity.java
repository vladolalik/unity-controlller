package com.bachelor.unity_remote_control;

import com.example.resultrecdemo.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	Spinner devNumber, countOFDev;
	//public static final String DEV_NUMBER = "DEVICE_NUMBER";
	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		devNumber = (Spinner)findViewById(R.id.dev_number);
		countOFDev = (Spinner)findViewById(R.id.num_of_dev);
		Log.d("Settings activity", "before spinner");
		populateSpinner();
		Log.d("Settings activity", "after initialize spinner");
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		updateUIFromPreferences();
		
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savePreferences();
				SettingsActivity.this.setResult(RESULT_OK);
				finish();
			}
		});
		
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SettingsActivity.this.setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	
	public void savePreferences(){
		int my_number = devNumber.getSelectedItemPosition()+1;
		int count_of_dev = countOFDev.getSelectedItemPosition()+1;
		Log.d("Spinner position", Integer.toString(my_number));
		Editor editor = prefs.edit();
		editor.putInt(getResources().getString(R.string.DEV_NUMBER), my_number);
		editor.putInt(getResources().getString(R.string.COUNT_OF_DEVICES), count_of_dev);
		editor.commit();
		
	}
	
	private void updateUIFromPreferences(){
		int my_number = prefs.getInt(getResources().getString(R.string.DEV_NUMBER), 1);
		devNumber.setSelection(my_number-1);
		
		int num_of_dev = prefs.getInt(getResources().getString(R.string.COUNT_OF_DEVICES), 1);
		countOFDev.setSelection(num_of_dev-1);
	}
	
	private void populateSpinner(){
		ArrayAdapter<CharSequence> fAdapter;
		fAdapter = ArrayAdapter.createFromResource(this, R.array.device_number, android.R.layout.simple_spinner_item );
		int spinner_dd_item = android.R.layout.simple_spinner_dropdown_item;
		fAdapter.setDropDownViewResource(spinner_dd_item);
		devNumber.setAdapter(fAdapter);
		
		ArrayAdapter<CharSequence> numAdapter;
		numAdapter = ArrayAdapter.createFromResource(this, R.array.device_number, android.R.layout.simple_spinner_item );
		spinner_dd_item = android.R.layout.simple_spinner_dropdown_item;
		numAdapter.setDropDownViewResource(spinner_dd_item);
		countOFDev.setAdapter(numAdapter);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	



}
