package com.example.xchen.searchtool.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xchen.searchtool.Component.PaginationAdapter;
import com.example.xchen.searchtool.Domain.Catalog;
import com.example.xchen.searchtool.Domain.Item;
import com.example.xchen.searchtool.OnListItemClickListener;
import com.example.xchen.searchtool.R;
import com.example.xchen.searchtool.Service.CatalogService;
import com.example.xchen.searchtool.Service.ItemService;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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

    OnListItemClickListener myListener;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try{
            myListener = (OnListItemClickListener)activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnListItemClickListener");
        }
    }

    @BindView(R.id.fabOnItem) FloatingActionButton fabOnItem;
    @BindView(R.id.itemList)
    ListView itemList;
    private Unbinder unbinder;

    PaginationAdapter<Item> itemAdapter;
    Handler handler = new Handler();

    final int LOAD_STATE_IDLE = 0;//没有在加载，并且服务器上还有数据没加载
    final int LOAD_STATE_LOADING = 1;//正在加载状态
    final int LOAD_STATE_FINISH = 2;//表示服务器上的全部数据都已加载完毕
    int loadState = LOAD_STATE_IDLE;//记录加载的状态
    int MAX_COUNT;
    int EACH_COUNT;

    String catalogId;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;

    //Fragment对用户可见的标记
    private boolean isUIVisible;

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
        loadState = LOAD_STATE_IDLE;

        Bundle args = getArguments();
        catalogId = args.getString("catalogId");
        MAX_COUNT = catalogService.GetEnabledItemsSizeByCatalog(realm, catalogId);

        ArrayList<Item> items = new ArrayList<>();
        itemAdapter= new PaginationAdapter<>(items);
        itemList.setAdapter(itemAdapter);

        itemList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i("onScrollStateChanged", scrollState+"");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("onS", "firstVisibleItem"+firstVisibleItem+" visibleItemCount"+visibleItemCount+" totalItemCount"+totalItemCount);
                if(firstVisibleItem+visibleItemCount == totalItemCount){
                    if(loadState == LOAD_STATE_IDLE){
                        Log.i("onScroll", "firstVisibleItem"+firstVisibleItem+" visibleItemCount"+visibleItemCount+" totalItemCount"+totalItemCount);
                        loadState=LOAD_STATE_LOADING;
                        loadMore();
                    }
                }
            }
        });

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtitemmanageritemtitle = (TextView)view.findViewById(R.id.txtitemmanageritemtitle);
                TextView txtitemmanageritemdisplayorder = (TextView)view.findViewById(R.id.txtitemmanageritemdisplayorder);
                TextView txtitemmanageritemurl = (TextView)view.findViewById(R.id.txtitemmanageritemurl);
                TextView txtitemmanageritemisenable = (TextView)view.findViewById(R.id.txtcitemmanageritemisenabled);
                TextView txtitemmanageritemisimage = (TextView)view.findViewById(R.id.txtitemmanageritemisiamge);
                TextView txtitemmanageritemindex = (TextView)view.findViewById(R.id.txtitemmanageritemindex);

                LoadItemAdminDialog(false, txtitemmanageritemtitle.getText().toString(),
                        txtitemmanageritemurl.getText().toString(), txtitemmanageritemdisplayorder.getText().toString(),
                        txtitemmanageritemisenable.getText().toString()=="是", txtitemmanageritemisimage.getText().toString()=="是",
                        txtitemmanageritemindex.getText().toString());
            }
        });

        /*itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtitemmanageritemtitle = (TextView)view.findViewById(R.id.txtitemmanageritemtitle);
                TextView txtitemmanageritemdisplayorder = (TextView)view.findViewById(R.id.txtitemmanageritemdisplayorder);
                TextView txtitemmanageritemurl = (TextView)view.findViewById(R.id.txtitemmanageritemurl);
                TextView txtitemmanageritemisenable = (TextView)view.findViewById(R.id.txtcitemmanageritemisenabled);
                TextView txtitemmanageritemisimage = (TextView)view.findViewById(R.id.txtitemmanageritemisiamge);
                TextView txtitemmanageritemindex = (TextView)view.findViewById(R.id.txtitemmanageritemindex);

                LoadItemAdminDialog(false, txtitemmanageritemtitle.getText().toString(),
                        txtitemmanageritemurl.getText().toString(), txtitemmanageritemdisplayorder.getText().toString(),
                        txtitemmanageritemisenable.getText().toString()=="是", txtitemmanageritemisimage.getText().toString()=="是",
                        txtitemmanageritemindex.getText().toString());

                return true;
            }
        });*/
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

        fabOnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadItemAdminDialog(true, "","","",true,true,"");
            }
        });

        EACH_COUNT = 15;

        fabOnItem.attachToListView(itemList);

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        /*realm.removeAllChangeListeners();*/
        realm.close();
        unbinder.unbind();
    }

    private void SaveItem(String ItemName, String ItemUrl, String ItemDisplayOrder,
                            String hideOrShowForIsImage, String hideOrShowForIsEnabled)
    {
        itemService.CreateItem(realm, ItemName, ItemUrl, Integer.parseInt(ItemDisplayOrder),
                hideOrShowForIsEnabled=="True", hideOrShowForIsImage=="True", "BaiDu", catalogId);

        refreshmain();
        Toast.makeText(getContext(),
                ItemName + " " + ItemUrl+ " " + hideOrShowForIsEnabled + " " + hideOrShowForIsImage,
                Toast.LENGTH_SHORT).show();
    }

    public void UpdateItem(String itemIndex, String itemName, String itemUrl, String itemDisplayOrder,
                              String isImage, String isEnabled){
        itemService.UpdateItem(realm, itemIndex, itemName, itemUrl, Integer.parseInt(itemDisplayOrder),
                isEnabled=="True",isImage=="True", "BaiDu");

        refreshmain();
    }

    private void DeleteItem(String itemId)
    {
        itemService.DeleteItem(realm, itemId, catalogId);

        refreshmain();
        Toast.makeText(getContext(),
                itemId,
                Toast.LENGTH_SHORT).show();
    }

    private void loadMore()
    {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loaditemData();
                itemAdapter.notifyDataSetChanged();
                if (loadState==LOAD_STATE_FINISH) {
                    Toast.makeText(getContext(),
                            "已经全部加载完毕",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);
    }

    private void loaditemData()
    {
        int dataIndex;
        int count = itemAdapter.getCount();
        dataIndex = count + Math.min(count + EACH_COUNT, MAX_COUNT);
        List<Item> list = catalogService.GetAllItemsByCatalog(realm, catalogId, count, Math.min(count + EACH_COUNT, MAX_COUNT));
        itemAdapter.add(list);
        /*for(dataIndex = count; dataIndex < Math.min(count+EACH_COUNT, MAX_COUNT); dataIndex++) {
            Catalog model = new Catalog();
            model.setName("分类" + dataIndex);
            model.setIsEnabled(false);
            catalogAdapter.add(model);
        }*/
        //如果服务器上的全部数据都已加载完毕
        if (dataIndex == MAX_COUNT) {
            loadState = LOAD_STATE_FINISH;
        }
        else {
            loadState = LOAD_STATE_IDLE;
        }
    }

    private void LoadItemAdminDialog(Boolean isNew, String title, String url, String displayOrder,
                                     Boolean isEnabled, Boolean isImage, final String itemIndex)
    {
        AlertDialog.Builder item_mangater_dialog = new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.adminitemfragment_itemedit_dialog_layout, null);
        item_mangater_dialog.setTitle("管理按钮");
        item_mangater_dialog.setView(dialogView);

        final EditText txtItemName= (EditText)dialogView.findViewById(R.id.txtItemName);
        final LinearLayoutCompat llItemUrl = (LinearLayoutCompat)dialogView.findViewById(R.id.itemeditdialogsecondlinear);
        final EditText txtItemUrl = (EditText)dialogView.findViewById(R.id.txtItemUrl);
        final EditText txtItemDisplayOrder = (EditText)dialogView.findViewById(R.id.txtItemDisplayOrder);
        final Switch isImageSwitch = (Switch)dialogView.findViewById(R.id.switchItemImage);
        final TextView lblForSwitchItemIsImage = (TextView)dialogView.findViewById(R.id.lblForSwitchItemIsImage);
        final TextView hideOrShowForIsImage = (TextView)dialogView.findViewById(R.id.hideOrshowForIsImage);
        final Switch delSwitch = (Switch)dialogView.findViewById(R.id.switchItemEnable);
        final TextView lblForSwitchItemIsEnable = (TextView)dialogView.findViewById(R.id.lblForSwitchItemIsEnable);
        final TextView hideOrShowForIsEnabled = (TextView)dialogView.findViewById(R.id.hideOrshowForIsEnabled);

        if(isNew)
        {
            isImageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        lblForSwitchItemIsImage.setText("是图片");
                        hideOrShowForIsImage.setText("True");
                        llItemUrl.setVisibility(View.GONE);
                    }
                    else {
                        lblForSwitchItemIsImage.setText("不是图片");
                        hideOrShowForIsImage.setText("False");
                        llItemUrl.setVisibility(View.VISIBLE);
                    }
                }
            });

            delSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        lblForSwitchItemIsEnable.setText("显示");
                        hideOrShowForIsEnabled.setText("True");
                    }
                    else {
                        lblForSwitchItemIsEnable.setText("不显示");
                        hideOrShowForIsEnabled.setText("False");
                    }
                }
            });

            item_mangater_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SaveItem(txtItemName.getText().toString(), txtItemUrl.getText().toString(), txtItemDisplayOrder.getText().toString(),
                            hideOrShowForIsImage.getText().toString(), hideOrShowForIsEnabled.getText().toString());
                }
            });
        }
        else
        {
            txtItemName.setText(title);
            txtItemDisplayOrder.setText(displayOrder);
            txtItemUrl.setText(url);
            hideOrShowForIsEnabled.setText(isEnabled ? "True" : "False");
            lblForSwitchItemIsEnable.setText(isEnabled ? "显示" : "不显示");
            delSwitch.setChecked(isEnabled);
            hideOrShowForIsImage.setText(isImage ? "True" : "False");
            lblForSwitchItemIsImage.setText(isImage ? "是图片" : "不是图片");
            llItemUrl.setVisibility(isImage ? View.GONE : View.VISIBLE);
            isImageSwitch.setChecked(isImage);
            isImageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        lblForSwitchItemIsImage.setText("是图片");
                        hideOrShowForIsImage.setText("True");
                        llItemUrl.setVisibility(View.GONE);
                    }
                    else {
                        lblForSwitchItemIsImage.setText("不是图片");
                        hideOrShowForIsImage.setText("False");
                        llItemUrl.setVisibility(View.VISIBLE);
                    }
                }
            });

            delSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        lblForSwitchItemIsEnable.setText("显示");
                        hideOrShowForIsEnabled.setText("True");
                    }
                    else {
                        lblForSwitchItemIsEnable.setText("不显示");
                        hideOrShowForIsEnabled.setText("False");
                    }
                }
            });

            item_mangater_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UpdateItem(itemIndex, txtItemName.getText().toString(), txtItemUrl.getText().toString(),
                            txtItemDisplayOrder.getText().toString(), hideOrShowForIsImage.getText().toString(),
                            hideOrShowForIsEnabled.getText().toString());
                }
            });

            item_mangater_dialog.setNegativeButton("彻底删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeleteItem(itemIndex);
                }
            });
        }
        item_mangater_dialog.show();
    }

    public void refreshmain(){
        List<Item> list = catalogService.GetAllItemsByCatalog(realm, catalogId,
                0, Math.min(0 + EACH_COUNT, catalogService.GetAllItemsSizeByCatalog(realm,catalogId)));
        itemAdapter.clear();
        itemAdapter.add(list);
        itemAdapter.notifyDataSetChanged();
    }
}
