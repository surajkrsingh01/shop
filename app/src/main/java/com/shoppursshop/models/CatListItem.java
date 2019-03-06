package com.shoppursshop.models;

import java.util.List;

public class CatListItem {
    private String title,desc;
    private int id,type,position;
    private boolean isSelectingAll;
    private List<Object> itemList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Object> getItemList() {
        return itemList;
    }

    public void setItemList(List<Object> itemList) {
        this.itemList = itemList;
    }

    public boolean isSelectingAll() {
        return isSelectingAll;
    }

    public void setSelectingAll(boolean selectingAll) {
        isSelectingAll = selectingAll;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
