package com.wb.puzzlegame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.log;

public class MainView extends View {
    private static  final  String TAG="MainView";
    private  Context context;
    private Bitmap back; //屏幕尺寸
    private Paint paint;  //碎片
    private  int tileWidth; //切块图片的宽度
    private int tileHeight;  //切块图片的高度
    private Bitmap[] bitmapTiles; //用于存储图片
    private int[][] dataTiles;   //取出的碎片数据
    private Board tilesBoard;   //边距
    private final int COL=3;
    private final int ROW=3;  //行和列的定义
    private int[][] dir= {
            {-1, 0},//左
            {0, -1},//上
            {1, 0},//右
            {0, 1}//下
    };
    private boolean isSuccess;
    private Bitmap bitmap;

    public MainView(Context context,String path){
        super(context);
        this.context=context;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //存储缩略图
        bitmap = BitmapFactory.decodeStream(fis);
        paint=new Paint();
        paint.setAntiAlias(true);
        init();
        startGame();
        log(MainActivity.getScreenWidth()+","+MainActivity.getScreenHeight());
    }
    public MainView(Context context) {
        super(context);
        this.context=context;
        paint=new Paint();
        paint.setAntiAlias(true);
        init();
        startGame();
        log(MainActivity.getScreenWidth()+","+MainActivity.getScreenHeight());
    }

    //初始化
    private void init() {
        ////载入图像，并将图片切成块
        if (bitmap!=null){
            back = Bitmap.createScaledBitmap(bitmap, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), true);
            tileWidth=back.getWidth()/COL;
            tileHeight=back.getHeight()/ROW;
            bitmapTiles=new Bitmap[COL*ROW];
            int idx=0;
            for (int i=0;i<ROW;i++){
                for (int j=0;j<COL;j++){
                    bitmapTiles[idx++]=Bitmap.createBitmap(back,j*tileWidth,i*tileHeight,tileWidth,tileHeight);
                }
            }
        }else {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream assetInputStream = assetManager.open("timg.jpg");
            bitmap = BitmapFactory.decodeStream(assetInputStream);
            back = Bitmap.createScaledBitmap(bitmap, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), true);
        }  catch (IOException e) {
            e.printStackTrace();
        }
        tileWidth=back.getWidth()/COL;
        tileHeight=back.getHeight()/ROW;
        bitmapTiles=new Bitmap[COL*ROW];
        int idx=0;
        for (int i=0;i<ROW;i++){
            for (int j=0;j<COL;j++){
                bitmapTiles[idx++]=Bitmap.createBitmap(back,j*tileWidth,i*tileHeight,tileWidth,tileHeight);
            }
        }
        }
    }
    /**
     * 开始游戏
     */
    private void startGame() {
        tilesBoard=new Board();
        dataTiles=tilesBoard.createRandomBoard(ROW,COL);
        isSuccess=false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        for (int i=0;i<ROW;i++){
            for (int j=0;j<COL;j++){
                int idx=dataTiles[i][j];
                if(idx==ROW*COL-1&&!isSuccess)
                    continue;
                canvas.drawBitmap(bitmapTiles[idx],j*tileWidth,i*tileHeight,paint);
            }
        }
    }
    /**
     * 将屏幕上的点转换成,对应拼图块的索引
     * @param x
     * @param y
     * @return
     */
    private Point xyToIndex(int x,int y)
    {
        int extraX=x%tileWidth>0?1:0;
        int extraY=x%tileWidth>0?1:0;
        int col=x/tileWidth+extraX;
        int row=y/tileHeight+extraY;

        return new Point(col-1,row-1);
    }
    //设置它的触摸事件的处理并加以判断拼图是否成功
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            Point point = xyToIndex((int) event.getX(), (int) event.getY());

            for(int i=0;i<dir.length;i++)
            {
                int newX=point.getX()+dir[i][0];
                int newY=point.getY()+dir[i][1];

                if(newX>=0&&newX<COL&&newY>=0&&newY<ROW){
                    if(dataTiles[newY][newX]==COL*ROW-1)
                    {
                        int temp=dataTiles[point.getY()][point.getX()];
                        dataTiles[point.getY()][point.getX()]=dataTiles[newY][newX];
                        dataTiles[newY][newX]=temp;
                        invalidate();
                        if(tilesBoard.isSuccess(dataTiles)){
                            isSuccess=true;
                            invalidate();
                            //设置对话框
                            new AlertDialog.Builder(context)
                                    .setTitle("拼图成功")
                                    .setCancelable(false)
                                    .setMessage("恭喜你拼图成功")
                                    .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startGame();
                                        }
                                    })
                                    .setNegativeButton("上一页", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(0);
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void log(String log) {
        System.out.println("--------------->MainView:"+log);
    }
    private void printArray(int[][] arr){
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<arr.length;i++){
            for (int j=0;j<arr[i].length;j++){
                sb.append(arr[i][j]+",");
            }
            sb.append("\n");
        }
        log(sb.toString());
    }

}
