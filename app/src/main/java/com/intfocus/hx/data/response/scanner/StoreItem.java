package com.intfocus.hx.data.response.scanner;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/22 上午09:48
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */
@DatabaseTable(tableName = "store_data")
public class StoreItem implements Serializable {
    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;
    @DatabaseField(columnName = "obj_id", dataType = DataType.STRING, defaultValue = "")
    private String id;
    @DatabaseField(columnName = "name", dataType = DataType.STRING, defaultValue = "")
    private String name;

    public StoreItem() {
    }

    public StoreItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
