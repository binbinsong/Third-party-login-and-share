package com.login.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_auth).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auth:
                startActivity(new Intent(this, AuthActivity.class));
                break;
            case R.id.btn_share:
                startActivity(new Intent(this, ShareActivity.class));
                break;
        }
    }
}
