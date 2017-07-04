public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private RelativeLayout mViewPagerContainer;
    private static int TOTAL_COUNT = 10;
    private ArrayList<Integer> mList;
    private float downX;
    private float downY;
    //默认距离
    private final static float DISTANCE = 10;

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
        mViewPager.setPageMargin(0);

        //将容器的触摸事件反馈给ViewPager
        mViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
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
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击两侧跳转
                    int currentItem = mViewPager.getCurrentItem();
                    if (currentItem == position){
                        Toast.makeText(MainActivity.this,"点点点",Toast.LENGTH_SHORT).show();
                    }
                    mViewPager.setCurrentItem(position);
                   // Log.e("imageView index = ", "" + position);
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
            if (position < -1) {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
                // Log.e("TAG", view + " , " + position + "");
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