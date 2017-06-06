package com.linyuzai.demo4requestbutton

import android.app.Activity
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import com.linyuzai.requestbutton.*
import org.jetbrains.anko.*

class AnkoActivity : Activity() {

    var requestButton: RequestButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        linearLayout {
            orientation = LinearLayout.VERTICAL
            requestButton = requestButton {
                backgroundResource = R.drawable.bg
                iconSize = dip(2)
                iconSpacing = dip(25)
                iconStyle = Style.TICK_HALF_CIRCLE
                speedMultiplier = 1.8f
                textColor = Color.WHITE
                textSize = sp(16).toFloat()
                textWidth = wrapContent
                defaultText = "default"
                failureText = "failure"
                progressText = "progress"
                successText = "success"
                beforeRequest {
                    true
                }
                onRequest {
                    toast("request")
                    Handler().postDelayed({ requestSuccess() }, 2000)
                }
                onFinish {
                    toast("finish:$it")
                }
            }.lparams(width = matchParent, height = dip(50)) {
                bottomMargin = dip(5)
            }
        }
    }
}
