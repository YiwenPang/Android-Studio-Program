package com.example.a20201011;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ImageScaleActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scale);

        iv_scale=findViewById(R.id.iv_scale);

        findViewById(R.id.btn_center).setOnClickListener(this);
        findViewById(R.id.btn_fitcenter).setOnClickListener(this);
        findViewById(R.id.btn_centerCrop).setOnClickListener(this);
        findViewById(R.id.btn_centerInside).setOnClickListener(this);
        findViewById(R.id.btn_fitXY).setOnClickListener(this);
        findViewById(R.id.btn_fitStart).setOnClickListener(this);
        findViewById(R.id.btn_fitEnd).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.btn_center){
            iv_scale.setScaleType(ImageView.ScaleType.CENTER);
        } else if(v.getId()==R.id.btn_fitcenter){
            iv_scale.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else if(v.getId()==R.id.btn_centerCrop){
            iv_scale.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if(v.getId()==R.id.btn_centerInside){
            iv_scale.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else if(v.getId()==R.id.btn_fitXY){
            iv_scale.setScaleType(ImageView.ScaleType.FIT_XY);
        } else if(v.getId()==R.id.btn_fitStart){
            iv_scale.setScaleType(ImageView.ScaleType.FIT_START);
        } else if(v.getId()==R.id.btn_fitEnd){
            iv_scale.setScaleType(ImageView.ScaleType.FIT_END);
        }
    }
}