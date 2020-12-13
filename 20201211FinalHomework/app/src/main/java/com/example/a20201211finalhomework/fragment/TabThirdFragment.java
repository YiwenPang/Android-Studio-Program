package com.example.a20201211finalhomework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.example.a20201211finalhomework.MainActivity;
import com.example.a20201211finalhomework.MainApplication;
import com.example.a20201211finalhomework.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a20201211finalhomework.ShoppingDetailActivity;
import com.example.a20201211finalhomework.TabFragmentActivity;
import com.example.a20201211finalhomework.adapter.CartAdapter;
import com.example.a20201211finalhomework.bean.CartInfo;
import com.example.a20201211finalhomework.bean.GoodsInfo;
import com.example.a20201211finalhomework.bean.GoodsInfo2;
import com.example.a20201211finalhomework.database.CartDBHelper;
import com.example.a20201211finalhomework.database.GoodsDBHelper;
import com.example.a20201211finalhomework.util.FileUtil;
import com.example.a20201211finalhomework.util.SharedUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class TabThirdFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    private static final String TAG = "TabThirdFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private TextView tv_total_price;
    private Group gp_content;
    private Group gp_empty;
    private int mCount; // 购物车中的商品数量
    private GoodsDBHelper mGoodsHelper; // 声明一个商品数据库的帮助器对象
    private CartDBHelper mCartHelper; // 声明一个购物车数据库的帮助器对象

    private ListView lv_cart; // 声明一个列表视图对象
    private CartInfo mCurrentGood;  // 声明当前的商品对象
    private View mCurrentView;  // 声明一个当前视图的对象
    private Handler mHandler = new Handler();  // 声明一个处理器对象，用于长按菜单

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_third.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_tab_third, container, false);
        tv_total_price = (TextView)mView.findViewById(R.id.tv_total_price);
        gp_content = (Group) mView.findViewById(R.id.gp_content);
        gp_empty = (Group) mView.findViewById(R.id.gp_empty);
        mView.findViewById(R.id.btn_shopping_channel).setOnClickListener(this);
        mView.findViewById(R.id.btn_settle).setOnClickListener(this);

        // 从布局视图中获取名叫lv_cart的列表视图
        lv_cart = mView.findViewById(R.id.lv_cart);
        return mView;
    }
    // 显示购物车图标中的商品数量
    private void showCount(int count) {
        mCount = count;
        if (mCount == 0) {
            gp_content.setVisibility(View.GONE);
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
            //These codes are from Peter Pang.
            Intent intent = new Intent(mContext, TabFragmentActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_settle) { // 点击了“结算”按钮
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
        }
    }

    // 商品项的点击事件
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentGood = mCartArray.get(position);
        goDetail(mCurrentGood.goods_id);
    }

    // 商品项的长按事件
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentGood = mCartArray.get(position);
        // 保存当前长按的列表项视图
        mCurrentView = view;
        // 延迟100毫秒后执行任务mPopupMenu，留出时间让长按事件走完流程
        mHandler.postDelayed(mPopupMenu, 100);
        return true;
    }

    // 定义一个上下文菜单的弹出任务
    private Runnable mPopupMenu = new Runnable() {
        @Override
        public void run() {
            // 取消lv_cart的点击监听器
            lv_cart.setOnItemClickListener(null);
            // 取消lv_cart的长按监听器
            lv_cart.setOnItemLongClickListener(null);
            // 注册列表项视图的上下文菜单
            registerForContextMenu(mCurrentView);
            // 为该列表项视图弹出上下文菜单
            getActivity().openContextMenu(mCurrentView);
            // 注销列表项视图的上下文菜单
            unregisterForContextMenu(mCurrentView);
            // 构建购物车商品列表的适配器对象
            CartAdapter adapter = new CartAdapter(mContext, mCartArray);
            // 给lv_cart设置商品列表适配器
            lv_cart.setAdapter(adapter);
            // 重新设置lv_cart的点击监听器
            lv_cart.setOnItemClickListener(TabThirdFragment.this);
            // 重新设置lv_cart的长按监听器
            lv_cart.setOnItemLongClickListener(TabThirdFragment.this);
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // 从menu_cart.xml中构建菜单界面布局
        getActivity().getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_shopping) { // 点击了菜单项“去商场购物”
            // 跳转到商场页面
            Intent intent = new Intent(mContext, TabFragmentActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_clear) { // 点击了菜单项“清空购物车”
            // 清空购物车数据库
            mCartHelper.deleteAll();
            // 把最新的商品数量写入共享参数
            SharedUtil.getIntance(mContext).writeShared("count", "0");
            // 显示最新的商品数量
            showCount(0);
            Toast.makeText(mContext, "购物车已清空", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_return) { // 点击了菜单项“返回”
            getActivity().finish();
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // 从menu_goods.xml中构建菜单界面布局
        getActivity().getMenuInflater().inflate(R.menu.menu_goods, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_detail) { // 点击了菜单项“查看商品详情”
            // 跳转到查看商品详情页面
            goDetail(mCurrentGood.goods_id);
        } else if (id == R.id.menu_delete) { // 点击了菜单项“从购物车删除”
            // 从购物车删除商品的数据库操作
            mCartHelper.delete("goods_id=" + mCurrentGood.goods_id);
            // 更新购物车中的商品数量
            int left_count = mCount - mCurrentGood.count;
            // 把最新的商品数量写入共享参数
            SharedUtil.getIntance(mContext).writeShared("count", "" + left_count);
            // 显示最新的商品数量
            showCount(left_count);
            Toast.makeText(mContext, "已从购物车删除" + mCurrentGood.goods.name, Toast.LENGTH_SHORT).show();
            // 刷新购物车列表
            showCart();
        }
        return true;
    }

    // 跳转到商品详情页面
    private void goDetail(long rowid) {
        Intent intent = new Intent(mContext, ShoppingDetailActivity.class);
        intent.putExtra("goods_id", rowid);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取共享参数保存的购物车中的商品数量
        mCount = Integer.parseInt(SharedUtil.getIntance(mContext).readShared("count", "0"));
        showCount(mCount);
        // 获取商品数据库的帮助器对象
        mGoodsHelper = GoodsDBHelper.getInstance(mContext, 1);
        // 打开商品数据库的写连接
        mGoodsHelper.openWriteLink();
        // 获取购物车数据库的帮助器对象
        mCartHelper = CartDBHelper.getInstance(mContext, 1);
        // 打开购物车数据库的写连接
        mCartHelper.openWriteLink();
        // 模拟从网络下载商品图片
        downloadGoods();
        // 展示购物车中的商品列表
        showCart();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 关闭商品数据库的数据库连接
        mGoodsHelper.closeLink();
        // 关闭购物车数据库的数据库连接
        mCartHelper.closeLink();
    }

    // 声明一个购物车中的商品信息队列
    private ArrayList<CartInfo> mCartArray = new ArrayList<CartInfo>();

    // 展示购物车中的商品列表
    private void showCart() {
        // 查询购物车数据库中所有的商品记录
        mCartArray = mCartHelper.query("1=1");
        Log.d(TAG, "mCartArray.size()=" + mCartArray.size());
        if (mCartArray == null || mCartArray.size() <= 0) {
            return;
        }
        for (int i = 0; i < mCartArray.size(); i++) {
            CartInfo info = mCartArray.get(i);
            // 根据商品编号查询商品数据库中的商品记录
            GoodsInfo2 goods = mGoodsHelper.queryById(info.goods_id);
            info.goods = goods;
            // 补充商品记录的商品详情
            mCartArray.set(i, info);
        }

        // 构建购物车商品列表的适配器对象
        CartAdapter adapter = new CartAdapter(mContext, mCartArray);
        // 给lv_cart设置商品列表适配器
        lv_cart.setAdapter(adapter);
        // 给lv_cart设置列表项点击监听器
        lv_cart.setOnItemClickListener(this);
        // 给lv_cart设置列表项长按监听器
        lv_cart.setOnItemLongClickListener(this);

        // 重新计算购物车中的商品总金额
        refreshTotalPrice();
    }

    // 重新计算购物车中的商品总金额
    private void refreshTotalPrice() {
        int total_price = 0;
        for (CartInfo info : mCartArray) {
            total_price += info.goods.price * info.count;
        }
        tv_total_price.setText("" + total_price);
    }

    private String mFirst = "true"; // 是否首次打开

    //模拟网络数据，初始化数据库中的商品信息
    private void downloadGoods() {
        // 查询商品数据库中所有商品记录
        ArrayList<GoodsInfo2> goodsArray = mGoodsHelper.query("1=1");
        for (int i = 0; i < goodsArray.size(); i++) {
            GoodsInfo2 info = goodsArray.get(i);
            // 从指定路径读取图片文件的位图数据
            Bitmap thumb = BitmapFactory.decodeFile(info.thumb_path);
            // 把该位图对象保存到应用实例的全局变量中
            MainApplication.getInstance().mIconMap.put(info.rowid, thumb);
        }
    }
}
