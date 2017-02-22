package com.example.tonyw.acgwarehouse.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.tonyw.acgwarehouse.R;


/**
 * Created by tonywu10 on 2016/12/11.
 */

public class AboutActivity extends AppCompatActivity{
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mToolbar= (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
