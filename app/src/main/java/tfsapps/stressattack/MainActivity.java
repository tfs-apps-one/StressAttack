package tfsapps.stressattack;

import androidx.appcompat.app.AppCompatActivity;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

    private int enemy_type = 0;
    private int enemy_hp = 0;
    private int damege = 0;
    private String enemynamestr = "";
    private String gamestr = "";
    private String bak1_gamestr = "";
    private String bak2_gamestr = "";
    private String bak3_gamestr = "";
    private String bak4_gamestr = "";
    private String bak5_gamestr = "";
    private ProgressBar prog = null;
    private TextView story = null;
    private TextView ename = null;
    private int seqno = 0;
    private int display_hold = 0;
    private int game_step = 0;
    //ゲーム進行ステップ
    final int G_INIT = 0;
    final int G_STORY = 1;
    final int G_BATTLE = 2;
    final int G_RESULT = 3;
    final int G_ENDING = 4;
    //インターバル時間
    final int TIME_EFFECT = 4;
    final int TIME_PROG_SHORT = 15;
    final int TIME_PROG_LONG = 20;
    final int TIME_PROG_HI_LONG = 100;

    //サウンド関係
    private AudioManager am;
    private int start_volume;

    private int bgm_index = 1;
    private MediaPlayer bgm = null;
    private MediaPlayer s_menu = null;
    private MediaPlayer bs_normal = null;
    private MediaPlayer bs_boss = null;
    private MediaPlayer efs_11 = null;
    private MediaPlayer efs_12 = null;
    private MediaPlayer efs_21 = null;
    private MediaPlayer efs_22 = null;
    private MediaPlayer efs_31 = null;
    private MediaPlayer efs_32 = null;
    private MediaPlayer efs_41 = null;
    private MediaPlayer efs_42 = null;

    final String E_NAME_1 = "オーガ";
    final String E_NAME_2 = "ミイラ";
    final String B_NAME_1 = "ゴーレム";
    final String B_NAME_2 = "闇ドラゴン";

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
                if (enemy_hp == 0 || display_hold > 0){
                    ;
                }
                else {
                    EventGameView();
                }
        }
        return super.onTouchEvent(event);
    }

    /****************************************************
     ゲーム進行ステップのセット
     ****************************************************/
    public void GameNextStep() {
        switch (game_step){
            default:
                game_step = G_STORY;
                break;
            case G_STORY:
                game_step = G_BATTLE;
                break;
            case G_BATTLE:
                game_step = G_RESULT;
                break;
            case G_RESULT:
                game_step = G_ENDING;
                break;
            case G_ENDING:
                game_step = G_INIT;
                break;
        }
    }

    /****************************************************
        ゲーム関係パラメータの初期化
    ****************************************************/
    public void GameParaInit() {

        int en_type = rand.nextInt(100);
        int boss_type = rand.nextInt(100);

        if (enemy == null) {
            enemy = (ImageView) findViewById(R.id.img_enemy);
        }
        if (prog == null) {
            prog = (ProgressBar) findViewById(R.id.progress);
        }
        if (ename == null){
            ename = (TextView) findViewById(R.id.text_enemy_name);
        }
        if (story == null){
            story = (TextView) findViewById(R.id.text_story);
        }

        game_step = G_INIT;     //ゲーム進行
        if (en_type < 50){
            if ((en_type % 2) == 0){
                enemy_type = 1;         //敵通常
            }
            else{
                enemy_type = 2;         //敵通常
            }
            enemy_hp = 100;             //敵ＨＰ
            BgmStart(2);
        }
        else{
            if ((boss_type % 2) == 0) {
                enemy_type = 11;         //敵BOSS
            }
            else{
                enemy_type = 12;        //敵BOSS
            }
            enemy_hp = 200;             //敵ＨＰ
            BgmStart(3);
        }
        prog.setMin(0);
        prog.setMax(enemy_hp);

        damege = 0;             //敵へのダメージ
        seqno = 0;              //ターン数
        display_hold = 0;       //エフェクトホールド状態
        enemynamestr = "";      //敵名前
        gamestr = "";           //ゲーム進行文字
        bak1_gamestr = "";      //ゲーム進行文字　前１行目
        bak2_gamestr = "";      //ゲーム進行文字　前２行目
        bak3_gamestr = "";      //ゲーム進行文字　前３行目
        bak4_gamestr = "";      //ゲーム進行文字　前４行目
        bak5_gamestr = "";      //ゲーム進行文字　前５行目
    }
    /****************************************************
     ゲーム表示（エンディング）
     ****************************************************/
    public void GameEndingView() {
        //敵イメージ
        EnemyDisp(99);
        BgmStart(4);
        //ダメージバー
        prog.setProgress(0);
        //敵名前
        enemynamestr = "";
        ename.setText(enemynamestr);
        //ストリー
        gamestr =   "　エンディング\n" +
                "　";
        story.setText(gamestr);
        display_hold = TIME_PROG_HI_LONG;
        GameNextStep();
    }
    /****************************************************
     ゲーム表示（バトル結果）
     ****************************************************/
    public void GameResultView() {
        //敵イメージ
        EnemyDisp(99);
        //ダメージバー
        prog.setProgress(0);
        //敵名前
        enemynamestr = "";
        ename.setText(enemynamestr);
        //ストリー
        gamestr =   "　勇者のストレスが解消された\n" +
                    "　＜戦闘結果＞";
        story.setText(gamestr);
        display_hold = TIME_PROG_LONG;
        GameNextStep();
    }
    /****************************************************
        ゲーム表示（バトル中）
     ****************************************************/
    public void GameButtleView(){
        //敵イメージ
        if(enemy_hp == 0) {
            EnemyDisp(99);
        }
        else{
            EnemyDisp(0);
        }
        //ダメージバー
        prog.setProgress(enemy_hp);
        //敵名前
        ename.setText(enemynamestr);
        //ストリー
        story.setText(gamestr);
    }
    /****************************************************
     ゲーム表示（ストーリー）
     ****************************************************/
    public void GameStoryView() {
        //敵イメージ
        EnemyDisp(99);
        //ダメージバー
        prog.setProgress(0);
        //敵名前
        enemynamestr = "";
        ename.setText(enemynamestr);
        //ストリー
        gamestr =   "　勇者がストレスの森を歩いていると・・・・\n" +
                    "　なんと！？\n\n　敵が現れた！！";
        story.setText(gamestr);
//        enemy_hp = 100;
//        seqno = 0;
        display_hold = TIME_PROG_SHORT;
        GameNextStep();
    }
    /****************************************************
     ゲーム表示（メイン）
     ****************************************************/
    public void GameView() {
        if (display_hold > 0) {
            display_hold--;
            return;
        }

        switch (game_step){
            default:
            case G_INIT:
                GameParaInit();
                GameNextStep();
                break;
            case G_STORY:
                GameStoryView();
                break;
            case G_BATTLE:
                GameButtleView();
                break;
            case G_RESULT:
                GameResultView();
                break;
            case G_ENDING:
                GameEndingView();
                break;
        }

    }


    /****************************************************
         モンスター表示
     ****************************************************/
    public void EnemyDisp(int at_type) {

        // 非バトル中のモンスター画像表示
        if (at_type == 99) {
            enemynamestr = "";
            enemy.setImageResource(0);
            enemy.setBackgroundResource(R.drawable.bak_11);
            return;
        }

        // 以下モンスターごとに表示
        if (enemy_type == 1) {
            enemynamestr = E_NAME_1;
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.e_10);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.e_11);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.e_12);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.e_13);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.e_14);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
            }
        }
        if (enemy_type == 2) {
            enemynamestr = E_NAME_2;
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.e_20);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.e_21);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.e_22);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.e_23);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.e_24);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
            }
        }
        else if(enemy_type == 11){
            enemynamestr = B_NAME_1;
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.b_10);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.b_11);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.b_12);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.b_13);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.b_14);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
            }
        }
        else if(enemy_type == 12){
            enemynamestr = B_NAME_2;
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.b_20);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.b_21);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.b_22);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.b_23);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.b_24);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
            }
        }
    }

    /****************************************************
     モンスター表示
     ****************************************************/
    public void EffectSoundStart(int at_type) {

        switch (at_type) {
            default:
            case 1:
                if (efs_11.isPlaying()==false) efs_11.start();
                if (efs_12.isPlaying()==false) efs_12.start();
                break;
            case 2:
                if (efs_21.isPlaying()==false) efs_21.start();
                if (efs_22.isPlaying()==false) efs_22.start();
                break;
            case 3:
                if (efs_31.isPlaying()==false) efs_31.start();
                if (efs_32.isPlaying()==false) efs_32.start();
                break;
            case 4:
                if (efs_41.isPlaying()==false) efs_41.start();
                if (efs_42.isPlaying()==false) efs_42.start();
                break;
        }
    }
    /****************************************************
        ゲーム表示（タップ時）
     ****************************************************/
    public void EventGameView(){

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

        EnemyDisp(at_type);         //モンスター画像
        EffectSoundStart(at_type);  //エフェクト音

        switch (at_type){
            default:
            case 1:
                at_str = "斬撃で ";
                damege = 5;
                break;
            case 2:
                at_str = "打撃で ";
                damege = 5;
                break;
            case 3:
                at_str = "鋭い雷撃で ";
                damege = 15;
                display_hold = TIME_EFFECT;
                break;
            case 4:
                at_str = "大爆発で ";
                damege = 15;
                display_hold = TIME_EFFECT;
                break;
        }
        enemy_hp -= damege;
        seqno++;

        if (enemy_hp <= 0){
            gamestr = "　敵（＝ストレス）を倒した！！";
            display_hold = TIME_PROG_SHORT;
            GameNextStep();
        }
        else {
            bak5_gamestr = bak4_gamestr;
            bak4_gamestr = bak3_gamestr;
            bak3_gamestr = bak2_gamestr;
            bak2_gamestr = bak1_gamestr;
            bak1_gamestr = "　" + seqno + "ターン目:  " + "敵に"+ at_str + damege + " のダメージ\n";
            gamestr = "";
            gamestr += bak1_gamestr;
            gamestr += bak2_gamestr;
            gamestr += bak3_gamestr;
            gamestr += bak4_gamestr;
            gamestr += bak5_gamestr;
        }
    }

    /* ゲーム中のイベントタイマー */
    public void GameStart(){
        setContentView(R.layout.activity_sub);
        GameParaInit();

        //タイマーインスタンス生成
        GameTimer = new Timer();
        //タスククラスインスタンス生成
        gameTimerTask = new GameTimerTask();
        //タイマースケジュール設定＆開始
        GameTimer.schedule(gameTimerTask, 0, 100);  //100msec更新
    }

    public class GameTimerTask extends TimerTask {
        @Override
        public void run() {
            //ここに定周期で実行したい処理を記述します
            gHandler.post(new Runnable() {
                public void run() {
                    GameView();
                }
            });
        }
    }

    public void SoundInit(){

        if (am == null) {
            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            start_volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        }

        if (bgm == null){
            bgm = (MediaPlayer) MediaPlayer.create(this, R.raw.menu);
        }

        //以下攻撃エフェクト
        if (efs_11 == null){
            efs_11 = (MediaPlayer) MediaPlayer.create(this, R.raw.s_11);
        }
        if (efs_12 == null){
            efs_12 = (MediaPlayer) MediaPlayer.create(this, R.raw.s_11);
        }

        if (efs_21 == null){
            efs_21 = (MediaPlayer) MediaPlayer.create(this, R.raw.s_12);
        }
        if (efs_22 == null){
            efs_22 = (MediaPlayer) MediaPlayer.create(this, R.raw.s_12);
        }

        if (efs_31 == null){
            efs_31 = (MediaPlayer) MediaPlayer.create(this, R.raw.s_13);
        }
        if (efs_32 == null){
            efs_32 = (MediaPlayer) MediaPlayer.create(this, R.raw.s_13);
        }

        if (efs_41 == null){
            efs_41 = (MediaPlayer) MediaPlayer.create(this, R.raw.s_14);
        }
        if (efs_42 == null){
            efs_42 = (MediaPlayer) MediaPlayer.create(this, R.raw.s_14);
        }

    }

    public void SoundStop() {

        if (bgm != null){
            bgm.stop();     bgm.release();      bgm = null;
        }

        if (efs_11 != null){
            efs_11.stop();  efs_11.release();   efs_11 = null;
        }
        if (efs_12 != null){
            efs_12.stop();  efs_12.release();   efs_12 = null;
        }
        if (efs_21 != null){
            efs_21.stop();  efs_21.release();   efs_21 = null;
        }
        if (efs_22 != null){
            efs_22.stop();  efs_22.release();   efs_22 = null;
        }
        if (efs_31 != null){
            efs_31.stop();  efs_31.release();   efs_31 = null;
        }
        if (efs_32 != null){
            efs_32.stop();  efs_32.release();   efs_32 = null;
        }
        if (efs_41 != null){
            efs_41.stop();  efs_41.release();   efs_41 = null;
        }
        if (efs_42 != null){
            efs_42.stop();  efs_42.release();   efs_42 = null;
        }
    }
    /***************************************************
        音源処理
     ****************************************************/
    public void BgmStart(int index){

        if (bgm == null){
            return;
        }
        else{
            if (bgm_index != index) {
                bgm.stop();
                bgm.release();
                bgm = null;
            }
        }
        bgm_index = index;

        switch (index){
            default:
            case 1:
                if (bgm == null){
                    bgm = (MediaPlayer) MediaPlayer.create(this, R.raw.menu);
                }
                break;
            case 2:
                if (bgm == null){
                    bgm = (MediaPlayer) MediaPlayer.create(this, R.raw.battle);
                }
                break;
            case 3:
                if (bgm == null){
                    bgm = (MediaPlayer) MediaPlayer.create(this, R.raw.boss);
                }
                break;
            case 4:
                if (bgm == null){
                    bgm = (MediaPlayer) MediaPlayer.create(this, R.raw.ending);
                }
                break;
        }

        if (bgm.isPlaying() == false) {
            bgm.setLooping(true);
            bgm.start();
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

        /* サウンド */
        SoundInit();
        BgmStart(1);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        //  DB更新
        AppDBUpdated();
        //  サウンド
        SoundStop();
    }
    @Override
    public void onStop(){
        super.onStop();
        //  DB更新
        AppDBUpdated();
        //  サウンド
        SoundStop();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //  DB更新
        AppDBUpdated();
        //  サウンド
        SoundStop();
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