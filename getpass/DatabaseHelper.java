package com.tomi5548319.getpass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper { // TODO Add picture into the database
    
    private static final String DATABASE_NAME = "PassGenData.db";
    private static final String TABLE_NAME = "Passwords";
    private static final String COL_1 = "ID"; // int
    private static final String COL_2 = "VISIBLE_NAME"; // String (for recycler view purposes only)
    private static final String COL_3 = "NAME"; // String (for generation purposes only)
    private static final String COL_4 = "SEED"; // String
    private static final String COL_5 = "FLAGS"; // int

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

	// Database doesn't exist yet, so it has to be created
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" TEXT,"+COL_3+" TEXT,"+COL_4+" TEXT," +COL_5 +" INTEGER)");
    }

	// Database has changed - delete the old one
	// TODO Don't delete the old database, copy the data into the new one instead
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    boolean insertData(String name, String seed, int flags){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,name);
        contentValues.put(COL_4,seed);
        contentValues.put(COL_5,flags);

        long result = db.insert(TABLE_NAME, null, contentValues); // Insert data into the database

        return (result != -1);
    }

    Cursor getRecyclerData(){
        checkForUpdates();
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + COL_1 + ", " + COL_2 + " FROM " + TABLE_NAME, null);
    }

    /*Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase(); // Initialize the database
        return db.rawQuery("select * from " + TABLE_NAME,null); // Execute the SQL commands and return the result
    }*/

    Integer deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase(); // Initialize the database
        return db.delete(TABLE_NAME, "ID = ?",new String[]{"" + id}); // Returns 1 on successfull item removal
    }

    int getHighestID(){
        SQLiteDatabase db = this.getWritableDatabase(); // Initialize the database
        final String MY_QUERY = "SELECT MAX(" +COL_1+ ") AS " +COL_1+ " FROM " + TABLE_NAME; // SQL command which gets the highest ID from databse

        Cursor cursor = db.rawQuery(MY_QUERY, null); // Execute the SQL commands
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(COL_1));
        cursor.close();
        return id;
    }

    Cursor getViewData(int id){
        SQLiteDatabase db = this.getWritableDatabase(); // Initialize the database
        final String MY_QUERY = "SELECT " +COL_3+ "," +COL_4+ " FROM " + TABLE_NAME + " WHERE ID = " + id; // SQL command which gets the highest ID from databse

        return db.rawQuery(MY_QUERY, null);
    }

    Cursor getEditData(int id){
        SQLiteDatabase db = this.getWritableDatabase(); // Initialize the database
        final String MY_QUERY = "SELECT " +COL_2+ "," +COL_4+ " FROM " + TABLE_NAME + " WHERE ID = " + id; // SQL command which gets the highest ID from databse

        return db.rawQuery(MY_QUERY, null);
    }

    /*Cursor getRowData(int id){
        SQLiteDatabase db = this.getWritableDatabase(); // Initialize the database
        final String MY_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id; // SQL command which gets the row with specified ID

        return db.rawQuery(MY_QUERY, null);
    }*/

    boolean updateEditData(int id, String visible_name){
        SQLiteDatabase db = this.getWritableDatabase(); // Initialize the database
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2,visible_name);

        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{"" + id});

        return true;
    }

    void checkForUpdates(){ //TODO implement multiple tables (v1, v2...)
        // TODO copy data from the old database to the new one
        SQLiteDatabase db = this.getWritableDatabase(); // Initialize the database
        boolean newestVersion = true;

        Cursor res = db.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);

        String text = "";

		// TODO WHY???
        if (res.getCount() == 0){
            newestVersion = false;
        }else{
            res.moveToNext();
            text = res.getString(1);
            if(!text.equals(COL_1)){
                newestVersion = false;
            }else{
                res.moveToNext();
                text = res.getString(1);
                if(!text.equals(COL_2)){
                    newestVersion = false;
                }else{
                    res.moveToNext();
                    text = res.getString(1);
                    if(!text.equals(COL_3)){
                        newestVersion = false;
                    }else{
                        res.moveToNext();
                        text = res.getString(1);
                        if(!text.equals(COL_4)){
                            newestVersion = false;
                        }else{
                            res.moveToNext();
                            text = res.getString(1);
                            if(!text.equals(COL_5)){
                                newestVersion = false;
                            }else{

                            }
                        }
                    }
                }
            }
        }

        if (!newestVersion){
            onUpgrade(db, 1, 1);
        }

        res.close();

    }

}
