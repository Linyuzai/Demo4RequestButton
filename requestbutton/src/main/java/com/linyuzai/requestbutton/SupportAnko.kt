@file:Suppress("NOTHING_TO_INLINE")

package com.linyuzai.requestbutton

import android.app.Activity
import android.content.Context
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * Created by Administrator on 2017/5/23 0023.
 * 支持kotlin anko
 * @author Linyuzai
 */

inline fun ViewManager.requestButton(theme: Int = 0): com.linyuzai.requestbutton.RequestButton = requestButton(theme) {}
inline fun ViewManager.requestButton(theme: Int = 0, init: com.linyuzai.requestbutton.RequestButton.() -> Unit): com.linyuzai.requestbutton.RequestButton {
    return ankoView({ com.linyuzai.requestbutton.RequestButton(it) }, theme) { init() }
}

inline fun Context.requestButton(theme: Int = 0): com.linyuzai.requestbutton.RequestButton = requestButton(theme) {}
inline fun Context.requestButton(theme: Int = 0, init: com.linyuzai.requestbutton.RequestButton.() -> Unit): com.linyuzai.requestbutton.RequestButton {
    return ankoView({ com.linyuzai.requestbutton.RequestButton(it) }, theme) { init() }
}

inline fun Activity.requestButton(theme: Int = 0): com.linyuzai.requestbutton.RequestButton = requestButton(theme) {}
inline fun Activity.requestButton(theme: Int = 0, init: com.linyuzai.requestbutton.RequestButton.() -> Unit): com.linyuzai.requestbutton.RequestButton {
    return ankoView({ com.linyuzai.requestbutton.RequestButton(it) }, theme) { init() }
}

fun com.linyuzai.requestbutton.RequestButton.beforeRequest(l: () -> Boolean) {
    setBeforeRequestCallback(l)
}

fun com.linyuzai.requestbutton.RequestButton.onRequest(l: () -> Unit) {
    setRequestCallback(l)
}

fun com.linyuzai.requestbutton.RequestButton.onFinish(l: (Boolean) -> Unit) {
    setFinishCallback(l)
}