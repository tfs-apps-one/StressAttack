package tfsapps.stressattack;

/**
 * Created by FURUKAWA on 2017/11/03.
 */

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper
{
    private static final String TABLE = "appinfo";
    public MyOpenHelper(Context context) {
        super(context, "AppDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + "("
                + "isopen integer,"       //DBオープン
                + "level integer,"
                + "stamina integer,"
                + "money integer,"
                + "coin integer,"
                + "gold integer,"
                + "silver integer,"
                + "bronze integer,"
                + "stage integer,"
                + "scoop integer,"
                + "goggles integer,"
                + "ore_1 integer,"
                + "ore_2 integer,"
                + "ore_3 integer,"
                + "item_1 integer,"
                + "item_2 integer,"
                + "item_3 integer,"
                + "syssw_1 integer,"
                + "syssw_2 integer,"
                + "syssw_3 integer,"
                + "syssw_4 integer,"
                + "syssw_5 integer);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
