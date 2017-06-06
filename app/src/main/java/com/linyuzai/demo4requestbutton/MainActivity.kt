package com.linyuzai.demo4requestbutton

import android.graphics.Color
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.linyuzai.requestbutton.*
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.sp
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    internal val start0: RequestButton by lazy {
        findViewById(R.id.start0) as RequestButton
    }
    internal val start1: RequestButton by lazy {
        findViewById(R.id.start1) as RequestButton
    }
    internal val start2: RequestButton by lazy {
        findViewById(R.id.start2) as RequestButton
    }
    internal val half0: RequestButton by lazy {
        findViewById(R.id.half0) as RequestButton
    }
    internal val half1: RequestButton by lazy {
        findViewById(R.id.half1) as RequestButton
    }
    internal val half2: RequestButton by lazy {
        findViewById(R.id.half2) as RequestButton
    }
    internal val end0: RequestButton by lazy {
        findViewById(R.id.end0) as RequestButton
    }
    internal val end1: RequestButton by lazy {
        findViewById(R.id.end1) as RequestButton
    }
    internal val end2: RequestButton by lazy {
        findViewById(R.id.end2) as RequestButton
    }
    internal val start: Button by lazy {
        findViewById(R.id.start) as Button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)

        end2.onRequestCallback = object : OnRequestCallback {
            override fun beforeRequest(): Boolean {
                return true
            }

            override fun onRequest() {
                Toast.makeText(this@MainActivity, "request", Toast.LENGTH_SHORT).show()
            }

            override fun onFinish(isSuccess: Boolean) {
                Toast.makeText(this@MainActivity, "finish", Toast.LENGTH_SHORT).show()
            }
        }

        start.setOnClickListener {
            start0.startRequest()
            start1.startRequest()
            start2.startRequest()
            half0.startRequest()
            half1.startRequest()
            half2.startRequest()
            end0.startRequest()
            end1.startRequest()
            end2.startRequest()
            Handler().postDelayed({
                start0.requestSuccess()
                start1.requestSuccess()
                start2.requestSuccess()
                half0.requestSuccess()
                half1.requestSuccess()
                half2.requestSuccess()
                end0.requestSuccess()
                end1.requestSuccess()
                end2.requestSuccess()
            }, 2000)
        }
    }
}
