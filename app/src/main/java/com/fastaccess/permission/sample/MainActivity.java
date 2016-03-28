package com.fastaccess.permission.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        findViewById(R.id.badAss).setOnClickListener(this);
        findViewById(R.id.pagerActivity).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        boolean isBadAss = v.getId() == R.id.badAss;
        Intent intent;
        if (isBadAss) {
            intent = new Intent(this, SampleActivity.class);
        } else {
            intent = new Intent(this, SamplePagerActivity.class);
        }
        startActivity(intent);
        if (isBadAss) finish();
    }
}
