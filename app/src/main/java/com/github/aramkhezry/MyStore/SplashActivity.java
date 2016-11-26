package com.github.aramkhezry.MyStore;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;



public class SplashActivity extends PinActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ENABLE = 11;
    private Button singin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        this.findViewById(R.id.button_enable_pin).setOnClickListener(this);
        this.findViewById(R.id.button_change_pin).setOnClickListener(this);
        this.findViewById(R.id.button_unlock_pin).setOnClickListener(this);
        singin =(Button)findViewById(R.id.singin);
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SplashActivity.this, CustomPinActivity.class);
        switch (v.getId()) {
            case R.id.button_enable_pin:
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                startActivityForResult(intent, REQUEST_CODE_ENABLE);

                break;
            case R.id.button_change_pin:
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
                startActivity(intent);
                break;
            case R.id.button_unlock_pin:
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
                startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_ENABLE:
                Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}