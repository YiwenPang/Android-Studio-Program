package com.example.a20201101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a20201101.util.DateUtil;
import com.example.a20201101.util.FileUtil;

public class TextWriteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private boolean bMarried = false;
    private String mPath;
    private TextView tv_path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_write);

        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        tv_path = findViewById(R.id.tv_path);
        findViewById(R.id.btn_save).setOnClickListener(this);

        Spinner sp_married = findViewById(R.id.sp_married);
        sp_married.setOnItemSelectedListener(new TypeSelectedListener());

        // 获取当前App的私有存储目录
        mPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
    }

    class TypeSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            bMarried = (arg2 == 0) ? false : true;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            String name = et_name.getText().toString();
            String age = et_age.getText().toString();
            String height = et_height.getText().toString();
            String weight = et_weight.getText().toString();
            if (TextUtils.isEmpty(name)) {
                showToast("请先填写姓名");
                return;
            } else if (TextUtils.isEmpty(age)) {
                showToast("请先填写年龄");
                return;
            } else if (TextUtils.isEmpty(height)) {
                showToast("请先填写身高");
                return;
            } else if (TextUtils.isEmpty(weight)) {
                showToast("请先填写体重");
                return;
            }

            String content = "";
            content = String.format("%s　姓名：%s\n", content, name);
            content = String.format("%s　年龄：%s\n", content, age);
            content = String.format("%s　身高：%scm\n", content, height);
            content = String.format("%s　体重：%skg\n", content, weight);
            content = String.format("%s　婚否：%s\n", content, bMarried);
            content = String.format("%s　注册时间：%s\n", content, DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss"));

            // 检查SD卡状态,存储媒体已经挂载，并且挂载点可读/写。
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String file_path = mPath + DateUtil.getNowDateTime("") + ".txt";
                // 把文本字符串保存为文本文件
                FileUtil.saveText(file_path, content);
                tv_path.setText("用户注册信息文件的保存路径为：\n" + file_path);
                showToast("数据已写入SD卡文件");

                Intent intent = new Intent(this, TextReadActivity.class);
                startActivity(intent);
            } else {
                showToast("未发现已挂载的SD卡，请检查");
            }
        }
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }
}