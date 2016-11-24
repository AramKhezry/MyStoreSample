package com.github.aramkhezry.MyStore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

/**
 * Created by Raman on 11/24/2016.
 */

public class SplashActivity extends AppCompatActivity {

    private Button singin;
    private ShowHidePasswordEditText password;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);



        password=(ShowHidePasswordEditText)findViewById(R.id.password);
        singin=(Button)findViewById(R.id.singIn) ;
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });


        signUp=(TextView)findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wrapInScrollView = true;
                new MaterialDialog.Builder(SplashActivity.this)
                        .customView(R.layout.singup_layout, wrapInScrollView)
                        .title("ثبت نام")
                        .positiveText("ایجاد حساب")
                        .negativeText("انصراف")
                        .show();
            }
        });



    }



}
