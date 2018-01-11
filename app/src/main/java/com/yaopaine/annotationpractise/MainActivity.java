package com.yaopaine.annotationpractise;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.yaopaine.annotationpractise.annotation.ContentView;
import com.yaopaine.annotationpractise.annotation.OnClick;
import com.yaopaine.annotationpractise.annotation.ViewInject;
import com.yaopaine.annotationpractise.annotation.util.ViewInjectUtils;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.tv_content1)
    protected TextView tvContent1;

    @ViewInject(R.id.tv_content2)
    TextView tvContent2;

    @ViewInject(R.id.tv_content3)
    public TextView tvContent3;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        ViewInjectUtils.inject(this);
        //setContentView(R.layout.activity_main);
        tvTitle.setText("这是标题");
        tvContent1.setText("tvContent1");
        tvContent2.setText("tvContent2");
        tvContent3.setText("tvContent3");
    }

    @OnClick({R.id.tv_content1, R.id.tv_content2, R.id.tv_content3})
    private void onClick(View view) {
        if (view.getId() == R.id.tv_content1) {
            startActivity(new Intent(this, SecondActivity.class));
        }
        Log.e("TAG", "onClick: " + ((TextView) view).getText());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.e(TAG, "onPostCreate: ");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e(TAG, "onPostResume: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}
