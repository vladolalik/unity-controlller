package com.bachelor.unity_remote_control;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resultrecdemo.R;

@SuppressLint("ValidFragment") public class HomeFragment extends Fragment{
	
	InetAddress serverIP;
	TextView t;
	
	
	
	@Override
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
			try {
				String ip = getArguments().getString(getResources().getString(R.string.IP_ADRESS));
				if (ip!=null){
					ip=ip.replace("/", "");
					Log.d("GAMEPAD ip", ip);
					serverIP=InetAddress.getByName(ip);
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				Log.d("HOMEFRAGMENT", "bad format of ip");
				e.printStackTrace();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				Log.d("HOMEFRAGMENT", "parameter ip not found");
				e.printStackTrace();
			}
	      //Infla	te the layout for this fragment
		  Log.d("Fragment", "oncreateView");
		  View view = inflater.inflate(R.layout.home_fragment, container, false);
		  t=(TextView)view.findViewById(R.id.textMove);
	      if (serverIP!=null)
	    	  t.setText("Server IP address: "+serverIP.toString());
	      getActivity().setRequestedOrientation(
	                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	      Button connect=(Button)view.findViewById(R.id.connect_button12);
			connect.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (serverIP == null)
						((MainActivity) getActivity()).getServerIP();
					else
						Toast.makeText(((MainActivity) getActivity()), "You are already connected to server",
								Toast.LENGTH_SHORT).show();
					
				}
			});
	      return view;
	   }
	
	public void setText(){
		getActivity().setRequestedOrientation(
	            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		t=(TextView)getView().findViewById(R.id.textMove);
	      if (serverIP!=null)
	    	  t.setText(serverIP.toString());
	}
	


}