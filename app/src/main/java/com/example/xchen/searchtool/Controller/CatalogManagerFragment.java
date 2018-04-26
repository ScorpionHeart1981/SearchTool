package com.example.xchen.searchtool.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xchen.searchtool.Domain.Catalog;
import com.example.xchen.searchtool.MainActivity;
import com.example.xchen.searchtool.OnCatalogItemClickListener;
import com.example.xchen.searchtool.OnItemButtonClickListener;
import com.example.xchen.searchtool.R;
import com.example.xchen.searchtool.Service.PaginationAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XChen on 4/19/2018.
 */

public class CatalogManagerFragment extends Fragment {
    OnCatalogItemClickListener myListener;
    /*OnAddCatalogBtnClickListener myListener;*/

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try{
            myListener = (OnCatalogItemClickListener)activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCatalogItemClickListener");
        }
    }

    @BindView(R.id.btnAddCatalog) Button btnAddCatalog;
    @BindView(R.id.catalogList) ListView catalogList;
    private Unbinder unbinder;


    PaginationAdapter<Catalog> catalogAdapter;
    Handler handler = new Handler();

    final int LOAD_STATE_IDLE=0;//没有在加载，并且服务器上还有数据没加载
    final int LOAD_STATE_LOADING=1;//正在加载状态
    final int LOAD_STATE_FINISH=2;//表示服务器上的全部数据都已加载完毕
    int loadState = LOAD_STATE_IDLE;//记录加载的状态
    int MAX_COUNT;
    int EACH_COUNT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.admincatalogfragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        btnAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder catalog_mangater_dialog = new AlertDialog.Builder(getContext());
                final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.catalog_manager_dialog, null);
                catalog_mangater_dialog.setTitle("管理分类");
                catalog_mangater_dialog.setView(dialogView);
                final TextView hideOrShow = (TextView)dialogView.findViewById(R.id.hideOrshow);
                catalog_mangater_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextCatalogName= (EditText)dialogView.findViewById(R.id.editTextCatalogName);
                        EditText editTextCatalogDisplayOrder = (EditText)dialogView.findViewById(R.id.editTextCatalogDisplayOrder);
                        SaveCatalog(editTextCatalogName.getText().toString(),
                                editTextCatalogDisplayOrder.getText().toString(),
                                hideOrShow.getText().toString());
                    }
                });

                Switch delSwitch = (Switch)dialogView.findViewById(R.id.delswitch);
                delSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                            hideOrShow.setText("True");
                        else
                            hideOrShow.setText("False");
                    }
                });
                catalog_mangater_dialog.show();
            }
        });

        MAX_COUNT = 100;
        EACH_COUNT = 30;

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
                TextView tv = (TextView)view.findViewById(R.id.txtcatalogmanageritemtitle);
                myListener.OnCatalogItemClickListener(tv.getText().toString());
            }
        });

        catalogList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvcatalogtitlename = (TextView)view.findViewById(R.id.txtcatalogmanageritemtitle);
                TextView tvcatalogdiaplsyorder = (TextView)view.findViewById(R.id.txtcatalogmanageritemdisplayorder);
                TextView tvcataloghideorshow = (TextView)view.findViewById(R.id.txtcatalogmanageritemisenable);

                AlertDialog.Builder catalog_mangater_dialog = new AlertDialog.Builder(getContext());
                final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.catalog_manager_dialog, null);
                catalog_mangater_dialog.setTitle("管理分类");
                catalog_mangater_dialog.setView(dialogView);

                final EditText editTextCatalogName= (EditText)dialogView.findViewById(R.id.editTextCatalogName);
                editTextCatalogName.setText(tvcatalogtitlename.getText());
                final TextView hideOrShow = (TextView)dialogView.findViewById(R.id.hideOrshow);
                hideOrShow.setText(tvcataloghideorshow.getText() == "是" ? "True" : "False");
                final Switch delSwitch = (Switch)dialogView.findViewById(R.id.delswitch);
                delSwitch.setChecked(hideOrShow.getText().toString() == "True");
                delSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                            hideOrShow.setText("True");
                        else
                            hideOrShow.setText("False");
                    }
                });

                catalog_mangater_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextCatalogName= (EditText)dialogView.findViewById(R.id.editTextCatalogName);
                        EditText editTextCatalogDisplayOrder = (EditText)dialogView.findViewById(R.id.editTextCatalogDisplayOrder);

                        SaveCatalog(editTextCatalogName.getText().toString(),
                                editTextCatalogDisplayOrder.getText().toString(),
                                hideOrShow.getText().toString());
                    }
                });
                catalog_mangater_dialog.setNegativeButton("彻底删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextCatalogName= (EditText)dialogView.findViewById(R.id.editTextCatalogName);
                        EditText editTextCatalogDisplayOrder = (EditText)dialogView.findViewById(R.id.editTextCatalogDisplayOrder);
                        Switch delSwitch = (Switch)dialogView.findViewById(R.id.delswitch);
                        SaveCatalog(editTextCatalogName.getText().toString(),
                                editTextCatalogDisplayOrder.getText().toString(), hideOrShow.getText().toString());
                    }
                });
                catalog_mangater_dialog.show();

                return true;
            }
        });

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
        unbinder.unbind();
    }

    private void SaveCatalog(String catalogName, String catalogDisplayOrder, String isEnabled)
    {
        Toast.makeText(getContext(),
                catalogName + " "+ catalogDisplayOrder+ " " + isEnabled,
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
        }, 3000);
    }

    private void loadData()
    {
        int dataIndex;
        int count = catalogAdapter.getCount();
        for(dataIndex = count; dataIndex < Math.min(count+EACH_COUNT, MAX_COUNT); dataIndex++) {
            Catalog model = new Catalog();
            model.setName("分类" + dataIndex);
            model.setDeleted(false);
            catalogAdapter.add(model);
        }
        //如果服务器上的全部数据都已加载完毕
        if (dataIndex==MAX_COUNT) {
            loadState=LOAD_STATE_FINISH;
        }
        else {
            loadState=LOAD_STATE_IDLE;
        }
    }

}
