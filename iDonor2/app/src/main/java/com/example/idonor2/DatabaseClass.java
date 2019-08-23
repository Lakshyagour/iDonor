package com.example.idonor2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;

public class DatabaseClass {
    myDbHelper myhelper;
    public DatabaseClass(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public void insertImage(String uid, byte[] data)

    {


        SQLiteDatabase dbb = myhelper.getWritableDatabase();
       if (!checkIfExits(uid)) {
           ContentValues contentValues = new ContentValues();
           contentValues.put(myhelper.ImageKey, uid);
           contentValues.put(myhelper.ProfilePhoto, data);
           dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
       }
       else if(checkIfExits(uid))
       {updateImage(uid,data);}

    }

    private void updateImage(String uid, byte[] data) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ProfilePhoto,data);
        String[] whereArgs= {uid};
        dbb.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.ImageKey+" = ?",whereArgs );

    }

    public  boolean checkIfExits(String value)
{
    SQLiteDatabase dbb = myhelper.getWritableDatabase();
    String query="Select * from "+myDbHelper.TABLE_NAME+" where " +myDbHelper.ImageKey +"='"+value+"';";
    Cursor cursor=dbb.rawQuery(query,null);
    if(cursor.getCount()<=0)
    {
        cursor.close();
        return false;
    }
   else {cursor.close();
    return true;}
}
    private byte[] getBitmapAsArray(Bitmap img) {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG,0,outputStream);
        return  outputStream.toByteArray();
    }

    public Bitmap getImage(String uid)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uid};
        String[] columns = {myDbHelper.ProfilePhoto};
Cursor cursor=db.query(myDbHelper.TABLE_NAME,columns,myDbHelper.ImageKey+"=?",whereArgs,null,null,null);

        byte[] imagebyteArray = null;
        while (cursor.moveToNext())
        {
            imagebyteArray=cursor.getBlob(cursor.getColumnIndex(myDbHelper.ProfilePhoto));

        }
        return getbyteArrayToImage(imagebyteArray);

    }

    private Bitmap getbyteArrayToImage(byte[] imagebyteArray) {
        return BitmapFactory.decodeByteArray(imagebyteArray,0,imagebyteArray.length);
    }


    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;   // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String ImageKey = "imageKey";    //Column II
        private static final String ProfilePhoto= "Image";    // Column III
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+ "("+ ImageKey+" VARCHAR(225) PRIMARY KEY ,"+ProfilePhoto+" BLOB);";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                MessageClass.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                MessageClass.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                MessageClass.message(context,""+e);
            }
        }
    }
}

