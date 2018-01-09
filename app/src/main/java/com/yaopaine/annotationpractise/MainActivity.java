package com.yaopaine.annotationpractise;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.inject(this);
        //setContentView(R.layout.activity_main);
        tvTitle.setText("这是标题");
        tvContent1.setText("tvContent1");
        tvContent2.setText("tvContent2");
        tvContent3.setText("tvContent3");
    }

    @OnClick({R.id.tv_content1, R.id.tv_content2, R.id.tv_content3})
    private void onClick(View view) {
        Log.e("TAG", "onClick: " + ((TextView) view).getText());
    }
}
