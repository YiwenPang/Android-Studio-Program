package com.example.a20201108.bean;

public class CartInfo {
    public long rowid;
    public int sn;
    public long goods_id;
    public int count;
    public String update_time;

    // adapter新增
    public GoodsInfo goods;

    public CartInfo() {
        rowid = 0L;
        sn = 0;
        goods_id = 0L;
        count = 0;
        update_time = "";
        goods = new GoodsInfo();
    }
}
