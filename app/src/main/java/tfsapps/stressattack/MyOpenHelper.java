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
                + "isopen integer,"         //DBオープン
                + "level integer,"          //勇者のレベル
                + "damage integer,"         //勇者の能力：基本ダメージ
                + "critical integer,"       //勇者の能力：必殺の確率
                + "status_1 integer,"       //勇者の能力：予備１
                + "status_2 integer,"       //勇者の能力：予備２
                + "status_3 integer,"       //勇者の能力：予備３
                + "status_4 integer,"       //勇者の能力：予備４
                + "status_5 integer,"       //勇者の能力：予備５
                + "map integer,"            //システム：マップ（ステージ）
                + "jrate integer,"          //システム：浄化率
                + "jpoint integer,"         //システム：浄化ポイント
                + "sysm_1 integer,"         //システム：予備１
                + "sysm_2 integer,"         //システム：予備２
                + "sysm_3 integer,"         //システム：予備３
                + "sysm_4 integer,"         //システム：予備４
                + "sysm_5 integer,"         //システム：予備５
                + "enemy_1 integer,"        //敵１
                + "enemy_2 integer,"        //敵２
                + "enemy_3 integer,"        //敵３
                + "enemy_4 integer,"        //敵４
                + "enemy_5 integer,"        //敵５
                + "enemy_6 integer,"        //敵６
                + "enemy_7 integer,"        //敵７
                + "enemy_8 integer,"        //敵８
                + "enemy_9 integer,"        //敵９
                + "enemy_10 integer,"       //敵１０
                + "enemy_11 integer,"       //敵１１
                + "enemy_12 integer,"       //敵１２
                + "enemy_13 integer,"       //敵１３
                + "enemy_14 integer,"       //敵１４
                + "enemy_15 integer,"       //敵１５
                + "boss_1 integer,"         //ボス１
                + "boss_2 integer,"         //ボス２
                + "boss_3 integer,"         //ボス３
                + "boss_4 integer,"         //ボス４
                + "boss_5 integer,"         //ボス５
                + "boss_6 integer,"         //ボス６
                + "boss_7 integer,"         //ボス７
                + "boss_8 integer,"         //ボス８
                + "boss_9 integer,"         //ボス９
                + "boss_10 integer);");     //ボス１０
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
