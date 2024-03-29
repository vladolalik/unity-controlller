package com.bachelor.unity_remote_control;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.resultrecdemo.R;
import com.example.resultrecdemo.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MapViewActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	/*Draw map 
	 * 
	 * Scene Z: max=143, min=36
	 * 		 X: max=37, min=-40
	 * 
	 * */
	
	private final int sceneMaxZ=143;
	private final int sceneMaxX=37;
	private final int sceneScaleZ=36;
	private final int sceneScaleX=-40;
	
	private int mainActivityActiveFragment=1;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map_view);
		
		setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.map_back).setOnTouchListener(
				mDelayHideTouchListener);
		
		Bundle b = getIntent().getExtras();
		mainActivityActiveFragment=b.getInt("active_fragment");
		int x=b.getInt("x");
		int y=b.getInt("y");
		
		Button backButton=(Button)findViewById(R.id.map_back);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent returnIntent = new Intent();
				returnIntent.putExtra("active_fragment",mainActivityActiveFragment);
				setResult(RESULT_OK,returnIntent);     
				finish();
			}
		});
		
		/*Draw map 
		 * 
		 * Scene Z: max=143, min=36
		 * 		 X: max=37, min=-40
		 * 
		 * */
		
		Paint paint= new Paint();
		paint.setColor(Color.parseColor("#FF0000"));
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		Bitmap bg=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		
		Canvas canvas=new Canvas(bg);
		Bitmap map=getImage(R.drawable.map, width, height);
		Bitmap point=getImage(R.drawable.point1, 25, 44);
		Log.d("MAP XxY", String.valueOf(x) + " x " + String.valueOf(y));
		Point pPoint=computePoint(x, y, width, height);
		canvas.drawBitmap(map, 0, 0, paint);
		Log.d("Compute point MAP XxY", String.valueOf(pPoint.x) + " x " + String.valueOf(pPoint.y));
		canvas.drawBitmap(point, pPoint.x, pPoint.y, paint);
		LinearLayout ll=(LinearLayout)findViewById(R.id.mapLayout);
		ll.setBackgroundDrawable(new BitmapDrawable(bg));
	}
	
	public Bitmap getImage (int id, int width, int height) {
	    Bitmap bmp = BitmapFactory.decodeResource( getResources(), id );
	    Bitmap img = Bitmap.createScaledBitmap( bmp, width, height, true );
	   // bmp.recycle();
	    return img;
	}
	
	private Point computePoint(int x, int y, int width, int height){
		
		float xx=x-sceneScaleX;
		xx=xx/(sceneMaxX-sceneScaleX)*width;
		float yy=y-sceneScaleZ;
		yy=yy/(sceneMaxZ-sceneScaleZ)*height;
		yy=yy+44; // height of point pic
		Log.d("AA","sd");
		return new Point(Math.round(xx), Math.round(height-yy));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
