package org.unipd.nbeghin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nicola Beghin on 10/06/13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE="samples_accelerometer";
    private static final String DATABASE_TABLE_LINEAR = "samples_linear";
    private static final String DATABASE_TABLE_TEST = "samples_accelerometer_test";
    private static final String DATABASE_TABLE_LINEAR_TEST = "samples_accelerometer_linear_test";
    private static final String DATABASE_ANNOTATIONS_TEST = "annotations_test";
    private static final String DATABASE_NAME="accelbench.db";
    private static final int SCHEMA=8;

    public static String getDatabaseTable() {
        return DATABASE_TABLE;
    }
    
    public static String getDatabaseTableLinear() {
    	return DATABASE_TABLE_LINEAR;
    }
    
    public static String getDatabaseTableTest() {
    	return DATABASE_TABLE_TEST;
    }
    
    public static String getDatabaseTableLinearTest() {
    	return DATABASE_TABLE_LINEAR_TEST;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_TABLE+"\" (\"timestamp\" REAL, \"x\" REAL,\"y\" REAL,\"z\" REAL,"
        		+ "\"rotationX\" REAL, \"rotationY\" REAL, \"rotationZ\" REAL, " 
        		+ "\"sex\" TEXT, \"age\" TEXT, \"height\" TEXT, \"shoes\" TEXT, \"mode\" TEXT, "
        		+ "\"action\" TEXT, \"trunk\" INTEGER);");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_TABLE_LINEAR+"\" (\"x\" REAL,\"y\" REAL,\"z\" REAL,"
        		+ "\"rotationX\" REAL, \"rotationY\" REAL, \"rotationZ\" REAL, "
        		+ "\"sex\" TEXT, \"age\" TEXT, \"height\" TEXT, \"shoes\" TEXT, \"mode\" TEXT, "
        		+ "\"action\" TEXT, \"trunk\" INTEGER);");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_TABLE_TEST+"\" (\"timestamp\" REAL, \"x\" REAL,\"y\" REAL,\"z\" REAL,"
        		+ "\"rotationX\" REAL, \"rotationY\" REAL, \"rotationZ\" REAL, " 
        		+ "\"sex\" TEXT, \"age\" TEXT, \"height\" TEXT, \"shoes\" TEXT, \"mode\" TEXT, "
        		+ "\"trunk\" INTEGER);");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_TABLE_LINEAR_TEST+"\" (\"x\" REAL,\"y\" REAL,\"z\" REAL,"
        		+ "\"rotationX\" REAL, \"rotationY\" REAL, \"rotationZ\" REAL, "
        		+ "\"sex\" TEXT, \"age\" TEXT, \"height\" TEXT, \"shoes\" TEXT, \"mode\" TEXT, "
        		+ "\"trunk\" INTEGER);");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_ANNOTATIONS_TEST+"\" (\"trunk\" INTEGER, "
        		+ "\"annotation\" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LINEAR);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TEST);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LINEAR_TEST);
        onCreate(db);
    }
    
    public void onDestroy() {
    	
    }

}
