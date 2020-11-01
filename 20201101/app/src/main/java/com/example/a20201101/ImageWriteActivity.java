package com.example.a20201101;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class ImageWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout cl_info;
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
        setContentView(R.layout.activity_image_write);

        // 布局id
        cl_info = findViewById(R.id.cl_info);

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

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 获取布局cl_info绘图缓存中的位图数据
                Bitmap bitmap = cl_info.getDrawingCache();
                String file_path = mPath + DateUtil.getNowDateTime("") + ".png";
                // 把位图数据保存为图片文件
                FileUtil.saveImage(file_path, bitmap);
                // 回收位图对象
                bitmap.recycle();
                tv_path.setText("用户注册信息图片的保存路径为：\n" + file_path);
                showToast("图片已存入SD卡文件");

                Intent intent = new Intent(this, ImageReadActivity.class);
                startActivity(intent);
            } else {
                showToast("未发现已挂载的SD卡，请检查");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开启布局cl_info的绘图缓存
        cl_info.setDrawingCacheEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭布局cl_info的绘图缓存
        cl_info.setDrawingCacheEnabled(false);
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }
}