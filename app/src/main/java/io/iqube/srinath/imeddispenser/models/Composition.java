package io.iqube.srinath.imeddispenser.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Composition extends RealmObject {

    @SerializedName("id")
    @PrimaryKey
    int id;

    @SerializedName("name")
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
