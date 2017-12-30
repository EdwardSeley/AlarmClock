package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDatabaseHelper extends SQLiteOpenHelper {

    public AlarmDatabaseHelper(Context context) {
        super(context, AlarmTableSchema.NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + AlarmTableSchema.NAME +
        "(" + "_id integer primary key autoincrement, " +
        AlarmTableSchema.COLS.ALARM_ID + ", " +
        AlarmTableSchema.COLS.ALARM_TIME + ", " +
        AlarmTableSchema.COLS.ALARM_DATE + ", " +
        AlarmTableSchema.COLS.ALARM_TURNED_ON + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
