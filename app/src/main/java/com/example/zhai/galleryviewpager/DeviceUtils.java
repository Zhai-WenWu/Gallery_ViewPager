package com.example.zhai.galleryviewpager;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by Forrest on 16/5/4.
 */
public class DeviceUtils {
    public static final int DEVICE_SCALE = 640;
    /**
     * 获取屏幕宽和高
     * @param context
     * @return
     */
    public static int[] getScreenHW(Context context){
        int[] hw = new int[3];
        try {
            WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            hw[0] = dm.widthPixels;//屏幕宽带(像素)
            hw[1] = dm.heightPixels;//屏幕高度(像素)
            hw[2] = dm.densityDpi;//屏幕密度(120/160/240)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hw;
    }
    /**
     * 手机屏幕宽度
     * @param ctx
     * @return
     */
    public static int getWindowWidth(Context ctx){
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.widthPixels ;
    }

    /**
     * 手机屏幕高度
     * @param ctx
     * @return
     */
    public static int getWindowHeight(Context ctx){

        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.heightPixels ;
    }

}
