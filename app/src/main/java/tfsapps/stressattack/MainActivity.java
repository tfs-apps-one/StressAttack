package tfsapps.stressattack;

import androidx.appcompat.app.AppCompatActivity;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends AppCompatActivity  implements Game.Callback{

    static private Game myGame = null;
    private final Random rand = new Random(System.currentTimeMillis());

    //  DB関連
    public MyOpenHelper helper;             //DBアクセス
    private int db_isopen = 0;              //DB使用したか
    private int db_level = 0;               //DB
    private int db_stamina = 0;             //DB
    private int db_money = 0;               //DB
    private int db_coin = 0;                //DB
    private int db_gold = 0;                //DB
    private int db_silver = 0;              //DB
    private int db_bronze = 0;              //DB
    private int db_stage = 0;               //DB
    private int db_scoop = 0;               //DB
    private int db_goggles = 0;             //DB
    private int db_ore_1 = 0;               //DB
    private int db_ore_2= 0;                //DB
    private int db_ore_3 = 0;               //DB
    private int db_item_1 = 0;              //DB
    private int db_item_2 = 0;              //DB
    private int db_item_3 = 0;              //DB
    private int db_syssw_1 = 0;             //DB
    private int db_syssw_2 = 0;             //DB
    private int db_syssw_3 = 0;             //DB
    private int db_syssw_4 = 0;             //DB
    private int db_syssw_5 = 0;             //DB

    private boolean taskrun = false;
    private ImageView enemy = null;

    private Timer GameTimer;					//タイマー用
    private GameTimerTask gameTimerTask;		//タイマタスククラス
    private Handler gHandler = new Handler();   //UI Threadへのpost用ハンドラ

    private int enemy_hp = 0;
    private int damege = 0;
    private String gamestr = "";
    private String bak1_gamestr = "";
    private String bak2_gamestr = "";
    private String bak3_gamestr = "";
    private String bak4_gamestr = "";
    private String bak5_gamestr = "";
    private ProgressBar prog = null;
    private TextView story = null;
    private int seqno = 0;
    private int effect_hold = 0;
    private int game_step = 0;
    final int G_INIT = 0;
    final int G_STORY = 1;
    final int G_BATTLE = 2;
    final int G_RESULT = 3;
    final int G_ENDING = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /* タッチイベント（タップ処理）自機移動 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (enemy_hp == 0 || effect_hold > 0){
                    ;
                }
                else {
                    EventGameView();
                }
        }
        return super.onTouchEvent(event);
    }


    public void GameView(){
        //敵イメージ
        if (enemy == null) {
            enemy = (ImageView) findViewById(R.id.img_enemy);
        }
        if(enemy_hp == 0) {
            enemy.setImageResource(0);
        }
        else{
            enemy.setImageResource(R.drawable.enemy10);
        }
        enemy.setBackgroundResource(R.drawable.bak_11);

        //ダメージバー
        if (prog == null){
            prog = (ProgressBar) findViewById(R.id.progress);
            prog.setMin(0);
            prog.setMax(100);
        }
        if (damege == 0) {
            enemy_hp = 100;
            damege = 0;
            seqno = 0;
            gamestr = " モンスターが現れた\n";
            bak1_gamestr = "";
            bak2_gamestr = "";
            bak3_gamestr = "";
            bak4_gamestr = "";
            bak5_gamestr = "";
        }
        prog.setProgress(enemy_hp);

        //ストリー
        if (story == null){
            story = (TextView) findViewById(R.id.text_story);
        }
        story.setText(gamestr);
    }

    public void EventGameView(){
        if (enemy == null) {
            enemy = (ImageView) findViewById(R.id.img_enemy);
        }

        int at_rand = rand.nextInt(100);
        int sp_rand = rand.nextInt(100);
        int at_type = 0;
        String at_str = "";

        // 攻撃エフェクト  斬撃
        if (at_rand <= 50){
            at_type = 1;
            if (sp_rand >75){   //雷　斬撃
                at_type =3;
            }
        }
        //  打撃
        else{
            at_type = 2;
            if (sp_rand >75){   //爆発
                at_type =4;
            }
        }
        switch (at_type){
            default:
            case 1:
                at_str = "斬撃で ";
                damege = 5;
                enemy.setImageResource(R.drawable.enemy11);
                enemy.setBackgroundResource(R.drawable.bak_12);
                break;
            case 2:
                at_str = "打撃で ";
                damege = 5;
                enemy.setImageResource(R.drawable.enemy12);
                enemy.setBackgroundResource(R.drawable.bak_12);
                break;
            case 3:
                at_str = "鋭い雷撃で ";
                damege = 15;    effect_hold = 5;
                enemy.setImageResource(R.drawable.enemy13);
                enemy.setBackgroundResource(R.drawable.bak_13);
                break;
            case 4:
                at_str = "大爆発で ";
                damege = 15;    effect_hold = 5;
                enemy.setImageResource(R.drawable.enemy14);
                enemy.setBackgroundResource(R.drawable.bak_14);
                break;
        }
        enemy_hp -= damege;
        seqno++;

        if (enemy_hp <= 0){
            gamestr = " モンスターを倒した！！";
        }
        else {
            bak5_gamestr = bak4_gamestr;
            bak4_gamestr = bak3_gamestr;
            bak3_gamestr = bak2_gamestr;
            bak2_gamestr = bak1_gamestr;
            bak1_gamestr = " " + seqno + "ターン目:  " + "敵に"+ at_str + damege + " のダメージ\n";
            gamestr = "";
            gamestr += bak1_gamestr;
            gamestr += bak2_gamestr;
            gamestr += bak3_gamestr;
            gamestr += bak4_gamestr;
            gamestr += bak5_gamestr;
        }
    }

    public void GameStart(){
        setContentView(R.layout.activity_sub);
        //タイマーインスタンス生成
        GameTimer = new Timer();
        //タスククラスインスタンス生成
        gameTimerTask = new GameTimerTask();
        //タイマースケジュール設定＆開始
        GameTimer.schedule(gameTimerTask, 0, 200);  //100msec更新
    }

    public class GameTimerTask extends TimerTask {
        @Override
        public void run() {
            //ここに定周期で実行したい処理を記述します
            gHandler.post(new Runnable() {
                public void run() {
                    if (effect_hold <= 0) {
                        GameView();
                    }
                    else{
                        effect_hold--;
                    }
                }
            });
        }
    }

    /************************************************
     ゲームスタート
     ************************************************/
    public void onGameScreen(View v){
        GameStart();
    }
    /************************************************
     買い物
     ************************************************/
    public void onShop(View v){

    }
    /************************************************
     ステータス（戦歴）
     ************************************************/
    public void onStatus(View v){

    }

    /**
     * OS関連処理
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        //DBのロード
        /* データベース */
        helper = new MyOpenHelper(this);
        AppDBInitRoad();
    }
    @Override
    public void onResume() {
        super.onResume();
        //動画
    }
    @Override
    public void onPause(){
        super.onPause();
        //  DB更新
        AppDBUpdated();
    }
    @Override
    public void onStop(){
        super.onStop();
        //  DB更新
        AppDBUpdated();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //  DB更新
        AppDBUpdated();
    }


    /***************************************************
     　↓↓↓ 以下、DB関連処理　↓↓↓
     ***************************************************/

    /***************************************************
     DB初期ロードおよび設定
     ****************************************************/
    public void AppDBInitRoad() {
        SQLiteDatabase db = helper.getReadableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" isopen");
        sql.append(" ,level");
        sql.append(" ,stamina");
        sql.append(" ,money");
        sql.append(" ,coin");
        sql.append(" ,gold");
        sql.append(" ,silver");
        sql.append(" ,bronze");
        sql.append(" ,stage");
        sql.append(" ,scoop");
        sql.append(" ,goggles");
        sql.append(" ,ore_1");
        sql.append(" ,ore_2");
        sql.append(" ,ore_3");
        sql.append(" ,item_1");
        sql.append(" ,item_2");
        sql.append(" ,item_3");
        sql.append(" ,syssw_1");
        sql.append(" ,syssw_2");
        sql.append(" ,syssw_3");
        sql.append(" ,syssw_4");
        sql.append(" ,syssw_5");
        sql.append(" FROM appinfo;");
        try {
            Cursor cursor = db.rawQuery(sql.toString(), null);
            //TextViewに表示
            StringBuilder text = new StringBuilder();
            if (cursor.moveToNext()) {
                db_isopen = cursor.getInt(0);
                db_level = cursor.getInt(1);
                db_stamina = cursor.getInt(2);
                db_money = cursor.getInt(3);
                db_coin = cursor.getInt(4);
                db_gold = cursor.getInt(5);
                db_silver = cursor.getInt(6);
                db_bronze = cursor.getInt(7);
                db_stage = cursor.getInt(8);
                db_scoop = cursor.getInt(9);
                db_goggles = cursor.getInt(10);
                db_ore_1 = cursor.getInt(11);
                db_ore_2 = cursor.getInt(12);
                db_ore_3 = cursor.getInt(13);
                db_item_1 = cursor.getInt(14);
                db_item_2 = cursor.getInt(15);
                db_item_3 = cursor.getInt(16);
                db_syssw_1 = cursor.getInt(17);
                db_syssw_2 = cursor.getInt(18);
                db_syssw_3 = cursor.getInt(19);
                db_syssw_4 = cursor.getInt(20);
                db_syssw_5 = cursor.getInt(21);
            }
        } finally {
            db.close();
        }

        db = helper.getWritableDatabase();
        if (db_isopen == 0) {
            long ret;
            /* 新規レコード追加 */
            ContentValues insertValues = new ContentValues();
            insertValues.put("isopen", 1);
            insertValues.put("level", 1);
            insertValues.put("stamina", 100);
            insertValues.put("money", 0);
            insertValues.put("coin", 0);
            insertValues.put("gold", 0);
            insertValues.put("silver", 0);
            insertValues.put("bronze", 0);
            insertValues.put("stage", 0);
            insertValues.put("scoop", 0);
            insertValues.put("goggles", 0);
            insertValues.put("ore_1", 0);
            insertValues.put("ore_2", 0);
            insertValues.put("ore_3", 0);
            insertValues.put("item_1", 0);
            insertValues.put("item_2", 0);
            insertValues.put("item_3", 0);
            insertValues.put("syssw_1", 0);
            insertValues.put("syssw_2", 0);
            insertValues.put("syssw_3", 0);
            insertValues.put("syssw_4", 0);
            insertValues.put("syssw_5", 0);
            try {
                ret = db.insert("appinfo", null, insertValues);
            } finally {
                db.close();
            }
            db_isopen = 1;
            db_level = 1;
            db_stamina = 100;
            /*
            if (ret == -1) {
                Toast.makeText(this, "DataBase Create.... ERROR", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "DataBase Create.... OK", Toast.LENGTH_SHORT).show();
            }
             */
        } else {
            /*
            Toast.makeText(this, "Data Loading...  interval:" + db_interval, Toast.LENGTH_SHORT).show();
             */
        }
    }

    /***************************************************
     DB更新
     ****************************************************/
    public void AppDBUpdated() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("isopen", db_isopen);
        insertValues.put("level", db_level);
        insertValues.put("stamina", db_stamina);
        insertValues.put("money", db_money);
        insertValues.put("coin", db_coin);
        insertValues.put("gold", db_gold);
        insertValues.put("silver", db_silver);
        insertValues.put("bronze", db_bronze);
        insertValues.put("stage", db_stage);
        insertValues.put("scoop", db_scoop);
        insertValues.put("goggles", db_goggles);
        insertValues.put("ore_1", db_ore_1);
        insertValues.put("ore_2", db_ore_2);
        insertValues.put("ore_3", db_ore_3);
        insertValues.put("item_1", db_item_1);
        insertValues.put("item_2", db_item_2);
        insertValues.put("item_3", db_item_3);
        insertValues.put("syssw_1", db_syssw_1);
        insertValues.put("syssw_2", db_syssw_2);
        insertValues.put("syssw_3", db_syssw_3);
        insertValues.put("syssw_4", db_syssw_4);
        insertValues.put("syssw_5", db_syssw_5);
        int ret;
        try {
            ret = db.update("appinfo", insertValues, null, null);
        } finally {
            db.close();
        }
        /*
        if (ret == -1) {
            Toast.makeText(this, "Saving.... ERROR ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saving.... OK "+ "op=0:"+db_isopen+" interval=1:"+db_interval+" brightness=2:"+db_brightness, Toast.LENGTH_SHORT).show();
        }
         */
    }

}