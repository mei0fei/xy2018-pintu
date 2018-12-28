package com.wb.puzzlegame;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HighLevelActivity extends AppCompatActivity {
    private MediaPlayer mPlayer;//控制器
    MenuItem  gMenuItem=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPlayer = MediaPlayer.create(this,R.raw.jianxin);
        mPlayer.start();
        mPlayer.setLooping(true);

        super.onCreate(savedInstanceState);
        setContentView(new MainView2(this) );
        ActionBar actionBar=getSupportActionBar();
        getSupportActionBar().setTitle("图片华容道");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    //放置menu可以用getSupportActionBar().hide来隐藏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        gMenuItem= menu.findItem(R.id.start);//获取菜单按钮
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item:
                Toast.makeText(this,"原图" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_recover:
                //Toast.makeText(this,"返回",Toast.LENGTH_SHORT).show();
                finish();
                mPlayer.pause();
                break;
            case R.id.start:
            {
                try {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                            Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
                            gMenuItem.setTitle("播放音乐");
                        } else {
                            mPlayer.start();
                            mPlayer.setLooping(true);//循环播放
                            Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show();
                            gMenuItem.setTitle("暂停音乐");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.qietu:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //设置请求码，以便我们区分返回的数据
                startActivityForResult(intent, 101);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LevelActivity.RESULT_OK) {//如果选取成功
            Uri uri = convertUri(data.getData());      //获取选取文件的Uri并进行Uri格式转换
            String path = uri.getPath();
            Log.d("11111","地址："+path);

            setContentView(new MainView2(this,path));
            ActionBar actionBar = getSupportActionBar();
            getSupportActionBar().setTitle("图片华容道");
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
    }

    private Uri convertUri(Uri uri) {//将“convertUri”类型的Uri转换为“file：//的Uri”
        if (uri.toString().substring(0, 7).equals("content")) {//如果是以“content”开头
            String[] colName = {MediaStore.MediaColumns.DATA};  //声明要查询的字段
            Cursor cursor = getContentResolver().query(uri, colName, null, null, null);//以uri进行查询
            cursor.moveToFirst();           //移到查询结果的第一个目录
            uri = Uri.parse("file://" + cursor.getString(0));//将路径转换为Uri
            cursor.close();     //关闭查询结果+
        }
        return uri;//返回uri对象
    }
}
