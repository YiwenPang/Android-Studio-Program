package com.example.a20201211finalhomework.bean;

import com.example.a20201211finalhomework.R;

import java.util.ArrayList;

public class GoodsInfo2 {
    public long rowid; // 行号
    public int sn; // 序号
    public String name; // 名称
    public String desc; // 描述
    public float price; // 价格
    public String thumb_path; // 小图的保存路径
    public String pic_path; // 大图的保存路径
    public int thumb; // 小图的资源编号
    public int pic; // 大图的资源编号

    public GoodsInfo2() {
        rowid = 0L;
        sn = 0;
        name = "";
        desc = "";
        price = 0;
        thumb_path = "";
        pic_path = "";
        thumb = 0;
        pic = 0;
    }

    // 声明一个手机商品的名称数组
    private static String[] mNameArray = {
            "iPhone12", "iPhone12 Pro", "小米10", "小米10 青春版", "小米10 至尊纪念版", "Redmi K30 Pro", "华为 Mate40 Pro", "华为 P40 Pro"
    };
    // 声明一个手机商品的描述数组
    private static String[] mDescArray = {
            "Apple iPhone 12 256GB 移动联通电信5G手机",
            "Apple iPhone 12 Pro 512GB 移动联通电信5G手机",
            "小米 10 全网通 12GB+256GB",
            "小米 10 青春版 全网通 8GB+256GB",
            "小米 10 至尊纪念版 全网通 16GB+256GB",
            "Redmi K30 Pro 全网通 8GB+256GB",
            "华为 Mate40 Pro 全网通 8GB+512GB",
            "华为 P40 Pro 全网通 8GB+512GB"
    };
    // 声明一个手机商品的价格数组
    private static float[] mPriceArray = {7599, 11099, 4099, 2499, 6999, 3099, 7999, 7388};
    // 声明一个手机商品的小图数组
    private static int[] mThumbArray = {
            R.drawable.iphone12_s, R.drawable.iphone12pro_s, R.drawable.mi10_s, R.drawable.mi10qingchunban_s, R.drawable.mi10zhizunjinianban_s, R.drawable.redmik30pro_s, R.drawable.huaweimate40pro_s, R.drawable.huaweip40pro_s
    };
    // 声明一个手机商品的大图数组
    private static int[] mPicArray = {
            R.drawable.iphone12, R.drawable.iphone12pro, R.drawable.mi10, R.drawable.mi10qingchunban, R.drawable.mi10zhizunjinianban, R.drawable.redmik30pro, R.drawable.huaweimate40pro, R.drawable.huaweip40pro
    };

    // 获取默认的手机信息列表
    public static ArrayList<GoodsInfo2> getDefaultList() {
        ArrayList<GoodsInfo2> goodsList = new ArrayList<GoodsInfo2>();
        for (int i = 0; i < mNameArray.length; i++) {
            GoodsInfo2 info = new GoodsInfo2();
            info.name = mNameArray[i];
            info.desc = mDescArray[i];
            info.price = mPriceArray[i];
            info.thumb = mThumbArray[i];
            info.pic = mPicArray[i];
            goodsList.add(info);
        }
        return goodsList;
    }
}//These codes are from Peter Pang.