package com.example.xchen.searchtool.Domain;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by CX on 2018/4/18.
 */

public class Catalog extends RealmObject {
    public String name;
    public Boolean deleted;
    public int displayOrder;

    @PrimaryKey
    private String id;

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getDeleted()
    {
        return deleted;
    }
    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }

    public int getDisplayOrder(){ return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public RealmList<Item> items;
}
