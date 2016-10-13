package com.linyuzai.demo4requestbutton;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linyuzai.requestbutton.OnRequestCallback;
import com.linyuzai.requestbutton.RequestButton;

public class MainActivity extends AppCompatActivity {

    RequestButton start0, start1, start2, half0, half1, half2, end0, end1, end2;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        start0 = (RequestButton) findViewById(R.id.start0);
        start1 = (RequestButton) findViewById(R.id.start1);
        start2 = (RequestButton) findViewById(R.id.start2);
        half0 = (RequestButton) findViewById(R.id.half0);
        half1 = (RequestButton) findViewById(R.id.half1);
        half2 = (RequestButton) findViewById(R.id.half2);
        end0 = (RequestButton) findViewById(R.id.end0);
        end1 = (RequestButton) findViewById(R.id.end1);
        end2 = (RequestButton) findViewById(R.id.end2);

        end2.setOnRequestCallback(new OnRequestCallback() {
            @Override
            public boolean beforeRequest() {
                return true;
            }

            @Override
            public void onRequest() {
                Toast.makeText(MainActivity.this, "request", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish(boolean isSuccess) {
                Toast.makeText(MainActivity.this, "finish", Toast.LENGTH_SHORT).show();
            }
        });

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start0.startRequest();
                start1.startRequest();
                start2.startRequest();
                half0.startRequest();
                half1.startRequest();
                half2.startRequest();
                end0.startRequest();
                end1.startRequest();
                end2.startRequest();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start0.requestSuccess();
                        start1.requestSuccess();
                        start2.requestSuccess();
                        half0.requestSuccess();
                        half1.requestSuccess();
                        half2.requestSuccess();
                        end0.requestSuccess();
                        end1.requestSuccess();
                        end2.requestSuccess();
                    }
                }, 2000);
            }
        });
    }
}
