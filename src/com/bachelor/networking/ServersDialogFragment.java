package com.bachelor.networking;

import com.example.resultrecdemo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

@SuppressLint("ValidFragment") public class ServersDialogFragment extends DialogFragment{
	
		CharSequence[] serversIP;
		
	
		public ServersDialogFragment(CharSequence[] serversIP){
			this.serversIP=serversIP;
		}
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
			
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(R.string.SELECT_SERVER)
	               .setItems(serversIP, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                	   mListener.onDialogItemClick(ServersDialogFragment.this, id);
	                   }
	               });
	               
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
		
		/* The activity that creates an instance of this dialog fragment must
	     * implement this interface in order to receive event callbacks.
	     * Each method passes the DialogFragment in case the host needs to query it. */
	    public interface NoticeDialogListener {
	        public void onDialogItemClick(DialogFragment dialog, int id);
	    }
	    
	    // Use this instance of the interface to deliver action events
	    NoticeDialogListener mListener;
	    
	    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        // Verify that the host activity implements the callback interface
	        try {
	            // Instantiate the NoticeDialogListener so we can send events to the host
	            mListener = (NoticeDialogListener) activity;
	        } catch (ClassCastException e) {
	            // The activity doesn't implement the interface, throw exception
	            throw new ClassCastException(activity.toString()
	                    + " must implement NoticeDialogListener");
	        }
	    }
}
