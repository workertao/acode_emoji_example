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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

/**
 * user:yangtao
 * date:2018/6/151732
 * email:yangtao@bjxmail.com
 * introduce:仿IOS今日头条点赞效果
 */
public class AcodeEmojiView extends RelativeLayout {
    private Context context;
    private Paint paint;
    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private int mWidth;
    private int mHeight;
    private int[] icons = new int[]{R.mipmap.emoji1, R.mipmap.emoji2, R.mipmap.emoji3, R.mipmap.emoji4, R.mipmap.emoji5, R.mipmap.emoji6};
    private int resId = R.layout.emoji_view;
    private RelativeLayout rl_emoji_group;
    //起始坐标X
    private float startX;
    //起始坐标Y
    private float startY;

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
        View view = LayoutInflater.from(context).inflate(resId, this, true);
        rl_emoji_group = view.findViewById(R.id.rl_emoji_group);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        pos = new float[2];
        tan = new float[2];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /**
     * 添加emoji
     * 每次add创建5个emoji
     */
    public void addEmoji(View view) {
        startX = view.getX() - 30;
        startY = view.getY() - 30;
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = 50;
        params.height = 50;
        final ImageView iv1 = new ImageView(context);
        iv1.setLayoutParams(params);
        iv1.setImageResource(icons[new Random().nextInt(6)]);
        rl_emoji_group.addView(iv1);
        final ImageView iv2 = new ImageView(context);
        iv2.setLayoutParams(params);
        iv2.setImageResource(icons[new Random().nextInt(6)]);
        rl_emoji_group.addView(iv2);
        final ImageView iv3 = new ImageView(context);
        iv3.setLayoutParams(params);
        iv3.setImageResource(icons[new Random().nextInt(6)]);
        rl_emoji_group.addView(iv3);
        final ImageView iv4 = new ImageView(context);
        iv4.setLayoutParams(params);
        iv4.setImageResource(icons[new Random().nextInt(6)]);
        rl_emoji_group.addView(iv4);
        final ImageView iv5 = new ImageView(context);
        iv5.setLayoutParams(params);
        iv5.setImageResource(icons[new Random().nextInt(6)]);
        rl_emoji_group.addView(iv5);
        // 开启动画，并且用完销毁
        AnimatorSet set = getEmojiAnimSet(iv1, iv2, iv3, iv4, iv5);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation);
                rl_emoji_group.removeView(iv1);
                rl_emoji_group.removeView(iv2);
                rl_emoji_group.removeView(iv3);
                rl_emoji_group.removeView(iv4);
                rl_emoji_group.removeView(iv5);
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
        set2.setDuration(3000);
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
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValue = (float) valueAnimator.getAnimatedValue();
                if (currentValue == 1){
                    path.reset();
                    path1.reset();
                    path2.reset();
                    path3.reset();
                    path4.reset();
                }
                setIvXY(path, ivs[0]);
                setIvXY(path1, ivs[1]);
                setIvXY(path2, ivs[2]);
                setIvXY(path3, ivs[3]);
                setIvXY(path4, ivs[4]);
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
    private void setIvXY(Path path, ImageView iv) {
        PathMeasure measure = new PathMeasure(path, false);
        measure.getPosTan(measure.getLength() * currentValue, pos, tan);
        iv.setX(pos[0]);
        iv.setY(pos[1]);
    }
}
