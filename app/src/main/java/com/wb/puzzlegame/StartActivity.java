package com.wb.puzzlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();
        /*设置两个按钮来跳转到游戏界面
          本来想用两个fragmentg动态挂载在一个人Activity上的，
          后来发现这样跳转更简单一点
         */

        Button btn=(Button) findViewById(R .id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartActivity.this,LevelActivity.class);
                startActivity(i);
            }
        });
        Button btn3=(Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartActivity.this,HighLevelActivity.class);
                startActivity(i);

            }
        });
    }
}
