package com.joern.flywayexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.android.ContextHolder;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DatabaseHelper.class.getName();

    private static final String DB_NAME = "mydb";
    private static final int  DB_VERSION = 1;

    private static final String TABLE_DOGS = "dogs";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_COLOR = "color";
    private static final String COL_RACE = "race";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        migrate(context);
    }

    private void migrate(Context context){

        SQLiteDatabase db = context.openOrCreateDatabase(DB_NAME, 0, null);
        ContextHolder.setContext(context);
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:sqlite:" + db.getPath(), "", "");
        flyway.setBaselineOnMigrate(true);
        flyway.migrate();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "onUpgrade()");
    }

    public List<Dog> readDogs(){

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DOGS;
        Cursor c = db.rawQuery(selectQuery, null);

        List<Dog> result = null;

        if(c != null){

            result = new ArrayList<>();

            while(c.moveToNext()){

                String id = c.getString(c.getColumnIndex(COL_ID));
                String name = c.getString(c.getColumnIndex(COL_NAME));
                String color = c.getString(c.getColumnIndex(COL_COLOR));
                String race = c.getString(c.getColumnIndex(COL_RACE));

                Dog dog = new Dog(id, name, color, race);

                result.add(dog);
            }
            c.close();
        }
        return result;
    }

    public boolean createDog(String name, String color, String race){

        boolean dogCreated = false;

        try{
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_DOGS, null);

            ContentValues values = new ContentValues();
            values.put(COL_NAME, name);
            values.put(COL_COLOR, color);
            values.put(COL_RACE, race);

            long dbId = db.insert(TABLE_DOGS, null, values);
            dogCreated = dbId != -1;

            c.close();

        }catch(Exception ex){
            Log.d(LOG_TAG, "failed to create dog", ex);
        }
        return dogCreated;
    }

    public void deleteDogs(){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_DOGS, null, null);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}