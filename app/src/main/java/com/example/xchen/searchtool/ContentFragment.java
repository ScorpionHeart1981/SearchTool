package com.example.xchen.searchtool;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.ViewGroup.LayoutParams;

/**
 * Created by XChen on 4/3/2018.
 */

public class ContentFragment extends Fragment {
    OnButtonClickListener myListener;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try{
            myListener = (OnButtonClickListener)activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implementOnArticleSelectedListener");
        }
    }

    @BindView(R.id.catalogrow) LinearLayout catalogRow;
    @BindView(R.id.subCatalog) LinearLayout subCatalog;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.contentfragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        final String[] categoryNames = getAllCatalog();
        for(int i=0;i<categoryNames.length;i++) {
            Button button = (Button) getLayoutInflater().inflate(R.layout.catalog_button, null);
            button.setText(categoryNames[i]);
            button.setTag(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int)v.getTag();
                    LoadItemsByCatalogId(id);
                }
            });
            catalogRow.addView(button);
        }



        /*for(int i = 0; i < 7; i++)
        {
            LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.item_button_layout, null);
            Button btn1 = (Button) getLayoutInflater().inflate(R.layout.item_button, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            btn1.setLayoutParams(lp);
            btn1.setText("爱奇艺大世界算不到");
            btn1.setTag("爱奇艺世界算不到");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListener.OnButtonClickListener((String)v.getTag());
                }
            });
            ll.addView(btn1);
            Button btn2 = (Button) getLayoutInflater().inflate(R.layout.item_button, null);
            btn2.setLayoutParams(lp);
            btn2.setText("优酷");
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager viewPager = ((MainActivity)getActivity()).findViewById(R.id.viewPager);
                    viewPager.setCurrentItem(3);
                }
            });
            ll.addView(btn2);

            subCatalog.addView(ll);
        }*/

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    private String[] getAllCatalog()
    {
        return new String[]{"电影", "电视剧", "拍卖", "字画", "瓷器", "图片"};
    }

    private void LoadItemsByCatalogId(int id)
    {
        subCatalog.removeAllViews();
        String[] items = getItemsByCatalogId(id);
        LinearLayout ll = null;
        for(int i = 0; i < items.length; i++)
        {
            if(i % 2 == 0)
            {
                ll = (LinearLayout) getLayoutInflater().inflate(R.layout.item_button_layout, null);
                Button btn = (Button) getLayoutInflater().inflate(R.layout.item_button, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                btn.setLayoutParams(lp);
                btn.setText(items[i]);
                btn.setTag(items[i]);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myListener.OnButtonClickListener((String)v.getTag());
                    }
                });
                ll.addView(btn);
                subCatalog.addView(ll);
            }
            else
            {
                Button btn = (Button) getLayoutInflater().inflate(R.layout.item_button, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                btn.setLayoutParams(lp);
                btn.setText(items[i]);
                btn.setTag(items[i]);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myListener.OnButtonClickListener((String)v.getTag());
                    }
                });
                ll.addView(btn);
            }
        }
    }

    private String[] getItemsByCatalogId(int id)
    {
        if(id==0)
        {
            return new String[]{"优酷","爱奇艺","腾讯","央视网"};
        }
        else if (id==1)
        {
            return new String[]{"新浪","搜狐","百度"};
        }
        return new String[]{};
    }
}
