package com.example.a20201211finalhomework.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.a20201211finalhomework.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.a20201211finalhomework.adapter.RecyclerStaggeredAdapter;
import com.example.a20201211finalhomework.constant.ImageList;
import com.example.a20201211finalhomework.util.StatusBarUtil;
import com.example.a20201211finalhomework.util.Utils;
import com.example.a20201211finalhomework.widget.BannerPager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.example.a20201211finalhomework.adapter.RecyclerGridAdapter;
import com.example.a20201211finalhomework.bean.GoodsInfo;
import com.example.a20201211finalhomework.widget.SpacesItemDecoration;

public class TabFirstFragment extends Fragment implements BannerPager.BannerClickListener{
    private static final String TAG = "TabFirstFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private Button btn_top;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_tab_first, container, false);

        StatusBarUtil.fullScreen((Activity) mContext);
        // 从布局文件中获取名叫banner_top的横幅轮播条
        BannerPager banner = (BannerPager)mView.findViewById(R.id.banner_top);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
        params.height = (int) (Utils.getScreenWidth(mContext) * 250f / 640f);
        // 设置横幅轮播条的布局参数
        banner.setLayoutParams(params);
        // 设置横幅轮播条的广告图片队列
        banner.setImage(ImageList.getDefault());
        // 设置横幅轮播条的广告点击监听器
        banner.setOnBannerListener(this);
        // 开始广告图片的轮播滚动
        banner.start();

        // 从布局文件中获取名叫rv_staggered的循环视图
        RecyclerView rv_staggered = (RecyclerView)mView.findViewById(R.id.rv_staggered);
        // 创建一个垂直方向的瀑布流布局管理器
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                3, RecyclerView.VERTICAL);
        // 设置循环视图的布局管理器
        rv_staggered.setLayoutManager(manager);
        // 构建一个服装列表的瀑布流适配器
        RecyclerStaggeredAdapter adapter = new RecyclerStaggeredAdapter(mContext, GoodsInfo.getDefaultStag());
        // 设置瀑布流列表的点击监听器
        adapter.setOnItemClickListener(adapter);
        // 设置瀑布流列表的长按监听器
        adapter.setOnItemLongClickListener(adapter);
        // 给rv_staggered设置服装瀑布流适配器
        rv_staggered.setAdapter(adapter);
        // 设置rv_staggered的默认动画效果
        rv_staggered.setItemAnimator(new DefaultItemAnimator());
        // 给rv_staggered添加列表项之间的空白装饰
        rv_staggered.addItemDecoration(new SpacesItemDecoration(3));

        // 从布局文件中获取名叫rv_grid的循环视图
        RecyclerView rv_grid = (RecyclerView)mView.findViewById(R.id.rv_grid);
        // 创建一个垂直方向的网格布局管理器
        GridLayoutManager manager2 = new GridLayoutManager(mContext, 5);
        // 设置循环视图的布局管理器
        rv_grid.setLayoutManager(manager2);
        // 构建一个市场列表的网格适配器
        RecyclerGridAdapter adapter2 = new RecyclerGridAdapter(mContext, GoodsInfo.getDefaultGrid());
        // 设置网格列表的点击监听器
        adapter.setOnItemClickListener(adapter2);
        // 设置网格列表的长按监听器
        adapter.setOnItemLongClickListener(adapter2);
        //These codes are from Peter Pang.
        // 给rv_grid设置市场网格适配器
        rv_grid.setAdapter(adapter2);
        // 设置rv_grid的默认动画效果
        rv_grid.setItemAnimator(new DefaultItemAnimator());
        // 给rv_grid添加列表项之间的空白装饰
        rv_grid.addItemDecoration(new SpacesItemDecoration(1));

        return mView;
    }

    // 一旦点击了广告图，就回调监听器的onBannerClick方法
    public void onBannerClick(int position) {
        String desc = String.format("您点击了第%d张图片", position + 1);
        Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
    }
}
