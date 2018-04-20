package com.example.xchen.searchtool.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xchen.searchtool.Domain.Catalog;
import com.example.xchen.searchtool.R;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XChen on 4/20/2018.
 */

public class PaginationAdapter<T> extends BaseAdapter {
    ArrayList<T> items;
    String classname;

    public PaginationAdapter(ArrayList<T> paramItems){
        this.items = paramItems;
    }

    @Override
    public int getCount() {
        return items == null? 0 : items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(classname == null || classname.isEmpty())
            classname = items.get(0).getClass().getSimpleName().toLowerCase();

        if(classname.contains("catalog")){
            CatalogViewHolder viewHolder;
            if (convertView == null) {
                convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_manager_item,
                        null);
                viewHolder = new CatalogViewHolder();
                viewHolder.tv_catalogName = (TextView) convertView
                        .findViewById(R.id.txtcatalogmanageritemtitle);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CatalogViewHolder)convertView.getTag();
            }
            Catalog catalog = (Catalog)items.get(position);
            viewHolder.tv_catalogName.setText(catalog.getName());
        }

        return convertView;
    }

    public void add(T model) {
        if(items == null){
            items = new ArrayList<>();
        }
        items.add(model);
    }

    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    class CatalogViewHolder{
        TextView tv_catalogName;
    }
}
