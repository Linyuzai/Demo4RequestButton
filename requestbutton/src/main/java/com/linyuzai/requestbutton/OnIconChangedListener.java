package com.linyuzai.requestbutton;

/**
 * Created by Administrator on 2016/9/7 0007.
 * <p/>
 * call this when icon changed
 * <p/>
 * 当图标改变
 */
public interface OnIconChangedListener {
    /**
     * call this when request finished
     * <p/>
     * 请求结束时调用
     *
     * @param icon
     * @return if need to do circle and tick<p/>是否继续画圈及打钩
     */
    boolean onProgressFinished(RequestIcon icon);

    /**
     * call this when tick finish
     * <p/>
     * 打钩结束
     *
     * @param icon
     */
    void onTickFinished(RequestIcon icon);
}
