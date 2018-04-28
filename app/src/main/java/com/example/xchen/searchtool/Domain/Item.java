package com.example.xchen.searchtool.Domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by CX on 2018/4/18.
 */

public class Item extends RealmObject {
    public String title;
    public String url;
    public Boolean isEnabled;
    public int displayOrder;
    public String imageEngine;

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

    public Boolean getIsEnabled()
    {
        return isEnabled;
    }
    public void setIsEnabled(Boolean deleted)
    {
        this.isEnabled = deleted;
    }

    public int getDisplayOrder(){ return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public String getImageEngine(){return imageEngine;}
    public void setImageEngine(String imageEngine){this.imageEngine = imageEngine;}
}
