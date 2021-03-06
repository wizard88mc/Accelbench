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
    private static final String DATABASE_ANNOTATIONS_TEST = "annotations_test";
    private static final String DATABASE_NAME="accelbench.db";
    private static final int SCHEMA=10;

    public static String getDatabaseTable() {
        return DATABASE_TABLE;
    }
    
    public static String getDatabaseTableLinear() {
    	return DATABASE_TABLE_LINEAR;
    }
    
    public static String getDatabaseAnnotations() {
    	return DATABASE_ANNOTATIONS_TEST;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_TABLE+"\" (\"timestamp\" REAL, \"x\" REAL,\"y\" REAL,\"z\" REAL,"
        		+ "\"rotationX\" REAL, \"rotationY\" REAL, \"rotationZ\" REAL, " 
        		+ "\"sex\" TEXT, \"age\" TEXT, \"height\" TEXT, \"shoes\" TEXT, \"mode\" TEXT, "
        		+ "\"action\" TEXT, \"trunk\" INTEGER, \"testData\" INTEGER);");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_TABLE_LINEAR+"\" (\"timestamp\" REAL, \"x\" REAL,\"y\" REAL,\"z\" REAL,"
        		+ "\"rotationX\" REAL, \"rotationY\" REAL, \"rotationZ\" REAL, "
        		+ "\"sex\" TEXT, \"age\" TEXT, \"height\" TEXT, \"shoes\" TEXT, \"mode\" TEXT, "
        		+ "\"action\" TEXT, \"trunk\" INTEGER, \"testData\" INTEGER);");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS \""+DATABASE_ANNOTATIONS_TEST+"\" (\"trunk\" INTEGER, "
        		+ "\"trunkLinear\" INTEGER, \"annotation\" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LINEAR);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_ANNOTATIONS_TEST);
        onCreate(db);
    }
    
    public void onDestroy() {
    	
    }

}
