package com.example.xchen.searchtool.Service;

import com.example.xchen.searchtool.Domain.Catalog;
import com.example.xchen.searchtool.Domain.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by XChen on 4/27/2018.
 */

public class ItemService {
    public void CreateItem(Realm realm, final String title, final String url, final int displayOrder,
                           final Boolean isEnabled, final Boolean isImage, final String imageEngine, final String catalogId){
        final Catalog catalog = realm.where(Catalog.class).equalTo("id",catalogId).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item item = realm.createObject(Item.class, UUID.randomUUID().toString());
                item.setTitle(title);
                item.setUrl(url);
                item.setDisplayOrder(displayOrder);
                item.setIsEnabled(isEnabled);
                item.setIsImage(isImage);
                item.setImageEngine(imageEngine);

                catalog.items.add(item);
            }
        });
    }

    public void UpdateItem(Realm realm, final String id, final String title, final String url, final int displayOrder,
                           final Boolean isEnabled, final Boolean isImage, final String imageEngine){
        final Item item = QueryItemById(realm, id);
        if(item == null)
            return;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setTitle(title);
                item.setUrl(url);
                item.setDisplayOrder(displayOrder);
                item.setIsImage(isImage);
                item.setIsEnabled(isEnabled);
                item.setImageEngine(imageEngine);
            }
        });
    }

    public void DeleteItem(Realm realm, String itemId, String catalogId){
        Item item = QueryItemById(realm, itemId);
        if(item == null)
            return;

        final Catalog catalog = realm.where(Catalog.class).equalTo("id",catalogId).findFirst();

        realm.beginTransaction();
        item.deleteFromRealm();
        realm.commitTransaction();
    }

    public Item QueryItemById(Realm realm, String id){
        Item item = realm.where(Item.class).equalTo("id",id).findFirst();
        return item;
    }

    public List<Item> GetAllItem(Realm realm, int startIndex, int finalResult){
        RealmResults<Item> list = realm.where(Item.class).findAll().sort("displayOrder", Sort.ASCENDING);
        List<Item> retList = new ArrayList<>();
        for(int i = startIndex; i < finalResult; i++){
            retList.add(list.get(i));
        }
        return retList;
    }

    public List<Item> FindEnableItem(Realm realm, int startIndex, int finalResult){
        RealmResults<Item> list = realm.where(Item.class).equalTo("isEnabled",true).findAll().sort("displayOrder", Sort.ASCENDING);
        List<Item> retList = new ArrayList<>();
        for(int i = startIndex; i < finalResult; i++){
            retList.add(list.get(i));
        }
        return retList;
    }

    public int GetAllItemSize(Realm realm){
        return realm.where(Item.class).findAll().size();
    }

    public int GetEnabledItemsSize(Realm realm){
        return realm.where(Item.class).equalTo("isEnabled",true).findAll().size();
    }
}
