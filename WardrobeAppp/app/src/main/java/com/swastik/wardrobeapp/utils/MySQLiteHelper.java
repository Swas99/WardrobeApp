package com.swastik.wardrobeapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

/**
 * @author Swastik Sahu
 *
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myWardrobe.db";
    private static final int DATABASE_VERSION = 1;

    //region TOPS table - keys
    public static final String TABLE_TOPS = "TOPS";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BASE64_PIC = "picture";
    //endregion

    //region BOTTOMS table - keys
    public static final String TABLE_BOTTOMS = "BOTTOMS";
//    public static final String COLUMN_ID = "id";
//    public static final String COLUMN_BASE64_PIC = "picture";
    //endregion

    //region FAVORITES table - keys
    public static final String TABLE_FAVORITES = "Favorites";
    //    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOP = "top_pic";
    public static final String COLUMN_BOTTOM = "bottom_pic";
    //endregion

    //region TODAY's Pick table - keys
    public static final String TABLE_PICK_OF_THE_DAY = "TodaysPick";
    public static final String COLUMN_DAY = "day";
//    public static final String COLUMN_TOP = "top_pic";
//    public static final String COLUMN_BOTTOM = "bottom_pic";
    //endregion

    //region create Tops table
    private static final String CREATE_TOPS_TABLE =
            "create table " + TABLE_TOPS + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_BASE64_PIC + " text"
                    + " );";
    //endregion
    
    //region create Bottoms table
    private static final String CREATE_BOTTOMS_TABLE =
            "create table " + TABLE_BOTTOMS + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_BASE64_PIC + " text"
                    + " );";
    //endregion

    //region create Favorites table
    private static final String CREATE_FAVORITES_TABLE =
            "create table " + TABLE_FAVORITES + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_TOP + " text,"
                    + COLUMN_BOTTOM + " text"
                    + " );";
    //endregion

    //region create TodayPick table
    private static final String CREATE_PICK_OF_THE_DAY_TABLE =
            "create table " + TABLE_PICK_OF_THE_DAY + "("
                    + COLUMN_DAY + " integer, "
                    + COLUMN_TOP + " text,"
                    + COLUMN_BOTTOM + " text"
                    + " );";
    //endregion

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOPS_TABLE);
        db.execSQL(CREATE_BOTTOMS_TABLE);
        db.execSQL(CREATE_FAVORITES_TABLE);
        db.execSQL(CREATE_PICK_OF_THE_DAY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOTTOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICK_OF_THE_DAY);
        onCreate(db);
    }


    //region tops table operations
    public static void insertListToTopsTable(Context context, List<String> base64_pic)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        database.execSQL("delete from "+ TABLE_TOPS);

        for ( String pic : base64_pic) {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_BASE64_PIC, pic);
            database.insert(MySQLiteHelper.TABLE_TOPS, null, values);
        }
        database.close();
    }

    public static void insertRowToTopsTable(Context context, String pic)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        database.execSQL("delete from "+ TABLE_TOPS);

        ContentValues values = new ContentValues();
        values.put(COLUMN_BASE64_PIC, pic);
        database.insert(MySQLiteHelper.TABLE_TOPS, null, values);
        database.close();
    }

    public static void deleteRowFromTopsTable(Context context, String pic)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(TABLE_TOPS, COLUMN_BASE64_PIC + " = " + pic, null);
        database.close();
    }
    public static List<String> fetchListFromTopsTable(Context context)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        List<String> pics = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TOPS+ " ORDER BY " + COLUMN_ID + " DESC";;
        Cursor c = database.rawQuery(selectQuery, null);
        while (c.moveToNext())
            pics.add(c.getString(c.getColumnIndex(COLUMN_ID)));

        c.close();
        database.close();
        return pics;
    }
    public static String getPicByIdFromTopsTable(Context context,String id)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TOPS + " WHERE " + COLUMN_ID + " = " + id;
        Cursor c = database.rawQuery(selectQuery, null);
        while (c.moveToNext())
            return (c.getString(c.getColumnIndex(COLUMN_BASE64_PIC)));
        c.close();
        database.close();
        return null;
    }
    //endregion

    //region Bottoms table operations
    public static void insertListToBottomsTable(Context context, List<String> base64_pic)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        database.execSQL("delete from "+ TABLE_BOTTOMS);

        for ( String pic : base64_pic) {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_BASE64_PIC, pic);
            database.insert(MySQLiteHelper.TABLE_BOTTOMS, null, values);
        }
        database.close();
    }

    public static void insertRowToBottomsTable(Context context, String pic)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        database.execSQL("delete from "+ TABLE_BOTTOMS);

        ContentValues values = new ContentValues();
        values.put(COLUMN_BASE64_PIC, pic);
        database.insert(MySQLiteHelper.TABLE_BOTTOMS, null, values);
        database.close();
    }

    public static void deleteRowFromBottomsTable(Context context, String pic)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(TABLE_BOTTOMS, COLUMN_BASE64_PIC + " = " + pic, null);
        database.close();
    }
    public static List<String> fetchListFromBottomsTable(Context context)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        List<String> pics = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOTTOMS + " ORDER BY " + COLUMN_ID + " DESC";
        Cursor c = database.rawQuery(selectQuery, null);
        while (c.moveToNext())
            pics.add(c.getString(c.getColumnIndex(COLUMN_ID)));

        c.close();
        database.close();
        return pics;
    }
    public static String getPicByIdFromBottomsTable(Context context,String id)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_BOTTOMS + " WHERE " + COLUMN_ID + " = " + id;
        Cursor c = database.rawQuery(selectQuery, null);
        if (c.moveToNext())
            return (c.getString(c.getColumnIndex(COLUMN_BASE64_PIC)));
        c.close();
        database.close();
        return null;
    }
    //endregion

    //region Favorites table operations
    public static void insertRowToFavoritesTable(Context context, String top,String bottom)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        database.execSQL("delete from "+ TABLE_BOTTOMS);

        ContentValues values = new ContentValues();
        values.put(COLUMN_TOP, top);
        values.put(COLUMN_BOTTOM, bottom);
        database.insert(MySQLiteHelper.TABLE_FAVORITES, null, values);
        database.close();
    }

    public static void deleteRowFromFavoritesTable(Context context, String top,String bottom)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(TABLE_FAVORITES, COLUMN_TOP + "=" + top + " and " + COLUMN_BOTTOM + "=" + bottom, null);
        database.close();
    }

    public static boolean checkForEntryInFavoritesTable(Context context,String topId,String bottomId)
    {
        try {
            MySQLiteHelper dbHelper = new MySQLiteHelper(context);
            SQLiteDatabase database = dbHelper.getReadableDatabase();

            String selectQuery = "SELECT * FROM " + TABLE_FAVORITES + " WHERE " +
                    COLUMN_TOP + "=" + topId + " and " + COLUMN_BOTTOM + "=" + bottomId;
            Cursor c = database.rawQuery(selectQuery, null);
            boolean hasEntry = !(c.getCount()==0);
            c.close();
            database.close();
            return hasEntry;
        }catch (Exception e)
        {
        }
        return false;
    }
    //endregion


    //region PickOfTheDay table operations

    public static void insertIntoPickOfTheDayTable(Context context,int day,String topId,String bottomId)
    {
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL("delete from "+ TABLE_PICK_OF_THE_DAY);
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DAY, day);
        values.put(MySQLiteHelper.COLUMN_TOP, topId);
        values.put(MySQLiteHelper.COLUMN_BOTTOM, bottomId);
        database.insert(MySQLiteHelper.TABLE_PICK_OF_THE_DAY, null, values);
        database.close();
    }

    public static String getPickOfTheDay(Context context)
    {
        SharedPreferenceManager preferenceManager = SharedPreferenceManager.getSharedPreferenceManager(context);
        if(preferenceManager.getKeyData(CommonUtil.FIRST_RUN_FLAG).isEmpty())
            return "";
        CommonUtil.clearFirstRunFlag(preferenceManager);

        String pickOfTheDay,topId,bottomId;
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_PICK_OF_THE_DAY ;
            c = database.rawQuery(selectQuery, null);
            Calendar calendar = GregorianCalendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_MONTH);
            if(c.getCount()>0)
            {
                c.moveToFirst();
                int day = c.getInt(c.getColumnIndex(COLUMN_DAY));
                //Return today's pick
                if(day==today)
                {
                    return c.getString(c.getColumnIndex(COLUMN_TOP))
                            + "_" + c.getString(c.getColumnIndex(COLUMN_BOTTOM));
                }
            }

            //Generate new pick of the day
            if(Math.random()<.54)//54% chances of picking from favorites collection
            {
                selectQuery = "SELECT * FROM " + TABLE_FAVORITES ;
                c = database.rawQuery(selectQuery, null);
                if(c.getCount()>0)
                {
                    Random rand = new Random();
                    int randomItem = rand.nextInt(c.getCount());
                    c.moveToPosition(randomItem);
                    topId = c.getString(c.getColumnIndex(COLUMN_TOP));
                    bottomId = c.getString(c.getColumnIndex(COLUMN_BOTTOM));
                    pickOfTheDay = topId + "_" + bottomId;
                    insertIntoPickOfTheDayTable(context,today,topId,bottomId);
                    return pickOfTheDay;
                }
            }

            //64%
            selectQuery = "SELECT * FROM " + TABLE_TOPS ;
            c = database.rawQuery(selectQuery, null);
            if(c.getCount()>0)
            {
                Random rand = new Random();
                int randomItem = rand.nextInt(c.getCount());
                c.moveToPosition(randomItem);
                topId = c.getString(c.getColumnIndex(COLUMN_ID));

                selectQuery = "SELECT * FROM " + TABLE_BOTTOMS ;
                c = database.rawQuery(selectQuery, null);
                if(c.getCount()>0)
                {
                    randomItem = rand.nextInt(c.getCount());
                    c.moveToPosition(randomItem);
                    bottomId = c.getString(c.getColumnIndex(COLUMN_ID));
                    pickOfTheDay = topId + "_" + bottomId;
                    insertIntoPickOfTheDayTable(context,today,topId,bottomId);
                    c.close();
                    database.close();
                    return pickOfTheDay;
                }
            }
        }catch (Exception e)
        {
        }
        finally {
            if(c!=null)
                c.close();
            database.close();
        }
        return "";
    }

    //endregion

    public static void clearAllData(Context context) {

        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL("delete from "+ TABLE_TOPS + " ");
        database.execSQL("delete from "+ TABLE_BOTTOMS);
        database.execSQL("delete from "+ TABLE_FAVORITES);
        database.execSQL("delete from "+ TABLE_PICK_OF_THE_DAY);
        database.close();

    }
}