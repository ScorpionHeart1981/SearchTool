package com.example.xchen.searchtool.Service;

import com.example.xchen.searchtool.Domain.Item;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by XChen on 4/27/2018.
 */

public class ItemService {
    public void CreateItem(Realm realm, String title, String url, int displayOrder, Boolean isEnabled, String imageEngine){
        realm.beginTransaction();
        Item item = realm.createObject(Item.class);
        item.setTitle(title);
        item.setUrl(url);
        item.setDisplayOrder(displayOrder);
        item.setIsEnabled(isEnabled);
        item.setImageEngine(imageEngine);
        realm.commitTransaction();
    }

    public void UpdateItem(Realm realm, String id, String title, String url, int displayOrder, boolean isEnabled, String imageEngine){
        Item item = QueryItemById(realm, id);
        if(item == null)
            return;

        realm.beginTransaction();
        item.setTitle(title);
        item.setUrl(url);
        item.setDisplayOrder(displayOrder);
        item.setIsEnabled(isEnabled);
        item.setImageEngine(imageEngine);
        realm.commitTransaction();
    }

    public void DeleteItem(Realm realm, String id){
        Item item = QueryItemById(realm, id);
        if(item == null)
            return;

        realm.beginTransaction();
        item.deleteFromRealm();
        realm.commitTransaction();
    }

    public Item QueryItemById(Realm realm, String id){
        Item item = realm.where(Item.class).equalTo("id",id).findFirst();
        return item;
    }

    public List<Item> GetAllItem(Realm realm){
        return realm.where(Item.class).findAll().sort("displayOrder");
    }

    public List<Item> FindEnableItem(Realm realm){
        return realm.where(Item.class).equalTo("isEnabled",true).findAll().sort("displayOrder", Sort.ASCENDING);
    }
}
