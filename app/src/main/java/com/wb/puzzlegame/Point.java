package com.wb.puzzlegame;

public class Point {
    public Point(int x,int y){
        this.x=x;
        this.y=y;
    }//设置和获取滑块移动的位置
    public int getX(){
        return x;
    }
    public void  setX(int x){
        this.x=x;
    }
    public int getY(){
        return y;
    }
    public  void setY(int y){
        this.y=y;
    }
    private int x=0;
    private  int y=0;
}
