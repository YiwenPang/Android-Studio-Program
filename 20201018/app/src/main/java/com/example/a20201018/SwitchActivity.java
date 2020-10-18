package com.example.a20201018;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class SwitchActivity extends AppCompatActivity implements OnCheckedChangeListener {
    private Switch sw_status; // 声明一个开关按钮对象
    private Switch sw_ios; // 声明一个开关按钮对象
    private TextView tv_result; // 声明一个文本视图对象
    private TextView tv_ios_result; // 声明一个文本视图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        // 从布局文件中获取名叫sw_status的开关按钮
        sw_status = findViewById(R.id.sw_status);
        sw_ios = findViewById(R.id.sw_ios);
        // 从布局文件中获取名叫tv_result的文本视图
        tv_result = findViewById(R.id.tv_result);
        tv_ios_result = findViewById(R.id.tv_ios_result);
        // 给开关按钮设置选择监听器，一旦用户点击它，就触发监听器的onCheckedChanged方法
        sw_status.setOnCheckedChangeListener(this);
        refreshResult(sw_status);
        sw_ios.setOnCheckedChangeListener(this);
        refreshResult(sw_ios);
    }

    // 刷新Switch按钮的开关状态说明
    private void refreshResult(CompoundButton buttonView) {
        String result = String.format("Switch按钮的状态是%s",
                (buttonView.isChecked()) ? "开" : "关");
        if (buttonView.getId() == R.id.sw_status) {
            tv_result.setText(result);
        } else if (buttonView.getId() == R.id.sw_ios) {
            tv_ios_result.setText(result);
        }

    }

    // 选择事件的处理方法
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        refreshResult(buttonView);
    }
}