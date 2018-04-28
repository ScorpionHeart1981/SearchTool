package com.example.xchen.searchtool.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xchen.searchtool.Domain.Catalog;
import com.example.xchen.searchtool.Domain.Item;
import com.example.xchen.searchtool.R;
import com.example.xchen.searchtool.Service.CatalogService;
import com.example.xchen.searchtool.Service.ItemService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by XChen on 4/19/2018.
 */

public class ItemManagerFragment extends Fragment {
    private Realm realm;
    /*private RealmChangeListener realmListener;*/

    @BindView(R.id.lbladminitemfragment1) TextView lbladminitemfragment1;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;

    //Fragment对用户可见的标记
    private boolean isUIVisible;

    private Unbinder unbinder;

    CatalogService catalogService;
    ItemService itemService;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            /*isViewCreated = false;
            isUIVisible = false;*/
        }
    }

    private void loadData()
    {
        Bundle args = getArguments();
        String catalogId = args.getString("catalogId");
        Catalog catalog = catalogService.QueryCatalogById(realm, catalogId);
        lbladminitemfragment1.setText(catalog.getName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.adminitemfragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();
        /*realmListener = new RealmChangeListener<RealmResults<Item>>() {
            @Override
            public void onChange(RealmResults<Item> items) {
                // ... do something with the updates (UI, etc.) ...
            }};
        realm.addChangeListener(realmListener);*/

        catalogService = new CatalogService();
        itemService = new ItemService();

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        /*realm.removeAllChangeListeners();*/
        realm.close();
        unbinder.unbind();
    }
}
