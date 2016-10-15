package com.linyuzai.requestbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class RequestIcon extends View {

    public static final String TAG = RequestIcon.class.getSimpleName();

    /**
     * max speed multiplier
     * <p>
     * 最大速度乘数
     */
    public static final float MAX_MULTIPLIER = 2.0f;
    /**
     * min speed multiplier
     * <p>
     * 最小速度乘数
     */
    public static final float MIN_MULTIPLIER = 0.5f;
    /**
     * max circle progress
     * <p>
     * 最大圆圈进度/百分比
     */
    public static final float CIRCLE_PROGRESS_MAX = 100f;
    /**
     * first line progress
     * <p>
     * 第一条线所占最大进度/百分比
     */
    public static final float FIRST_LINE_PROGRESS = 33.3f;
    /**
     * second line progress
     * <p>
     * 第二条线所占最大进度/百分比
     */
    public static final float SECOND_LINE_PROGRESS = 66.6f;
    /**
     * icon change listener
     * <p>
     * 图标变化监听器
     */
    private OnIconChangedListener listener;
    /**
     * icon color,white default
     * <p>
     * 图标颜色，默认白色
     */
    private int iconColor = Color.WHITE;
    /**
     * icon size,the paint width,5px default
     * <p>
     * 图标尺寸，及画笔宽度，默认5px
     */
    private int iconSize = 5;
    /**
     * icon style,the style of when do tick,tick when circle start default
     * <p>
     * 图标样式，什么时候打钩，默认开始画圆就开始打钩
     */
    private Style iconStyle = Style.TICK_START_CIRCLE;
    /**
     * the x index of the center of the circle
     * <p>
     * 圆心的x坐标
     */
    private int centerX;
    /**
     * the y index of the center of the circle
     * <p>
     * 圆心的y坐标
     */
    private int centerY;
    /**
     * the radius of the circle
     * <p>
     * 圆的半径
     */
    private int radius;
    /**
     * the first line of tick
     * <p>
     * 打钩的第一条线
     */
    private Line firstLine;
    /**
     * the second line of tick
     * <p>
     * 打钩的第二条线
     */
    private Line secondLine;
    /**
     * the x of line
     * <p>
     * 线段的x坐标
     */
    private float lineX;
    /**
     * the progress of line
     * <p>
     * 线段的进度/百分比
     */
    private float lineProgress;
    /**
     * the increase of progress
     * <p>
     * 线段进度/百分比的增量
     */
    private float lineProgressIncrease;
    /**
     * the progress of circle
     * <p>
     * 圆的进度/百分比
     */
    private float progress;
    /**
     * the first progress
     * <p>
     * 第一个圆弧的进度/百分比
     */
    private float firstProgress;
    /**
     * the second progress
     * <p>
     * 第二个圆弧的进度/百分比
     */
    private float secondProgress;
    /**
     * the progress of half circle
     * <p>
     * 经过半个圆的进度/百分比
     */
    private float halfPathProgress = -1f;
    /**
     * the speed multiplier,1.8 default
     * <p>
     * 速度的乘数，默认1.8倍
     */
    private float speedMultiplier = 1.8f;
    /**
     * the interval of progress
     * <p>
     * 每一圈之间的间隔时间
     */
    private int interval;
    /**
     * if circle and tick
     * <p>
     * 是否画圈和打钩
     */
    private boolean progressToCircle;
    /**
     * the state of icon,idle default
     * <p>
     * 图标状态，默认空闲状态
     */
    private State state = State.IDLE;

    private Paint paint;
    private RectF rectF;

    public RequestIcon(Context context) {
        super(context);
        init(context, null);
    }

    public RequestIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RequestIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RequestIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RequestIcon);
            iconColor = a.getColor(R.styleable.RequestIcon_icon_color, Color.WHITE);
            iconSize = a.getDimensionPixelOffset(R.styleable.RequestIcon_icon_size, 8);
            int ordinal = a.getInt(R.styleable.RequestIcon_icon_style, Style.TICK_START_CIRCLE.ordinal());
            iconStyle = Style.values()[ordinal];
            speedMultiplier = a.getFloat(R.styleable.RequestIcon_speed_multiplier, 1.8f);
            if (speedMultiplier < MIN_MULTIPLIER)
                speedMultiplier = MIN_MULTIPLIER;
            else if (speedMultiplier > MAX_MULTIPLIER)
                speedMultiplier = MAX_MULTIPLIER;
            a.recycle();
        }
        initPaint();
        setInterval();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(iconColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(iconSize);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void setRectF(int width, int height) {
        centerX = width / 2;
        centerY = height / 2;
        radius = Math.min(centerX - iconSize, centerY - iconSize);
        rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        firstLine = new Line(new Point(width * 0.28f, height * 0.53f), new Point(width * 0.43f, height * 0.68f));
        secondLine = new Line(new Point(width * 0.43f, height * 0.68f), new Point(width * 0.73f, height * 0.38f));

        lineX = firstLine.startPoint.x;
        lineProgressIncrease = width * 0.45f / 100f;
    }

    private void setInterval() {
        interval = (int) (400 / speedMultiplier);
    }

    private void reset() {
        state = State.IDLE;
        progress = 0f;
        firstProgress = 0f;
        secondProgress = 0f;
        progressToCircle = false;
        halfPathProgress = -1f;
        lineX = firstLine.startPoint.x;
        lineProgress = 0f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setRectF(w, h);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (state) {
            case IDLE:
                break;
            case PROGRESS:
                float startAngle = 360f * secondProgress * speedMultiplier / CIRCLE_PROGRESS_MAX;
                float sweepAngle = 360f * speedMultiplier * (firstProgress - secondProgress) / CIRCLE_PROGRESS_MAX;
                if (startAngle > 360f)
                    startAngle = 360f;
                if (sweepAngle < 0f)
                    sweepAngle = 0f;
                canvas.drawArc(rectF, -90f + startAngle, sweepAngle, false, paint);
                progress += speedMultiplier;
                firstProgress += 0.02f * (CIRCLE_PROGRESS_MAX - progress);
                secondProgress += 0.02 * progress;
                if (progress > CIRCLE_PROGRESS_MAX + 1f) {
                    progress = 0f;
                    firstProgress = 0f;
                    secondProgress = 0f;
                    if (progressToCircle) {
                        switch (iconStyle) {
                            case TICK_START_CIRCLE:
                                state = State.TICK_START_CIRCLE;
                                break;
                            case TICK_HALF_CIRCLE:
                                state = State.TICK_HALF_CIRCLE;
                                break;
                            case TICK_END_CIRCLE:
                                state = State.TICK_END_CIRCLE;
                                break;
                            default:
                                state = State.TICK_END_CIRCLE;
                                break;
                        }
                        if (listener != null) {
                            if (listener.onProgressFinished(this))
                                invalidate();
                            else
                                reset();
                        } else
                            invalidate();
                    } else
                        postInvalidateDelayed(interval);
                } else
                    invalidate();
                break;
            case TICK_START_CIRCLE:
                float startCircleAngle = 360f * speedMultiplier * firstProgress / CIRCLE_PROGRESS_MAX;
                if (startCircleAngle < 0f)
                    startCircleAngle = 0f;
                canvas.drawArc(rectF, -90f, startCircleAngle, false, paint);
                if (lineProgress > FIRST_LINE_PROGRESS) {
                    canvas.drawLine(firstLine.startPoint.x, firstLine.startPoint.y, firstLine.endPoint.x, firstLine.endPoint.y, paint);
                    canvas.drawLine(secondLine.startPoint.x, secondLine.startPoint.y, lineX, secondLine.getY(lineX), paint);
                } else {
                    canvas.drawLine(firstLine.startPoint.x, firstLine.startPoint.y, lineX, firstLine.getY(lineX), paint);
                }
                progress += speedMultiplier;
                firstProgress += 0.02f * (CIRCLE_PROGRESS_MAX - progress);

                lineProgress += speedMultiplier;
                lineX += lineProgressIncrease * speedMultiplier;
                if (progress > CIRCLE_PROGRESS_MAX + 1f) {
                    reset();
                    if (listener != null)
                        listener.onTickFinished(this);
                } else
                    invalidate();
                break;
            case TICK_HALF_CIRCLE:
                float halfCircleAngle = 360f * speedMultiplier * firstProgress / CIRCLE_PROGRESS_MAX;
                if (halfCircleAngle < 0f)
                    halfCircleAngle = 0f;
                canvas.drawArc(rectF, -90f, halfCircleAngle, false, paint);
                progress += speedMultiplier;
                firstProgress += 0.02f * (CIRCLE_PROGRESS_MAX - progress);
                if (speedMultiplier * firstProgress >= CIRCLE_PROGRESS_MAX / 2f) {
                    if (halfPathProgress < 0f)
                        halfPathProgress = progress / speedMultiplier;
                    if (lineProgress > FIRST_LINE_PROGRESS) {
                        canvas.drawLine(firstLine.startPoint.x, firstLine.startPoint.y, firstLine.endPoint.x, firstLine.endPoint.y, paint);
                        if (lineProgress > FIRST_LINE_PROGRESS + SECOND_LINE_PROGRESS + 1f)
                            canvas.drawLine(secondLine.startPoint.x, secondLine.startPoint.y, secondLine.endPoint.x, secondLine.endPoint.y, paint);
                        else
                            canvas.drawLine(secondLine.startPoint.x, secondLine.startPoint.y, lineX, secondLine.getY(lineX), paint);
                    } else {
                        canvas.drawLine(firstLine.startPoint.x, firstLine.startPoint.y, lineX, firstLine.getY(lineX), paint);
                    }
                    float multiplier = 125f / (100 - halfPathProgress);
                    lineProgress += multiplier * speedMultiplier;
                    lineX += multiplier * lineProgressIncrease * speedMultiplier;
                    if (progress > CIRCLE_PROGRESS_MAX + 1f) {
                        reset();
                        if (listener != null)
                            listener.onTickFinished(this);
                    } else
                        invalidate();
                } else
                    invalidate();
                break;
            case TICK_END_CIRCLE:
                if (progress > CIRCLE_PROGRESS_MAX + 1f) {
                    canvas.drawArc(rectF, -90f, 360f, false, paint);
                    if (lineProgress > FIRST_LINE_PROGRESS) {
                        canvas.drawLine(firstLine.startPoint.x, firstLine.startPoint.y, firstLine.endPoint.x, firstLine.endPoint.y, paint);
                        canvas.drawLine(secondLine.startPoint.x, secondLine.startPoint.y, lineX, secondLine.getY(lineX), paint);
                    } else {
                        canvas.drawLine(firstLine.startPoint.x, firstLine.startPoint.y, lineX, firstLine.getY(lineX), paint);
                    }
                    lineProgress += 3f * speedMultiplier;
                    lineX += 3f * lineProgressIncrease * speedMultiplier;
                    if (lineProgress > FIRST_LINE_PROGRESS + SECOND_LINE_PROGRESS + 1f) {
                        reset();
                        if (listener != null)
                            listener.onTickFinished(this);
                    } else
                        invalidate();
                } else {
                    float endCircleAngle = 360f * speedMultiplier * firstProgress / CIRCLE_PROGRESS_MAX;
                    if (endCircleAngle < 0f)
                        endCircleAngle = 0f;
                    canvas.drawArc(rectF, -90f, endCircleAngle, false, paint);
                    progress += speedMultiplier;
                    firstProgress += 0.02f * (CIRCLE_PROGRESS_MAX - progress);
                    invalidate();
                }
                break;
            default:
                break;
        }
    }

    public Style getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(Style iconStyle) {
        this.iconStyle = iconStyle;
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
        paint.setColor(iconColor);
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        paint.setStrokeWidth(iconSize);
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        if (speedMultiplier < MIN_MULTIPLIER)
            speedMultiplier = MIN_MULTIPLIER;
        else if (speedMultiplier > MAX_MULTIPLIER)
            speedMultiplier = MAX_MULTIPLIER;
        this.speedMultiplier = speedMultiplier;
        setInterval();
    }

    public OnIconChangedListener getOnIconChangedListener() {
        return listener;
    }

    public void setOnIconChangedListener(OnIconChangedListener listener) {
        this.listener = listener;
    }

    public State getState() {
        return state;
    }

    public void startProgress() {
        state = State.PROGRESS;
        invalidate();
    }

    /**
     * start circle when progress finish a round
     * <p>
     * 进度画完一圈后开始画圈，不是一调用就开始画
     */
    public void startCircle() {
        progressToCircle = true;
    }

    /**
     * line
     * <p>
     * 线
     */
    class Line {
        Point startPoint;
        Point endPoint;

        float k;
        float b;

        public Line(Point startPoint, Point endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;

            k = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);
            b = startPoint.y - k * startPoint.x;
        }

        public float getY(float x) {
            return k * x + b;
        }
    }

    /**
     * point
     * <p>
     * 点
     */
    class Point {
        float x;
        float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
