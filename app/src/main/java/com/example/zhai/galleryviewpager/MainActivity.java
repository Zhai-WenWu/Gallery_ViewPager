package com.example.zhai.galleryviewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {
    //点击索引
    private int click;
    private ViewPager mViewPager;
    private RelativeLayout mViewPagerContainer;
    private ArrayList<Integer> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerContainer = (RelativeLayout) findViewById(R.id.viewPagerContainer);
        mList = new ArrayList<>();
        mList.add(R.drawable.a);
        mList.add(R.drawable.b);
        mList.add(R.drawable.c);
        mList.add(R.drawable.d);
        mList.add(R.drawable.e);
        mList.add(R.drawable.f);
        mList.add(R.drawable.g);
        mList.add(R.drawable.h);
        mList.add(R.drawable.i);
        initViewPager();
    }

    private void initViewPager() {
        //设置ViewPager的布局
      /* RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                DeviceUtils.getWindowWidth(this) * 5 / 10,
                DeviceUtils.getWindowHeight(this) * 6 / 10);*/
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mViewPagerContainer.measure(w, h);
        //int width = mViewPagerContainer.getMeasuredWidth();
        int height = mViewPagerContainer.getMeasuredHeight();
        int windowWidth = DeviceUtils.getWindowWidth(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(windowWidth / 2, height * 8 / 10);

        /**** 重要部分  ******/
        //clipChild用来定义他的子控件是否要在他应有的边界内进行绘制。 默认情况下，clipChild被设置为true。 也就是不允许进行扩展绘制。
        mViewPager.setClipChildren(false);
        //父容器一定要设置这个，否则看不出效果
        mViewPagerContainer.setClipChildren(false);
        mViewPager.setLayoutParams(params);
        //为ViewPager设置PagerAdapter
        mViewPager.setAdapter(new MyPagerAdapter());
        //设置ViewPager切换效果，即实现画廊效果
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //设置预加载数量
        mViewPager.setOffscreenPageLimit(3);
        //设置每页之间的左右间隔
        mViewPager.setPageMargin(10);

        //将容器的触摸事件反馈给ViewPager
        mViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    View view = viewOfClickOnScreen(ev);
                    if (view != null) {
                        //mViewPager.setCurrentItem(mViewPager.indexOfChild(view));
                        click = mViewPager.indexOfChild(view);
                        mViewPager.setCurrentItem(click);
                        return false;
                    }
                }
                return mViewPager.dispatchTouchEvent(ev);
            }
        });
    }

    public View viewOfClickOnScreen(MotionEvent ev) {
        int childCount = mViewPager.getChildCount();
        int[] location = new int[2];
        for (int i = 0; i < childCount; i++) {
            View v = mViewPager.getChildAt(i);
            v.getLocationOnScreen(location);
            int minX = location[0];
            int minY = mViewPager.getTop();

            int maxX = location[0] + v.getWidth();
            int maxY = mViewPager.getBottom();

            float x = ev.getX();
            float y = ev.getY();

            if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
                return v;
            }
        }
        return null;
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();//ViewPager里的个数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(mList.get(position));
            ((ViewPager) container).addView(imageView);
            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    Toast.makeText(MainActivity.this, "点点" + index, Toast.LENGTH_SHORT).show();
                }
            });
            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    /**
     * 实现的原理是，在当前显示页面放大至原来的MAX_SCALE
     * 其他页面才是正常的的大小MIN_SCALE
     */
    class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MAX_SCALE = 1.2f;
        private static final float MIN_SCALE = 0.8f;//0.85f

        @Override
        public void transformPage(View view, float position) {
            //Log.e( "transformPage: ",position+"");
            //setScaleY只支持api11以上
            if (position < -1) {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
//              Log.e("TAG", view + " , " + position + "");
                float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);
                view.setScaleX(scaleFactor);
                //每次滑动后进行微小的移动目的是为了防止在三星的某些手机上出现两边的页面为显示的情况
                /*if (position > 0) {
                    view.setTranslationX(-scaleFactor * 2);
                } else if (position < 0) {
                    view.setTranslationX(scaleFactor * 2);
                }*/
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]

                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);

            }
        }

    }
}
