package com.example.a20201018;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        initSpinner();
        initSpinnerForSimpleAdapter();
    }

    // 定义下拉列表需要显示的文本数组
    private String[] starArray = {"水星", "金星", "地球", "火星", "木星", "土星"};

    // 初始化下拉框
    private void initSpinner() {
        // 声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(this, R.layout.item_select, starArray);
        // 设置数组适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.item_dropdown);

        // 从布局文件中获取名叫sp_dropdown的下拉框
        Spinner sp_dropdown = findViewById(R.id.sp_dropdown);
        Spinner sp_dialog = findViewById(R.id.sp_dialog);

        // 设置下拉框的标题
        sp_dialog.setPrompt("请选择行星");
        // 设置下拉框的数组适配器
        sp_dialog.setAdapter(starAdapter);
        // 设置下拉框默认显示第一项
        sp_dropdown.setSelection(1);
        sp_dialog.setSelection(3);
        // 给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        sp_dropdown.setOnItemSelectedListener(new MySelectedListener());
        sp_dialog.setOnItemSelectedListener(new MySelectedListener());
    }

    // 定义下拉列表需要显示的行星图标数组
    private int[] iconArray = {R.drawable.shuixing, R.drawable.jinxing, R.drawable.diqiu,
            R.drawable.huoxing, R.drawable.muxing, R.drawable.tuxing};

    // 初始化下拉框，演示简单适配器
    private void initSpinnerForSimpleAdapter() {
        // 声明一个映射对象的队列，用于保存行星的图标与名称配对信息
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // iconArray是行星的图标数组，starArray是行星的名称数组
        for (int i = 0; i < iconArray.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("icon", iconArray[i]);
            item.put("name", starArray[i]);
            // 把一个行星图标与名称的配对映射添加到队列当中
            list.add(item);
        }
        // 声明一个下拉列表的简单适配器，其中指定了图标与文本两组数据
        SimpleAdapter starAdapter = new SimpleAdapter(this, list,
                R.layout.item_simple, new String[]{"icon", "name"},
                new int[]{R.id.iv_icon, R.id.tv_name});
        // 设置简单适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.item_simple);
        // 从布局文件中获取名叫sp_icon的下拉框
        Spinner sp = findViewById(R.id.sp_icon);
        // 设置下拉框的标题
        sp.setPrompt("请选择行星");
        // 设置下拉框的简单适配器
        sp.setAdapter(starAdapter);
        // 设置下拉框默认显示第一项
        sp.setSelection(0);
        // 给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        sp.setOnItemSelectedListener(new MySelectedListener());
    }

    // 定义一个选择监听器，它实现了接口OnItemSelectedListener
    class MySelectedListener implements OnItemSelectedListener {
        /* 选择事件的处理方法
        adapter:适配器
        view:视图
        position:第几项
        id:id
        */
        public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
            //获取选择的项的值
            String sInfo = adapter.getItemAtPosition(position).toString();
            Toast.makeText(SpinnerActivity.this, "您选择的是" + sInfo, Toast.LENGTH_LONG).show();
        }

        // 未选择时的处理方法，通常无需关注
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}