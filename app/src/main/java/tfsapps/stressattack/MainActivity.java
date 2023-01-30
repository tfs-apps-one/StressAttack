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
import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

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
    final int EFFECT_5 = 100;
    final int EFFECT_6 = 100;
    final int EFFECT_7 = 50000;
    final int EFFECT_8 = 99999;

    private int popdispcount = 0;
    private boolean last_map = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    /****************************************************
         ゲーム進行　文言取得
     ****************************************************/
    public String GetStoryString(){
        String tmpstr = "";

        if (last_map == false){
            tmpstr =    "\n\n\n" +
                        "　ストレスが世界を覆っている・・\n" +
                        "　勇者（あなた）は勇敢に立ち上がった・・\n\n\n" +
                        "　さぁストレスを浄化する旅へ！！\n" +
                        "　ストレスの平原を浄化[100%]しよう♪" +
                        "　\n\n\n";
        }
        else{
            tmpstr =    "\n\n\n" +
                        "　ストレスの源になっている城へ・・\n" +
                        "　BOSSをすべて倒して世界を解放へ・・\n\n\n" +
                        "　強敵が待っている準備はできているか？\n" +
                        "　ストレスの城を浄化[100%]しよう♪" +
                        "　\n\n\n";
        }

        return tmpstr;
    }

    /****************************************************
         ゲーム進行　ポップアップ表示
     ****************************************************/
    public void GameStoryPopup() {
        AlertDialog.Builder guide = new AlertDialog.Builder(this);
        TextView vmessage = new TextView(this);
        if (popdispcount > 1){
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

        guide.setTitle(" 物語チェック ");
        guide.setIcon(R.drawable.book2);
        guide.setView(vmessage);
        guide.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                popdispcount++;
                GameStoryPopup();
            }
        });
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
        gamestr =   "　勇者はストレスのない週末を\n" +
                    "　しばらくの間・・・過ごすことにした";
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
        db_level += GetEnemyPoint(1);
        //敵イメージ
        EnemyDisp(99);
        //ダメージバー
        prog.setProgress(0);
        //敵名前
        enemynamestr = "";
        ename.setText(enemynamestr);
        //ストリー
        gamestr =   "　勇者のストレスが解消された\n" +
                    "　ストレスの森が少し浄化した\n\n" +
                    "　〜戦闘結果〜\n" +
                    "　勇者のＬｖが " + tmp_level + " ▶︎ " + db_level + " 上がった\n" +
                    "　" + GetEnemyName(false) + "を倒した\n" +
                    "　" + GetEnemyName(false) + "の欠片を手に入れた\n"+
                    "　浄化ポイント "+GetEnemyPoint(0)+" を手に入れた！！\n";
        story.setText(gamestr);
        story.setTextColor(getResources().getColor(R.color.white));
        display_hold = TIME_PROG_HI_LONG;

        db_jrate += GetEnemyPoint(2);
        db_jpoint += GetEnemyPoint(0);
        SetEnemyKillPoint();

        GameNextStep();
    }
    /****************************************************
     ゲーム表示（バトル中）敵ターン
     ****************************************************/
    public void GameButtleEnemyView(){

        int tmp_point = db_jpoint;
        db_jpoint = (db_jpoint - GetEnemyPoint(3));
        if (db_jpoint <= 0){
            db_jpoint = 0;
        }
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
                    "　" + GetEnemyName(false) + "の攻撃！！\n" +
                    "　勇者のストレスが増えて、敵が活性した。\n\n" +
                    "　浄化ポイントが " + tmp_point + " ▶︎ " + db_jpoint + " に減った";
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
        //ストリー
        gamestr =   "　勇者がストレスの草原を歩いていると・・・\n" +
                    "　なんと・・・！？\n\n" + "　ストレス（" + GetEnemyName(false) + "）が現れた！！\n"+
                    "　【タップ】してストレスを倒せ！！";
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

        TextView summap = (TextView) findViewById(R.id.text_submap_name);
        ImageView submap = (ImageView) findViewById(R.id.img_submap);

        /* タイトル部の表示 */
        if (last_map == false) {
            summap.setText("草原");

            if (db_jrate < 10) submap.setImageResource(R.drawable.map00);
            else if (db_jrate < 25) submap.setImageResource(R.drawable.map25);
            else if (db_jrate < 50) submap.setImageResource(R.drawable.map50);
            else if (db_jrate < 75) submap.setImageResource(R.drawable.map75);
            else submap.setImageResource(R.drawable.map99);
        }
        else{
            summap.setText("城");
            submap.setImageResource(R.drawable.map_last);
        }


        TextView mystatus = (TextView) findViewById(R.id.text_mystatus);
        String buf = "";
        buf += "　勇者のＬｖ："+db_level;
        buf += "\n　浄化率　　："+db_jrate + "％";
        mystatus.setText(buf);

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
                break;
            case 2:
                at_str = "打撃で ";
                damege = db_damage;
                display_hold = TIME_EFFECT_SHORT;
                break;
            case 3:
                at_str = "鋭い雷撃で ";
                damege = (db_damage * 3);
                display_hold = TIME_EFFECT;
                break;
            case 4:
                at_str = "大爆発で ";
                damege = (db_damage * 3);
                display_hold = TIME_EFFECT;
                break;
            case 5:
                at_str = "一閃で ";
                damege = (db_damage * 5);
                display_hold = TIME_EFFECT;
                break;
            case 6:
                at_str = "空間裂き ";
                damege = (db_damage * 6);
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
            gamestr += bak1_gamestr+"\n";
            gamestr += bak2_gamestr;
            gamestr += bak3_gamestr;
            gamestr += bak4_gamestr;
            gamestr += bak5_gamestr;
        }
    }

    /* タッチイベント（タップ処理）自機移動 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int enemy_at = rand.nextInt(100);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                if (enemy_hp == 0 || display_hold > 0){

                if (display_hold > 0) {
                    ;
                }
                else {
                    if (game_step == G_BATTLE) {
                        if (enemy_at > 95){
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
    /************************************************
        ゲームスタート（ラスボス戦）
     ************************************************/
    public void onGameLastMap(View v){

        AlertDialog.Builder guide = new AlertDialog.Builder(this);
        TextView vmessage = new TextView(this);


        if (false) {
//        if (db_map == 0) {
            //メッセージ
            String tmpstr = "";
            tmpstr =    "\n\n\n"+
                        "　ラスボスへの挑戦が出来ません\n"+
                        "　ストレスの平原の浄化率をあげて下さい\n\n"+
                        "　浄化率[100%]にする必要があります\n"+
                        "\n\n\n";
            vmessage.setText(tmpstr);
            vmessage.setBackgroundColor(getResources().getColor(R.color.gray));
            vmessage.setTextColor(getResources().getColor(R.color.white));
            vmessage.setTypeface(Typeface.DEFAULT_BOLD);
            guide.setTitle(" 物語チェック ");
            guide.setIcon(R.drawable.book2);
            guide.setView(vmessage);
            guide.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ;
                }
            });
            guide.create();
            guide.show();
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

        String get_after  = "[済] 浄化ポイント:";
        String get_before = "[　] 浄化ポイント:";
        String buf = "";
        boolean flag = true;
        int _color = 0;

        total.setText("累計浄化ポイント:"+db_jpoint);

        flag = GetEffectLevel(1);
        if (flag)   buf = get_after  + EFFECT_1;
        else        buf = get_before + EFFECT_1;
        l11.setText(buf);

        flag = GetEffectLevel(2);
        if (flag)   buf = get_after  + EFFECT_2;
        else        buf = get_before + EFFECT_2;
        l21.setText(buf);

        flag = GetEffectLevel(3);
        if (flag)   buf = get_after  + EFFECT_3;
        else        buf = get_before + EFFECT_3;
        l31.setText(buf);

        flag = GetEffectLevel(4);
        if (flag)   buf = get_after  + EFFECT_4;
        else        buf = get_before + EFFECT_4;
        l41.setText(buf);

        flag = GetEffectLevel(5);
        if (flag)   buf = get_after  + EFFECT_5;
        else        buf = get_before + EFFECT_5;
        l12.setText(buf);

        flag = GetEffectLevel(6);
        if (flag)   buf = get_after  + EFFECT_6;
        else        buf = get_before + EFFECT_6;
        l22.setText(buf);

        flag = GetEffectLevel(7);
        if (flag)   buf = get_after  + EFFECT_7;
        else        buf = get_before + EFFECT_7;
        l32.setText(buf);

        flag = GetEffectLevel(8);
        if (flag)   buf = get_after  + EFFECT_8;
        else        buf = get_before + EFFECT_8;
        l42.setText(buf);

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
            case 4: if (db_jpoint >= EFFECT_4)  ret = true;
                    else                        ret = false;
                    break;
            case 5: if (db_jpoint >= EFFECT_5)  ret = true;
                    else                        ret = false;
                    break;
            case 6: if (db_jpoint >= EFFECT_6)  ret = true;
                    else                        ret = false;
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

        tmp += "\n　〜　勇者ステータス　〜 \n"
                + "　Ｌｖ　　　　：" + db_level + "\n"
                + "　必殺発生確率：" + (db_critical) + "％\n"
                + "　通常ダメージ：" + db_damage + "\n"
                + "　必殺ダメージ：" + (db_damage*3) + "\n";

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
        ;
        history.setText(tmp);
    }


    /************************************************
         メイン画面へ戻る
     ************************************************/
    public void onBack(View v){
        GameTimer.cancel(); GameTimer = null;
        enemy = null;
        prog = null;
        ename = null;
        story = null;
        popdispcount = 0;

        SoundStop();
        setContentView(R.layout.activity_main);

        /* サウンド */
        SoundInit();
        BgmStart(1);
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
                    case 2: enemy_type = E_TYPE_2;  enemy_hp = 150; break;
                    case 3: enemy_type = E_TYPE_3;  enemy_hp = 200; break;
                    case 4: enemy_type = E_TYPE_4;  enemy_hp = 250; break;
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
            if (db_jrate < 110) {
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
                BgmStart(6);
                enemy_type = B_TYPE_6;  enemy_hp = 3000;
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
                level = 1;
                rate = 1;
                minus_point = 3;
                break;
            case B_TYPE_1:
            case B_TYPE_2:
                point = 50;
                level = 3;
                rate = 3;
                minus_point = 10;
                break;
            case B_TYPE_3:
            case B_TYPE_4:
            case B_TYPE_5:
                point = 100;
                level = 5;
                rate = 5;
                minus_point = 25;
                break;
            case B_TYPE_6:
            case B_TYPE_7:
            case B_TYPE_8:
            case B_TYPE_9:
            case B_TYPE_10:
                point = 110;
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
                tmp +=  "BOSS:ストレス";    break;

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
        sql.append(" FROM appinfo;");
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
            try {
                ret = db.insert("appinfo", null, insertValues);
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