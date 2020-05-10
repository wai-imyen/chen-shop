package com.yen.androbe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ResetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        // 初始化
        setToolbar();
        setupNav();
    }
}
