package tfsapps.stressattack;

import androidx.appcompat.app.AppCompatActivity;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {
//public class MainActivity extends AppCompatActivity {

    private final Random rand = new Random(System.currentTimeMillis());

    //  DB関連
    public MyOpenHelper helper;             //DBアクセス
    private int db_isopen = 0;              //DB使用したか

    private int db_level = 0;               //DB　勇者
    private int db_damage = 0;              //DB
    private int db_critical = 0;            //DB
    private int db_status_1 = 0;            //DB
    private int db_status_2 = 0;            //DB
    private int db_status_3 = 0;            //DB
    private int db_status_4 = 0;            //DB
    private int db_status_5 = 0;            //DB

    private int db_map = 0;                 //DB　システム
    private int db_jrate = 0;               //DB
    private int db_jpoint = 0;              //DB
    private int db_sysm_1 = 0;              //DB
    private int db_sysm_2 = 0;              //DB
    private int db_sysm_3 = 0;              //DB
    private int db_sysm_4 = 0;              //DB
    private int db_sysm_5 = 0;              //DB

    private int db_enemy_1 = 0;             //DB　敵
    private int db_enemy_2 = 0;             //DB
    private int db_enemy_3 = 0;             //DB
    private int db_enemy_4 = 0;             //DB
    private int db_enemy_5 = 0;             //DB
    private int db_enemy_6 = 0;             //DB
    private int db_enemy_7 = 0;             //DB
    private int db_enemy_8 = 0;             //DB
    private int db_enemy_9 = 0;             //DB
    private int db_enemy_10 = 0;            //DB
    private int db_enemy_11 = 0;            //DB
    private int db_enemy_12 = 0;            //DB
    private int db_enemy_13 = 0;            //DB
    private int db_enemy_14 = 0;            //DB
    private int db_enemy_15 = 0;            //DB

    private int db_boss_1 = 0;              //DB　ボス
    private int db_boss_2 = 0;              //DB　
    private int db_boss_3 = 0;              //DB　
    private int db_boss_4 = 0;              //DB　
    private int db_boss_5 = 0;              //DB　
    private int db_boss_6 = 0;              //DB　
    private int db_boss_7 = 0;              //DB　
    private int db_boss_8 = 0;              //DB　
    private int db_boss_9 = 0;              //DB　
    private int db_boss_10 = 0;             //DB　

    private int db_lastboss = 0;            //DB　ラストボス挑戦　
    private int db_brate = 0;               //DB　ラストダンジョン浄化率
    private int db_bpoint = 0;              //DB　ラストダンジョン浄化ポイント　
    private int db_present_a = 0;           //DB　プレゼント（攻撃）　
    private int db_present_b = 0;           //DB　プレゼント（BOSS）

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
    private ImageView gameimg = null;
    private TextView gamettl = null;
    private TextView gamesta = null;

    private int seqno = 0;
    private int display_hold = 0;
    private int game_step = 0;
    //ゲーム進行ステップ
    final int G_INIT = 0;
    final int G_STORY = 1;
    final int G_BATTLE = 2;
    final int G_BATTLE_ENEMY = 3;
    final int G_RESULT = 4;
    final int G_ENDING = 5;
    //インターバル時間
    final int TIME_EFFECT_SHORT = 2;
    final int TIME_EFFECT = 4;
    final int TIME_PROG_VERY_SHORT = 5;
    final int TIME_PROG_SHORT = 15;
    final int TIME_PROG_LONG = 20;
    final int TIME_PROG_HI_LONG = 50;
    final int TIME_PROG_FOREVER = 9000;

    //サウンド関係
    private AudioManager am;
    private int start_volume;

    private int ebgm_index = 1;
    private MediaPlayer ebgm = null;
    private int bgm_index = 1;
    private MediaPlayer bgm = null;
    private MediaPlayer s_menu = null;
    private MediaPlayer bs_normal = null;
    private MediaPlayer bs_boss = null;
    /*
    private MediaPlayer efs_11 = null;
    private MediaPlayer efs_12 = null;
    private MediaPlayer efs_21 = null;
    private MediaPlayer efs_22 = null;
    private MediaPlayer efs_31 = null;
    private MediaPlayer efs_32 = null;
    private MediaPlayer efs_41 = null;
    private MediaPlayer efs_42 = null;
    */

    final int E_TYPE_1 = 1;
    final String E_NAME_1 = "オーガ　　　　";
    final int E_TYPE_2 = 2;
    final String E_NAME_2 = "ミイラ　　　　";
    final int E_TYPE_3 = 3;
    final String E_NAME_3 = "バッファロー　";
    final int E_TYPE_4 = 4;
    final String E_NAME_4 = "さまよう剣士　";
    final int E_TYPE_5 = 5;
    final String E_NAME_5 = "シャドー　　　";
    final int E_TYPE_6 = 6;
    final String E_NAME_6 = "ゴーレム　　　";
    final int E_TYPE_7 = 7;
    final String E_NAME_7 = "トロル　　　　";
    final int E_TYPE_8 = 8;
    final String E_NAME_8 = "＊＊＊　　　　";
    final int E_TYPE_9 = 9;
    final String E_NAME_9 = "＊＊＊　　　　";
    final int E_TYPE_10 = 10;
    final String E_NAME_10 = "＊＊＊　　　　";

    final int B_TYPE_1 = 51;
    final String B_NAME_1 = "土ドラゴン　　";
    final int B_TYPE_2 = 52;
    final String B_NAME_2 = "闇ドラゴン　　";
    final int B_TYPE_3 = 53;
    final String B_NAME_3 = "風ドラゴン　　";
    final int B_TYPE_4 = 54;
    final String B_NAME_4 = "水ドラゴン　　";
    final int B_TYPE_5 = 55;
    final String B_NAME_5 = "火ドラゴン　　";
    final int B_TYPE_6 = 56;
    final String B_NAME_6 = "ストレス　　　";
    final int B_TYPE_7 = 57;
    final String B_NAME_7 = "＊＊＊＊　　　";
    final int B_TYPE_8 = 58;
    final String B_NAME_8 = "＊＊＊＊　　　";
    final int B_TYPE_9 = 59;
    final String B_NAME_9 = "＊＊＊＊　　　";
    final int B_TYPE_10 = 60;
    final String B_NAME_10 = "＊＊＊＊　　";

    final int EFFECT_1 = 0;
    final int EFFECT_2 = 50;
    final int EFFECT_3 = 100;
    //test
//    final int EFFECT_4 = 1000;
//    final int EFFECT_5 = 6000;
//    final int EFFECT_6 = 10000;
    final int EFFECT_4 = 100;
    final int EFFECT_5 = 500;
    final int EFFECT_6 = 1000;
    final int EFFECT_7 = 50000;
    final int EFFECT_8 = 99999;

    private int popdispcount = 0;
    private boolean last_map = false;

    // 広告
    private boolean isAdLoad = false;
    private RewardedVideoAd mRewardedVideoAd;

    // テストID
//    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    // 本物ID
    private static final String AD_UNIT_ID = "ca-app-pub-4924620089567925/5983300287";

    // テストID(APPは本物でOK)
    private static final String APP_ID = "ca-app-pub-4924620089567925~4407043472";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // リワード広告
        MobileAds.initialize(this, APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    /**
     * OS関連処理
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

    /**
     リワード広告処理
     */
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(AD_UNIT_ID,new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        String _tmp = getString(R.string.present_complete);
        Toast.makeText(this, _tmp, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        GetPresent();
        AppDBUpdated();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        String _tmp = getString(R.string.present_complete_err);
        Toast.makeText(this, _tmp+i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    /****************************************************
         ゲーム進行　文言取得
     ****************************************************/
    public String GetStoryString(){
        String tmpstr = "";

        if (last_map == false){
            tmpstr =    "\n\n\n" +
                    "　" + getString(R.string.mess_1_1)+
                    "\n" +
                    "　" + getString(R.string.mess_1_2)+
                    "\n\n\n" +
                    "　" + getString(R.string.mess_1_3)+
                    "\n" +
                    "　" + getString(R.string.mess_1_4)+
                    "　\n\n\n";
        }
        else{
            if (db_boss_6 == 1) {
                tmpstr = "\n\n\n" +
                        "　"+ getString(R.string.mess_2_1)+
                        "\n" +
                        "　"+ getString(R.string.mess_2_2)+
                        "\n\n\n" +
                        "　"+ getString(R.string.mess_2_3)+
                        "\n" +
                        "　"+ getString(R.string.mess_2_4)+
                        "　\n\n\n";
            }
            else{
                tmpstr = "\n\n\n" +
                        "　" + getString(R.string.mess_3_1)+
                        "\n" +
                        "　" + getString(R.string.mess_3_2)+
                        "\n\n\n" +
                        "　" + getString(R.string.mess_3_3)+
                        "\n" +
                        "　" + getString(R.string.mess_3_4)+
                        "　\n\n\n";
            }
        }

        return tmpstr;
    }

    /****************************************************
         ゲーム進行　ポップアップ表示
     ****************************************************/
    public void GameStoryPopup() {
        AlertDialog.Builder guide = new AlertDialog.Builder(this);
        TextView vmessage = new TextView(this);

        if (popdispcount >= 1){
            display_hold = 0;
            GameNextStep();
            GameView();
            return;
        }

        //メッセージ
        vmessage.setText( GetStoryString() );
        vmessage.setBackgroundColor(getResources().getColor(R.color.gray));
        vmessage.setTextColor(getResources().getColor(R.color.white));
        vmessage.setTypeface(Typeface.DEFAULT_BOLD);

        guide.setTitle( getString(R.string.common_pop_1) );
        guide.setIcon(R.drawable.book2);
        guide.setView(vmessage);
        guide.setPositiveButton( getString(R.string.common_pop_2) , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                popdispcount++;
                display_hold = 0;
                GameNextStep();
                GameView();
                return;
//              GameStoryPopup();
            }
        });
        guide.setCancelable(false);
        guide.create();
        guide.show();
        display_hold = TIME_PROG_FOREVER;
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
            case G_BATTLE_ENEMY:
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
    public void SetGameStep(int step) {
        game_step = step;
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
        if (gameimg == null){
            gameimg = (ImageView) findViewById(R.id.img_submap);
        }
        if (gamettl == null) {
            gamettl = (TextView) findViewById(R.id.text_submap_name);
        }
        if (gamesta == null){
            gamesta = (TextView) findViewById(R.id.text_mystatus);
        }

        game_step = G_INIT;     //ゲーム進行

        SetEnemyType();
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
        gamestr =   "　" + getString(R.string.mess_4_1)+
                    "\n" +
                    "　" + getString(R.string.mess_4_2)+
                    "\n";

        /* 平原の浄化率が100％達成 */
        if (last_map == false && db_lastboss > 0){
            gamestr += "\n"+
                    "　" + getString(R.string.mess_5_1)+
                    "\n";
        }
        /* ラスボスを討伐した時 */
        if (last_map == true && enemy_type == B_TYPE_6){
            if (enemy_hp <= 0 && seqno > 0){
                if (db_boss_6 == 1) {
                    popdispcount = 0;
                }
                gamestr =   "　" + getString(R.string.mess_6_1)+
                            "\n\n" +
                            "　" + getString(R.string.mess_6_2)+
                            "\n"+
                            "　" + getString(R.string.mess_6_3);
            }
        }

        story.setText(gamestr);
        story.setTextColor(getResources().getColor(R.color.white));
        display_hold = TIME_PROG_HI_LONG;
        GameNextStep();
    }
    /****************************************************
     ゲーム表示（バトル結果）
     ****************************************************/
    public void GameResultView() {
        int tmp_level = db_level;
        int _refpoint = 0;
        db_level += GetEnemyPoint(1);
        if (db_level > 200){
            db_level = 200;
        }
        //敵イメージ
        EnemyDisp(99);
        //ダメージバー
        prog.setProgress(0);
        //敵名前
        enemynamestr = "";
        ename.setText(enemynamestr);
        //ストリー
        gamestr =   "　" + getString(R.string.mess_7_1)+
                    "\n" +
                    "　" + getString(R.string.mess_7_2)+
                    "\n\n" +
                    "　" + getString(R.string.mess_7_3)+
                    "\n" +
                    "　" + getString(R.string.mess_7_4)+ tmp_level + " ▶︎ " + db_level + " " + getString(R.string.mess_7_5)+
                    "\n" +
                    "　" + GetEnemyName(false) + getString(R.string.mess_7_6)+
                    "\n" +
                    "　" + getString(R.string.mess_7_7) + GetEnemyPoint(0) + " " + getString(R.string.mess_7_8) +
                    "\n";

        _refpoint = GetEnemyPoint(4);
        if (_refpoint > 0){
            db_present_a += _refpoint;
            gamestr += "　" + getString(R.string.mess_7_9) + _refpoint + getString(R.string.mess_7_10);
        }

        story.setText(gamestr);
        story.setTextColor(getResources().getColor(R.color.white));
        display_hold = TIME_PROG_HI_LONG;

        if (last_map) {
            db_brate += GetEnemyPoint(2);
            db_jpoint += GetEnemyPoint(0);
        }
        else{
            db_jrate += GetEnemyPoint(2);
            db_jpoint += GetEnemyPoint(0);
            if (db_jrate >= 100) {
                db_lastboss = 1;
            }
        }

        SetEnemyKillPoint();
        GameNextStep();
    }
    /****************************************************
     ゲーム表示（バトル中）敵ターン
     ****************************************************/
    public void GameButtleEnemyView(){

        int tmp_point = 0;
        int aft_point = 0;

        /*
        if (last_map) {
            tmp_point = db_bpoint;
            db_bpoint = (db_bpoint - GetEnemyPoint(3));
            if (db_bpoint <= 0) {
                db_bpoint = 0;
            }
            aft_point = db_bpoint;
        }else{
            tmp_point = db_jpoint;
            db_jpoint = (db_jpoint - GetEnemyPoint(3));
            if (db_jpoint <= 0) {
                db_jpoint = 0;
            }
            aft_point = db_jpoint;
        }
         */
        tmp_point = db_jpoint;
        db_jpoint = (db_jpoint - GetEnemyPoint(3));
        if (db_jpoint <= 0) {
            db_jpoint = 0;
        }
        aft_point = db_jpoint;

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
        if (enemy_hp == 0) {
            ;
        } else {
            gamestr =
                        "　" + GetEnemyName(false) + getString(R.string.mess_8_1)+
                        "\n" +
                        "　" + getString(R.string.mess_8_2) +
                        "\n\n" +
                        "　" + getString(R.string.mess_8_3) + tmp_point + " ▶︎ " + aft_point + "　" + getString(R.string.mess_8_4);

            story.setText(gamestr);
            story.setTextColor(getResources().getColor(R.color.red));
        }
        SetGameStep(G_BATTLE);
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
        story.setTextColor(getResources().getColor(R.color.white));
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
        if (last_map == false){
            gamestr =   "　" + getString(R.string.mess_9_1)+
                        "\n";
        }
        else{
            gamestr =   "　" + getString(R.string.mess_9_2)+
                        "\n";
        }
        //ストリー
        gamestr +=  "　" + getString(R.string.mess_9_3)+
                    "\n\n" +
                    "　" + getString(R.string.mess_9_4)+ GetEnemyName(false) + getString(R.string.mess_9_5) +
                    "\n"+
                    "　" + getString(R.string.mess_9_6);

        story.setText(gamestr);
        story.setTextColor(getResources().getColor(R.color.white));
        display_hold = TIME_PROG_LONG;
        GetHeroPara(0); GetHeroPara(1);
        GameNextStep();
    }
    /****************************************************
     ゲーム表示（メイン）
     ****************************************************/
    public void GameView() {
        int _rate = 0;

        /* タイトル部の表示 */
        if (last_map == false) {
            gamettl.setText(getString(R.string.mess_10_1));
            if (db_jrate < 10) gameimg.setImageResource(R.drawable.map00);
            else if (db_jrate < 25) gameimg.setImageResource(R.drawable.map25);
            else if (db_jrate < 50) gameimg.setImageResource(R.drawable.map50);
            else if (db_jrate < 75) gameimg.setImageResource(R.drawable.map75);
            else gameimg.setImageResource(R.drawable.map99);
        }
        else{
            gamettl.setText(getString(R.string.mess_10_2));
            gameimg.setImageResource(R.drawable.map_last);
        }

        String buf = "";
        buf += "　" + getString(R.string.mess_10_3) + db_level;
        if (last_map)   _rate = db_brate;
        else            _rate = db_jrate;
        buf +=  "\n" +
                "　" + getString(R.string.mess_10_4) + _rate + "％（" + db_jpoint + "）";

        gamesta.setText(buf);

        if (display_hold > 0) {
            display_hold--;
            return;
        }

        switch (game_step){
            default:
            case G_INIT:
                GameParaInit();
                GameStoryPopup();
//                GameNextStep();
                break;
            case G_STORY:
                GameStoryView();
                break;
            case G_BATTLE:
                GameButtleView();
                break;
            case G_BATTLE_ENEMY:
                GameButtleEnemyView();
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
        ゲーム表示（タップ時）
     ****************************************************/
    public void EventGameView(){

        int at_rand = rand.nextInt(100);
        int sp_rand = rand.nextInt(100);
        int sp2_rand = rand.nextInt(100);
        int chit = (100 - db_critical); // 現在の必殺が出る確率
        int at_type = 0;
        String at_str = "";
        String _title = "";
        String _message = "";

        //test
        //chit = 30;
        // 攻撃エフェクト  斬撃
        if (at_rand <= 50){
            at_type = 1;
            if (sp_rand > chit){   //雷　斬撃
                at_type =3;
            }
        }
        //  打撃
        else{
            at_type = 2;
            if (sp_rand > chit){   //爆発
                at_type =4;
            }
        }
        //特別必殺
        if (at_type >= 3){
            if (sp2_rand > 70) {
                at_type = 5;
                if (sp2_rand > 90){
                    at_type = 6;
                }
            }
        }

        //エフェクトが取得できているか？
        if (GetEffectLevel(at_type) == false){
            at_type = 1;
        }
        EnemyDisp(at_type);         //モンスター画像
        EffectBgmStart(at_type);
        //EffectSoundStart(at_type);  //エフェクト音

        switch (at_type){
            default:
            case 1:
                at_str = "斬撃で ";
                damege = db_damage;
                display_hold = TIME_EFFECT_SHORT;
//                display_hold = 200;
                break;
            case 2:
                at_str = "打撃で ";
                damege = (db_damage * 110) / 100;
                display_hold = TIME_EFFECT_SHORT;
//                display_hold = 200;
                break;
            case 3:
                at_str = "鋭い雷撃で ";
                damege = (db_damage * 3);
                display_hold = TIME_EFFECT;
//                display_hold = 200;
                break;
            case 4:
                at_str = "大爆発で ";
                damege = (db_damage * 330) / 100;
                display_hold = TIME_EFFECT;
//                display_hold = 200;
                break;
            case 5:
                at_str = "一閃で ";
                damege = (db_damage * 5);
                display_hold = TIME_EFFECT;
//                display_hold = 200;
                break;
            case 6:
                at_str = "空間裂き ";
                damege = (db_damage * 6);
                display_hold = TIME_EFFECT;
//                display_hold = 200;
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
            bak1_gamestr =  "　" + seqno + "ターン目:  " + "敵に"+ at_str + damege + " のダメージ"+
                            "\n";
            gamestr = "";
            gamestr += bak1_gamestr+"\n";
            gamestr += bak2_gamestr;
            gamestr += bak3_gamestr;
            gamestr += bak4_gamestr;
            gamestr += bak5_gamestr;
        }
        /* 50ターン以上は退却 */
        if (seqno > 50){
            _title = getString(R.string.mess_12_1);
            _message =
                    "\n\n\n"+
                    "　" + getString(R.string.mess_12_2)+
                    "\n"+
                    "　" + getString(R.string.mess_12_3)+
                    "\n\n"+
                    "　" + getString(R.string.mess_12_4)+
                    "\n"+
                    "\n\n\n";
            DialogDisplay(_title,_message,3);
            return;
        }
    }

    /* タッチイベント（タップ処理）自機移動 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int enemy_at = rand.nextInt(100);
        int value = 90; //ステージ「平原」の被弾率
        if (last_map){
            value = 85; //ステージ「城」の被弾率
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                if (enemy_hp == 0 || display_hold > 0){

                if (display_hold > 0) {
                    ;
                }
                else {
                    if (game_step == G_BATTLE) {
                        if (enemy_at > value){
                            EffectBgmStart(10);
                            display_hold = TIME_PROG_VERY_SHORT;
                            GameButtleEnemyView();
                        }
                        else {
                            EventGameView();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
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

    /************************************************
        メイン画面へ戻る
     ************************************************/
    public void onBack(View v){

        String _title = "";
        String _message = "";

        if (GameTimer != null) {
            GameTimer.cancel();
        }
        GameTimer = null;

        enemy = null;
        prog = null;
        ename = null;
        story = null;
        gameimg = null;
        gamettl = null;
        gamesta = null;
        popdispcount = 0;

        SoundStop();
        setContentView(R.layout.activity_main);

        /* サウンド */
        SoundInit();
        BgmStart(1);

        if (game_step == G_BATTLE || game_step == G_BATTLE_ENEMY) {
            _title = getString(R.string.mess_12_1);
            _message = "\n\n\n"+
                        "　" + getString(R.string.mess_12_2)+
                        "\n"+
                        "　" + getString(R.string.mess_12_3)+
                        "\n\n"+
                        "　" + getString(R.string.mess_12_4)+
                        "\n"+
                        "\n\n\n";
            DialogDisplay(_title, _message, 0);
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
        if (ebgm == null){
            ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.s_11);
        }
    }

    public void SoundStop() {

        if (bgm != null){
            bgm.stop();     bgm.release();      bgm = null;
        }
        if (ebgm != null){
            ebgm.stop();    ebgm.release();     ebgm = null;
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
            case 5:
                if (bgm == null){
                    bgm = (MediaPlayer) MediaPlayer.create(this, R.raw.boss2);
                }
                break;
            case 6:
                if (bgm == null){
                    bgm = (MediaPlayer) MediaPlayer.create(this, R.raw.lastboss);
                }
                break;
        }

        if (bgm.isPlaying() == false) {
            bgm.setLooping(true);
            bgm.start();
        }

    }
    /***************************************************
        エフェクト音源処理
     ****************************************************/
    public void EffectBgmStart(int index){

        if (ebgm == null){
            return;
        }
        else{
            ebgm.stop();
            ebgm.release();
            ebgm = null;
        }
        ebgm_index = index;

        switch (index){
            default:
            case 1:
                if (ebgm == null){
                    ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.s_11);
                }
                break;
            case 2:
                if (ebgm == null){
                    ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.s_12);
                }
                break;
            case 3:
                if (ebgm == null){
                    ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.s_13);
                }
                break;
            case 4:
                if (ebgm == null){
                    ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.s_14);
                }
                break;
            case 5:
                if (ebgm == null){
                    ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.s_15);
                }
                break;
            case 6:
                if (ebgm == null){
                    ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.s_16);
                }
                break;
            case 10:
                if (ebgm == null){
                    ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.hit);
                }
                break;
            case 20:
                if (ebgm == null){
                    ebgm = (MediaPlayer) MediaPlayer.create(this, R.raw.levelup);
                }
                break;
        }

        if (ebgm.isPlaying() == false) {
            ebgm.start();
        }

    }

    /************************************************
        ゲームスタート
     ************************************************/
    public void onGameScreen(View v){

        last_map = false;
        /* ゲームスタート */
        GameStart();
    }

    public void DialogDisplay(String title, String message, int next_btn){
        AlertDialog.Builder guide = new AlertDialog.Builder(this);
        TextView vmessage = new TextView(this);

        vmessage.setText(message);
        vmessage.setBackgroundColor(getResources().getColor(R.color.gray));
        vmessage.setTextColor(getResources().getColor(R.color.white));
        vmessage.setTypeface(Typeface.DEFAULT_BOLD);
        guide.setTitle(title);
        guide.setCancelable(false);
        guide.setIcon(R.drawable.book2);
        guide.setView(vmessage);
        guide.setPositiveButton( getString(R.string.common_pop_2), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (next_btn){
                    default:
                    case 0:                 break;
                    case 1: guidePage2();   break;
                    case 2: bguidePage2();  break;
                    case 3: SetGameStep(G_ENDING); break;
                }
            }
        });
        guide.create();
        guide.show();
    }

    /************************************************
        ゲームスタート（ラスボス戦）
     ************************************************/
    public void onGameLastMap(View v){

        if (db_lastboss <= 0) {
            //メッセージ
            String tmptitle =  getString(R.string.common_pop_1);
            String tmpstr = "";
            tmpstr =    "\n\n\n"+
                        "　" + getString(R.string.mess_13_1)+
                        "\n"+
                        "　" + getString(R.string.mess_13_2)+
                        "\n\n"+
                        "　" + getString(R.string.mess_13_3)+
                        "\n"+
                        "\n\n\n";
            DialogDisplay(tmptitle,tmpstr,0);
        }
        else{
            last_map = true;
            /* ゲームスタート */
            GameStart();
        }
    }

    /************************************************
        買い物（攻撃）
     ************************************************/
    public void onShop(View v){
        /* エフェクト入手状況 */
        setContentView(R.layout.activity_list);
        EffectDisp();
    }
    public void EffectDisp(){
        /* 左列 */
        TextView l11 = (TextView) findViewById(R.id.text_list11);
        TextView l21 = (TextView) findViewById(R.id.text_list21);
        TextView l31 = (TextView) findViewById(R.id.text_list31);
        TextView l41 = (TextView) findViewById(R.id.text_list41);
        /* 右列 */
        TextView l12 = (TextView) findViewById(R.id.text_list12);
        TextView l22 = (TextView) findViewById(R.id.text_list22);
        TextView l32 = (TextView) findViewById(R.id.text_list32);
        TextView l42 = (TextView) findViewById(R.id.text_list42);

        TextView total = (TextView) findViewById(R.id.text_list_total);

        String get_after  = getString(R.string.shop_1);
        String get_before = getString(R.string.shop_2);
        String get_attack = getString(R.string.shop_3);
        String get_attack2 = getString(R.string.shop_4);
        String buf = "";
        boolean flag = true;
        int _color = 0;

        total.setText( "　" + getString(R.string.shop_5) + db_jpoint );

        flag = GetEffectLevel(1);
        if (flag)   buf = get_after  + get_attack + "　１";
        else        buf = get_before + get_attack + "　１";
        l11.setText(buf);
        if (flag)   l11.setTextColor(getResources().getColor(R.color.white));
        else        l11.setTextColor(getResources().getColor(R.color.gray));

        flag = GetEffectLevel(2);
        if (flag)   buf = get_after  + get_attack + "　２";
        else        buf = get_before + get_attack + "　２";
        l21.setText(buf);
        if (flag)   l21.setTextColor(getResources().getColor(R.color.white));
        else        l21.setTextColor(getResources().getColor(R.color.gray));

        flag = GetEffectLevel(3);
        if (flag)   buf = get_after  + get_attack2 + "　３";
        else        buf = get_before + get_attack2 + "　３";
        l31.setText(buf);
        if (flag)   l31.setTextColor(getResources().getColor(R.color.white));
        else        l31.setTextColor(getResources().getColor(R.color.gray));

        flag = GetEffectLevel(4);
        if (flag)   buf = get_after  + get_attack2 + "　４";
        else        buf = get_before + get_attack2 + "　４";
        l41.setText(buf);
        if (flag)   l41.setTextColor(getResources().getColor(R.color.white));
        else        l41.setTextColor(getResources().getColor(R.color.gray));

        flag = GetEffectLevel(5);
        if (flag)   buf = get_after  + get_attack2 + "　５";
        else        buf = get_before + get_attack2 + "　５";
        l12.setText(buf);
        if (flag)   l12.setTextColor(getResources().getColor(R.color.white));
        else        l12.setTextColor(getResources().getColor(R.color.gray));

        flag = GetEffectLevel(6);
        if (flag)   buf = get_after  + get_attack2 + "　６";
        else        buf = get_before + get_attack2 + "　６";
        l22.setText(buf);
        if (flag)   l22.setTextColor(getResources().getColor(R.color.white));
        else        l22.setTextColor(getResources().getColor(R.color.gray));

        flag = GetEffectLevel(7);
        if (flag)   buf = get_after  + get_attack2 + "　７";
        else        buf = get_before + get_attack2 + "　７";
        l32.setText(buf);
        if (flag)   l32.setTextColor(getResources().getColor(R.color.white));
        else        l32.setTextColor(getResources().getColor(R.color.gray));

        flag = GetEffectLevel(8);
        if (flag)   buf = get_after  + get_attack2 + "　８";
        else        buf = get_before + get_attack2 + "　８";
        l42.setText(buf);
        if (flag)   l42.setTextColor(getResources().getColor(R.color.white));
        else        l42.setTextColor(getResources().getColor(R.color.gray));

    }
    public void guidePage2() {
        String tmptitle = getString(R.string.shop_6);
        String tmpstr = "";
        tmpstr ="\n\n"+
                getString(R.string.shop_7_5) +
                "\n"+
                getString(R.string.shop_8_1) + EFFECT_5 +
                "\n"+
                getString(R.string.shop_8_2) + getString(R.string.shop_parts_2)  +
                "\n"+
                "\n"+
                getString(R.string.shop_7_6) +
                "\n"+
                getString(R.string.shop_8_1) + EFFECT_6 +
                "\n"+
                getString(R.string.shop_8_2) + getString(R.string.shop_parts_3)  +
                "\n"+
                "\n"+
                getString(R.string.shop_7_7) +
                "\n"+
                getString(R.string.shop_8_1) + "????" +
                "\n"+
                getString(R.string.shop_8_2) + getString(R.string.shop_parts_0) +
                "\n"+
                "\n"+
                getString(R.string.shop_7_8) +
                "\n"+
                getString(R.string.shop_8_1) + "????" +
                "\n"+
                getString(R.string.shop_8_2) + getString(R.string.shop_parts_0)  +
                "\n"+
                " \n\n";

        DialogDisplay(tmptitle,tmpstr,0);
    }
    public void guidePage1() {
        String tmptitle = getString(R.string.shop_6);
        String tmpstr = "";
        tmpstr ="\n\n"+
                getString(R.string.shop_7_1) +
                "\n"+
                getString(R.string.shop_8_1) + EFFECT_1 +
                "\n"+
                getString(R.string.shop_8_2)  + getString(R.string.shop_parts_0) +
                "\n"+
                "\n"+
                getString(R.string.shop_7_2) +
                "\n"+
                getString(R.string.shop_8_1) + EFFECT_2 +
                "\n"+
                getString(R.string.shop_8_2) + getString(R.string.shop_parts_0) +
                "\n"+
                "\n"+
                getString(R.string.shop_7_3) +
                "\n"+
                getString(R.string.shop_8_1)  + EFFECT_3 +
                "\n"+
                getString(R.string.shop_8_2)  + getString(R.string.shop_parts_0) +
                "\n"+
                "\n"+
                getString(R.string.shop_7_4) +
                "\n"+
                getString(R.string.shop_8_1) + EFFECT_4 +
                "\n"+
                getString(R.string.shop_8_2) + getString(R.string.shop_parts_1) +
                "\n"+
                " \n\n";

        DialogDisplay(tmptitle,tmpstr,1);
    }
    public void onGuide(View v) {
        guidePage1();
    }

    public void onBackList(View v) {
        setContentView(R.layout.activity_main);
        BgmStart(1);
    }

    public boolean GetEffectLevel(int type){
        boolean ret = false;

        switch (type){
            case 1: if (db_jpoint >= EFFECT_1)  ret = true;
                    else                        ret = false;
                    break;
            case 2: if (db_jpoint >= EFFECT_2)  ret = true;
                    else                        ret = false;
                    break;
            case 3: if (db_jpoint >= EFFECT_3)  ret = true;
                    else                        ret = false;
                    break;
            case 4: if (db_jpoint >= EFFECT_4 && db_boss_2 >= 1){
                        ret = true;
                    }
                    else{
                        ret = false;
                    }
                    break;
            case 5: if (db_jpoint >= EFFECT_5 && db_present_a >= 30) {
                        ret = true;
                     }
                    else{
                        ret = false;
                    }
                    break;
            case 6: if (db_jpoint >= EFFECT_6 && db_present_a >= 100) {
                        ret = true;
                    }
                    else{
                        ret = false;
                    }
                    break;
            case 7: if (db_jpoint >= EFFECT_7)  ret = true;
                    else                        ret = false;
                    break;
            case 8: if (db_jpoint >= EFFECT_8)  ret = true;
                    else                        ret = false;
                    break;
        }
        return ret;
    }


    /************************************************
        ボス一覧
     ************************************************/
    public void onBoss(View v){
        /* エフェクト入手状況 */
        setContentView(R.layout.activity_blist);
        BossListDisp();
    }
    public void BossListDisp(){
        /* 左列 */
        TextView l11 = (TextView) findViewById(R.id.text_listb11);
        TextView l21 = (TextView) findViewById(R.id.text_listb21);
        TextView l31 = (TextView) findViewById(R.id.text_listb31);
        TextView l41 = (TextView) findViewById(R.id.text_listb41);
        /* 右列 */
        TextView l12 = (TextView) findViewById(R.id.text_listb12);
        TextView l22 = (TextView) findViewById(R.id.text_listb22);
        TextView l32 = (TextView) findViewById(R.id.text_listb32);
        TextView l42 = (TextView) findViewById(R.id.text_listb42);

        TextView total = (TextView) findViewById(R.id.text_blist_total);

        String get_after  = "[討伐済] ";
        String get_before = "[　　　] ";
        String buf = "";
        boolean flag = true;
        int _color = 0;

        total.setText("");

        flag = GetBossBattle(1);
        if (flag)   buf = get_after  + B_NAME_1;
        else        buf = get_before + B_NAME_1;
        l11.setText(buf);
        if (flag)   l11.setTextColor(getResources().getColor(R.color.white));
        else        l11.setTextColor(getResources().getColor(R.color.gray));

        flag = GetBossBattle(2);
        if (flag)   buf = get_after  + B_NAME_2;
        else        buf = get_before + B_NAME_2;
        l21.setText(buf);
        if (flag)   l21.setTextColor(getResources().getColor(R.color.white));
        else        l21.setTextColor(getResources().getColor(R.color.gray));

        flag = GetBossBattle(3);
        if (flag)   buf = get_after  + B_NAME_3;
        else        buf = get_before + B_NAME_3;
        l31.setText(buf);
        if (flag)   l31.setTextColor(getResources().getColor(R.color.white));
        else        l31.setTextColor(getResources().getColor(R.color.gray));

        flag = GetBossBattle(4);
        if (flag)   buf = get_after  + B_NAME_4;
        else        buf = get_before + B_NAME_4;
        l41.setText(buf);
        if (flag)   l41.setTextColor(getResources().getColor(R.color.white));
        else        l41.setTextColor(getResources().getColor(R.color.gray));

        flag = GetBossBattle(5);
        if (flag)   buf = get_after  + B_NAME_5;
        else        buf = get_before + B_NAME_5;
        l12.setText(buf);
        if (flag)   l12.setTextColor(getResources().getColor(R.color.white));
        else        l12.setTextColor(getResources().getColor(R.color.gray));

        flag = GetBossBattle(6);
        if (flag)   buf = get_after  + B_NAME_6;
        else        buf = get_before + B_NAME_6;
        l22.setText(buf);
        if (flag)   l22.setTextColor(getResources().getColor(R.color.white));
        else        l22.setTextColor(getResources().getColor(R.color.gray));

        flag = GetBossBattle(7);
        if (flag)   buf = get_after  + B_NAME_7;
        else        buf = get_before + B_NAME_7;
        l32.setText(buf);
        if (flag)   l32.setTextColor(getResources().getColor(R.color.white));
        else        l32.setTextColor(getResources().getColor(R.color.gray));

        flag = GetBossBattle(8);
        if (flag)   buf = get_after  + B_NAME_8;
        else        buf = get_before + B_NAME_8;
        l42.setText(buf);
        if (flag)   l42.setTextColor(getResources().getColor(R.color.white));
        else        l42.setTextColor(getResources().getColor(R.color.gray));
    }
    public boolean GetBossBattle(int type){
        boolean ret = false;

        switch (type){
            case 1: if (db_boss_1 > 0)  ret = true;
            else                        ret = false;
                break;
            case 2: if (db_boss_2 > 0)  ret = true;
            else                        ret = false;
                break;
            case 3: if (db_boss_3 > 0)  ret = true;
            else                        ret = false;
                break;
            case 4: if (db_boss_4 > 0)  ret = true;
            else                        ret = false;
                break;
            case 5: if (db_boss_5 > 0)  ret = true;
            else                        ret = false;
                break;
            case 6: if (db_boss_6 > 0)  ret = true;
            else                        ret = false;
                break;
            case 7: if (db_boss_7 > 0)  ret = true;
            else                        ret = false;
                break;
            case 8: if (db_boss_8 > 0)  ret = true;
            else                        ret = false;
                break;
        }
        return ret;
    }
    public void bguidePage2() {
        String tmptitle = "　ＢＯＳＳ説明　";
        String tmpstr = "";
        tmpstr ="\n\n"+
                "　" + B_NAME_5 + "\n"+
                "　遭遇条件：ステージ ▶ ︎" + "城" + "\n"+
                "　遭遇条件：必要素材 ▶ ︎" + "なし" + "\n"+
                "\n"+
                "　" + B_NAME_6 + "\n"+
                "　遭遇条件：ステージ ▶ ︎" + "城" + "\n"+
                "　遭遇条件：必要素材 ▶ ︎" + "なし" + "\n"+
                "\n"+
                "　" + B_NAME_7 + "\n"+
                "　遭遇条件：ステージ ▶ ︎" + "不明" + "\n"+
                "　遭遇条件：必要素材 ▶ ︎" + "なし" + "\n"+
                "\n"+
                "　" + B_NAME_8 + "\n"+
                "　遭遇条件：ステージ ▶ ︎" + "不明" + "\n"+
                "　遭遇条件：必要素材 ▶ ︎" + "なし" + "\n"+
                " \n\n";

        DialogDisplay(tmptitle,tmpstr,0);
    }
    public void bguidePage1() {
        String tmptitle = "　ＢＯＳＳ説明　";
        String tmpstr = "";
        tmpstr ="\n\n"+
                "　" + B_NAME_1 + "\n"+
                "　遭遇条件：ステージ ▶ ︎" + "草原" + "\n"+
                "　遭遇条件：必要素材 ▶ ︎" + "なし" + "\n"+
                "\n"+
                "　" + B_NAME_2 + "\n"+
                "　遭遇条件：ステージ ▶ ︎" + "平原" + "\n"+
                "　遭遇条件：必要素材 ▶ ︎" + "なし" + "\n"+
                "\n"+
                "　" + B_NAME_3 + "\n"+
                "　遭遇条件：ステージ ▶ ︎" + "城" + "\n"+
                "　遭遇条件：必要素材 ▶ ︎" + "なし" + "\n"+
                "\n"+
                "　" + B_NAME_4 + "\n"+
                "　遭遇条件：ステージ ▶ ︎" + "城" + "\n"+
                "　遭遇条件：必要素材 ▶ ︎" + "なし" + "\n"+
                " \n\n";

        DialogDisplay(tmptitle,tmpstr,2);
    }

    public void onbGuide(View v) {
        bguidePage1();
    }
    public void onBackbList(View v) {
        setContentView(R.layout.activity_main);
        BgmStart(1);
    }


    /************************************************
         戦　歴
     ************************************************/
    public void onStatus(View v){
        GetHeroPara(0); GetHeroPara(1);
        setContentView(R.layout.activity_his);
        HistoryDisp();
    }
    public void onBackHis(View v) {
        setContentView(R.layout.activity_main);
        BgmStart(1);
    }
    public void HistoryDisp() {
        ImageView imgmap = (ImageView) findViewById(R.id.img_map);

        String tmp = "";
        TextView map = (TextView) findViewById(R.id.text_map_name);
        map.setText("〜　ストレスの草原　状態　〜");

        TextView now = (TextView) findViewById(R.id.text_nowprog);
        String mapsta = "";
        mapsta += "　浄化率："+ db_jrate + "％";
        if (db_jrate < 10 ) {
            mapsta += "\n　ストレスが充満している";
            imgmap.setImageResource(R.drawable.map00);
        }else if (db_jrate < 25){
            mapsta += "\n　少しストレスが減ってきた";
            imgmap.setImageResource(R.drawable.map25);
        }else if (db_jrate < 50){
            mapsta += "\n　ストレスが減ってきた";
            imgmap.setImageResource(R.drawable.map50);
        }else if (db_jrate < 75){
            mapsta += "\n　少しリラックスできる";
            imgmap.setImageResource(R.drawable.map75);
        }else{
            mapsta += "\n　楽しい気分になった";
            imgmap.setImageResource(R.drawable.map99);
        }
        now.setText(mapsta);
        now.setTextColor(getResources().getColor(R.color.teal_200));

        ProgressBar map_bar = (ProgressBar) findViewById(R.id.map_progress);
        map_bar.setMin(0);
        map_bar.setMax(100);
        map_bar.setProgress(db_jrate);

        TextView history = (TextView) findViewById(R.id.text_history);

        tmp += "\n　〜　ストーリー　〜 \n"
                + "　平原　浄化率　　：" + db_jrate + "％\n"
                + "　城　　浄化率　　：" + db_brate + "％\n"
                + "　リラックスの結晶：" + db_present_a + "個\n";


        tmp += "\n　〜　勇者ステータス　〜 \n"
                + "　Ｌｖ　　　　　　：" + db_level + "\n"
                + "　必殺発生確率　　：" + (db_critical) + "％\n"
                + "　通常ダメージ　　：" + db_damage + "\n"
                + "　必殺ダメージ　　：" + (db_damage*3) + "\n";

        tmp += "\n　〜　戦歴モンスター　〜 \n"
                + "　" + E_NAME_1 + "の討伐数：" + db_enemy_1 + " \n"
                + "　" + E_NAME_2 + "の討伐数：" + db_enemy_2 + " \n"
                + "　" + E_NAME_3 + "の討伐数：" + db_enemy_3 + " \n"
                + "　" + E_NAME_4 + "の討伐数：" + db_enemy_4 + " \n"
                + "　" + E_NAME_5 + "の討伐数：" + db_enemy_5 + " \n"
                + "　" + E_NAME_6 + "の討伐数：" + db_enemy_6 + " \n"
                + "　" + E_NAME_7 + "の討伐数：" + db_enemy_7 + " \n"
                + "　" + E_NAME_8 + "の討伐数：" + db_enemy_8 + " \n"
                + "　" + E_NAME_9 + "の討伐数：" + db_enemy_9 + " \n"
                + "　" + E_NAME_10 + "の討伐数：" + db_enemy_10 + " \n"
                + "\n"
                + "　〜　戦歴BOSS　〜 \n"
                + "　" + B_NAME_1 + "の討伐数：" + db_boss_1 + " \n"
                + "　" + B_NAME_2 + "の討伐数：" + db_boss_2 + " \n"
                + "　" + B_NAME_3 + "の討伐数：" + db_boss_3 + " \n"
                + "　" + B_NAME_4 + "の討伐数：" + db_boss_4 + " \n"
                + "　" + B_NAME_5 + "の討伐数：" + db_boss_5 + " \n"
                + "　" + B_NAME_6 + "の討伐数：" + db_boss_6 + " \n"
        ;
        history.setText(tmp);
    }
    /************************************************
         レベルアップ
     ************************************************/
    public void GetLevelUp(){
        String tmptitle = "　Level UP！！　";
        String tmpstr = "";
        int tmp_a = db_level;
        db_level = 50;
        int tmp_b = db_jpoint;
        db_jpoint = 150;
        tmpstr =    "\n\n\n"+
                "　勇者のLv（ " + tmp_a + " ▶︎ " + db_level + " ）が上がった\n"+
                "　浄化ポイント（ " + tmp_b + " ▶︎ " + db_jpoint + " ）を入手\n"+
                "  \n\n"+
                "　\n\n"+
                "\n\n\n";
        DialogDisplay(tmptitle,tmpstr,0);
        EffectBgmStart(20);
    }

    public void onLevel(View v){
        AlertDialog.Builder guide = new AlertDialog.Builder(this);
        TextView vmessage = new TextView(this);
        String tmptitle = "　覚醒チェック　";
        String tmpstr = "";

        if (db_level >= 50) {
            tmpstr =    "\n\n\n"+
                    "　覚醒する必要はありません\n"+
                    "　あなたはストレスに強くなっています\n\n"+
                    "　\n"+
                    "\n\n\n";
            DialogDisplay(tmptitle,tmpstr,0);
            return;
        }
        else {
            //メッセージ
            tmpstr =    "\n\n\n"+
                    "　勇者を覚醒して【 Lv50 】しますか？\n"+
                    "　加えて浄化ポイントを【 150 】にします\n\n"+
                    "　\n"+
                    "\n\n\n";

            vmessage.setText(tmpstr);
            vmessage.setBackgroundColor(getResources().getColor(R.color.gray));
            vmessage.setTextColor(getResources().getColor(R.color.white));
            vmessage.setTypeface(Typeface.DEFAULT_BOLD);
            guide.setTitle(tmptitle);
            guide.setCancelable(false);
            guide.setIcon(R.drawable.book2);
            guide.setView(vmessage);

            guide.setPositiveButton( getString(R.string.common_pop_3), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GetLevelUp();
                }
            });
            guide.setNegativeButton( getString(R.string.common_pop_4), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        guide.create();
        guide.show();
    }

    /************************************************
        宝箱
     ************************************************/
    public void GetPresent(){
        String tmptitle = "　宝GET！！　";
        String tmpstr = "";
        int tmp_present = db_present_a;
        db_present_a += 3;

        tmpstr =    "\n\n\n"+
                "　リフレッシュの結晶（ " + tmp_present + " ▶︎ " + db_present_a + " ）を入手\n"+
                "  \n\n"+
                "　\n\n"+
                "\n\n\n";
        DialogDisplay(tmptitle,tmpstr,0);
    }
    public void onPresent(View v){
        AlertDialog.Builder guide = new AlertDialog.Builder(this);
        TextView vmessage = new TextView(this);
        String tmptitle = "　宝箱チェック　";
        String tmpstr = "";
        int tmp = db_present_a;

        //メッセージ
        tmpstr =    "\n\n\n"+
                "　広告動画を視聴して宝箱を手に入れますか？\n\n"+
                "　【 リフレッシュの結晶 】が入手できます\n　（新たな攻撃エフェクトの入手に必要）\n"+
                "　\n"+
                "\n\n\n";
        vmessage.setText(tmpstr);
        vmessage.setBackgroundColor(getResources().getColor(R.color.gray));
        vmessage.setTextColor(getResources().getColor(R.color.white));
        vmessage.setTypeface(Typeface.DEFAULT_BOLD);
        guide.setTitle(tmptitle);
        guide.setCancelable(false);
        guide.setIcon(R.drawable.treasure);
        guide.setView(vmessage);
        guide.setPositiveButton(getString(R.string.common_pop_5), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO リワード処理
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
                return;
            }
        });
        guide.setNegativeButton(getString(R.string.common_pop_4), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        guide.create();
        guide.show();
    }


    /**
     * 勇者のパラメータ取得　攻撃力、必殺確率
     * @param type
     * @return
     */
    public int GetHeroPara(int type) {
        int tmp_damage = db_damage;
        int tmp_critical = db_critical;

        if (type == 0){
            tmp_damage = 5 + (db_level / 10);
            db_damage = tmp_damage;
            return tmp_damage;
        }
        else{
            tmp_critical = (db_level);
            db_critical = tmp_critical;
            if (db_critical > 99){
                db_critical = 99;
            }
            return tmp_critical;
        }
    }

    /***************************************************
     　↓↓↓ 以下、モンスター表示関連　↓↓↓
     ***************************************************/

    /**
     モンスターの遭遇
     **/
    public void SetEnemyType() {
        int ttype = rand.nextInt(100);  //通常orボスの確率

        if (last_map == false){
            int btype = rand.nextInt(3);    //ボスの検索
            int etype = rand.nextInt(8);    //通常モンスターの検索

            // 通常モンスター　約７割
            if (ttype < 70){
                BgmStart(2);

                switch (etype){
                    default:
                    case 1: enemy_type = E_TYPE_1;  enemy_hp = 100; break;
                    case 2: enemy_type = E_TYPE_2;  enemy_hp = 120; break;
                    case 3: enemy_type = E_TYPE_3;  enemy_hp = 150; break;
                    case 4: enemy_type = E_TYPE_4;  enemy_hp = 200; break;
                    case 5: enemy_type = E_TYPE_5;  enemy_hp = 250; break;
                    case 6: enemy_type = E_TYPE_6;  enemy_hp = 300; break;
                    case 7: enemy_type = E_TYPE_7;  enemy_hp = 300; break;
                }
            }
            // ボス　約３割
            else{
                BgmStart(3);

                switch (btype){
                    default:
                    case 1: enemy_type = B_TYPE_1;  enemy_hp = 500; break;
                    case 2: enemy_type = B_TYPE_2;  enemy_hp = 600; break;
                }
            }
        }
        else{
            int btype;
            if (db_brate < 70) {
                btype = rand.nextInt(4);    //ボスの検索
                BgmStart(5);
                switch (btype){
                    default:
                    case 1: enemy_type = B_TYPE_3;  enemy_hp = 750; break;
                    case 2: enemy_type = B_TYPE_4;  enemy_hp = 750; break;
                    case 3: enemy_type = B_TYPE_5;  enemy_hp = 850; break;
                }
            }
            else{
                btype = rand.nextInt(5);    //ボスの検索

                switch (btype){
                    default:
                    case 1: enemy_type = B_TYPE_3;  enemy_hp = 750; break;
                    case 2: enemy_type = B_TYPE_4;  enemy_hp = 750; break;
                    case 3: enemy_type = B_TYPE_5;  enemy_hp = 850; break;
                    case 4: enemy_type = B_TYPE_6;  enemy_hp = 3300;break;
                }
                if (enemy_type == B_TYPE_6){
                    BgmStart(6);
                }
                else{
                    BgmStart(5);
                }
            }
        }
    }

    /**
        モンスターの討伐後の経験値
     **/
    public int GetEnemyPoint(int pointype){
        int point = 0;
        int level = 0;
        int rate = 0;
        int minus_point = 0;
        int refpoint = 0;
        int refrand = rand.nextInt(100);

        if (enemy_type >= B_TYPE_1){
            if (refrand > 65){  //35%　リフレッシュの結晶の入手確率
                refpoint = 2;
            }
        }

        switch (enemy_type) {
            case E_TYPE_1:
            case E_TYPE_2:
            case E_TYPE_3:
            case E_TYPE_4:
            case E_TYPE_5:
                point = 10;
                level = 1;
                rate = 1;
                minus_point = 1;
                break;
            case E_TYPE_6:
            case E_TYPE_7:
            case E_TYPE_8:
            case E_TYPE_9:
            case E_TYPE_10:
                point = 20;
                level = 2;
                rate = 2;
                minus_point = 3;
                break;
            case B_TYPE_1:
                point = 50;
                level = 3;
                rate = 3;
                minus_point = 5;
                break;
            case B_TYPE_2:
                point = 60;
                level = 4;
                rate = 4;
                minus_point = 5;
                break;
            case B_TYPE_3:
                point = 100;
                level = 5;
                rate = 5;
                minus_point = 25;
                break;
            case B_TYPE_4:
                point = 115;
                level = 5;
                rate = 8;
                minus_point = 25;
                break;
            case B_TYPE_5:
                point = 130;
                level = 5;
                rate = 10;
                minus_point = 30;
                break;
            case B_TYPE_6:
            case B_TYPE_7:
            case B_TYPE_8:
            case B_TYPE_9:
            case B_TYPE_10:
                point = 220;
                level = 5;
                rate = 5;
                minus_point = 30;
                break;
        }
        switch (pointype)
        {
            case 0: return point;           //浄化ポイント
            case 1: return level;           //レベルアップ分
            case 2: return rate;            //浄化率
            case 3: return minus_point;     //被浄化ポイント
            case 4: return refpoint;
        }
        return 0;
    }
    /**
        モンスターの討伐数の更新
     **/
    public void SetEnemyKillPoint(){
        switch (enemy_type) {
            case E_TYPE_1:      db_enemy_1++;   break;
            case E_TYPE_2:      db_enemy_2++;   break;
            case E_TYPE_3:      db_enemy_3++;   break;
            case E_TYPE_4:      db_enemy_4++;   break;
            case E_TYPE_5:      db_enemy_5++;   break;
            case E_TYPE_6:      db_enemy_6++;   break;
            case E_TYPE_7:      db_enemy_7++;   break;
            case E_TYPE_8:      db_enemy_8++;   break;
            case E_TYPE_9:      db_enemy_9++;   break;
            case E_TYPE_10:     db_enemy_10++;  break;

            case B_TYPE_1:      db_boss_1++;    break;
            case B_TYPE_2:      db_boss_2++;    break;
            case B_TYPE_3:      db_boss_3++;    break;
            case B_TYPE_4:      db_boss_4++;    break;
            case B_TYPE_5:      db_boss_5++;    break;
            case B_TYPE_6:      db_boss_6++;    break;
            case B_TYPE_7:      db_boss_7++;    break;
            case B_TYPE_8:      db_boss_8++;    break;
            case B_TYPE_9:      db_boss_9++;    break;
            case B_TYPE_10:     db_boss_10++;   break;

            default: break;
        }
    }

    /**
        モンスターの名前取得
    **/
    public String GetEnemyName(boolean space){
        String tmp = "";
        if (space)  tmp += "　　　　敵：";

        switch (enemy_type){
            case E_TYPE_1:
                tmp +=  "オーガ";          break;
            case E_TYPE_2:
                tmp +=  "ミイラ";          break;
            case E_TYPE_3:
                tmp +=  "バッファロー";     break;
            case E_TYPE_4:
                tmp +=  "さまよう剣士";     break;
            case E_TYPE_5:
                tmp +=  "シャドー";        break;
            case E_TYPE_6:
                tmp +=  "ゴーレム";        break;
            case E_TYPE_7:
                tmp +=  "トロル";          break;
            case B_TYPE_1:
                tmp +=  "BOSS:土ドラゴン";  break;
            case B_TYPE_2:
                tmp +=  "BOSS:闇ドラゴン";  break;
            case B_TYPE_3:
                tmp +=  "BOSS:風ドラゴン";  break;
            case B_TYPE_4:
                tmp +=  "BOSS:水ドラゴン";  break;
            case B_TYPE_5:
                tmp +=  "BOSS:火ドラゴン";  break;
            case B_TYPE_6:
                tmp +=  "ラスボス:ストレス"; break;

        }
        return tmp;
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

        enemynamestr = GetEnemyName(true);
        // 以下モンスターごとに表示
        /**
         * 通常モンスターの表示
         **/
        if (enemy_type == E_TYPE_1) {
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
                case 5:
                    enemy.setImageResource(R.drawable.e_15);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.e_16);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;

            }
        }
        else if (enemy_type == E_TYPE_2) {
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
                case 5:
                    enemy.setImageResource(R.drawable.e_25);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.e_26);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        else if (enemy_type == E_TYPE_3) {
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.e_30);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.e_31);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.e_32);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.e_33);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.e_34);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.e_35);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.e_36);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        else if (enemy_type == E_TYPE_4) {
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.e_40);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.e_41);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.e_42);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.e_43);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.e_44);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.e_45);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.e_46);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        else if (enemy_type == E_TYPE_5) {
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.e_50);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.e_51);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.e_52);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.e_53);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.e_54);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.e_55);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.e_56);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        else if (enemy_type == E_TYPE_6) {
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.e_60);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.e_61);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.e_62);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.e_63);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.e_64);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.e_65);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.e_66);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        else if (enemy_type == E_TYPE_7) {
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.e_70);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.e_71);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.e_72);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.e_73);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.e_74);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.e_75);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.e_76);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        /**
         * ボスの表示
        **/
        else if(enemy_type == B_TYPE_1){
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
                case 5:
                    enemy.setImageResource(R.drawable.b_15);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.b_16);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        else if(enemy_type == B_TYPE_2){
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
                case 5:
                    enemy.setImageResource(R.drawable.b_25);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.b_26);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }

        else if(enemy_type == B_TYPE_3){
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.b_30);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.b_31);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.b_32);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.b_33);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.b_34);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.b_35);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.b_36);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        else if(enemy_type == B_TYPE_4){
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.b_40);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.b_41);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.b_42);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.b_43);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.b_44);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.b_45);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.b_46);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        else if(enemy_type == B_TYPE_5){
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.b_50);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.b_51);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.b_52);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.b_53);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.b_54);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.b_55);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.b_56);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
        /* ラスボス　ストレス戦 */
        else if(enemy_type == B_TYPE_6){
            switch (at_type) {
                case 0:
                    enemy.setImageResource(R.drawable.b_60);
                    enemy.setBackgroundResource(R.drawable.bak_11);
                    break;
                case 1:
                    enemy.setImageResource(R.drawable.b_61);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 2:
                    enemy.setImageResource(R.drawable.b_62);
                    enemy.setBackgroundResource(R.drawable.bak_12);
                    break;
                case 3:
                    enemy.setImageResource(R.drawable.b_63);
                    enemy.setBackgroundResource(R.drawable.bak_13);
                    break;
                case 4:
                    enemy.setImageResource(R.drawable.b_64);
                    enemy.setBackgroundResource(R.drawable.bak_14);
                    break;
                case 5:
                    enemy.setImageResource(R.drawable.b_65);
                    enemy.setBackgroundResource(R.drawable.bak_15);
                    break;
                case 6:
                    enemy.setImageResource(R.drawable.b_66);
                    enemy.setBackgroundResource(R.drawable.bak_16);
                    break;
            }
        }
    }

    /****************************************************
        モンスター表示
     ****************************************************/
    /*
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
    */

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
        sql.append(" ,damage");
        sql.append(" ,critical");
        sql.append(" ,status_1");
        sql.append(" ,status_2");
        sql.append(" ,status_3");
        sql.append(" ,status_4");
        sql.append(" ,status_5");
        sql.append(" ,map");
        sql.append(" ,jrate");
        sql.append(" ,jpoint");
        sql.append(" ,sysm_1");
        sql.append(" ,sysm_2");
        sql.append(" ,sysm_3");
        sql.append(" ,sysm_4");
        sql.append(" ,sysm_5");
        sql.append(" ,enemy_1");
        sql.append(" ,enemy_2");
        sql.append(" ,enemy_3");
        sql.append(" ,enemy_4");
        sql.append(" ,enemy_5");
        sql.append(" ,enemy_6");
        sql.append(" ,enemy_7");
        sql.append(" ,enemy_8");
        sql.append(" ,enemy_9");
        sql.append(" ,enemy_10");
        sql.append(" ,enemy_11");
        sql.append(" ,enemy_12");
        sql.append(" ,enemy_13");
        sql.append(" ,enemy_14");
        sql.append(" ,enemy_15");
        sql.append(" ,boss_1");
        sql.append(" ,boss_2");
        sql.append(" ,boss_3");
        sql.append(" ,boss_4");
        sql.append(" ,boss_5");
        sql.append(" ,boss_6");
        sql.append(" ,boss_7");
        sql.append(" ,boss_8");
        sql.append(" ,boss_9");
        sql.append(" ,boss_10");
        sql.append(" ,lastboss");
        sql.append(" ,brate");
        sql.append(" ,bpoint");
        sql.append(" ,present_a");
        sql.append(" ,present_b");
        sql.append(" FROM appinfo2;");
        try {
            Cursor cursor = db.rawQuery(sql.toString(), null);
            //TextViewに表示
            StringBuilder text = new StringBuilder();
            if (cursor.moveToNext()) {
                db_isopen = cursor.getInt(0);
                db_level = cursor.getInt(1);
                db_damage = cursor.getInt(2);
                db_critical = cursor.getInt(3);
                db_status_1 = cursor.getInt(4);
                db_status_2 = cursor.getInt(5);
                db_status_3 = cursor.getInt(6);
                db_status_4 = cursor.getInt(7);
                db_status_5 = cursor.getInt(8);
                db_map = cursor.getInt(9);
                db_jrate = cursor.getInt(10);
                db_jpoint = cursor.getInt(11);
                db_sysm_1 = cursor.getInt(12);
                db_sysm_2 = cursor.getInt(13);
                db_sysm_3 = cursor.getInt(14);
                db_sysm_4 = cursor.getInt(15);
                db_sysm_5 = cursor.getInt(16);
                db_enemy_1 = cursor.getInt(17);
                db_enemy_2 = cursor.getInt(18);
                db_enemy_3 = cursor.getInt(19);
                db_enemy_4 = cursor.getInt(20);
                db_enemy_5 = cursor.getInt(21);
                db_enemy_6 = cursor.getInt(22);
                db_enemy_7 = cursor.getInt(23);
                db_enemy_8 = cursor.getInt(24);
                db_enemy_9 = cursor.getInt(25);
                db_enemy_10 = cursor.getInt(26);
                db_enemy_11 = cursor.getInt(27);
                db_enemy_12 = cursor.getInt(28);
                db_enemy_13 = cursor.getInt(29);
                db_enemy_14 = cursor.getInt(30);
                db_enemy_15 = cursor.getInt(31);
                db_boss_1 = cursor.getInt(32);
                db_boss_2 = cursor.getInt(33);
                db_boss_3 = cursor.getInt(34);
                db_boss_4 = cursor.getInt(35);
                db_boss_5 = cursor.getInt(36);
                db_boss_6 = cursor.getInt(37);
                db_boss_7 = cursor.getInt(38);
                db_boss_8 = cursor.getInt(39);
                db_boss_9 = cursor.getInt(40);
                db_boss_10 = cursor.getInt(41);
                db_lastboss = cursor.getInt(42);
                db_brate = cursor.getInt(43);
                db_bpoint = cursor.getInt(44);
                db_present_a = cursor.getInt(45);
                db_present_b = cursor.getInt(46);
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
            insertValues.put("damage", 5);
            insertValues.put("critical", 5);
            insertValues.put("status_1", 0);
            insertValues.put("status_2", 0);
            insertValues.put("status_3", 0);
            insertValues.put("status_4", 0);
            insertValues.put("status_5", 0);
            insertValues.put("map", 0);
            insertValues.put("jrate", 0);
            insertValues.put("jpoint", 0);
            insertValues.put("sysm_1", 0);
            insertValues.put("sysm_2", 0);
            insertValues.put("sysm_3", 0);
            insertValues.put("sysm_4", 0);
            insertValues.put("sysm_5", 0);
            insertValues.put("enemy_1", 0);
            insertValues.put("enemy_2", 0);
            insertValues.put("enemy_3", 0);
            insertValues.put("enemy_4", 0);
            insertValues.put("enemy_5", 0);
            insertValues.put("enemy_6", 0);
            insertValues.put("enemy_7", 0);
            insertValues.put("enemy_8", 0);
            insertValues.put("enemy_9", 0);
            insertValues.put("enemy_10", 0);
            insertValues.put("enemy_11", 0);
            insertValues.put("enemy_12", 0);
            insertValues.put("enemy_13", 0);
            insertValues.put("enemy_14", 0);
            insertValues.put("enemy_15", 0);
            insertValues.put("boss_1", 0);
            insertValues.put("boss_2", 0);
            insertValues.put("boss_3", 0);
            insertValues.put("boss_4", 0);
            insertValues.put("boss_5", 0);
            insertValues.put("boss_6", 0);
            insertValues.put("boss_7", 0);
            insertValues.put("boss_8", 0);
            insertValues.put("boss_9", 0);
            insertValues.put("boss_10", 0);
            insertValues.put("lastboss", 0);
            insertValues.put("brate", 0);
            insertValues.put("bpoint", 0);
            insertValues.put("present_a", 0);
            insertValues.put("present_b", 0);
            try {
                ret = db.insert("appinfo2", null, insertValues);
            } finally {
                db.close();
            }
            db_isopen = 1;
            db_level = 1;
            db_damage = 5;
            db_critical = 5;
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
        insertValues.put("damage", db_damage);
        insertValues.put("critical", db_critical);
        insertValues.put("status_1", db_status_1);
        insertValues.put("status_2", db_status_2);
        insertValues.put("status_3", db_status_3);
        insertValues.put("status_4", db_status_4);
        insertValues.put("status_5", db_status_5);
        insertValues.put("map", db_map);
        insertValues.put("jrate", db_jrate);
        insertValues.put("jpoint", db_jpoint);
        insertValues.put("sysm_1", db_sysm_1);
        insertValues.put("sysm_2", db_sysm_2);
        insertValues.put("sysm_3", db_sysm_3);
        insertValues.put("sysm_4", db_sysm_4);
        insertValues.put("sysm_5", db_sysm_5);
        insertValues.put("enemy_1", db_enemy_1);
        insertValues.put("enemy_2", db_enemy_2);
        insertValues.put("enemy_3", db_enemy_3);
        insertValues.put("enemy_4", db_enemy_4);
        insertValues.put("enemy_5", db_enemy_5);
        insertValues.put("enemy_6", db_enemy_6);
        insertValues.put("enemy_7", db_enemy_7);
        insertValues.put("enemy_8", db_enemy_8);
        insertValues.put("enemy_9", db_enemy_9);
        insertValues.put("enemy_10", db_enemy_10);
        insertValues.put("enemy_11", db_enemy_11);
        insertValues.put("enemy_12", db_enemy_12);
        insertValues.put("enemy_13", db_enemy_13);
        insertValues.put("enemy_14", db_enemy_14);
        insertValues.put("enemy_15", db_enemy_15);
        insertValues.put("boss_1", db_boss_1);
        insertValues.put("boss_2", db_boss_2);
        insertValues.put("boss_3", db_boss_3);
        insertValues.put("boss_4", db_boss_4);
        insertValues.put("boss_5", db_boss_5);
        insertValues.put("boss_6", db_boss_6);
        insertValues.put("boss_7", db_boss_7);
        insertValues.put("boss_8", db_boss_8);
        insertValues.put("boss_9", db_boss_9);
        insertValues.put("boss_10", db_boss_10);
        insertValues.put("lastboss", db_lastboss);
        insertValues.put("brate", db_brate);
        insertValues.put("bpoint", db_bpoint);
        insertValues.put("present_a", db_present_a);
        insertValues.put("present_b", db_present_b);

        int ret;
        try {
            ret = db.update("appinfo2", insertValues, null, null);
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