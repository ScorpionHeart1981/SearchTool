package com.example.xchen.searchtool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.xchen.searchtool.Controller.CatalogManagerFragment;
import com.example.xchen.searchtool.Controller.ItemManagerFragment;
import com.example.xchen.searchtool.Component.NoScrollViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CX on 2018/4/18.
 */

public class AdminActivity extends AppCompatActivity implements OnCatalogItemClickListener {
    @BindView(R.id.admincontentviewpager) NoScrollViewPager admincontentviewpager;

    CatalogManagerFragment catalogManagerFragment = new CatalogManagerFragment();
    ItemManagerFragment itemManagerFragment  = new ItemManagerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admincontent_activity_main);

        ButterKnife.bind(this);

        admincontentviewpager.setOffscreenPageLimit(0);
        admincontentviewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        /*addBadgeAt(0, 5);*/
                        return catalogManagerFragment;
                    case 1:
                        return itemManagerFragment;
                }
                return null;
            }

            @Override
            public int getItemPosition(Object object){
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    public static void loadContentAdmin(Context mContext) {
        Intent intent = new Intent(mContext, AdminActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void OnCatalogItemClickListener(String catalogName)
    {
        Bundle bundle = new Bundle();
        bundle.putString("catalogName",catalogName);
        itemManagerFragment.setArguments(bundle);
        admincontentviewpager.setCurrentItem(1);
    }
}
