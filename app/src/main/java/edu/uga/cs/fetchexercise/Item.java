package edu.uga.cs.fetchexercise;

import androidx.annotation.NonNull;

public class Item {

    int id;
    int listId;
    String name;

    public Item(int id, int listId, String name) {
        this.id = id;
        this.listId = listId;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item [id=" + id + ", listId=" + listId + ", name=" + name + "]";
    }
}
