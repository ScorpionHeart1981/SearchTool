package com.example.xchen.searchtool.Service;

import com.example.xchen.searchtool.Domain.Catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by XChen on 4/27/2018.
 */

public class CatalogService {
    public void CreateCatalog(Realm realm, final String name, final int displayOrder, final Boolean isEnabled){
        /*realm.beginTransaction();
        Catalog catalog = realm.createObject(Catalog.class, UUID.randomUUID().toString());
        catalog.setName(name);
        catalog.setDisplayOrder(displayOrder);
        catalog.setIsEnabled(isEnabled);
        realm.commitTransaction();*/

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Catalog catalog = realm.createObject(Catalog.class, UUID.randomUUID().toString());
                catalog.setName(name);
                catalog.setDisplayOrder(displayOrder);
                catalog.setIsEnabled(isEnabled);
            }
        });
    }

    public void UpdateCatalog(Realm realm, final String id, final String name, final int displayOrder, final boolean isEnabled){
        final Catalog catalog = QueryCatalogById(realm, id);
        if(catalog == null)
            return;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                catalog.setName(name);
                catalog.setDisplayOrder(displayOrder);
                catalog.setIsEnabled(isEnabled);
            }
        });
    }

    public void DeleteCatalog(Realm realm, String id){
        Catalog catalog = QueryCatalogById(realm, id);
        if(catalog == null)
            return;

        realm.beginTransaction();
        catalog.items.deleteAllFromRealm();
        catalog.deleteFromRealm();
        realm.commitTransaction();
    }

    public Catalog QueryCatalogById(Realm realm, String id){
        Catalog catalog = realm.where(Catalog.class).equalTo("id",id).findFirst();
        return catalog;
    }

    public List<Catalog> GetAllCatalog(Realm realm, int startIndex, int finalResult){
        RealmResults<Catalog> list = realm.where(Catalog.class).findAll().sort("displayOrder", Sort.ASCENDING);
        List<Catalog> retList = new ArrayList<>();
        for(int i = startIndex; i < finalResult; i++){
            retList.add(list.get(i));
        }

        return retList;
    }

    public List<Catalog> FindEnabledCatalog(Realm realm, int startIndex, int finalResult){
        RealmResults<Catalog> list = realm.where(Catalog.class).equalTo("isEnabled",true).findAll().sort("displayOrder", Sort.ASCENDING);
        List<Catalog> retList = new ArrayList<>();
        for(int i = startIndex; i < finalResult; i++){
            retList.add(list.get(i));
        }

        return retList;
    }

    public int GetAllCatalogSize(Realm realm){
        return realm.where(Catalog.class).findAll().size();
    }

    public int GetEnabledCatalogSize(Realm realm){
        return realm.where(Catalog.class).equalTo("isEnabled",true).findAll().size();
    }
}
