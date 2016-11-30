package com.github.aramkhezry.MyStore;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.astuetz.PagerSlidingTabStrip;
import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.PinCompatActivity;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;

public class MainActivity  extends PinCompatActivity {
    PagerSlidingTabStrip tabs;
    ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //enable passcod to main activity
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setTimeout(30000);
        lockManager.getAppLock().setLogoId(R.drawable.security_lock);


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                1);

        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_option; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            new MaterialDialog.Builder(MainActivity.this)
                    .contentColor(Color.WHITE)
                    .theme(Theme.DARK)
                    .contentGravity(GravityEnum.START)
                    .titleGravity(GravityEnum.START)
                    .buttonsGravity(GravityEnum.START)
                    .title("contact me at Twitter and Telegram and GitHub :")
                    .contentColor(Color.RED)
                    .contentGravity(GravityEnum.START)
                    .negativeText("Telegram : @aramkhezry")
                    .positiveText("Twitter : @aramkherzy")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String url = "http://www.twitter.com/aramkhezry";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String url = "https://telegram.me/aramkhezry";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    })
                    .show();

        }
        if (id == R.id.setting) {
            Intent intent =new  Intent(MainActivity.this,SplashActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"خاطرات برگزیده", "خاطرات "};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = MemoryFoveritFragment.newInstance(position);
                    break;
                case 1:
                    fragment = MemoryFragment.newInstance(position);
                    break;
            }
            return fragment;
        }
    }
}
