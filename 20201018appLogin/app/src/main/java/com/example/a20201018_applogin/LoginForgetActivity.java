package com.example.a20201018_applogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a20201018_applogin.bean.UserInfo;
import com.example.a20201018_applogin.database.UserDBHelper;
import com.example.a20201018_applogin.util.DateUtil;

public class LoginForgetActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_password_first; // 声明一个编辑框对象
    private EditText et_password_second; // 声明一个编辑框对象
    private EditText et_verifycode; // 声明一个编辑框对象
    private String mVerifyCode; // 验证码
    private String mPhone; // 手机号码

    private UserDBHelper mHelper; // 声明一个用户数据库的帮助器对象

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget);

        // 从布局文件中获取名叫et_password_first的编辑框
        et_password_first = findViewById(R.id.et_password_first);
        // 从布局文件中获取名叫et_password_second的编辑框
        et_password_second = findViewById(R.id.et_password_second);
        // 从布局文件中获取名叫et_verifycode的编辑框
        et_verifycode = findViewById(R.id.et_verifycode);

        findViewById(R.id.btn_verifycode).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

        // 从前一个页面获取要修改密码的手机号码
        mPhone = getIntent().getStringExtra("phone");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获得用户数据库帮助器的一个实例
        mHelper = UserDBHelper.getInstance(this, 2);
        // 恢复页面，则打开数据库连接
        mHelper.openWriteLink();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停页面，则关闭数据库连接
        mHelper.closeLink();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_verifycode) { // 点击了“获取验证码”按钮
            if (mPhone == null || mPhone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            // 生成六位随机数字的验证码
            mVerifyCode = String.format("%06d", (int) ((Math.random() * 9 + 1) * 100000));
            // 弹出提醒对话框，提示用户六位验证码数字
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请记住验证码");
            builder.setMessage("手机号" + mPhone + "，本次验证码是" + mVerifyCode + "，请输入验证码");
            builder.setPositiveButton("好的", null);
            AlertDialog alert = builder.create();
            alert.show();
        } else if (v.getId() == R.id.btn_confirm) { // 点击了“确定”按钮
            String password_first = et_password_first.getText().toString();
            String password_second = et_password_second.getText().toString();
            if (password_first.length() < 6 || password_second.length() < 6) {
                Toast.makeText(this, "请输入正确的新密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password_first.equals(password_second)) {
                Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!et_verifycode.getText().toString().equals(mVerifyCode)) {
                Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            } else {
                UserInfo info = new UserInfo();
                info.phone = mPhone;
                info.pwd = password_first;
                info.update_time = DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss");
                mHelper.update(info, "phone=" + mPhone);

                Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
                // 把修改好的新密码返回给前一个页面
                Intent intent = new Intent();
                intent.putExtra("new_password", password_first);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }
}