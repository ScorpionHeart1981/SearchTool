package com.example.xchen.searchtool.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.xchen.searchtool.Domain.Catalog;
import com.example.xchen.searchtool.OnListItemClickListener;
import com.example.xchen.searchtool.R;
import com.example.xchen.searchtool.Component.PaginationAdapter;
import com.example.xchen.searchtool.Service.CatalogService;
import com.melnykov.fab.FloatingActionButton;

import org.w3c.dom.Text;

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

public class CatalogManagerFragment extends Fragment {
    private Realm realm;
    /*private RealmChangeListener realmListener;*/

    OnListItemClickListener myListener;
    /*OnAddCatalogBtnClickListener myListener;*/

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

    /*@BindView(R.id.btnAddCatalog) Button btnAddCatalog;*/
    @BindView(R.id.catalogList) ListView catalogList;
    @BindView(R.id.fabOnCatalog) FloatingActionButton fabOnCatalog;
    private Unbinder unbinder;

    PaginationAdapter<Catalog> catalogAdapter;
    Handler handler = new Handler();

    final int LOAD_STATE_IDLE = 0;//没有在加载，并且服务器上还有数据没加载
    final int LOAD_STATE_LOADING = 1;//正在加载状态
    final int LOAD_STATE_FINISH = 2;//表示服务器上的全部数据都已加载完毕
    int loadState = LOAD_STATE_IDLE;//记录加载的状态
    int MAX_COUNT;
    int EACH_COUNT;

    CatalogService catalogService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.admincatalogfragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();
        /*realmListener = new RealmChangeListener<RealmResults<Catalog>>() {
            @Override
            public void onChange(RealmResults<Catalog> catalogs) {
                // ... do something with the updates (UI, etc.) ...
            }};
        realm.addChangeListener(realmListener);*/

        catalogService = new CatalogService();

        fabOnCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadCatalogAdminDialog(true, "","",true,"");
            }
        });

        MAX_COUNT = catalogService.GetAllCatalogSize(realm);
        EACH_COUNT = 15;

        ArrayList<Catalog> catalogs = new ArrayList<>();
        catalogAdapter= new PaginationAdapter<>(catalogs);
        catalogList.setAdapter(catalogAdapter);

        catalogList.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        catalogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view.findViewById(R.id.txtcatalogmanageritemindex);
                Bundle bundle = new Bundle();
                bundle.putString("catalogId",tv.getText().toString());
                myListener.OnItemClickListener(bundle);
            }
        });

        catalogList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtcatalogmanageritemtitle = (TextView)view.findViewById(R.id.txtcatalogmanageritemtitle);
                TextView txtcatalogmanageritemdisplayorder = (TextView)view.findViewById(R.id.txtcatalogmanageritemdisplayorder);
                TextView txtcatalogmanageritemisenable = (TextView)view.findViewById(R.id.txtcatalogmanageritemisenable);
                TextView txtcatalogmanageritemindex = (TextView)view.findViewById(R.id.txtcatalogmanageritemindex);

                LoadCatalogAdminDialog(false, txtcatalogmanageritemtitle.getText().toString(),
                        txtcatalogmanageritemdisplayorder.getText().toString(),
                        txtcatalogmanageritemisenable.getText().toString()=="是",
                        txtcatalogmanageritemindex.getText().toString());

                return true;
            }
        });

        fabOnCatalog.attachToListView(catalogList);

        /*registerForContextMenu(catalogList);*/

        return view;
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);

        if(v==catalogList){
            menu.setHeaderTitle("请选择：");
            menu.add(0,0,0,"删除该类");
            menu.add(0,1,0,"取消");
        }
    }*/

    /*@Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case 0:
                String tmp = "menuInfo.position:" + String.valueOf(menuInfo.position);
                Toast.makeText(getContext(), tmp, Toast.LENGTH_LONG).show();
                break;
            case 1:
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }*/

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        /*unregisterForContextMenu(catalogList);*/
        /*realm.removeAllChangeListeners();*/
        realm.close();
        unbinder.unbind();
    }

    private void SaveCatalog(String catalogName, String catalogDisplayOrder, String isEnabled)
    {
        catalogService.CreateCatalog(realm, catalogName, Integer.parseInt(catalogDisplayOrder),isEnabled=="True");

        refreshmain();
        Toast.makeText(getContext(),
                catalogName + " "+ catalogDisplayOrder+ " " + isEnabled,
                Toast.LENGTH_SHORT).show();
    }

    public void UpdateCatalog(String catalogId, String catalogName, String catalogDisplayOrder, String isEnabled){
        catalogService.UpdateCatalog(realm, catalogId, catalogName, Integer.parseInt(catalogDisplayOrder),isEnabled=="True");

        refreshmain();
    }

    private void DeleteCatalog(String catalogId)
    {
        catalogService.DeleteCatalog(realm, catalogId);

        refreshmain();
        Toast.makeText(getContext(),
                catalogId,
                Toast.LENGTH_SHORT).show();
    }

    private void loadMore()
    {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                catalogAdapter.notifyDataSetChanged();
                if (loadState==LOAD_STATE_FINISH) {
                    Toast.makeText(getContext(),
                            "已经全部加载完毕",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);
    }

    private void loadData()
    {
        int dataIndex;
        int count = catalogAdapter.getCount();
        dataIndex = count + Math.min(count + EACH_COUNT, MAX_COUNT);
        List<Catalog> list = catalogService.GetAllCatalog(realm, count, Math.min(count + EACH_COUNT, MAX_COUNT));
        catalogAdapter.add(list);
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

    private void LoadCatalogAdminDialog(Boolean isNew, String name, String displayOrder, Boolean isEnable, final String itemIndex)
    {
        AlertDialog.Builder catalog_mangater_dialog = new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.admincatalogfragment_catalogedit_dialog_layout, null);
        catalog_mangater_dialog.setTitle("管理分类");
        catalog_mangater_dialog.setView(dialogView);

        final EditText txtCatalogName= (EditText)dialogView.findViewById(R.id.txtCatalogName);
        final EditText txtCatalogDisplayOrder = (EditText)dialogView.findViewById(R.id.txtCatalogDisplayOrder);
        final Switch delSwitch = (Switch)dialogView.findViewById(R.id.switchCatalogEnable);
        final TextView hideOrShow = (TextView)dialogView.findViewById(R.id.hideOrshow);
        final TextView lblForSwitchCatalogEnable = (TextView)dialogView.findViewById(R.id.lblForSwitchCatalogEnable);

        if(isNew)
        {
            catalog_mangater_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SaveCatalog(txtCatalogName.getText().toString(),
                            txtCatalogDisplayOrder.getText().toString(),
                            hideOrShow.getText().toString());
                }
            });

            delSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        hideOrShow.setText("True");
                        lblForSwitchCatalogEnable.setText("显示");
                    }
                    else {
                        hideOrShow.setText("False");
                        lblForSwitchCatalogEnable.setText("不显示");
                    }
                }
            });
        }
        else
        {
            txtCatalogName.setText(name);
            txtCatalogDisplayOrder.setText(displayOrder);
            hideOrShow.setText(isEnable ? "True" : "False");
            lblForSwitchCatalogEnable.setText(isEnable? "显示":"不显示");
            delSwitch.setChecked(isEnable);
            delSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        hideOrShow.setText("True");
                        lblForSwitchCatalogEnable.setText("显示");
                    }
                    else {
                        hideOrShow.setText("False");
                        lblForSwitchCatalogEnable.setText("不显示");
                    }
                }
            });

            catalog_mangater_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UpdateCatalog(itemIndex, txtCatalogName.getText().toString(),
                            txtCatalogDisplayOrder.getText().toString(),
                            hideOrShow.getText().toString());
                }
            });
            catalog_mangater_dialog.setNegativeButton("彻底删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeleteCatalog(itemIndex);
                }
            });
        }
        catalog_mangater_dialog.show();
    }

    public void refreshmain(){
        List<Catalog> list = catalogService.GetAllCatalog(realm, 0, Math.min(0 + EACH_COUNT, catalogService.GetAllCatalogSize(realm)));
        catalogAdapter.clear();
        catalogAdapter.add(list);
        catalogAdapter.notifyDataSetChanged();
    }
}
