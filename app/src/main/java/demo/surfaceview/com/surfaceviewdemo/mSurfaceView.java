package demo.surfaceview.com.surfaceviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    //用于标注线程是否继续
    private boolean Flag=true;

    //SurfaceHolder
    SurfaceHolder surfaceHolder;

    //定义画笔
    Paint paint=new Paint();

    //雨滴的集合
    public List<Line> lines=new ArrayList<>();

    //Random对象 用于随机生成雨滴的X轴坐标
    Random random=new Random();

    public mSurfaceView(Context context) {
        super(context);
    }

    public mSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);

        //设置背景透明
        this.setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    public mSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //初始化画笔等
        init();

        Flag=true;

        //启动线程绘制雨滴
        new Thread(this).start();
    }

    private void init() {
        //设置画笔颜色
        paint.setColor(Color.WHITE);
        //抗锯齿
        paint.setAntiAlias(true);
        //设置画笔颜色
        paint.setColor(Color.WHITE);
        //设置画笔模式为填充
        paint.setStyle(Paint.Style.FILL);
        //设置画笔宽度为2
        paint.setStrokeWidth(2f);
    }

    //添加雨滴
    private void addline() {
            Line line=new Line();
            //随机生成雨滴的起始X坐标
            line.startx=random.nextInt(getWidth());
            //设置雨滴的起始y坐标为-60  从屏幕外开始运动
            line.starty=-60;
            //雨滴偏移3个像素 看起来不会太直
            line.stopx=line.getStartx()+3;
            //雨滴的长度
            line.stopy=line.starty+60;
            //添加到集合
            lines.add(line);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Flag=false;
    }


    @Override
    public void run() {
        Canvas canvas=null;
        Line line=null;
        while (Flag){
            canvas=surfaceHolder.lockCanvas();
            //清空画布
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            //遍历雨滴集合
            for (int i = 0; i < lines.size(); i++) {
                line=lines.get(i);
                //绘制雨滴
                canvas.drawLine(line.getStartx(),line.getStarty(),line.getStopx(),line.getStopy(),paint);

                //绘制雨滴之后 更改雨滴的Y轴坐标 下次绘制时即可更新位置 使雨滴下落
                //取三个随机数 每个随机数代表3种不同的长度以及下落速度
                int c=random.nextInt(3);
                if (c==0){
                    line.setStarty(line.getStarty()+20);
                    line.setStopy(line.getStarty()+40);
                }
                if (c==1){
                    line.setStarty(line.getStarty()+30);
                    line.setStopy(line.getStarty()+50);
                }
                if (c==2){
                    line.setStarty(line.getStarty()+15);
                    line.setStopy(line.getStarty()+30);
                }
                if (c==3){
                    line.setStarty(line.getStarty()+40);
                    line.setStopy(line.getStarty()+35);
                }

            }

            //解锁画布
            surfaceHolder.unlockCanvasAndPost(canvas);

            //添加雨滴
            addline();
            //当雨滴大于100条时 删除第一个 让雨滴保持在100条
            if (lines.size()>100){
                lines.remove(0);
            }
            Log.d("log", "run: "+lines.size());
        }
    }

    //雨的类，每一条线代表一个雨滴
    class Line{
        private int startx;//线的起始X坐标
        private int starty;//线的起始Y坐标
        private int stopx;//线的结束X坐标
        private int stopy;//线的结束Y坐标

        public int getStartx() {
            return startx;
        }

        public void setStartx(int startx) {
            this.startx = startx;
        }

        public int getStarty() {
            return starty;
        }

        public void setStarty(int starty) {
            this.starty = starty;
        }

        public int getStopx() {
            return stopx;
        }

        public void setStopx(int stopx) {
            this.stopx = stopx;
        }

        public int getStopy() {
            return stopy;
        }

        public void setStopy(int stopy) {
            this.stopy = stopy;
        }


    }
}
