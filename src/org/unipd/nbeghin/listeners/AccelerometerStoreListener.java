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
	public static DbAdapter		db;
	private float[]				lastValuesRotationVector;
	public static Settings 		settings = null;
	private static final String	UNDEFINED_ACTION		= "UNDEFINED";
	private boolean isTestData = false;

	public void closeDb() {
		db.close();
	}
	
	public void setTestData(boolean testData) {
		this.isTestData = testData;
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
			
			if (lastValuesRotationVector != null) {
				db.saveSampleAccelerometer(event.timestamp, x, y, z, 
						lastValuesRotationVector[0], lastValuesRotationVector[1], lastValuesRotationVector[2], 
					settings.getSex(), settings.getAge(), settings.getHeight(), 
					settings.getShoes(), settings.getMode(), settings.getAction(), settings.getTestData());
			}
		}
		else if (event.sensor == SamplingStoreService.mLinearAcceleration) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			if (lastValuesRotationVector != null) {
				db.saveSampleLinearAcceleration(event.timestamp, x, y, z, 
						lastValuesRotationVector[0], lastValuesRotationVector[1], lastValuesRotationVector[2], 
					settings.getSex(), settings.getAge(), settings.getHeight(), 
					settings.getShoes(), settings.getMode(), settings.getAction(), settings.getTestData());
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
	
	public void saveNotes(String text) {
		db.saveNotesForTestData(text);
	}
}
