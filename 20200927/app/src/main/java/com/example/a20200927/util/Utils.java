package com.example.a20200927.util;

import android.content.Context;

public class Utils {
    //根据手机的分辨率从dp的单位转换为px
    public static  int dip2px(Context context, float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);//向上取整
    }

    //从px转换成dp
    public static int px2dip(Context context,float pxValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue*scale+0.5f);//向上取整
    }
}
