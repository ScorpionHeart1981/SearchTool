package com.example.xchen.searchtool.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.xchen.searchtool.Model.BtnItemModel;
import com.example.xchen.searchtool.OnFrontEndContentFragmentItemButtonClickListener;
import com.example.xchen.searchtool.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Created by CX on 2018/4/18.
 */

public class ContentFragment extends Fragment {
    private Realm realm;

    OnFrontEndContentFragmentItemButtonClickListener myListener;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try{
            myListener = (OnFrontEndContentFragmentItemButtonClickListener)activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implementOnArticleSelectedListener");
        }
    }

    @BindView(R.id.catalogrow)
    LinearLayout catalogRow;
    @BindView(R.id.itemrow) LinearLayout itemrow;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.contentfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();

        final String[] categoryNames = getAllCatalog();
        for(int i=0;i<categoryNames.length;i++) {
            Button button = (Button) getLayoutInflater().inflate(R.layout.contentfragment_toplinecatalog_button_layout, null);
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
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        realm.close();
        unbinder.unbind();
    }

    private String[] getAllCatalog()
    {
        return new String[]{"电影", "电视剧", "拍卖", "字画", "瓷器", "图片"};
    }

    private void LoadItemsByCatalogId(int id)
    {
        itemrow.removeAllViews();
        String[] items = getItemsByCatalogId(id);
        LinearLayout ll = null;
        for(int i = 0; i < items.length; i++)
        {
            if(i % 2 == 0)
            {
                ll = (LinearLayout) getLayoutInflater().inflate(R.layout.contentfragment_middlelinelinear_layout, null);
                Button btn = (Button) getLayoutInflater().inflate(R.layout.contentfragment_middlelinelinear_item_button_layout, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                btn.setLayoutParams(lp);
                btn.setText(items[i]);
                BtnItemModel model = new BtnItemModel();
                model.url = "http://www.baidu.com";
                model.title = items[i];
                btn.setTag(model);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BtnItemModel m = (BtnItemModel)v.getTag();
                        myListener.OnButtonClickListener(m.url,m.title);
                    }
                });
                ll.addView(btn);
                itemrow.addView(ll);
            }
            else
            {
                Button btn = (Button) getLayoutInflater().inflate(R.layout.contentfragment_middlelinelinear_item_button_layout, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                btn.setLayoutParams(lp);
                btn.setText(items[i]);
                BtnItemModel model = new BtnItemModel();
                model.url = "http://www.163.com";
                model.title = items[i];
                btn.setTag(model);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BtnItemModel m = (BtnItemModel)v.getTag();
                        myListener.OnButtonClickListener(m.url,m.title);
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

