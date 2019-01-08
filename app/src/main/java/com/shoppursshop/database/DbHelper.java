package com.shoppursshop.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shoppursshop.utilities.Constants;

/**
 * Created by Shweta on 6/9/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = Constants.APP_NAME+".db";
    public static final String EVENT_TABLE = "Event";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String UPDATED_AT = "updatedAt";
    public static final String CREATED_AT = "createdAt";
    private Context context;

    public static final String CREATE_EVENT_TABLE = "create table "+EVENT_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+EVENT_TABLE);
        onCreate(db);
    }

  /*  public boolean insertBookingData(String level0Category,String level1Category,String level2Category,String receiptNumber,
                                  String date,String comment,String photo,int taxRate,String creditCardFee,double amount,
                                     double bookToAccountDebit,double bookToAccountCredit,String version)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LEVEL_0_CATEGORY, level0Category);
        contentValues.put(LEVEL_1_CATEGORY, level1Category);
        contentValues.put(LEVEL_2_CATEGORY, level2Category);
        contentValues.put(RECEIPT_NUMBER, receiptNumber);
        contentValues.put(DATE, date);
        contentValues.put(COMMENT, comment);
        contentValues.put(PHOTO, photo);
        contentValues.put(TAX_RATE, taxRate);
        contentValues.put(CREDIT_CARD_FEE, creditCardFee);
        contentValues.put(AMOUNT, amount);
        contentValues.put(BOOK_TO_ACCOUNT_DEBIT, bookToAccountDebit);
        contentValues.put(BOOK_TO_ACCOUNT_CREDIT, bookToAccountCredit);
        contentValues.put(VERSION, version);
     //   contentValues.put(AMOUNT_CREDIT, amountCredit);

     //   contentValues.put(AMOUNT_DEBIT, amountDebit);

        db.insert("Booking", null, contentValues);
        return true;
    }

    public ArrayList<Booking> getAllBookings(String month){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from Booking where strftime('%m',_date)=?";
        Cursor res =  db.rawQuery(query, new String[]{month});
       // res.moveToFirst();
        ArrayList<Booking> bookingList=new ArrayList<>();
        if(res.moveToFirst()){
            do{
                Booking booking=new Booking(res.getString(res.getColumnIndex(LEVEL_0_CATEGORY)),
                        res.getString(res.getColumnIndex(LEVEL_1_CATEGORY)),res.getString(res.getColumnIndex(LEVEL_2_CATEGORY)),
                        res.getString(res.getColumnIndex(RECEIPT_NUMBER)), res.getString(res.getColumnIndex(DATE)),
                        res.getString(res.getColumnIndex(COMMENT)),res.getString(res.getColumnIndex(PHOTO)),
                        res.getInt(res.getColumnIndex(TAX_RATE)),res.getString(res.getColumnIndex(CREDIT_CARD_FEE)),
                        res.getDouble(res.getColumnIndex(AMOUNT)),res.getInt(res.getColumnIndex(BOOK_TO_ACCOUNT_DEBIT)),
                        res.getInt(res.getColumnIndex(BOOK_TO_ACCOUNT_CREDIT)),res.getString(res.getColumnIndex(VERSION)));
                bookingList.add(booking);
            }while (res.moveToNext());
        }


        return bookingList;
    }

     public boolean updateSessionGuestCheckedOutServerStatus(String id,String eventId,String eventCatId,String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECKED_OUT_SERVER_SYNC_STATUS,status);
        contentValues.put(UPDATED_AT, Utility.getTimeStamp());
        db.update(SESSION_GUEST_TABLE, contentValues,ID+" =? and "+EVENT_ID+" =? and "+EVENT_CATEGORY_ID+" =?",
                new String[]{id,eventId,eventCatId});
        db.close();
        return true;
    }

    public boolean deleteSessionGuestTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SESSION_GUEST_TABLE, null, null);
        return true;
    }*/

    public boolean dropAndCreateBookingTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+EVENT_TABLE);
        onCreate(db);
        return true;
    }


}
