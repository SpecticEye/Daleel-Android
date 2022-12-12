package com.example.daleel;

import static com.example.daleel.DbBitmapUtil.getBytes;
import static com.example.daleel.data.Places.places;

import static java.security.AccessController.getContext;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PlaceDB.db";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create place table
        String CREATE_PLACE_TABLE = "CREATE TABLE places ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "category TEXT, " +
                "street TEXT," +
                "postal_code TEXT," +
                "city TEXT," +
                "country TEXT," +
                "phone TEXT, " +
                "image BLOB)";

        String CREATE_FAVORITES_TABLE = "CREATE TABLE favorites ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "place_id INTEGER, FOREIGN KEY (place_id)  REFERENCES places(id))";


        // create all tables
        db.execSQL(CREATE_PLACE_TABLE);
        db.execSQL(CREATE_FAVORITES_TABLE);

        // Dummy Data
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.gelsenkirchen_central_mosque);
        byte[] image = getBytes(bitmap);
        for (int i = 0; i < places.length; i++) {
            // create ContentValues to add key
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, places[i][0]);
            values.put(KEY_CATEGORY, places[i][1]);
            values.put(KEY_STREET, places[i][2]);
            values.put(KEY_POSTAL_CODE, places[i][3]);
            values.put(KEY_CITY, places[i][4]);
            values.put(KEY_COUNTRY, places[i][5]);
            values.put(KEY_PHONE, places[i][6]);
            values.put(KEY_IMAGE, image);

            // insert
            db.insert(TABLE_PLACES,
                    null,
                    values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older places table if existed
        db.execSQL("DROP TABLE IF EXISTS places");
        db.execSQL("DROP TABLE IF EXISTS favorites");

        // create fresh places table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) place + get all places + delete all places
     */

    // Places table name
    private static final String TABLE_PLACES = "places";

    // Places Table Columns names
    public static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_STREET = "street";
    private static final String KEY_POSTAL_CODE = "postal_code";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IMAGE = "image";

    private static final String TABLE_FAVORITES = "favorites";
    private static final String KEY_PLACE_ID = "place_id";

    private static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_STREET, KEY_POSTAL_CODE, KEY_CITY, KEY_COUNTRY, KEY_PHONE, KEY_IMAGE};

    public void addPlace(Place place) {

        // get refrence to writable db
        SQLiteDatabase db = getWritableDatabase();

        // create ContentValues to add key
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, place.getName());
        values.put(KEY_CATEGORY, place.getCategory());
        values.put(KEY_STREET, place.getStreet());
        values.put(KEY_POSTAL_CODE, place.getPostalCode());
        values.put(KEY_CITY, place.getCity());
        values.put(KEY_COUNTRY, place.getCountry());
        values.put(KEY_PHONE, place.getPhone());
        values.put(KEY_IMAGE, place.getImage());

        // insert
        db.insert(TABLE_PLACES,
                null,
                values);

        // close
        db.close();
    }

    // Get All Places
    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<Place>();

        // 1. build the query
        String GET_ALL = "SELECT * FROM places";

        // 2. get reference to writable DB
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(GET_ALL, null);

        // 3. go over each row, build place and add it to list
        try {
            if (c.moveToLast()) {
                do {
                    Place place = new Place(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                            c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getBlob(8));
                    places.add(place);
                } while (c.moveToPrevious());
            }
        } catch (Exception e) {
            Log.d("DB", "Error while trying to get posts from database");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        db.close();
        // return places
        return places;
    }


    // Deleting single place
    public void deletePlaces() {

        // 1. get reference to writable DB
        SQLiteDatabase db = getWritableDatabase();

        // 2. delete
        db.beginTransaction();
        try {
            db.delete(TABLE_PLACES, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("DB", "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }

        // 3. close
        db.close();

        Log.d("deletePlaces", "All Places deleted");
    }

    public Place getPlace(int id) {

        Place place = new Place();

        // 1. get reference to readable DB
        SQLiteDatabase db = getReadableDatabase();
        // 2. build query
        String GET_BOOK = "SELECT * FROM places WHERE id = " + id;

        // 3. if we got results get the first one
        Cursor c = db.rawQuery(GET_BOOK, null);

        if (c.moveToFirst()) {
            place.setId(c.getInt(0));
            place.setName(c.getString(1));
            place.setCategory(c.getString(2));
            place.setStreet(c.getString(3));
            place.setPostalCode(c.getString(4));
            place.setCity(c.getString(5));
            place.setCountry(c.getString(6));
            place.setPhone(c.getString(7));
            place.setImage(c.getBlob(8));
        }

        // 4. build place object
        Log.d("getPlace(" + id + ")", place.toString());

        db.close();

        // 5. return place
        return place;
    }

    // Updating single place
    public int updatePlace(Place place) {

        int i = 0;

        // 1. get reference to writable DB
        SQLiteDatabase db = getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        i = place.getId();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, place.getName());
        values.put(KEY_CATEGORY, place.getCategory());
        values.put(KEY_STREET, place.getStreet());
        values.put(KEY_POSTAL_CODE, place.getPostalCode());
        values.put(KEY_CITY, place.getCity());
        values.put(KEY_COUNTRY, place.getCountry());
        values.put(KEY_PHONE, place.getPhone());
        values.put(KEY_IMAGE, place.getImage());
        // 3. updating row
        db.update(TABLE_PLACES, values, KEY_ID + "=?", new String[]{String.valueOf(place.getId())});

        // 4. close
        db.close();
        return i;

    }

    public void addFavorite(int id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_ID, id);

        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public void deleteFavorite(int id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_FAVORITES, "id=" + id, null);
        db.close();
    }

    public List<Place> getAllFavorites() {
        List<Place> places = new ArrayList<Place>();

        // 1. build the query
        String GET_ALL = "SELECT * FROM places INNER JOIN favorites ON places.id=favorites.place_id";

        // 2. get reference to writable DB
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(GET_ALL, null);

        // 3. go over each row, build place and add it to list
        try {
            if (c.moveToLast()) {
                do {
                    Place place = new Place(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                            c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getBlob(8));
                    places.add(place);
                } while (c.moveToPrevious());
            }
        } catch (Exception e) {
            Log.d("DB", "Error while trying to get posts from database");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        db.close();
        // return places
        return places;
    }

    public boolean isFravorite(int i) {
        // implement
        return true;
    }

    public Cursor searchForPlaces(String searchString) {
        //String FIND_PLACES = "SELECT * FROM places WHERE KEY_NAME Like? %" + searchText+"%";

        // 2. get reference to writable DB
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[]{KEY_ID};
        searchString = "%" + searchString + "%";
        String where = KEY_NAME + " LIKE ?";
        String[] whereArgs = new String[]{searchString};

        Cursor cursor = null;

        try {
            if (db == null) {
                db = getReadableDatabase();
            }
            cursor = db.query(TABLE_PLACES, columns, where, whereArgs, null, null, null);
        } catch (Exception e) {
            Log.d("search_db", "SEARCH EXCEPTION! " + e);
        }

        return cursor;
    }
}

