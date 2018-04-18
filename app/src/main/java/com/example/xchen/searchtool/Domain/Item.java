package com.example.xchen.searchtool.Domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by CX on 2018/4/18.
 */

public class Item extends RealmObject {
    public String title;
    public String url;
    public Boolean deleted;

    @PrimaryKey
    public String id;

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return title;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public Boolean getDeleted()
    {
        return deleted;
    }
    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }
}
