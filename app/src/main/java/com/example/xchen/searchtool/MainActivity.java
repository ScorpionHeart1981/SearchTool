package com.example.xchen.searchtool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.xchen.searchtool.Controller.AccessoryFragment;
import com.example.xchen.searchtool.Controller.AdminFragment;
import com.example.xchen.searchtool.Controller.ContentFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity implements OnFrontEndContentFragmentItemButtonClickListener {
    @BindView(R.id.bnve) BottomNavigationViewEx bnve;
    @BindView(R.id.viewPager) ViewPager viewPager;

    private ContentFragment contentFragment = new ContentFragment();
    private AdminFragment adminFragment = new AdminFragment();
    private AccessoryFragment accessoryFragment = new AccessoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){}

            @Override
            public void onPageSelected(int position){
                if(position<=2) {
                    bnve.getMenu().getItem(position).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state){}
        });

        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewPager.setCurrentItem(item.getOrder());
                return true;
            }
        });

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        /*addBadgeAt(0, 5);*/
                        return contentFragment;
                    case 1:
                        /*addBadgeAt(1, 3);*/
                        return accessoryFragment;
                    case 2:
                        /*addBadgeAt(2, 8);*/
                        return adminFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        bnve.enableItemShiftingMode(false);
        bnve.enableShiftingMode(false);
        bnve.enableAnimation(true);
    }

    private Badge addBadgeAt(int position, int number){
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(-20, 2, true)
                .bindTarget(bnve.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if(Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                            Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT);
                    }
                });
    }

    @Override
    public void OnButtonClickListener(String searchUrl, String title)
    {
        WebViewActivity.loadUrl(this, searchUrl, title);
    }
}
