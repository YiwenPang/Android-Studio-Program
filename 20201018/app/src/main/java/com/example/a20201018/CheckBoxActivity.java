package com.example.a20201018;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class CheckBoxActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_box);

        CheckBox ck_system = findViewById(R.id.ck_system);
        CheckBox ck_custom = findViewById(R.id.ck_custom);
        CheckBox ck_custom2 = findViewById(R.id.ck_custom2);

        // 给ck_system设置勾选监听器，一旦用户点击复选框，就触发监听器的onCheckedChanged方法
        ck_system.setOnCheckedChangeListener(this);
        // 给ck_custom设置勾选监听器，一旦用户点击复选框，就触发监听器的onCheckedChanged方法
        ck_custom.setOnCheckedChangeListener(this);
        // 给ck_custom2设置勾选监听器，一旦用户点击复选框，就触发监听器的onCheckedChanged方法
        ck_custom2.setOnCheckedChangeListener(this);

        Drawable drawable= ContextCompat.getDrawable(this,R.drawable.checkbox_selector);
        drawable.setBounds(0,0,46,46);
        ck_custom2.setCompoundDrawablePadding(10);
        ck_custom2.setCompoundDrawables(drawable,null,null,null);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String desc = String.format("您%s了这个CheckBox", isChecked ? "勾选" : "取消勾选");
        buttonView.setText(desc);
    }
}