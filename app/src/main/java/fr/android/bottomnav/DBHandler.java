package fr.android.bottomnav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "UserList";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "data";
    public static final String COLUMN_NAME_FIRSTNAME = "firstname";
    public static final String COLUMN_NAME_LASTNAME = "lastname";
    public static final String COLUMN_NAME_MAIL = "mail";
    public static final String COLUMN_NAME_AGE = "age";
    public static final String COLUMN_NAME_COUNTRY = "country";
    public static final String COLUMN_NAME_SIZE = "size";
    public static final String COLUMN_NAME_WEIGHT = "weight";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_NAME_FIRSTNAME + " TEXT, "
                + COLUMN_NAME_LASTNAME + " TEXT,"
                + COLUMN_NAME_MAIL + " TEXT,"
                + COLUMN_NAME_AGE + " INTEGER,"
                + COLUMN_NAME_COUNTRY + " TEXT,"
                + COLUMN_NAME_SIZE + " REAL,"
                + COLUMN_NAME_WEIGHT + " REAL)";
        db.execSQL(query);
    }

//This method is use to add new User to our sqlite database.
    public void addNewUser(String first, String last, String mail, int age, String country, double height, double weight) {

    //On below line we are creating a variable for our database to write in it.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

    //On below line we are passing all values along with its key and value pair.
        values.put(COLUMN_NAME_FIRSTNAME, first);
        values.put(COLUMN_NAME_LASTNAME, last);
        values.put(COLUMN_NAME_MAIL, mail);
        values.put(COLUMN_NAME_AGE, age);
        values.put(COLUMN_NAME_COUNTRY, country);
        values.put(COLUMN_NAME_SIZE, height);
        values.put(COLUMN_NAME_WEIGHT, weight);

    //After adding all values we are passing content values to our table.
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

/*//This method is use to retrieve user informations from our sqlite database :
    public void getUser(String mail){
        //ArrayList<User> userData = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

    //Declaring sql request :
        Cursor cursorUser = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE mail = '"+mail+"'", null);

    //Parcouring dataset :
        if (cursorUser.moveToFirst()) {
            do {
    //Save data :
                User.firstName = cursorUser.getString(0);
                User.lastName = cursorUser.getString(1);
                User.email = cursorUser.getString(2);
                User.age = cursorUser.getInt(3);
                User.country = cursorUser.getString(4);
                User.height = cursorUser.getDouble(5);
                User.weight = cursorUser.getDouble(6);

            } while (cursorUser.moveToNext());

        }

    //Clear memory :
        cursorUser.close();
        //return userData;
    }*/

    public void getUser(String mail){
        SQLiteDatabase db = this.getReadableDatabase();

        //Declaring sql request :
        Cursor cursorUser = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE mail = '"+mail+"'", null);

        Log.d("getUser", "test");

        //going through the dataset :
        if (cursorUser.moveToFirst()) {
            do {
                Log.d("getUser", User.firstName + " " + User.lastName + " " + User.email + " " + User.age + " " + User.country + " " + User.height + " " + User.weight);

                //Save data :
                User.firstName = cursorUser.getString(0);
                User.lastName = cursorUser.getString(1);
                User.email = cursorUser.getString(2);
                User.age = cursorUser.getInt(3);
                User.country = cursorUser.getString(4);
                User.height = cursorUser.getDouble(5);
                User.weight = cursorUser.getDouble(6);

                Log.d("getUser", User.firstName + " " + User.lastName + " " + User.email + " " + User.age + " " + User.country + " " + User.height + " " + User.weight);
            } while (cursorUser.moveToNext());
        }

        //Clear memory :
        cursorUser.close();
    }

    public void updateTable(String email, String firstName, String lastName, int age, String country, double height, double weight){
        //Querie to run :
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+ TABLE_NAME +" SET " +
                "firstname = '"+firstName+"', " +
                "lastname = '"+lastName+"', " +
                "age = "+age+", " +
                "country = '"+country+"', " +
                "size = "+height+", " +
                "weight = "+weight+"" +
                " WHERE mail = '" +email+"'"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}