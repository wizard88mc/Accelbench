package org.unipd.nbeghin.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.models.Settings;
import org.unipd.nbeghin.services.SamplingStoreService;
import org.unipd.nbeghin.utils.DbAdapter;

/**
 * Created by Nicola Beghin on 15/06/13.
 */
public class AccelerometerStoreListener implements SensorEventListener {
	private boolean				mInitialized			= false;
	public DbAdapter			db;
	private float				mLastX, mLastY, mLastZ;
	private float 				mLastXLinear, mLastYLinear, mLastZLinear;
	private float[]				lastValuesRotationVector;
	private double lastValueTimestamp = 0;
	public static Settings settings;
	private static final String	UNDEFINED_ACTION		= "UNDEFINED";

	public void closeDb() {
		db.close();
	}

	public AccelerometerStoreListener(Context context) {
		this(context, UNDEFINED_ACTION);
	}

	public AccelerometerStoreListener(Context context, String action) {
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
			
			// update last value for next onSensorChanged
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			if (lastValuesRotationVector != null) {
				db.saveSampleAccelerometer(x, y, z, lastValuesRotationVector[0], lastValuesRotationVector[1], lastValuesRotationVector[2], 
					event.timestamp, action, sensorDelay, accelerometer_position);
			}
		}
		else if (event.sensor == SamplingStoreService.mLinearAcceleration) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			mLastXLinear = x;
			mLastYLinear = y;
			mLastZLinear = z;
			if (lastValuesRotationVector != null) {
				db.saveSampleLinearAcceleration(x, y, z, lastValuesRotationVector[0], lastValuesRotationVector[1], lastValuesRotationVector[2], 
					event.timestamp, action, sensorDelay, accelerometer_position);
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
