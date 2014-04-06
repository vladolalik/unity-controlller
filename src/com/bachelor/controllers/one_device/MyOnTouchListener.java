package com.bachelor.controllers.one_device;

import com.bachelor.networking.SendMessage;
import com.bachelor.unity_remote_control.MainActivity;
import com.example.resultrecdemo.R;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MyOnTouchListener implements View.OnTouchListener  {

	float dx, dy, _xDelta, _yDelta;
	ImageView backView, midView;
	String msg;
	MainActivity mainActivity;
	
	public MyOnTouchListener(ImageView backView, ImageView midView, String msg, MainActivity mainActivity){
		this.backView=backView;
		this.midView=midView;
		this.mainActivity=mainActivity;
		this.msg=msg;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		joystickMovement(event, backView, midView);
		return true;
	}
	
	private void joystickMovement(MotionEvent event, ImageView backView, ImageView midView){
		final int x = (int) event.getRawX();
		final int y = (int) event.getRawY();
		// float x, y;
		int origX = midView.getLeft();
		int origY = midView.getTop();
		// bod v strede joysticku
		final PointF middlePoint = new PointF(backView.getWidth() / 2,
				backView.getHeight() / 2);

		// bod v strede navrchu joysticku, podla ktoreho budem pocitat uhol
		// natocenia
		final PointF zeroPoint = new PointF(backView.getWidth() / 2, 0);
		float backViewRadius = backView.getWidth() / 2;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			origX = midView.getLeft();
			origY = midView.getTop();
			_xDelta = (int) (x - midView.getTranslationX());
			_yDelta = (int) (y - midView.getTranslationY());
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// vypocet uhlu
			PointF touchPoint = new PointF(event.getX(), event.getY()); // suradnice
																		// bodu
																		// dotyku
			float angle = getAngle(middlePoint, zeroPoint, touchPoint);
			Log.d("Angle", String.valueOf(angle));

			// vzdialenost bodu dotyku od stredu

			float vMidToTouch = getDistanceTwoPoints(middlePoint, touchPoint);
			//float vMidToCircle = getDistanceTwoPoints(middlePoint, new PointF(midView.getX(), midView.getY()));
			Log.d("Intensity", String.valueOf(vMidToTouch));
			sendMessage(msg + " "
					+ String.valueOf(angle) + " "
					+ String.valueOf(getMovementIntensity(backViewRadius, vMidToTouch)));

			// pohyb gulicky v strede

			Log.d("xdelta", String.valueOf(x - _xDelta));
			Log.d("yDelta", String.valueOf(y - _yDelta));

			if (Math.abs(x - _xDelta) < backViewRadius
					&& Math.abs(y - _yDelta) < backViewRadius) {
				midView.setTranslationX(x - _xDelta);
				midView.setTranslationY(y - _yDelta);
			}

			Log.d("joystick x y",
					String.valueOf(midView.getX() + " x "
							+ String.valueOf(midView.getY())));
			break;
		}

		case MotionEvent.ACTION_UP: {
			// vrati joystick spat do stredu
			midView.setX(origX);
			midView.setY(origY);

			break;
		}
		}
	}
	
	private float getAngle(PointF middle, PointF zero, PointF touch) {
		return (float) Math.abs(Math.toDegrees(Math.atan2(touch.x - middle.x,
				touch.y - middle.y)
				- Math.atan2(zero.x - middle.x, zero.y - middle.y)));
	}

	private float getDistanceTwoPoints(PointF a, PointF b) {
		return (float) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y)
				* (a.y - b.y));
	}
	
	/**
	 * Funkcia pomocou asyncTask-u odosle spravu serveru
	 * 
	 * @param msg
	 */
	public void sendMessage(String msg) {
		new SendMessage(mainActivity).execute(msg);
	}
	
	private float getMovementIntensity(float joystickMax, float value){
		int maxValue=mainActivity.getResources().getInteger(R.integer.MAX_VALUE_JOYSTICK_MOV);
		float intensity=(value/joystickMax)*maxValue;
		if (intensity>maxValue){
			return maxValue;
		}
		return intensity;
	}

}
