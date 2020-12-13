package com.example.a20201211finalhomework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a20201211finalhomework.bean.UserInfo;
import com.example.a20201211finalhomework.database.UserDBHelper;
import com.example.a20201211finalhomework.util.DateUtil;
import com.example.a20201211finalhomework.util.ViewUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private RadioGroup rg_login;
    private RadioButton rb_password;
    private RadioButton rb_verifycode;
    private EditText et_phone;
    private TextView tv_password;
    private EditText et_password;
    private Button btn_forget;
    private Button btn_login;
    private Button btn_signup;
    private Switch ck_remember;

    private int mRequestCode = 0;
    private int mType = 2; // 用户类型
    private boolean bRemember = false;
    private String mPassword = "111111";
    private String mVerifyCode; // 验证码

    private SharedPreferences mShared;

    private UserDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rg_login = findViewById(R.id.rg_login);
        rb_password = findViewById(R.id.rb_password);
        rb_verifycode = findViewById(R.id.rb_verifycode);
        et_phone = findViewById(R.id.et_phone);
        tv_password = findViewById(R.id.tv_password);
        et_password = findViewById(R.id.et_password);
        btn_forget = findViewById(R.id.btn_forget);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        ck_remember = findViewById(R.id.ck_remember);

        rg_login.setOnCheckedChangeListener(new RadioListener());
        ck_remember.setOnCheckedChangeListener(new CheckListener());
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone));
        et_password.addTextChangedListener(new HideTextWatcher(et_password));

        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);

        initTypeSpinner();

        mShared = getSharedPreferences("share_login", MODE_PRIVATE);
        et_password.setOnFocusChangeListener(this);

        mShared=getSharedPreferences("share_login",MODE_PRIVATE);
        String phone=mShared.getString("phone","");
        String password=mShared.getString("password","");
        et_phone.setText(phone);
        et_password.setText(password);

    }

    private String[] typeArray = {"个人用户", "公司用户","18990160 庞怡文"};

    private void initTypeSpinner() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.item_select, typeArray);
        typeAdapter.setDropDownViewResource(R.layout.item_dropdown);
        Spinner sp_type = findViewById(R.id.sp_type);
        sp_type.setPrompt("请选择用户类型");
        sp_type.setAdapter(typeAdapter);
        sp_type.setSelection(mType);
        sp_type.setOnItemSelectedListener(new TypeSelectedListener());
    }

    class TypeSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            mType = arg2;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private class RadioListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rb_password) {
                tv_password.setText("登录密码");
                et_password.setHint("请输入密码");
                btn_forget.setText("忘记密码");
                ck_remember.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.rb_verifycode) {
                tv_password.setText("　验证码");
                et_password.setHint("请输入验证码");
                btn_forget.setText("获取验证码");
                ck_remember.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.ck_remember) {
                bRemember = isChecked;
            }
        }
    }

    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;
        private CharSequence mStr;

        HideTextWatcher(EditText v) {
            super();
            mView = v;
            mMaxLength = ViewUtil.getMaxLength(v);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mStr = s;
        }

        public void afterTextChanged(Editable s) {
            if (mStr == null || mStr.length() == 0)
                return;
            if ((mStr.length() == 11 && mMaxLength == 11) ||
                    (mStr.length() == 6 && mMaxLength == 6)) {
                ViewUtil.hideOneInputMethod(MainActivity.this, mView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String phone = et_phone.getText().toString();
        if (v.getId() == R.id.btn_forget) {
            if (phone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rb_password.isChecked()) {
                Intent intent = new Intent(this, LoginForgetActivity.class);
                intent.putExtra("phone", phone);
                startActivityForResult(intent, mRequestCode);
            }else if (rb_verifycode.isChecked()) {
                mVerifyCode = String.format("%06d", (int) ((Math.random() * 9 + 1) * 100000));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请记住验证码");
                builder.setMessage("手机号" + phone + "，本次验证码是" + mVerifyCode + "，请输入验证码");
                builder.setPositiveButton("好的", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else if (v.getId() == R.id.btn_login) {
            if (phone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rb_password.isChecked()) {
                UserInfo info = mHelper.queryByPhone(et_phone.getText().toString());
                if (!et_password.getText().toString().equals(info.pwd)) {
                    Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                } else {
                    loginSuccess();
                }
            } else if (rb_verifycode.isChecked()) {
                if (!et_password.getText().toString().equals(mVerifyCode)) {
                    Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else {
                    loginSuccess();
                }
            }
        } else if (v.getId() == R.id.btn_signup){
            if (rb_password.isChecked()) {
                Intent intent = new Intent(this, SQLiteWriteActivity.class);
                startActivityForResult(intent, mRequestCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && data != null) {
            mPassword = data.getStringExtra("new_password");
        }
    }

    @Override
    protected void onRestart() {
        et_password.setText("");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHelper = UserDBHelper.getInstance(this, 2);
        mHelper.openWriteLink();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.closeLink();
    }

    private void loginSuccess() {
        if (bRemember) {
            SharedPreferences.Editor editor = mShared.edit();
            editor.putString("phone", et_phone.getText().toString());
            editor.putString("password", et_password.getText().toString());
            editor.commit();

            UserInfo info = new UserInfo();
            info.phone = et_phone.getText().toString();
            info.pwd = et_password.getText().toString();
            info.update_time = DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss");
            mHelper.insert(info);
        }

        String desc = String.format("您的手机号码是%s，类型是%s。恭喜你通过登录验证，点击“确定”按钮返回上个页面",
                et_phone.getText().toString(), typeArray[mType]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录成功");
        builder.setMessage(desc);
        builder.setPositiveButton("确定返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("我再看看", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String phone = et_phone.getText().toString();
        if (v.getId() == R.id.et_password) {
            if (phone.length() > 0 && hasFocus) {
                UserInfo info = mHelper.queryByPhone(phone);
                if (info != null) {
                    et_password.setText(info.pwd);
                    mPassword = info.pwd;
                }
            }
        }
    }
}//These codes are from Peter Pang.