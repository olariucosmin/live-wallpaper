package com.smalldev.robotl;

import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class RobotLive extends WallpaperService {
	Bitmap mainBitmap = null;
	Bitmap eyeBitmap = null;
	Bitmap resizedMainBitmap = null;
	Bitmap resizedEyeBitmap = null;

	Bitmap trickBitmap = null;
	Bitmap resizedTrickBitmap = null;

	Bitmap shakedBitmap = null;
	Bitmap resizedShakedBitmap = null;

	public static final String SHARED_PREFS_NAME = "live_settings";
	float scaleWidth = 0;
	SensorManager sm = null;
	List list;
	List list2;
	public Paint mPaint = null;
	public float x = 0;
	public float y = 0;
	public float z = 0;
	float mtouchX, mtouchY;
	long pressedTime = 0;
	boolean clicked = false;
	boolean initialized = false;
	public SharedPreferences mPrefs = null;
	public String themeString = "";
	public boolean shaked = false;
	long shakedTime = 0;
	int shakeTemp = 0;
	int clickCount = 0;

	private float m_totalForcePrev;

	public SensorEventListener sel = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			/* Isn't required for this example */
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			float[] values = event.values;
			x = values[0];
			y = values[1];
			z = values[2];
		}

	};

	public SensorEventListener sel2 = new SensorEventListener() {

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			/* Isn't required for this example */
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			double forceThreshHold = 1.5f;
			float[] values = event.values;

			double totalForce = 0.0f;
			totalForce += Math.pow(values[SensorManager.DATA_X]
					/ SensorManager.GRAVITY_EARTH, 2.0);
			totalForce += Math.pow(values[SensorManager.DATA_Y]
					/ SensorManager.GRAVITY_EARTH, 2.0);
			totalForce += Math.pow(values[SensorManager.DATA_Z]
					/ SensorManager.GRAVITY_EARTH, 2.0);
			totalForce = Math.sqrt(totalForce);

			if ((totalForce < forceThreshHold)
					&& (m_totalForcePrev > forceThreshHold)) {
				shaked = true;
				shakedTime = System.currentTimeMillis();
			}

			m_totalForcePrev = (float) totalForce;
		}

	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate() {
		super.onCreate();
		mPrefs = RobotLive.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
		
		changeTheme();
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		list = sm.getSensorList(Sensor.TYPE_ORIENTATION);
		list2 = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (list.size() > 0) {
			sm.registerListener(sel, (Sensor) list.get(0),
					SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			Toast.makeText(getBaseContext(), "Error: No Accelerometer.",
					Toast.LENGTH_LONG);
		}

		if (list2.size() > 0) {
			sm.registerListener(sel2, (Sensor) list2.get(0),
					SensorManager.SENSOR_DELAY_GAME);
		} else {
			Toast.makeText(getBaseContext(), "Error: No Accelerometer.",
					Toast.LENGTH_LONG);
		}

		System.out.println("CREATEEEE");
		initialized = false;
	}

	public void changeTheme() {
		// TODO Auto-generated method stub
		String theme = mPrefs.getString("Themes", "wtheme");
		themeString = theme;
		Log.e("CO", themeString);
		if (theme.equals("btheme")) {
			mainBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.main);
			eyeBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.eyes);
			trickBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.maintrick);
			shakedBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.main);

			mPaint = new Paint();
			mPaint.setColor(0xffffffff);
			// mPaint.setAntiAlias(true);
			// mPaint.setStrokeWidth(2);
			// mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.FILL);
			initialized = false;
			return;
		}
		
		if (theme.equals("wtheme")) {
			mainBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.main);
			eyeBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.whiteeyes);
			trickBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.maintrick);
			shakedBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.main);

			mPaint = new Paint();
			mPaint.setColor(0xffffffff);
			// mPaint.setAntiAlias(true);
			// mPaint.setStrokeWidth(2);
			// mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.FILL);
			initialized = false;
			return;
		}
		
		if (theme.equals("rtheme")) {
			mainBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.main);
			eyeBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.redeyes);
			trickBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.maintrick);
			shakedBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.main);

			mPaint = new Paint();
			mPaint.setColor(0xffffffff);
			// mPaint.setAntiAlias(true);
			// mPaint.setStrokeWidth(2);
			// mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.FILL);
			initialized = false;
			return;
		}
		
		if (theme.equals("ytheme")) {
			mainBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.main);
			eyeBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.yelloweyes);
			trickBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.maintrick);
			shakedBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.main);

			mPaint = new Paint();
			mPaint.setColor(0xffffffff);
			// mPaint.setAntiAlias(true);
			// mPaint.setStrokeWidth(2);
			// mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.FILL);
			initialized = false;
			return;
		}
		
		if (theme.equals("gtheme")) {
			mainBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.main);
			eyeBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.greeneyes);
			trickBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.maintrick);
			shakedBitmap = BitmapFactory.decodeResource(getResources(),	R.drawable.main);

			mPaint = new Paint();
			mPaint.setColor(0xffffffff);
			// mPaint.setAntiAlias(true);
			// mPaint.setStrokeWidth(2);
			// mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.FILL);
			initialized = false;
			return;
		}
	}

	@Override
	public void onDestroy() {
		if (list.size() > 0) {
			sm.unregisterListener(sel);
		}
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new WallEngine();
	}

	class WallEngine extends Engine implements
			SharedPreferences.OnSharedPreferenceChangeListener {

		private final Handler mHandler = new Handler();

		private final Runnable mDrawCube = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		private boolean mVisible;

		public WallEngine() {
			mPrefs.registerOnSharedPreferenceChangeListener(this);
			onSharedPreferenceChanged(mPrefs, null);
		}

		public void onSharedPreferenceChanged(SharedPreferences prefs,
				String key) {
			if (key != null) {
				Log.e("CO", "K:" + key);
				if (key.equals("Themes")) {
					changeTheme();
				}
			}
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawCube);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mDrawCube);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			// store the center of the surface, so we can draw the cube in the
			// right spot
			drawFrame();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawCube);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			drawFrame();
		}

		/*
		 * Store the position of the touch event so we can use it for drawing
		 * later
		 */
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				mtouchX = event.getX();
				mtouchY = event.getY();

				pressedTime = System.currentTimeMillis();
				System.out.println("Pressed");
				clicked = true;
				clickCount++;

			} else {
				mtouchX = -1;
				mtouchY = -1;
			}

			super.onTouchEvent(event);
		}

		/*
		 * Draw one frame of the animation. This method gets called repeatedly
		 * by posting a delayed Runnable. You can do any drawing you want in
		 * here. This example draws a wireframe cube.
		 */
		void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();
			final Rect frame = holder.getSurfaceFrame();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					drawCube(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			mHandler.removeCallbacks(mDrawCube);
			if (mVisible) {
				mHandler.postDelayed(mDrawCube, 1000 / 25);
			}
		}

		void drawCube(Canvas c) {
			int newWidth = c.getWidth();
			int newHeight = c.getHeight();
			int pic_width = 0;
			int pic_height = 0;

			if (initialized == false) {
				pic_width = mainBitmap.getWidth();
				pic_height = mainBitmap.getHeight();
				Matrix matrix = new Matrix();
				scaleWidth = ((float) newWidth) / pic_width;

				matrix.postScale(scaleWidth, scaleWidth);
				resizedMainBitmap = Bitmap.createBitmap(mainBitmap, 0, 0,
						pic_width, pic_height, matrix, true);

				pic_width = eyeBitmap.getWidth();
				pic_height = eyeBitmap.getHeight();
				matrix = new Matrix();
				scaleWidth = ((float) newWidth) / pic_width;
				matrix.postScale(scaleWidth, scaleWidth);
				// System.out.println(scaleWidth);
				resizedEyeBitmap = Bitmap.createBitmap(eyeBitmap, 0, 0, pic_width, pic_height, matrix, true);

				pic_width = trickBitmap.getWidth();
				pic_height = trickBitmap.getHeight();
				matrix = new Matrix();
				scaleWidth = ((float) newWidth) / pic_width;
				matrix.postScale(scaleWidth, scaleWidth);
				// System.out.println(scaleWidth);
				resizedTrickBitmap = Bitmap.createBitmap(trickBitmap, 0, 0,
						pic_width, pic_height, matrix, true);

				pic_width = shakedBitmap.getWidth();
				pic_height = shakedBitmap.getHeight();
				matrix = new Matrix();
				scaleWidth = ((float) newWidth) / pic_width;
				matrix.postScale(scaleWidth, scaleWidth);
				// System.out.println(scaleWidth);
				resizedShakedBitmap = Bitmap.createBitmap(shakedBitmap, 0, 0,
						pic_width, pic_height, matrix, true);

				initialized = true;
			}

			c.drawRect(new Rect(0, 0, c.getWidth(), c.getHeight()), mPaint);
			int offsety = newHeight / 2 - resizedMainBitmap.getWidth() / 2;
			// z = -90;

			if (y < -(90))
				y = -90;
			if (y > (90))
				y = 90;

			float maxmovex = resizedEyeBitmap.getWidth() * 20 / 380;
			float maxmovey = resizedEyeBitmap.getHeight() * 20 / 323;

			if (shaked) {
				c.drawBitmap(resizedMainBitmap, 0, offsety, null);
				if (System.currentTimeMillis() - shakedTime < 2000) {
					if (shakeTemp % 2 == 0) {
						c.drawBitmap(resizedEyeBitmap, 5,
								(offsety + ((y + 30) * maxmovey) / 90), null);
						shakeTemp++;
					} else {
						c.drawBitmap(resizedEyeBitmap, -5,
								(offsety + ((y + 30) * maxmovey) / 90), null);
						shakeTemp++;
					}

				} else {
					c.drawBitmap(resizedEyeBitmap, 0, offsety, null);
					shaked = false;
				}
			} else {
				c.drawBitmap(resizedEyeBitmap, (z * maxmovex) / 90,(offsety + ((y + 30) * maxmovey) / 90), mPaint);
				if (clicked == true && clickCount > 5) {
					if (System.currentTimeMillis() - pressedTime < 1000) {
						c.drawBitmap(resizedTrickBitmap, 0, offsety, null);
					} else {
						c.drawBitmap(resizedMainBitmap, 0, offsety, null);
						clicked = false;
						clickCount = 0;
					}
				} else {
					c.drawBitmap(resizedMainBitmap, 0, offsety, null);
				}
			}
		}
	}
}