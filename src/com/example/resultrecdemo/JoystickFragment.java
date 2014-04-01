package com.example.resultrecdemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.shapes.ArcShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


 public class JoystickFragment extends Fragment{
	
	DatagramSocket socket;
	InetAddress serverIP;
	
	/*public GamePadFragment(InetAddress serverIP, FragmentActivity mainActivity){
		this.serverIP=serverIP;
		this.mainActivity=mainActivity;	
	}*/
	
	@SuppressLint("NewApi") @Override
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
			Log.d("GAMEPAD", "bad format of ip");
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("GAMEPAD", "parameter ip not found");
			e.printStackTrace();
		}
	       //Inflate the layout for this fragment
		 getActivity().setRequestedOrientation(
		            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 View view = inflater.inflate(R.layout.joystick_fragment, container, false);
	
		 
		 final ImageView imgView = (ImageView) view.findViewById(R.id.imgViewJoyStick);
		 final ImageView joystickMiddle = (ImageView) view.findViewById(R.id.imageViewJoyStickMiddle);
		 
		 imgView.setOnTouchListener(new View.OnTouchListener() {
		 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				float x, y, dx=0, dy=0; 
				int origX=joystickMiddle.getLeft();
				int origY=joystickMiddle.getTop();
				// bod v strede joysticku
				final PointF middlePoint=new PointF(imgView.getWidth()/2, imgView.getHeight()/2);
				
				// bod v strede navrchu joysticku, podla ktoreho budem pocitat uhol natocenia
				final PointF zeroPoint=new PointF(imgView.getWidth()/2, 0);
				Log.d("middlePoint", middlePoint.x + " x " + middlePoint.y);
				Log.d("zeroPoint", zeroPoint.x + " x " + zeroPoint.y);
				
				 switch(event.getAction()){
				 	case MotionEvent.ACTION_DOWN: {
				 		origX=joystickMiddle.getLeft();
				 		origY=joystickMiddle.getTop();
				 		 Log.d("stred xxy",String.valueOf(origX)+" x "+String.valueOf(origY));
				 		x=event.getX();
				 		y=event.getY();
				 		dx=x-joystickMiddle.getX();
				 		dy=y-joystickMiddle.getY();
				 		Log.d("dx", String.valueOf(dx));
				 		break;
				 	}
				 	
				 	case MotionEvent.ACTION_MOVE: {
				 		// pohyb gulicky v strede
				 		Log.d("JOYSTICK", String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
				 		
				 		PointF evPoint=moveJoystick(event.getX(),event.getY(),0,0,joystickMiddle,imgView);
						joystickMiddle.setX(evPoint.x-dx);
						joystickMiddle.setY(evPoint.y-dy);
						
						// vypocet uhlu
						PointF touchPoint=new PointF(event.getX(), event.getY()); // suradnice bodu dotyku
						float angle=(float) Math.abs(Math.toDegrees(Math.atan2(touchPoint.x - middlePoint.x,touchPoint.y - middlePoint.y)-
		                        Math.atan2(zeroPoint.x- middlePoint.x,zeroPoint.y- middlePoint.y)));
						Log.d("Angle", String.valueOf(angle));
						
						// vzdialenost bodu dotyku od stredu
						
						float vMidToTouch=(float) Math.sqrt((middlePoint.x-touchPoint.x)*(middlePoint.x-touchPoint.x) + (middlePoint.y-touchPoint.y)*(middlePoint.y-touchPoint.y));
						Log.d("Intensity", String.valueOf(vMidToTouch));
						sendMessage(getResources().getString(R.string.rotate)+" "+String.valueOf(angle)+" "+String.valueOf(vMidToTouch));
						/*float vMidToZer=(float) Math.sqrt((middlePoint.x-zeroPoint.x)*(middlePoint.x-zeroPoint.x) + (middlePoint.y-zeroPoint.y)*(middlePoint.y-zeroPoint.y));
						float vMidToTouch=(float) Math.sqrt((middlePoint.x-touchPoint.x)*(middlePoint.x-touchPoint.x) + (middlePoint.y-touchPoint.y)*(middlePoint.y-touchPoint.y));
						float vZeroToTouch=(float) Math.sqrt((zeroPoint.x-touchPoint.x)*(zeroPoint.x-touchPoint.x) + (zeroPoint.y-touchPoint.y)*(zeroPoint.y-touchPoint.y));
						
						float angle=(float) Math.acos((vMidToZer*vMidToZer + vMidToTouch*vMidToTouch-vZeroToTouch*vZeroToTouch)/(2*vMidToZer*vMidToTouch));
						Log.d("angle", String.valueOf(Math.toDegrees(angle)));
						Log.d("angle radians", String.valueOf(angle));
						*/
						
						break;
				 	}
				 	
				 	case MotionEvent.ACTION_UP: {
				 		Log.d("orgix", Float.toString(origX));
				 		joystickMiddle.setX(origX);
				 		joystickMiddle.setY(origY);
				 		
						break;
				 	}
				 		
				 }
				return true;
			}
		});
		 
		 final ImageView imgViewMov = (ImageView) view.findViewById(R.id.imgViewJoystickMov);
		 final ImageView joystickMidMov = (ImageView) view.findViewById(R.id.imgViewJoystickMidMov);
		 imgViewMov.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				float x, y, dx=0, dy=0; 
				int origX=joystickMidMov.getLeft();
				int origY=joystickMidMov.getTop();
				// bod v strede joysticku
				final PointF middlePoint=new PointF(imgViewMov.getWidth()/2, imgViewMov.getHeight()/2);
				
				// bod v strede navrchu joysticku, podla ktoreho budem pocitat uhol natocenia
				final PointF zeroPoint=new PointF(imgViewMov.getWidth()/2, 0);
				Log.d("middlePoint", middlePoint.x + " x " + middlePoint.y);
				Log.d("zeroPoint", zeroPoint.x + " x " + zeroPoint.y);
				
				 switch(event.getAction()){
				 	case MotionEvent.ACTION_DOWN: {
				 		origX=joystickMidMov.getLeft();
				 		origY=joystickMidMov.getTop();
				 		 Log.d("stred xxy",String.valueOf(origX)+" x "+String.valueOf(origY));
				 		x=event.getX();
				 		y=event.getY();
				 		dx=x-joystickMidMov.getX();
				 		dy=y-joystickMidMov.getY();
				 		Log.d("dx", String.valueOf(dx));
				 		break;
				 	}
				 	
				 	case MotionEvent.ACTION_MOVE: {
				 		// pohyb gulicky v strede
				 		Log.d("JOYSTICK", String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
				 		
				 		PointF evPoint=moveJoystick(event.getX(),event.getY(),50,0,joystickMidMov,imgViewMov);
						joystickMidMov.setX(evPoint.x-dx);
						joystickMidMov.setY(evPoint.y-dy);
						
						// vypocet uhlu
						PointF touchPoint=new PointF(event.getX(), event.getY()); // suradnice bodu dotyku
						float angle=(float) Math.abs(Math.toDegrees(Math.atan2(touchPoint.x - middlePoint.x,touchPoint.y - middlePoint.y)-
		                        Math.atan2(zeroPoint.x- middlePoint.x,zeroPoint.y- middlePoint.y)));
						Log.d("Angle", String.valueOf(angle));
						
						// vzdialenost bodu dotyku od stredu
						
						float vMidToTouch=(float) Math.sqrt((middlePoint.x-touchPoint.x)*(middlePoint.x-touchPoint.x) + (middlePoint.y-touchPoint.y)*(middlePoint.y-touchPoint.y));
						Log.d("Intensity", String.valueOf(vMidToTouch));
						sendMessage(getResources().getString(R.string.movement)+" "+String.valueOf(angle)+" "+String.valueOf(vMidToTouch));
						/*float vMidToZer=(float) Math.sqrt((middlePoint.x-zeroPoint.x)*(middlePoint.x-zeroPoint.x) + (middlePoint.y-zeroPoint.y)*(middlePoint.y-zeroPoint.y));
						float vMidToTouch=(float) Math.sqrt((middlePoint.x-touchPoint.x)*(middlePoint.x-touchPoint.x) + (middlePoint.y-touchPoint.y)*(middlePoint.y-touchPoint.y));
						float vZeroToTouch=(float) Math.sqrt((zeroPoint.x-touchPoint.x)*(zeroPoint.x-touchPoint.x) + (zeroPoint.y-touchPoint.y)*(zeroPoint.y-touchPoint.y));
						
						float angle=(float) Math.acos((vMidToZer*vMidToZer + vMidToTouch*vMidToTouch-vZeroToTouch*vZeroToTouch)/(2*vMidToZer*vMidToTouch));
						Log.d("angle", String.valueOf(Math.toDegrees(angle)));
						Log.d("angle radians", String.valueOf(angle));
						*/
						
						break;
				 	}
				 	
				 	case MotionEvent.ACTION_UP: {
				 		Log.d("orgix", Float.toString(origX));
				 		joystickMidMov.setX(origX);
				 		joystickMidMov.setY(origY);
				 		
						break;
				 	}
				 		
				 }
				return true;
			}
		});
		 
	     return view;
	   }
	
	
	
	/**
	 * Funkcia ktora vypocita hranicne body, kam sa moze gulicka v strede pohnut
	 * 
	 * @param eX
	 * @param eY
	 * @param jMid
	 * @param joystick
	 * @return float[] 
	 */
	private PointF moveJoystick(float eX, float eY,float bulgaria_const_X,float bulgaria_const_Y  ,ImageView jMid, ImageView joystick){
		
		PointF p=new PointF();
		p.x=eX;
		p.y=eY;
		
 		if (eX<0 + bulgaria_const_X){//((joystickMiddle.getWidth()/2))){
 			p.x=0 + bulgaria_const_X;//(joystickMiddle.getWidth()/2); 
 			Log.d("joystick width", String.valueOf(jMid.getWidth()));
 		}
 		if (eX>(joystick.getWidth()-(jMid.getWidth()/2)+bulgaria_const_X)){
 			p.x=(joystick.getWidth()+bulgaria_const_X)-(jMid.getWidth()/2);
 		}
 		if (eY<0+bulgaria_const_Y){//((joystickMiddle.getHeight()/2))){
 			p.y=0+bulgaria_const_Y;//(joystickMiddle.getHeight()/2); 
 		}
 		if (eY>(joystick.getHeight()-(jMid.getHeight()/2)+bulgaria_const_Y)){
 			p.y=(joystick.getHeight()+bulgaria_const_Y)-(jMid.getHeight()/2);
 		}
 		return p;
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

					byte[] buf = params[0].getBytes();
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
			Toast.makeText(getActivity(),
					"Please make connection with server", Toast.LENGTH_SHORT)
					.show();
		} else {
			asyncTsk.execute(msg);
		}
	}
	
	

}
