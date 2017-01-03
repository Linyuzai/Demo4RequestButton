package com.linyuzai.requestbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class RequestButton extends LinearLayout implements View.OnClickListener, OnIconChangedListener {

    public static final String TAG = RequestButton.class.getSimpleName();
    /**
     * the callback of request
     * <p>
     * 请求回调
     */
    private OnRequestCallback callback;
    /**
     * the spacing between icon and text
     * <p>
     * 图标和文字的间隔
     */
    private int iconSpacing = 0;
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
    private Style iconStyle = Style.TICK_END_CIRCLE;
    /**
     * the speed multiplier,1.8 default
     * <p>
     * 速度的乘数，默认1.8倍
     */
    private float speedMultiplier = 1.8f;
    /**
     * the default text of the button
     * <p>
     * 按钮的默认文字
     */
    private String defaultText = "default";
    /**
     * the text of button when request
     * <p>
     * 请求时按钮的文字
     */
    private String progressText = "progress";
    /**
     * the text of button when success
     * <p>
     * 请求成功时按钮的文字
     */
    private String successText = "success";
    /**
     * the text of button when failure
     * <p>
     * 请求失败时按钮的文字
     */
    private String failureText = "failure";
    /**
     * the text color of button
     * <p>
     * 按钮字体颜色
     */
    private int textColor = Color.BLACK;
    /**
     * the text size of button
     * <p>
     * 按钮字体大小
     */
    private float textSize = 20f;
    /**
     * the width of text,wrap content default
     * <p>
     * 文本宽度，默认包含内容
     */
    private int textWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * if request is success
     * <p>
     * 请求是否成功
     */
    private boolean isSuccess;

    private RequestIcon icon;
    private TextView buttonText;

    public RequestButton(Context context) {
        super(context);
        init(context, null);
    }

    public RequestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RequestButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RequestButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setOnClickListener(this);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RequestButton);
            iconSpacing = a.getDimensionPixelSize(R.styleable.RequestButton_request_icon_spacing, 0);
            iconColor = a.getColor(R.styleable.RequestButton_request_icon_color, Color.WHITE);
            iconSize = a.getDimensionPixelOffset(R.styleable.RequestButton_request_icon_size, 5);
            int ordinal = a.getInt(R.styleable.RequestButton_request_icon_style, Style.TICK_START_CIRCLE.ordinal());
            iconStyle = Style.values()[ordinal];
            speedMultiplier = a.getFloat(R.styleable.RequestButton_request_speed_multiplier, 1.8f);

            defaultText = a.getString(R.styleable.RequestButton_text_default);
            progressText = a.getString(R.styleable.RequestButton_text_progress);
            successText = a.getString(R.styleable.RequestButton_text_success);
            failureText = a.getString(R.styleable.RequestButton_text_failure);
            textColor = a.getColor(R.styleable.RequestButton_text_color, Color.BLACK);
            textSize = a.getDimensionPixelSize(R.styleable.RequestButton_text_size, 20);
            textWidth = a.getDimensionPixelSize(R.styleable.RequestButton_text_width, -1);

            a.recycle();
        }

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        linearLayout.setGravity(Gravity.RIGHT);

        buttonText = new TextView(context);
        buttonText.setLayoutParams(new LayoutParams(textWidth == -1 ? ViewGroup.LayoutParams.WRAP_CONTENT : textWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonText.setGravity(Gravity.CENTER);

        View view = new View(context);
        view.setLayoutParams(new LayoutParams(0, 0, 1f));

        icon = new RequestIcon(context);
        icon.setOnIconChangedListener(this);

        linearLayout.addView(icon);
        addView(linearLayout);
        addView(buttonText);
        addView(view);

        icon.setIconColor(iconColor);
        icon.setIconSize(iconSize);
        icon.setIconStyle(iconStyle);
        icon.setSpeedMultiplier(speedMultiplier);

        buttonText.setText(defaultText);
        buttonText.setTextColor(textColor);
        buttonText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    @Override
    public void onClick(View v) {
        if (callback != null && !callback.beforeRequest())
            return;
        startRequest();
    }

    @Override
    public boolean onProgressFinished(RequestIcon icon) {
        if (isSuccess) {
            buttonText.setText(successText);
            return true;
        } else {
            buttonText.setText(failureText);
            if (callback != null)
                callback.onFinish(false);
            return false;
        }
    }

    @Override
    public void onTickFinished(RequestIcon icon) {
        if (callback != null)
            callback.onFinish(true);
    }

    public void startRequest() {
        if (icon.getState() == State.IDLE) {
            buttonText.setText(progressText);
            icon.startProgress();
            if (callback != null)
                callback.onRequest();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LayoutParams params = new LayoutParams((int) (h * 0.7), (int) (h * 0.7));
        params.setMargins(0, 0, iconSpacing, 0);
        icon.setLayoutParams(params);
    }

    /**
     * call this by hand when request success
     * <p>
     * 请求成功是手动调用这个方法
     */
    public void requestSuccess() {
        isSuccess = true;
        icon.startCircle();
    }

    /**
     * call this by hand when request failure
     * <p>
     * 请求失败是手动调用这个方法
     */
    public void requestFailure() {
        isSuccess = false;
        icon.startCircle();
    }

    public Style getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(Style iconStyle) {
        this.iconStyle = iconStyle;
        icon.setIconStyle(iconStyle);
    }

    public OnRequestCallback getOnRequestCallback() {
        return callback;
    }

    public void setOnRequestCallback(OnRequestCallback callback) {
        this.callback = callback;
    }

    public int getIconSpacing() {
        return iconSpacing;
    }

    public void setIconSpacing(int iconSpacing) {
        this.iconSpacing = iconSpacing;
        ((LinearLayout.LayoutParams) icon.getLayoutParams()).setMargins(0, 0, iconSpacing, 0);
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
        icon.setIconColor(iconColor);
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        icon.setIconSize(iconSize);
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
        icon.setSpeedMultiplier(speedMultiplier);
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getProgressText() {
        return progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
    }

    public String getSuccessText() {
        return successText;
    }

    public void setSuccessText(String successText) {
        this.successText = successText;
    }

    public String getFailureText() {
        return failureText;
    }

    public void setFailureText(String failureText) {
        this.failureText = failureText;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        buttonText.setTextColor(textColor);
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        buttonText.setTextSize(textSize);
    }

    public int getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(int textWidth) {
        this.textWidth = textWidth;
        buttonText.getLayoutParams().width = textWidth;
    }

    /**
     * get the TextView do what you want
     *
     * @return the text view
     */
    public TextView getButtonTextView() {
        return buttonText;
    }
}
