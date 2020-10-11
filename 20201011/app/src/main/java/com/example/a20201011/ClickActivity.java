package com.example.a20201011;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_click=findViewById(R.id.btn_click);
        btn_click.setOnClickListener(new MyOnClickListener());
        btn_click.setOnLongClickListener(new MyOnLongClickListener());
    }

    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if(v.getId()==R.id.btn_click){
                Toast.makeText(ClickActivity.this,"您点击了控件"+((TextView) v).getText(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MyOnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v){
            if(v.getId()==R.id.btn_click){
                Toast.makeText(ClickActivity.this,"您点击了控件"+((TextView) v).getText(),Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }
}