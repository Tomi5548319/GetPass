package com.tomi5548319.getpass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper { // TODO Add picture into the database
    
    private static final String DATABASE_NAME = "PassGenData.db";
    private static final String TABLE_NAME = "Passwords";
    private static final String TABLE_NAME_V2 = "Passwords_v2";
    private static final String COL_1 = "ID"; // int
    private static final String COL_2 = "VISIBLE_NAME"; // String (for recycler view purposes only)
    private static final String COL_3 = "NAME"; // String (for generation purposes only)
    private static final String COL_4 = "SEED"; // String
    private static final String COL_5 = "FLAGS"; // int
    private static final String COL_6 = "LENGTH"; // int
    private static final String COL_7 = "SMALL"; // boolean (INTEGER)
    private static final String COL_8 = "BIG"; // boolean (INTEGER)
    private static final String COL_9 = "NUMBERS"; // boolean (INTEGER)
    private static final String COL_10 = "BASIC_CHARS"; // boolean (INTEGER)
    private static final String COL_11 = "ADVANCED_CHARS"; // boolean (INTEGER)
    private static final String COL_12 = "CUSTOM_CHARS"; // String

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

	// Database doesn't exist yet, so it has to be created
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME_V2 + "("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT," +COL_2+" TEXT," +COL_3+" TEXT," +COL_4+" TEXT,"
                +COL_5+" INTEGER," +COL_6 +" INTEGER," +COL_7 +" INTEGER," +COL_8 +" INTEGER," +COL_9 +" INTEGER," +COL_10 +" INTEGER," +COL_11 +" INTEGER," +COL_12 +" TEXT)");
    }

	// Database has changed - copy data from old tables to the newest one, and delete old tables
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		checkForUpdates();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    boolean insertData(String name, int length, boolean smallBool, boolean bigBool, boolean numbersBool, boolean basicCharsBool, boolean advancedCharsBool, String customChars, String seed, int flags){
        int small = (smallBool) ? 1 : 0;
        int big = (bigBool) ? 1 : 0;
        int numbers = (numbersBool) ? 1 : 0;
        int basicChars = (basicCharsBool) ? 1 : 0;
        int advancedChars = (advancedCharsBool) ? 1 : 0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,name);
        contentValues.put(COL_4,seed);
        contentValues.put(COL_5,flags);
        contentValues.put(COL_6,length);
        contentValues.put(COL_7,small);
        contentValues.put(COL_8,big);
        contentValues.put(COL_9,numbers);
        contentValues.put(COL_10,basicChars);
        contentValues.put(COL_11,advancedChars);
        contentValues.put(COL_12,customChars);

        long result = db.insert(TABLE_NAME_V2, null, contentValues);

        return (result != -1);
    }

    Cursor getRecyclerData(){
        checkForUpdates();
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + COL_1 + ", " + COL_2 + " FROM " + TABLE_NAME_V2, null);
    }

    Integer deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_V2, "ID = ?",new String[]{"" + id}); // Returns 1 on successfull item removal
    }

    int getHighestID(){
        SQLiteDatabase db = this.getWritableDatabase();
        final String MY_QUERY = "SELECT MAX(" +COL_1+ ") AS " +COL_1+ " FROM " + TABLE_NAME_V2;

        Cursor cursor = db.rawQuery(MY_QUERY, null); // Execute the SQL commands
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(COL_1));
        cursor.close();
        return id;
    }

    Cursor getViewData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        final String MY_QUERY = "SELECT " +COL_3+ "," +COL_4+ " FROM " + TABLE_NAME_V2 + " WHERE ID = " + id;

        return db.rawQuery(MY_QUERY, null);
    }

    Cursor getEditData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        final String MY_QUERY = "SELECT " +COL_2+ "," +COL_4+ " FROM " + TABLE_NAME_V2 + " WHERE ID = " + id;

        return db.rawQuery(MY_QUERY, null);
    }

    boolean updateEditData(int id, String visible_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2,visible_name);

        db.update(TABLE_NAME_V2, contentValues, "id = ?", new String[]{"" + id});

        return true;
    }

    void checkForUpdates(){

        SQLiteDatabase db = this.getWritableDatabase();

        final String MY_QUERY_OLD = "SELECT " +COL_1+ "," +COL_2+ "," +COL_3+ "," +COL_4+ "," +COL_5+ " FROM " + TABLE_NAME;
        Cursor res = db.rawQuery(MY_QUERY_OLD, null);

        if(res.getCount() != 0)
            while(res.moveToNext()){ // Insert data into each row
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_1, res.getInt(0));
                contentValues.put(COL_2, res.getString(1));
                contentValues.put(COL_3, res.getString(2));
                contentValues.put(COL_4, res.getString(3));
                contentValues.put(COL_5, res.getInt(4));
                contentValues.put(COL_6, 16);
                contentValues.put(COL_7, 1);
                contentValues.put(COL_8, 1);
                contentValues.put(COL_9, 1);
                contentValues.put(COL_10, 1);
                contentValues.put(COL_11, 1);
                contentValues.put(COL_12, "");

                db.insert(TABLE_NAME_V2, null, contentValues);

            }

        res.close();

    }
}

// Cursor res = db.rawQuery("PRAGMA table_info(" + TABLE_NAME_V2 + ")", null);