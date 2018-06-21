package com.acode.emoji;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * user:yangtao
 * date:2018/6/151732
 * email:yangtao@bjxmail.com
 * introduce:仿IOS今日头条点赞效果
 */
public class AcodeEmojiView extends RelativeLayout {
    private Context context;
    private int mWidth;
    private int mHeight;
    //画笔
    private Paint paint;
    // 当前点的实际位置
    private float[] pos;
    // 当前点的tangent值,用于计算图片所需旋转的角度
    private float[] tan;
    //动画插值器，其实就是几种动画效果
    private Interpolator[] interpolators = new Interpolator[4];
    //图片
    private int[] icons = new int[]{R.mipmap.emoji1, R.mipmap.emoji2, R.mipmap.emoji3, R.mipmap.emoji4, R.mipmap.emoji5, R.mipmap.emoji6};
    //起始坐标X
    private float startX = -1;
    //起始坐标Y
    private float startY = -1;
    //点击次数 这个用于展示
    private int clickCount;
    //点击次数备份   这个用于真正的统计点击次数
    private int clickCountBackups;

    public AcodeEmojiView(Context context) {
        super(context);
        this.context = context;
        setWillNotDraw(false);
        init();
    }

    public AcodeEmojiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setWillNotDraw(false);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);
        paint.setTextSize(50);
        paint.setAntiAlias(true);
        pos = new float[2];
        tan = new float[2];
        // 插值器
        interpolators[0] = new AccelerateDecelerateInterpolator(); // 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
        interpolators[1] = new AccelerateInterpolator();  // 在动画开始的地方速率改变比较慢，然后开始加速
        interpolators[2] = new DecelerateInterpolator(); // 在动画开始的地方快然后慢
        interpolators[3] = new LinearInterpolator();  // 以常量速率改变
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("post", "startX:" + startX + "startY:" + startY);
        if (startX == -1 || startY == -1 || clickCount == 0) {
            return;
        }
        String msg = null;
        if (clickCount > 0 && clickCount <= 10) {
            msg = "加油~";
        } else if (clickCount > 10 && clickCount <= 20) {
            msg = "再点快点，就能看到我了~";
        } else if (clickCount > 20 && clickCount <= 30) {
            msg = "还是不够快啊~";
        } else if (clickCount > 30 && clickCount <= 40) {
            msg = "要快，快~";
        } else if (clickCount > 40 && clickCount <= 50) {
            msg = "好吧，你还算真诚~";
        } else if (clickCount > 50 && clickCount <= 60) {
            msg = "我决定告诉你个事情~";
        } else if (clickCount > 60 && clickCount < 100) {
            msg = "今晚吃鸡，7.30上线！";
        } else {
            msg = "总有傻子在坚持，后边其实啥也没有！";
        }
        float offset = 0;
        if (msg.length() > 5) {
            offset = (msg.length() - 5) * 20;
        }
        canvas.drawText(msg + clickCount, startX - offset, startY - 75, paint);
    }

    /**
     * 添加emoji
     * 每次add创建5个emoji
     */
    public void addEmoji(View view) {
        clickCount++;
        clickCountBackups++;
        startX = view.getX() - 30;
        startY = view.getY() - 30;
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = 80;
        params.height = 80;
        final ImageView iv1 = new ImageView(context);
        iv1.setLayoutParams(params);
        iv1.setImageResource(icons[new Random().nextInt(6)]);
        addView(iv1);
        final ImageView iv2 = new ImageView(context);
        iv2.setLayoutParams(params);
        iv2.setImageResource(icons[new Random().nextInt(6)]);
        addView(iv2);
        final ImageView iv3 = new ImageView(context);
        iv3.setLayoutParams(params);
        iv3.setImageResource(icons[new Random().nextInt(6)]);
        addView(iv3);
        final ImageView iv4 = new ImageView(context);
        iv4.setLayoutParams(params);
        iv4.setImageResource(icons[new Random().nextInt(6)]);
        addView(iv4);
        final ImageView iv5 = new ImageView(context);
        iv5.setLayoutParams(params);
        iv5.setImageResource(icons[new Random().nextInt(6)]);
        addView(iv5);
        // 开启动画，并且用完销毁
        AnimatorSet set = getEmojiAnimSet(iv1, iv2, iv3, iv4, iv5);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation);
                removeView(iv1);
                removeView(iv2);
                removeView(iv3);
                removeView(iv4);
                removeView(iv5);
            }
        });
    }

    /**
     * 动画集合
     * 缩放，透明渐变
     * 贝塞尔曲线的动画
     */
    private AnimatorSet getEmojiAnimSet(ImageView... ivs) {
        AnimatorSet set = new AnimatorSet();
        for (int i = 0; i < ivs.length; i++) {
            // 1.alpha动画
            ObjectAnimator alpha = ObjectAnimator.ofFloat(ivs[i], "alpha", 1f, 0);
            // 2.缩放动画
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivs[i], "scaleX", 1f, 0);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivs[i], "scaleY", 1f, 0);
            // 动画集合，playTogether同时执行这几个动画
            set.playTogether(alpha, scaleX, scaleY);
        }
        // 贝塞尔曲线动画
        ValueAnimator bzier = getBzierAnimator(ivs);
        //重新声明一个动画集合
        AnimatorSet set2 = new AnimatorSet();
        set2.play(bzier).with(set);
        set2.setDuration(2000);
        return set2;
    }

    private float randomX() {
        return new Random().nextInt(mWidth);
    }

    private float randomY() {
        return new Random().nextInt(mHeight);
    }

    private ValueAnimator getBzierAnimator(final ImageView... ivs) {
        final Path path = new Path();
        final Path path1 = new Path();
        final Path path2 = new Path();
        final Path path3 = new Path();
        final Path path4 = new Path();
        path.moveTo(startX, startY);
        path.quadTo(randomX(), randomY(), randomX(), randomY());
        path1.moveTo(startX, startY);
        path1.quadTo(randomX(), randomY(), randomX(), randomY());
        path2.moveTo(startX, startY);
        path2.quadTo(randomX(), randomY(), randomX(), randomY());
        path3.moveTo(startX, startY);
        path3.quadTo(randomX(), randomY(), randomX(), randomY());
        path4.moveTo(startX, startY);
        path4.quadTo(randomX(), randomY(), randomX(), randomY());
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(interpolators[new Random().nextInt(4)]);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
                float currentValue = (float) valueAnimator.getAnimatedValue();
                if (currentValue == 1) {
                    path.reset();
                    path1.reset();
                    path2.reset();
                    path3.reset();
                    path4.reset();
                    clickCountBackups--;
                    Log.d("post", "clickCount:" + clickCount + "  " + "   clickCountBackups: " + clickCountBackups);
                    if (clickCountBackups == 0) {
                        Log.d("post", "动画全部执行完毕");
                        clickCount = 0;
                    }
                }
                setIvXY(path, ivs[0], currentValue);
                setIvXY(path1, ivs[1], currentValue);
                setIvXY(path2, ivs[2], currentValue);
                setIvXY(path3, ivs[3], currentValue);
                setIvXY(path4, ivs[4], currentValue);
            }
        });
        return valueAnimator;
    }

    /**
     * PathMeasure  让iv跟着path走
     *
     * @param path
     * @param iv
     */
    private void setIvXY(Path path, ImageView iv, float currentValue) {
        PathMeasure measure = new PathMeasure(path, false);
        measure.getPosTan(measure.getLength() * currentValue, pos, tan);
        iv.setX(pos[0]);
        iv.setY(pos[1]);
    }
}
