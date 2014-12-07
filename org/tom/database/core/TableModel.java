package org.tom.database.core;

public abstract class TableModel {

    protected String tableName;

    public TableModel(String tableName){
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
