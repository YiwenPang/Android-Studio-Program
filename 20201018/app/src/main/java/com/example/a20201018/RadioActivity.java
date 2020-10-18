package com.example.a20201018;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class RadioActivity extends AppCompatActivity implements OnCheckedChangeListener {
    private TextView tv_sex; // 声明一个文本视图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        // 从布局文件中获取名叫tv_sex的文本视图
        tv_sex = findViewById(R.id.tv_sex);
        // 从布局文件中获取名叫rg_sex的单选组
        RadioGroup rg_sex = findViewById(R.id.rg_sex);

        RadioButton rb_scale = findViewById(R.id.rb_scale);
        RadioButton rb_scale2 = findViewById(R.id.rb_scale2);

        // 设置单选按钮图片样式
        Drawable drawable = this.getResources().getDrawable(R.drawable.radio_selector);
        drawable.setBounds(0,0,46,46);
        rb_scale.setCompoundDrawablePadding(10);
        rb_scale.setCompoundDrawables(drawable,null,null,null);

        Drawable drawable2 = this.getResources().getDrawable(R.drawable.radio_selector);
        drawable2.setBounds(0,0,46,46);
        rb_scale2.setCompoundDrawablePadding(10);
        rb_scale2.setCompoundDrawables(drawable2,null,null,null);

        // 给rg_sex设置单选监听器，一旦用户点击组内的单选按钮，就触发监听器的onCheckedChanged方法
        rg_sex.setOnCheckedChangeListener(this);
    }

    // 在用户点击组内的单选按钮时触发
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_male) {
            tv_sex.setText("哇哦，你是个帅气的男孩");
        } else if (checkedId == R.id.rb_female) {
            tv_sex.setText("哇哦，你是个漂亮的女孩");
        }
    }
}