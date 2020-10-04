package com.example.a20201004;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.a20201004.util.DateUtil;

public class bbs extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {
    private TextView tv_bbs;
    private TextView tv_control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs);

        tv_control=findViewById(R.id.tv_control);
        tv_control.setOnClickListener(this);
        tv_control.setOnLongClickListener(this);

        tv_bbs=findViewById(R.id.tv_bbs);
        tv_bbs.setOnClickListener(this);
        tv_bbs.setOnLongClickListener(this);

        tv_bbs.setMovementMethod(new ScrollingMovementMethod());
    }

    private String[] mChatStr={"雷神小动","刺云雨零耶","君将留","虽不零","吾将留妹留者"};

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.tv_control||v.getId()==R.id.tv_bbs){

            int random=(int)(Math.random()*10)%5;

            String newStr=String.format("%s\n%s %s",tv_bbs.getText().toString(),DateUtil.getNowTime(),mChatStr[random]);

            tv_bbs.setText(newStr);
        }
    }

    @Override
    public boolean onLongClick(View v){
        if(v.getId()==R.id.tv_control||v.getId()==R.id.tv_bbs){
            tv_bbs.setText("");
        }
        return true;
    }
}