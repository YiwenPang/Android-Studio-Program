package com.example.a20201108;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a20201108.bean.CartInfo;
import com.example.a20201108.bean.GoodsInfo;
import com.example.a20201108.database.CartDBHelper;
import com.example.a20201108.database.GoodsDBHelper;
import com.example.a20201108.util.SharedUtil;
import com.example.a20201108.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ShoppingCartActivity";
    private TextView tv_total_price;
    private Group gp_content;
    private LinearLayout ll_cart;
    private Group gp_empty;
    private int mCount; // 购物车中的商品数量
    private GoodsDBHelper mGoodsHelper; // 声明一个商品数据库的帮助器对象
    private CartDBHelper mCartHelper; // 声明一个购物车数据库的帮助器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        tv_total_price = findViewById(R.id.tv_total_price);
        gp_content = findViewById(R.id.gp_content);
        ll_cart = findViewById(R.id.ll_cart);
        gp_empty = findViewById(R.id.gp_empty);
        findViewById(R.id.btn_shopping_channel).setOnClickListener(this);
        findViewById(R.id.btn_settle).setOnClickListener(this);
    }

    // 显示购物车图标中的商品数量
    private void showCount(int count) {
        mCount = count;
        if (mCount == 0) {
            gp_content.setVisibility(View.GONE);
            ll_cart.removeAllViews();
            gp_empty.setVisibility(View.VISIBLE);
        } else {
            gp_content.setVisibility(View.VISIBLE);
            gp_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_shopping_channel) { // 点击了“商场”按钮
            // 跳转到手机商场页面
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_settle) { // 点击了“结算”按钮
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("结算商品");
            builder.setMessage("客官抱歉，支付功能尚未开通，请下次再来");
            builder.setPositiveButton("我知道了", null);
            builder.create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 从menu_cart.xml中构建菜单界面布局
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_shopping) { // 点击了菜单项“去商场购物”
            // 跳转到商场页面
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_clear) { // 点击了菜单项“清空购物车”
            // 清空购物车数据库
            mCartHelper.deleteAll();
            ll_cart.removeAllViews();
            // 把最新的商品数量写入共享参数
            SharedUtil.getIntance(this).writeShared("count", "0");
            // 显示最新的商品数量
            showCount(0);
            mCartGoods.clear();
            mGoodsMap.clear();
            Toast.makeText(this, "购物车已清空", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_return) { // 点击了菜单项“返回”
            finish();
        }
        return true;
    }

    // 声明一个根据视图编号查找商品信息的映射
    private HashMap<Integer, CartInfo> mCartGoods = new HashMap<Integer, CartInfo>();
    // 声明一个触发上下文菜单的视图对象
    private View mContextView;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // 保存该商品行的视图，以便删除商品时一块从列表移除该行
        mContextView = v;
        // 从menu_goods.xml中构建菜单界面布局
        getMenuInflater().inflate(R.menu.menu_goods, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CartInfo info = mCartGoods.get(mContextView.getId());
        int id = item.getItemId();
        if (id == R.id.menu_detail) { // 点击了菜单项“查看商品详情”
            // 跳转到查看商品详情页面
            goDetail(info.goods_id);
        } else if (id == R.id.menu_delete) { // 点击了菜单项“从购物车删除”
            long goods_id = info.goods_id;
            // 从购物车删除商品的数据库操作
            mCartHelper.delete("goods_id=" + goods_id);
            // 从购物车列表中删除该商品行
            ll_cart.removeView(mContextView);
            // 更新购物车中的商品数量
            int left_count = mCount - info.count;
            for (int i = 0; i < mCartArray.size(); i++) {
                if (goods_id == mCartArray.get(i).goods_id) {
                    left_count = mCount - mCartArray.get(i).count;
                    mCartArray.remove(i);
                    break;
                }
            }
            // 把最新的商品数量写入共享参数
            SharedUtil.getIntance(this).writeShared("count", "" + left_count);
            // 显示最新的商品数量
            showCount(left_count);
            Toast.makeText(this, "已从购物车删除" + mGoodsMap.get(goods_id).name, Toast.LENGTH_SHORT).show();
            mGoodsMap.remove(goods_id);
            refreshTotalPrice();
        }
        return true;
    }

    // 跳转到商品详情页面
    private void goDetail(long rowid) {
        Intent intent = new Intent(this, ShoppingDetailActivity.class);
        intent.putExtra("goods_id", rowid);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取共享参数保存的购物车中的商品数量
        mCount = Integer.parseInt(SharedUtil.getIntance(this).readShared("count", "0"));
        showCount(mCount);
        // 获取商品数据库的帮助器对象
        mGoodsHelper = GoodsDBHelper.getInstance(this, 1);
        // 打开商品数据库的写连接
        mGoodsHelper.openWriteLink();
        // 获取购物车数据库的帮助器对象
        mCartHelper = CartDBHelper.getInstance(this, 1);
        // 打开购物车数据库的写连接
        mCartHelper.openWriteLink();
        // 展示购物车中的商品列表
        showCart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 关闭商品数据库的数据库连接
        mGoodsHelper.closeLink();
        // 关闭购物车数据库的数据库连接
        mCartHelper.closeLink();
    }

    // 声明一个起始的视图编号
    private int mBeginViewId = 0x7F24FFF0;
    // 声明一个购物车中的商品信息队列
    private ArrayList<CartInfo> mCartArray = new ArrayList<CartInfo>();
    // 声明一个根据商品编号查找商品信息的映射
    private HashMap<Long, GoodsInfo> mGoodsMap = new HashMap<Long, GoodsInfo>();

    // 展示购物车中的商品列表
    private void showCart() {
        // 查询购物车数据库中所有的商品记录
        mCartArray = mCartHelper.query("1=1");
        Log.d(TAG, "mCartArray.size()=" + mCartArray.size());
        if (mCartArray == null || mCartArray.size() <= 0) {
            return;
        }
        // 移除线性视图ll_cart下面的所有子视图
        ll_cart.removeAllViews();
        // 创建一个线性视图ll_row
        LinearLayout ll_row = newLinearLayout(LinearLayout.HORIZONTAL, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mCartArray.size(); i++) {
            final CartInfo info = mCartArray.get(i);
            // 根据商品编号查询商品数据库中的商品记录
            GoodsInfo goods = mGoodsHelper.queryById(info.goods_id);
            Log.d(TAG, "name=" + goods.name + ",price=" + goods.price + ",desc=" + goods.desc);
            mGoodsMap.put(info.goods_id, goods);
            // 创建该商品行的水平线性视图，从左到右依次为商品小图、商品名称与描述、商品数量、商品单价、商品总价。
            ll_row = newLinearLayout(LinearLayout.HORIZONTAL, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll_row.setPadding(10,20,10,20);
            // 设置该线性视图的编号
            ll_row.setId(mBeginViewId + i);
            // 添加商品小图
            ImageView iv_thumb = new ImageView(this);
            LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(
                    0, Utils.dip2px(this, 85), 2);
            iv_thumb.setLayoutParams(iv_params);
            iv_thumb.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv_thumb.setImageBitmap(MainApplication.getInstance().mIconMap.get(info.goods_id));
            ll_row.addView(iv_thumb);
            // 添加商品名称与描述
            LinearLayout ll_name = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
            ll_name.setLayoutParams(params);
            ll_name.setOrientation(LinearLayout.VERTICAL);
            ll_name.addView(newTextView(-3, 1, Gravity.LEFT, goods.name, Color.BLACK, 17));
            ll_name.addView(newTextView(-3, 1, Gravity.LEFT, goods.desc, Color.GRAY, 12));
            ll_row.addView(ll_name);
            // 添加商品数量、单价和总价
            ll_row.addView(newTextView(1, 1, Gravity.CENTER, "" + info.count, Color.BLACK, 17));
            ll_row.addView(newTextView(1, 1, Gravity.RIGHT, "" + (int) goods.price, Color.BLACK, 15));
            ll_row.addView(newTextView(1, 1, Gravity.RIGHT, "" + (int) (info.count * goods.price), Color.RED, 17));
            // 给商品行添加点击事件
            ll_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goDetail(info.goods_id);
                }
            });
            // 给商品行注册上下文菜单，为防止重复注册，这里先注销再注册
            unregisterForContextMenu(ll_row);
            registerForContextMenu(ll_row);
            mCartGoods.put(ll_row.getId(), info);
            // 往购物车列表添加该商品行
            ll_cart.addView(ll_row);
        }
        // 重新计算购物车中的商品总金额
        refreshTotalPrice();
    }

    // 重新计算购物车中的商品总金额
    private void refreshTotalPrice() {
        int total_price = 0;
        for (CartInfo info : mCartArray) {
            GoodsInfo goods = mGoodsMap.get(info.goods_id);
            total_price += goods.price * info.count;
        }
        tv_total_price.setText("" + total_price);
    }

    // 创建一个线性视图的框架
    private LinearLayout newLinearLayout(int orientation, int height) {
        LinearLayout ll_new = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height);
        ll_new.setLayoutParams(params);
        ll_new.setOrientation(orientation);
        ll_new.setBackgroundColor(Color.WHITE);
        return ll_new;
    }

    // 创建一个文本视图的模板
    private TextView newTextView(int height, float weight, int gravity, String text, int textColor, int textSize) {
        TextView tv_new = new TextView(this);
        if (height == -3) {  // 垂直排列
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, weight);
            tv_new.setLayoutParams(params);
        } else {  // 水平排列
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, (height == 0) ? LinearLayout.LayoutParams.WRAP_CONTENT : LinearLayout.LayoutParams.MATCH_PARENT, weight);
            tv_new.setLayoutParams(params);
        }
        tv_new.setText(text);
        tv_new.setTextColor(textColor);
        tv_new.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        tv_new.setGravity(Gravity.CENTER | gravity);
        return tv_new;
    }
}