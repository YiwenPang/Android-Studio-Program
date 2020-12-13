package com.example.a20201211finalhomework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.a20201211finalhomework.MainApplication;
import com.example.a20201211finalhomework.R;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a20201211finalhomework.ShoppingDetailActivity;
import com.example.a20201211finalhomework.TabSecondActivity;
import com.example.a20201211finalhomework.bean.CartInfo;
import com.example.a20201211finalhomework.bean.GoodsInfo;
import com.example.a20201211finalhomework.bean.GoodsInfo2;
import com.example.a20201211finalhomework.database.CartDBHelper;
import com.example.a20201211finalhomework.database.GoodsDBHelper;
import com.example.a20201211finalhomework.util.DateUtil;
import com.example.a20201211finalhomework.util.FileUtil;
import com.example.a20201211finalhomework.util.SharedUtil;
import com.example.a20201211finalhomework.util.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class TabSecondFragment extends Fragment{
    private static final String TAG = "TabSecondFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private TextView tv_count;
    private LinearLayout ll_channel;
    private int mCount; // 购物车中的商品数量
    private GoodsDBHelper mGoodsHelper; // 声明一个商品数据库的帮助器对象
    private CartDBHelper mCartHelper; // 声明一个购物车数据库的帮助器对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_tab_second, container, false);

        TextView tv_title = (TextView)mView.findViewById(R.id.tv_title);
        tv_count = (TextView)mView.findViewById(R.id.tv_count);
        ll_channel = (LinearLayout) mView.findViewById(R.id.ll_channel);
        tv_title.setText("庞怡文的商场");

        return mView;
    }

    // 把指定编号的商品添加到购物车
    private void addToCart(long goods_id) {
        mCount++;
        tv_count.setText("" + mCount);
        // 把购物车中的商品数量写入共享参数
        SharedUtil.getIntance(mContext).writeShared("count", "" + mCount);
        // 根据商品编号查询购物车数据库中的商品记录
        CartInfo info = mCartHelper.queryByGoodsId(goods_id);
        if (info != null) { // 购物车已存在该商品记录
            info.count++; // 该商品的数量加一
            info.update_time = DateUtil.getNowDateTime("");
            // 更新购物车数据库中的商品记录信息
            mCartHelper.update(info);
        } else { // 购物车不存在该商品记录
            info = new CartInfo();
            info.goods_id = goods_id;
            info.count = 1;
            info.update_time = DateUtil.getNowDateTime("");
            // 往购物车数据库中添加一条新的商品记录
            mCartHelper.insert(info);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取共享参数保存的购物车中的商品数量
        mCount = Integer.parseInt(SharedUtil.getIntance(mContext).readShared("count", "0"));
        tv_count.setText("" + mCount);
        //These codes are from Peter Pang.
        // 获取商品数据库的帮助器对象
        mGoodsHelper = GoodsDBHelper.getInstance(mContext, 1);
        // 打开商品数据库的读连接
        mGoodsHelper.openReadLink();
        // 获取购物车数据库的帮助器对象
        mCartHelper = CartDBHelper.getInstance(mContext, 1);
        // 打开购物车数据库的写连接
        mCartHelper.openWriteLink();
        // 模拟从网络下载商品图片
        downloadGoods();
        // 展示商品列表
        showGoods();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 关闭商品数据库的数据库连接
        mGoodsHelper.closeLink();
        // 关闭购物车数据库的数据库连接
        mCartHelper.closeLink();
    }

    private LinearLayout.LayoutParams mFullParams, mHalfParams;

    private void showGoods() {
        Log.d(TAG, "showGoods");
        // 移除线性布局ll_channel下面的所有子视图
        ll_channel.removeAllViews();
        // mFullParams这个布局参数的宽度占了一整行
        mFullParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // mHalfParams这个布局参数的宽度与其它布局平均分
        mHalfParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        // 给mHalfParams设置四周的空白距离
        mHalfParams.setMargins(Utils.dip2px(mContext, 2), Utils.dip2px(mContext, 2), Utils.dip2px(mContext, 2), Utils.dip2px(mContext, 2));
        // 创建一行的线性布局
        LinearLayout ll_row = newLinearLayout(LinearLayout.HORIZONTAL, 0);
        // 查询商品数据库中的所有商品记录
        ArrayList<GoodsInfo2> goodsArray = mGoodsHelper.query("1=1");
        Log.d(TAG, "size:" + goodsArray.size());
        int i = 0;
        for (; i < goodsArray.size(); i++) {
            final GoodsInfo2 info = goodsArray.get(i);
            // 创建一个商品项的垂直线性布局，从上到下依次列出商品标题、商品图片、商品价格
            LinearLayout ll_goods = newLinearLayout(LinearLayout.VERTICAL, 1);
            ll_goods.setBackgroundColor(Color.WHITE);
            // 添加商品标题
            TextView tv_name = new TextView(mContext);
            tv_name.setLayoutParams(mFullParams);
            tv_name.setGravity(Gravity.CENTER);
            tv_name.setText(info.name);
            tv_name.setTextColor(Color.BLACK);
            tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            ll_goods.addView(tv_name);
            // 添加商品小图
            ImageView iv_thumb = new ImageView(mContext);
            iv_thumb.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(mContext, 150)));
            iv_thumb.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv_thumb.setImageBitmap(MainApplication.getInstance().mIconMap.get(info.rowid));
            iv_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShoppingDetailActivity.class);
                    intent.putExtra("goods_id", info.rowid);
                    startActivity(intent);
                }
            });
            ll_goods.addView(iv_thumb);
            // 添加商品价格
            LinearLayout ll_bottom = newLinearLayout(LinearLayout.HORIZONTAL, 0);
            TextView tv_price = new TextView(mContext);
            tv_price.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
            tv_price.setGravity(Gravity.CENTER);
            tv_price.setText("" + (int) info.price);
            tv_price.setTextColor(Color.RED);
            tv_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            ll_bottom.addView(tv_price);
            // 添加购物车按钮
            Button btn_add = new Button(mContext);
            btn_add.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3));
            btn_add.setGravity(Gravity.CENTER);
            btn_add.setText("加入购物车");
            btn_add.setTextColor(Color.BLACK);
            btn_add.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart(info.rowid);
                    Toast.makeText(mContext,
                            "已添加一部" + info.name + "到购物车", Toast.LENGTH_SHORT).show();
                }
            });
            ll_bottom.addView(btn_add);
            ll_goods.addView(ll_bottom);
            // 把商品项添加到该行上
            ll_row.addView(ll_goods);
            // 每行放两个商品项，所以放满两个商品后，就要重新创建下一行的线性视图
            if (i % 2 == 1) {
                ll_channel.addView(ll_row);
                ll_row = newLinearLayout(LinearLayout.HORIZONTAL, 0);
            }
        }
        // 最后一行只有一个商品项，则补上一个空白格，然后把最后一行添加到ll_channel
        if (i % 2 == 0) {
            ll_row.addView(newLinearLayout(LinearLayout.VERTICAL, 1));
            ll_channel.addView(ll_row);
        }
    }

    // 创建一个线性视图的框架
    private LinearLayout newLinearLayout(int orientation, int weight) {
        LinearLayout ll_new = new LinearLayout(mContext);
        ll_new.setLayoutParams((weight == 0) ? mFullParams : mHalfParams);
        ll_new.setOrientation(orientation);
        return ll_new;
    }

    private String mFirst = "true"; // 是否首次打开

    // 模拟网络数据，初始化数据库中的商品信息
    private void downloadGoods() {
        // 获取共享参数保存的是否首次打开参数
        mFirst = SharedUtil.getIntance(mContext).readShared("first", "true");
        // 获取当前App的私有存储路径
        String path = MainApplication.getInstance().getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        if (mFirst.equals("true")) { // 如果是首次打开
            ArrayList<GoodsInfo2> goodsList = GoodsInfo2.getDefaultList();
            for (int i = 0; i < goodsList.size(); i++) {
                GoodsInfo2 info = goodsList.get(i);
                // 往商品数据库插入一条该商品的记录
                long rowid = mGoodsHelper.insert(info);
                info.rowid = rowid;
                // 往全局内存写入商品小图
                Bitmap thumb = BitmapFactory.decodeResource(getResources(), info.thumb);
                MainApplication.getInstance().mIconMap.put(rowid, thumb);
                String thumb_path = path + rowid + "_s.jpg";
                FileUtil.saveImage(thumb_path, thumb);
                info.thumb_path = thumb_path;
                // 往SD卡保存商品大图
                Bitmap pic = BitmapFactory.decodeResource(getResources(), info.pic);
                String pic_path = path + rowid + ".jpg";
                FileUtil.saveImage(pic_path, pic);
                pic.recycle();
                info.pic_path = pic_path;
                // 更新商品数据库中该商品记录的图片路径
                mGoodsHelper.update(info);
            }
        } else { // 不是首次打开
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
        // 把是否首次打开写入共享参数
        SharedUtil.getIntance(mContext).writeShared("first", "false");
    }
}
