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

    @NonNull
    @Override
    public String toString() {
        return "Item [id=" + id + ", listId=" + listId + ", name=" + name + "]";
    }
}
