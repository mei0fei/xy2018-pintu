package com.wb.puzzlegame;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by 杨兰海、康冬平、尹志勇 on 2018/12/21.
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE =20 ;
    private static  int screenWidth;//屏幕的宽度
    private  static  int screenHeight;//屏幕的高度

    private Uri uri;
    private MediaPlayer mper;
    private Button btn1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024,1024);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        getSupportActionBar().hide();
        screenWidth=metrics.widthPixels;
        screenHeight=metrics.heightPixels;
        setContentView(R.layout.activity_main);
        //判断是否有读取权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)//判断是否有读取权限
                != PackageManager.PERMISSION_GRANTED) {
            //弹出权限授予对话框，提示用户授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        Button btn=(Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//用于跳转到另一个activity
                Intent i=new Intent(MainActivity.this,StartActivity.class);
                startActivity(i);
            }
        });
        Button btn1=(Button) findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public  static  int getScreenWidth(){
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

}