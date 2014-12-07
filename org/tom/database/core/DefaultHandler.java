package org.tom.database.core;

import android.database.sqlite.SQLiteDatabase;

public abstract class DefaultHandler {

    protected TableModel model;
    protected Generator gen;

    public DefaultHandler(TableModel model) {
        this.model = model;
        gen = new Generator(model);
    }

    public TableModel getModel() {
        return model;
    }

    public Generator getGenerator() {
        return gen;
    }

    public void delete(SQLiteDatabase db){
        db.delete(getModel().getTableName(), null, null);
    }
}
