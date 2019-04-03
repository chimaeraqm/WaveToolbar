package com.crazydwarf.chimaeraqm.wavetoolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WaveToolbar extends Toolbar
{
    private Context mContext;
    private ImageView mImageView_AppIcon;
    private ImageView mImageView_MenuIcon;
    private ImageView mImageView_BackIcon;
    private TextView mTextView_Title;

    /**
     * related setting params
     */
    private Drawable mBackground;

    private String mTitle;
    private int mTitleSize;
    private int mTitleColor;

    private Drawable mAppIcon;
    private boolean mAppIconShow;
    private int mAppIconSize;
    private AppIconClickListener appIconClickListener;

    private Drawable mBackIcon;
    private int mBackIconSize;
    private BackIconClickListener backIconClickListener;

    private Drawable mMenuIcon;
    private boolean mMenuIconShow;
    private int mMenuIconSize;
    private MenuIconClickListener menuIconClickListener;

    private boolean mBackgroundBlur;
    private float mBackgroundBlurRatio;

    private Paint mAppIconbgPaint = new Paint();
    private Paint mMenuIconbgPaint = new Paint();
    private Paint mBackIconbgPaint = new Paint();

    private Path firstPath;
    private Path secondPath;
    private Paint backgroundPaint;
    private Paint firstWavePaint;
    private int firstWaveColor;
    private Paint secondWavePaint;
    private int secondWaveColor;

    private Canvas bitmapCanvas;
    private BitmapShader bitmapShader;

    private int mWave_Height;
    private int mWave_Width;
    private int mWave_Amlitude;
    private float mWave_Speed;
    private float wave_Angle;

    private WaveToolbarThread WaveToolbarThread;
    private WaveToolbarHandler WaveToolbarHandler;
    private boolean mLoop = true;
    private static final int MESSAGE_START_WAVE = 2001;

    public WaveToolbar(Context context) {
        this(context,null);
    }

    public WaveToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveToolbar);
        mBackground = typedArray.getDrawable(R.styleable.WaveToolbar_viewBackground);

        mTitle = typedArray.getString(R.styleable.WaveToolbar_title);
        mTitleSize = typedArray.getDimensionPixelSize(R.styleable.WaveToolbar_titleSize,0);
        mTitleColor = typedArray.getColor(R.styleable.WaveToolbar_titleColor, Color.WHITE);

        mAppIcon = typedArray.getDrawable(R.styleable.WaveToolbar_appIcon);
        mAppIconShow = typedArray.getBoolean(R.styleable.WaveToolbar_appIconShow,true);
        mAppIconSize = typedArray.getDimensionPixelSize(R.styleable.WaveToolbar_appIconSize,20);

        mBackIcon = typedArray.getDrawable(R.styleable.WaveToolbar_backIcon);
        mBackIconSize = typedArray.getDimensionPixelSize(R.styleable.WaveToolbar_backIconSize,20);

        mMenuIcon = typedArray.getDrawable(R.styleable.WaveToolbar_menuIcon);
        mMenuIconShow = typedArray.getBoolean(R.styleable.WaveToolbar_menuIconShow,true);
        mMenuIconSize = typedArray.getDimensionPixelSize(R.styleable.WaveToolbar_mentIconSize,20);

        mBackgroundBlur = typedArray.getBoolean(R.styleable.WaveToolbar_backgroundBlur,false);
        mBackgroundBlurRatio = typedArray.getFloat(R.styleable.WaveToolbar_backgroundBlurRatio,0f);

        mWave_Amlitude = typedArray.getInteger(R.styleable.WaveToolbar_wave_Amlitude,0);
        mWave_Speed = typedArray.getFloat(R.styleable.WaveToolbar_wave_Speed,0);

        firstWaveColor = typedArray.getColor(R.styleable.WaveToolbar_firstWaveColor, ContextCompat.getColor(mContext, R.color.colorBlue));
        secondWaveColor = typedArray.getColor(R.styleable.WaveToolbar_secondWaveColor, ContextCompat.getColor(mContext, R.color.colorTransBlue));

        typedArray.recycle();
        initView();
        paintSetup();
    }

    private void initView()
    {
        LayoutInflater.from(mContext).inflate(R.layout.wavetoolbar,this,true);

        setBackground(mBackground);

        mTextView_Title = findViewById(R.id.tv_title);
        mTextView_Title.setText(mTitle);
        mTextView_Title.setTextSize(TypedValue.COMPLEX_UNIT_PX ,mTitleSize);
        mTextView_Title.setTextColor(mTitleColor);

        mImageView_AppIcon = findViewById(R.id.im_icon);
        if(mAppIconShow)
        {
            mImageView_AppIcon.setVisibility(VISIBLE);
            mImageView_AppIcon.setBackground(mAppIcon);
            ViewGroup.LayoutParams layoutParams = mImageView_AppIcon.getLayoutParams();
            layoutParams.height = mAppIconSize;
            layoutParams.width = mAppIconSize;
            mImageView_AppIcon.setLayoutParams(layoutParams);
            mImageView_AppIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    appIconClickListener.OnClick();
                }
            });
        }
        else
        {
            mImageView_AppIcon.setVisibility(INVISIBLE);
        }

        mImageView_BackIcon = findViewById(R.id.im_back);
        mImageView_BackIcon.setBackground(mBackIcon);
        ViewGroup.LayoutParams backicon_layoutParams = mImageView_BackIcon.getLayoutParams();
        backicon_layoutParams.height = mBackIconSize;
        backicon_layoutParams.width = mBackIconSize;
        mImageView_BackIcon.setLayoutParams(backicon_layoutParams);
        mImageView_BackIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                backIconClickListener.OnClick();
            }
        });

        mImageView_MenuIcon = findViewById(R.id.im_menu);
        if(mMenuIconShow)
        {
            mImageView_MenuIcon.setVisibility(VISIBLE);
            mImageView_MenuIcon.setBackground(mMenuIcon);
            ViewGroup.LayoutParams menuicon_layoutParams = mImageView_MenuIcon.getLayoutParams();
            menuicon_layoutParams.height = mMenuIconSize;
            menuicon_layoutParams.width = mMenuIconSize;
            mImageView_MenuIcon.setLayoutParams(menuicon_layoutParams);
            mImageView_MenuIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    menuIconClickListener.OnClick(view);
                }
            });
        }
        else
        {
            mImageView_MenuIcon.setVisibility(INVISIBLE);
        }

        firstPath = new Path();
        secondPath = new Path();

        wave_Angle = 0;

        WaveToolbarThread = new WaveToolbarThread();
        WaveToolbarHandler = new WaveToolbarHandler();
        WaveToolbarThread.start();
    }

    private void paintSetup()
    {
        mAppIconbgPaint.setAntiAlias(true);
        mAppIconbgPaint.setStyle(Paint.Style.FILL);
        mAppIconbgPaint.setAntiAlias(true);
        mAppIconbgPaint.setColor(Color.BLUE);

        mMenuIconbgPaint.setAntiAlias(true);
        mMenuIconbgPaint.setStyle(Paint.Style.FILL);
        mMenuIconbgPaint.setAntiAlias(true);
        mMenuIconbgPaint.setColor(Color.WHITE);

        mBackIconbgPaint.setAntiAlias(true);
        mBackIconbgPaint.setStyle(Paint.Style.FILL);
        mBackIconbgPaint.setAntiAlias(true);
        mBackIconbgPaint.setColor(Color.WHITE);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);

        firstWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstWavePaint.setAntiAlias(true);
        firstWavePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        firstWavePaint.setColor(firstWaveColor);
        firstWavePaint.setStyle(Paint.Style.FILL);
        firstWavePaint.setStrokeWidth(10);

        secondWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondWavePaint.setAntiAlias(true);
        secondWavePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        secondWavePaint.setStyle(Paint.Style.FILL);
        secondWavePaint.setColor(secondWaveColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        backgroundPaint.setShader(bitmapShader);

        mWave_Width = getWidth();
        mWave_Height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        firstPath.reset();
        secondPath.reset();

        firstPath.moveTo(0,0);
        secondPath.moveTo(0,0);

        int index = 0;
        while (index <= mWave_Width)
        {
            float endY = (float) (Math.sin((float) index / (float) mWave_Width * 2f * Math.PI +wave_Angle) * (float) mWave_Amlitude + mWave_Height - mWave_Amlitude);
            firstPath.lineTo(index, endY);
            float endY1 = (float) (Math.sin((float) index / (float) mWave_Width * 2f * Math.PI +wave_Angle+90) * (float) mWave_Amlitude + mWave_Height - mWave_Amlitude);
            secondPath.lineTo(index, endY1);
            index++;
        }

        firstPath.lineTo(index-1,0);
        firstPath.close();

        secondPath.lineTo(index-1,0);
        secondPath.close();

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        bitmapCanvas.drawPaint(paint);

        bitmapCanvas.drawPath(firstPath, firstWavePaint);
        bitmapCanvas.drawPath(secondPath, secondWavePaint);
        canvas.drawRect(0,0,getWidth(),getHeight(),backgroundPaint);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        float appIconHeight = mImageView_AppIcon.getHeight();
//        float appIconWidth = mImageView_AppIcon.getWidth();
//        RectF appIconRect = new RectF();
//        appIconRect = calculateAppIconBounds();
//        float appIconRadius = Math.max(appIconHeight/2.0f,appIconWidth/2.0f);
//        canvas.drawCircle(appIconRect.centerX(), appIconRect.centerY(), appIconRadius, mAppIconbgPaint);
        super.dispatchDraw(canvas);
    }

    private RectF calculateAppIconBounds() {
        int availableWidth  = mImageView_AppIcon.getWidth() - mImageView_AppIcon.getPaddingLeft() - mImageView_AppIcon.getPaddingRight();
        int availableHeight = mImageView_AppIcon.getHeight() - mImageView_AppIcon.getPaddingTop() - mImageView_AppIcon.getPaddingBottom();
        int sideLength = Math.min(availableWidth, availableHeight);

        float left = mImageView_AppIcon.getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = mImageView_AppIcon.getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    public void setmBackground(Drawable mBackground) {
        this.mBackground = mBackground;
    }

    public void setmTextView_Title(TextView mTextView_Title) {
        this.mTextView_Title = mTextView_Title;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmTitleSize(int mTitleSize) {
        this.mTitleSize = mTitleSize;
    }

    public void setmTitleColor(int mTitleColor) {
        this.mTitleColor = mTitleColor;
    }

    public void setmAppIcon(Drawable mAppIcon) {
        this.mAppIcon = mAppIcon;
    }

    public void setmAppIconShow(boolean mAppIconShow) {
        this.mAppIconShow = mAppIconShow;
    }

    public void setmAppIconSize(int mAppIconSize) {
        this.mAppIconSize = mAppIconSize;
    }

    public void setmBackIcon(Drawable mBackIcon) {
        this.mBackIcon = mBackIcon;
    }

    public void setmBackIconSize(int mBackIconSize) {
        this.mBackIconSize = mBackIconSize;
    }

    public void setmMenuIcon(Drawable mMenuIcon) {
        this.mMenuIcon = mMenuIcon;
    }

    public void setmMenuIconSize(int mMenuIconSize) {
        this.mMenuIconSize = mMenuIconSize;
    }

    public void setmBackgroundBlur(boolean mBackgroundBlur) {
        this.mBackgroundBlur = mBackgroundBlur;
    }

    public void setmBackgroundBlurRatio(float mBackgroundBlurRatio) {
        this.mBackgroundBlurRatio = mBackgroundBlurRatio;
    }

    /**
     * click listener interface for appicon, menuicon, backicon respectivly
     */
    public void setBackIconClickListener(BackIconClickListener backIconClickListener) {
        this.backIconClickListener = backIconClickListener;
    }

    public void setAppIconClickListener(AppIconClickListener appIconClickListener) {
        this.appIconClickListener = appIconClickListener;
    }

    public void setMenuIconClickListener(MenuIconClickListener menuIconClickListener) {
        this.menuIconClickListener = menuIconClickListener;
    }

    public interface BackIconClickListener{
        void OnClick();
    }

    public interface MenuIconClickListener{
        void OnClick(View view);
    }

    public interface AppIconClickListener{
        void OnClick();
    }

    public float getWave_Angle() {
        return wave_Angle;
    }

    public void setWave_Angle(float wave_Angle) {
        this.wave_Angle = wave_Angle;
        invalidate();
    }

    private final class WaveToolbarThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (mLoop) {
                wave_Angle = wave_Angle - 1 * mWave_Speed;
                if (wave_Angle == 0) {
                    wave_Angle = 360;
                }
                WaveToolbarHandler.sendEmptyMessage(MESSAGE_START_WAVE);
                SystemClock.sleep(10);
            }
        }
    }

    private final class WaveToolbarHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (MESSAGE_START_WAVE == msg.what) {
                invalidate();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mLoop = false;
        if (WaveToolbarThread != null) {
            WaveToolbarThread.interrupt();
            WaveToolbarThread = null;
        }
        if (WaveToolbarHandler != null) {
            WaveToolbarHandler.removeMessages(MESSAGE_START_WAVE);
            WaveToolbarHandler = null;
        }
    }}
