package org.unipd.nbeghin.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.services.SamplingStoreService;
import org.unipd.nbeghin.utils.DbAdapter;

/**
 * Created by Nicola Beghin on 15/06/13.
 */
public class AccelerometerStoreListener implements SensorEventListener {
	private boolean				mInitialized			= false;
	private float				minDiff					= 0.0f;
	public DbAdapter			db;
	private float				mLastX, mLastY, mLastZ;
	private float 				mLastXLinear, mLastYLinear, mLastZLinear;
	private String				action;
	private int					sensorDelay;
	private String				accelerometer_position	= null;
	private float[]				lastValuesRotationVector;
	private double lastValueTimestamp = 0;
	private static final String	UNDEFINED_ACTION		= "UNDEFINED";

	public void closeDb() {
		db.close();
	}

	public float getMinDiff() {
		return minDiff;
	}

	public void setMinDiff(float minDiff) {
		this.minDiff = minDiff;
	}

	public void setAccelerometerPosition(String position) {
		this.accelerometer_position = position;
	}

	public void setSensorDelay(int sensorDelay) {
		this.sensorDelay = sensorDelay;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public AccelerometerStoreListener(Context context) {
		this(context, UNDEFINED_ACTION);
	}

	public AccelerometerStoreListener(Context context, String action) {
		this.action = action;
		db = new DbAdapter(context);
		db.open();
		Log.i(MainActivity.AppName, "DB connection opened successfully (" + db.getCount(false) + " pre-existing rows for accelerometer)");
		Log.i(MainActivity.AppName, "DB connection opened successfully (" + db.getCount(true) + " pre-existing rows for linear_acceleration)");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		if (event.sensor == SamplingStoreService.mAccelerometer){
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			if (!mInitialized) {
				mLastX = x;
				mLastY = y;
				mLastZ = z;
				mInitialized = true;
			} else {
				if (Math.abs(mLastX - x) < minDiff) x = mLastX; // if delta < NOISE then use previous value
				if (Math.abs(mLastY - y) < minDiff) y = mLastY; // if delta < NOISE then use previous value
				if (Math.abs(mLastZ - z) < minDiff) z = mLastZ; // if delta < NOISE then use previous value
			}
			// update last value for next onSensorChanged
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			if (lastValuesRotationVector != null) {
				db.saveSampleAccelerometer(x, y, z, lastValuesRotationVector[0], lastValuesRotationVector[1], lastValuesRotationVector[2], 
					event.timestamp, action, sensorDelay, accelerometer_position, minDiff);
			}
		}
		else if (event.sensor == SamplingStoreService.mLinearAcceleration) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			if (!mInitialized) {
				mLastXLinear = x;
				mLastYLinear = y;
				mLastZLinear = z;
				mInitialized = true;
			} else {
				if (Math.abs(mLastXLinear - x) < minDiff) x = mLastXLinear; // if delta < NOISE then use previous value
				if (Math.abs(mLastYLinear - y) < minDiff) y = mLastYLinear; // if delta < NOISE then use previous value
				if (Math.abs(mLastZLinear - z) < minDiff) z = mLastZLinear; // if delta < NOISE then use previous value
			}
			// update last value for next onSensorChanged
			mLastXLinear = x;
			mLastYLinear = y;
			mLastZLinear = z;
			if (lastValuesRotationVector != null) {
				db.saveSampleLinearAcceleration(x, y, z, lastValuesRotationVector[0], lastValuesRotationVector[1], lastValuesRotationVector[2], 
					event.timestamp, action, sensorDelay, accelerometer_position, minDiff);
			}
		}
		else if (event.sensor == SamplingStoreService.mRotationVector) {
			lastValuesRotationVector = (float[])event.values.clone();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO
	}
}
