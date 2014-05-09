package org.unipd.nbeghin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Nicola Beghin on 10/06/13.
 */
public class DbAdapter {

        @SuppressWarnings("unused")
        private static final String LOG_TAG = DbAdapter.class.getSimpleName();

        private Context context;
        public SQLiteDatabase database;
        private DatabaseHelper dbHelper;
        private int trunkAccelerometer=1;
        private int trunkLinearAcceleration=1;

        // Database fields
        private static String DATABASE_TABLE;
        private static String DATABASE_TABLE_LINEAR;
        private static String DATABASE_TABLE_TEST;
        private static String DATABASE_TABLE_LINEAR_TEST;
        public static final String KEY_timestamp = "timestamp";
        public static final String KEY_x = "x";
        public static final String KEY_y = "y";
        public static final String KEY_z = "z";
        public static final String KEY_rotationX = "rotationX";
        public static final String KEY_rotationY = "rotationY";
        public static final String KEY_rotationZ = "rotationZ";
        public static final String KEY_sex = "sex";
        public static final String KEY_age = "age";
        public static final String KEY_height = "height";
        public static final String KEY_shoes = "shoes";
        public static final String KEY_mode = "mode";
        public static final String KEY_action = "action";
        public static final String KEY_trunk = "trunk";
        
        public DbAdapter(Context context) {
            this.context = context;
        }

        public DbAdapter open() throws SQLException {
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
            DATABASE_TABLE = DatabaseHelper.getDatabaseTable();
            DATABASE_TABLE_LINEAR = DatabaseHelper.getDatabaseTableLinear();
            DATABASE_TABLE_TEST = DatabaseHelper.getDatabaseTableTest();
            DATABASE_TABLE_LINEAR_TEST = DatabaseHelper.getDatabaseTableLinearTest();
            this.getNewTrunkIdAccelerometer();
            this.getNewTrunkIdLinear();
            return this;
        }

        public void close() {
            database.close();
        }

        public String getDbPath() {
            return database.getPath();
        }

        private ContentValues createContentValues(float x, float y, float z, float xRotation, float yRotation, float zRotation, 
        		long timestamp, String action, int trunk, String mode, float minDiff) {
            ContentValues values = new ContentValues();
            values.put(KEY_x, x);
            values.put(KEY_y, y);
            values.put(KEY_z, z);
            values.put(KEY_rotationX, xRotation);
            values.put(KEY_rotationY, yRotation);
            values.put(KEY_rotationZ, zRotation);
            values.put(KEY_timestamp, timestamp);
            values.put(KEY_action, action);
            values.put(KEY_trunk, trunk);
            values.put(KEY_mode, mode);
            return values;
        }

        public void getNewTrunkIdAccelerometer() {
            this.trunkAccelerometer=1;
            if (this.getCount(false)>0) this.trunkAccelerometer=(int)database.compileStatement("SELECT MAX(trunk)+1 FROM " + DATABASE_TABLE).simpleQueryForLong();
        }
        
        public void getNewTrunkIdLinear() {
        	this.trunkLinearAcceleration=1;
            if (this.getCount(true)>0) this.trunkLinearAcceleration=(int)database.compileStatement("SELECT MAX(trunk)+1 FROM " + DATABASE_TABLE_LINEAR).simpleQueryForLong();
        }

        public long getCount(boolean linear) {
        	if (!linear) {
        		return database.compileStatement("SELECT COUNT(*) FROM " + DATABASE_TABLE).simpleQueryForLong();
        	}
        	else {
        		return database.compileStatement("SELECT COUNT(*) FROM " + DATABASE_TABLE_LINEAR).simpleQueryForLong();
        	}
        }

        //create a contact
        public void saveSampleAccelerometer(float x, float y, float z, float xRotation, float yRotation, float zRotation, 
        		long timestamp, String action, int sensorDelay, String mode, float minDiff) {
            database.insertOrThrow(DATABASE_TABLE, null, createContentValues(x,y,z, xRotation, yRotation, zRotation, timestamp, action, sensorDelay, trunkAccelerometer, mode, minDiff));
        }
        
        public void saveSampleLinearAcceleration(float x, float y, float z, float xRotation, float yRotation, float zRotation, long timestamp, 
        		String action, int sensorDelay, String mode, float minDiff) {
        	
        	database.insertOrThrow(DATABASE_TABLE_LINEAR, null, createContentValues(x, y, z, xRotation, yRotation, zRotation, timestamp, action, sensorDelay, trunkLinearAcceleration, mode, minDiff));
        }

        public void cleanDb() {
            database.execSQL("DELETE FROM "+DATABASE_TABLE);
            database.execSQL("DELETE FROM " + DATABASE_TABLE_LINEAR);
            database.execSQL("VACUUM");
        }
}
