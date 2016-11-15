package edu.upi.student.fikryalfarisimuslim.googlemaps;

/**
 * Created by Fikry Al Farisi M on 15/11/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBadapter {

    public static class Markers{
        public String id;
        public String nama;
        public double lat;
        public double lng;
        public int stats;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setNama(String nama){
            this.nama = nama;
        }
        public String getNama(){
            return this.nama;
        }
        public void setLat(double lat){
            this.lat = lat;
        }
        public double getLat(){
            return this.lat;
        }
        public void setLng(double lng){
            this.lng = lng;
        }
        public double getLng(){
            return this.lng;
        }
        public void setStats(int stats){this.stats = stats;}
        public int getStats(){ return this.stats;}
    }

    private static final String TAG = "DBAdapter"; //used for logging database version changes

    // Field Names:
    public static final String KEY_ROWID = "_id";
    public static final String KEY_Name = "Name";
    public static final String KEY_Lat = "Lat";
    public static final String KEY_Lng = "Lng";
    public static final String stats = "bln";

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_Name,KEY_Lat,KEY_Lng,stats};


    // Info database:
    public static final String DATABASE_NAME = "DBMaps";
    public static final String DATABASE_TABLE = "marker";
    public static final int DATABASE_VERSION = 1; // The version number must be incremented each time a change to DB structure occurs.

    //SQL untuk membuat database
    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY, "
                    + KEY_Name + " TEXT NOT NULL, "
                    + KEY_Lat + " DOUBLE NOT NULL, "
                    + KEY_Lng + " DOUBLE NOT NULL, "
                    + stats + " INT NOT NULL "
                    + ");";



    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DBadapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Membuka koneksi ke database.
    public DBadapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Menutup koneksi ke database
    public void close() {
        myDBHelper.close();
    }

    // Insert ke database.
    public long insertRow(String name,double lat, double lng, int statz) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_Name, name);
        newValues.put(KEY_Lat, lat);
        newValues.put(KEY_Lng, lng);
        newValues.put(stats, statz);
        //initialValues.put(KEY_DATE, date);

        // Menambah data ke database
        return db.insert(DATABASE_TABLE, null, newValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    // Mengambil semua data.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String name,double lat, double lng, int statz) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_Name, name);
        newValues.put(KEY_Lat, lat);
        newValues.put(KEY_Lng, lng);
        newValues.put(stats, statz);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public Markers getMarker(String id){
        Cursor cur = null;
        Markers T = new Markers();
        String[] cols = new String[]{KEY_ROWID,KEY_Name, KEY_Lat, KEY_Lng, stats};

        String[] param = {id};
        cur = db.query("marker",cols,"_id=?",param,null,null,null);

        if(cur.getCount()>0){
            cur.moveToFirst();
            T.id = cur.getString(0);
            T.nama = cur.getString(1);
            T.lat = cur.getDouble(2);
            T.lng = cur.getDouble(3);
            T.stats = cur.getInt(4);

        }

        return T;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
            _db.execSQL("INSERT INTO " + DATABASE_TABLE + " VALUES ('1', 'UPI', -6.863243, 107.595045, 0)");
            _db.execSQL("INSERT INTO " + DATABASE_TABLE + " VALUES ('2', 'UNPAS', -6.866418, 107.593617, 0)");
            _db.execSQL("INSERT INTO " + DATABASE_TABLE + " VALUES ('3', 'STPB', -6.869273, 107.593446, 0)");
            _db.execSQL("INSERT INTO " + DATABASE_TABLE + " VALUES ('4', 'Masjid Al-Furqon', -6.862988, 107.593467, 0)");
            _db.execSQL("INSERT INTO " + DATABASE_TABLE + " VALUES ('5', 'Paris Van Java', -6.889043, 107.596066, 0)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }

}