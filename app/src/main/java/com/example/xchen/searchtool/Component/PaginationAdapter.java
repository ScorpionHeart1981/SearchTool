package com.example.xchen.searchtool.Component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xchen.searchtool.Domain.Catalog;
import com.example.xchen.searchtool.Domain.Item;
import com.example.xchen.searchtool.R;

import java.util.ArrayList;
import java.util.List;

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
                convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.admincatalogfragment_listviewitem_layout,
                        null);
                viewHolder = new CatalogViewHolder();
                viewHolder.tv_catalogName = (TextView) convertView.findViewById(R.id.txtcatalogmanageritemtitle);
                viewHolder.tv_catalogDisplayOrder = (TextView) convertView.findViewById(R.id.txtcatalogmanageritemdisplayorder);
                viewHolder.tv_catalogIndex = (TextView) convertView.findViewById(R.id.txtcatalogmanageritemindex);
                viewHolder.tv_catalogIsEnabled = (TextView) convertView.findViewById(R.id.txtcatalogmanageritemisenable);
                viewHolder.img_catalogIsEnabled = (ImageView) convertView.findViewById(R.id.txtcatalogmanageritemisenableimage);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CatalogViewHolder)convertView.getTag();
            }
            Catalog catalog = (Catalog)items.get(position);
            viewHolder.tv_catalogName.setText(catalog.getName());
            viewHolder.tv_catalogDisplayOrder.setText(String.valueOf(catalog.getDisplayOrder()));
            viewHolder.tv_catalogIndex.setText(catalog.getId());
            viewHolder.tv_catalogIsEnabled.setText(catalog.getIsEnabled()? "是" : "否");
            if(catalog.getIsEnabled()) {
                viewHolder.img_catalogIsEnabled.setImageResource(R.drawable.ic_check_circle_black_24dp);
                Drawable mydrawable = viewHolder.img_catalogIsEnabled.getDrawable();
                mydrawable.setTint(Color.GREEN);
            }
            else {
                viewHolder.img_catalogIsEnabled.setImageResource(R.drawable.ic_highlight_off_black_24dp);
                Drawable mydrawable = viewHolder.img_catalogIsEnabled.getDrawable();
                mydrawable.setTint(Color.RED);
            }
        }
        else if(classname.contains("item")){
            ItemViewHolder viewHolder;
            if (convertView == null) {
                convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.adminitemfragment_listviewitem_layout,
                        null);
                viewHolder = new ItemViewHolder();
                viewHolder.img_itemIsEnabled = (ImageView)convertView.findViewById(R.id.imgitemmanageritemisenabledimage);
                viewHolder.tv_itemName = (TextView) convertView.findViewById(R.id.txtitemmanageritemtitle);
                viewHolder.img_itemIsImage = (ImageView)convertView.findViewById(R.id.imgitemmanageritemisimageimage);
                viewHolder.tv_itemDisplayOrder = (TextView) convertView.findViewById(R.id.txtitemmanageritemdisplayorder);
                viewHolder.tv_itemIndex = (TextView) convertView.findViewById(R.id.txtitemmanageritemindex);
                viewHolder.tv_itemUrl = (TextView) convertView.findViewById(R.id.txtitemmanageritemurl);
                viewHolder.tv_itemIsImage = (TextView) convertView.findViewById(R.id.txtitemmanageritemisiamge);
                viewHolder.tv_itemIsEnabled = (TextView) convertView.findViewById(R.id.txtcitemmanageritemisenabled);
                viewHolder.ll_itemUrl = (LinearLayoutCompat) convertView.findViewById(R.id.listitemmiddlelinear);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ItemViewHolder)convertView.getTag();
            }
            Item item = (Item) items.get(position);
            viewHolder.tv_itemName.setText(item.getTitle());
            viewHolder.tv_itemUrl.setText(item.getUrl());
            viewHolder.tv_itemIndex.setText(item.getId());
            viewHolder.tv_itemDisplayOrder.setText(String.valueOf(item.getDisplayOrder()));
            viewHolder.tv_itemIsImage.setText(item.getIsImage()? "是" : "否");
            viewHolder.tv_itemIsEnabled.setText(item.getIsEnabled()? "是" : "否");
            if(item.getIsEnabled()) {
                viewHolder.img_itemIsEnabled.setImageResource(R.drawable.ic_check_circle_black_24dp);
                Drawable mydrawable = viewHolder.img_itemIsEnabled.getDrawable();
                mydrawable.setTint(Color.GREEN);
            }
            else {
                viewHolder.img_itemIsEnabled.setImageResource(R.drawable.ic_highlight_off_black_24dp);
                Drawable mydrawable = viewHolder.img_itemIsEnabled.getDrawable();
                mydrawable.setTint(Color.RED);
            }
            if(item.getIsImage()){
                viewHolder.img_itemIsImage.setVisibility(View.VISIBLE);
                viewHolder.ll_itemUrl.setVisibility(View.GONE);
            }
            else{
                viewHolder.img_itemIsImage.setVisibility(View.INVISIBLE);
                viewHolder.ll_itemUrl.setVisibility(View.VISIBLE);
            }
        }

        if(position % 2 == 1){
            convertView.setBackgroundColor(Color.WHITE);
        }
        else {
            convertView.setBackgroundColor(Color.parseColor("#EC9FD5"));
        }

        return convertView;
    }

    public void add(T model) {
        if(items == null){
            items = new ArrayList<>();
        }
        items.add(model);
    }

    public void add(List<T> list){
        if(items == null) {
            items = new ArrayList<>();
        }
        items.addAll(list);
    }

    public void clear(){
        if(items != null)
            items.clear();
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
        TextView tv_catalogDisplayOrder;
        TextView tv_catalogIndex;
        TextView tv_catalogIsEnabled;
        ImageView img_catalogIsEnabled;
    }

    class ItemViewHolder{
        TextView tv_itemName;
        TextView tv_itemUrl;
        TextView tv_itemIndex;
        TextView tv_itemDisplayOrder;
        TextView tv_itemIsImage;
        ImageView img_itemIsImage;
        TextView tv_itemIsEnabled;
        ImageView img_itemIsEnabled;
        LinearLayoutCompat ll_itemUrl;
    }
}
