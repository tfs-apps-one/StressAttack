package tfsapps.stressattack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import java.util.Random;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private ImageView enemy = null;

    public Paint paint;
    public Canvas canvas;
    private static final long FPS = 60;
    private final Random rand = new Random(System.currentTimeMillis());

    public Game(Context context){
        super(context);
        getHolder().addCallback(this);
        paint = new Paint();
    }

    protected void drawObject(Canvas canvas) {

        /* ゲーム画面 */
        enemy = (ImageView) findViewById(R.id.img_enemy);
        enemy.setImageResource(R.drawable.enemy10);
    }

    /********************************************************************************
     ゲーム全体の描画間隔
     *********************************************************************************/
    private class DrawThread extends Thread {
        private boolean isFinished;
        @Override
        public void run() {
            super.run();
            SurfaceHolder holder = getHolder();
            while (!isFinished) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawObject(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }

                try {
                    sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private DrawThread drawThread;

    public interface Callback {
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void startDrawThread() {
        stopDrawThread();
        drawThread = new DrawThread();
        drawThread.start();
    }

    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }
        drawThread.isFinished = true;
        drawThread = null;
        return true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        startDrawThread();  //描画スレッド起動
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    /* タッチイベント（タップ処理）自機移動 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
        }
        return super.onTouchEvent(event);
    }
}
