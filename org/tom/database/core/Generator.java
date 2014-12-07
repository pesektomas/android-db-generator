package org.tom.database.core;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class Generator {

    private TableModel tableModel;
    private TableView tableView;

    private final List<Field> fields;
    private final List<Column> columnFields;

    private String tablePrimaryKeyColumn;
    private Object tablePrimaryKeyValue;

    private final String tableName;

    public Generator(TableView view) {
        this.tableView = view;
        this.tableName = view.getClass().getAnnotation(View.class).name();

        fields = new LinkedList<Field>();
        columnFields = new LinkedList<Column>();
    }

    public Generator(TableModel table) {
        this.tableModel = table;
        this.tableName = table.getClass().getAnnotation(Table.class).name();
        this.fields = new LinkedList<Field>();
        this.columnFields = new LinkedList<Column>();

        for (Field field : table.getClass().getFields()) {
            Column columnAnn = field.getAnnotation(Column.class);
            if (columnAnn == null) {
                continue;
            }
            this.columnFields.add(columnAnn);
            this.fields.add(field);

            if (columnAnn.primaryKey()) {
                this.tablePrimaryKeyColumn = columnAnn.name();
            }
        }
    }

    public String insert() throws IllegalArgumentException, IllegalAccessException {
        StringBuilder insert = new StringBuilder();

        insert.append("INSERT INTO ");
        insert.append(tableName);


        StringBuilder insertColumn = new StringBuilder();
        StringBuilder insertValues = new StringBuilder();
        int count = 1;

        for (Field field : fields) {
            Column columnAnn = field.getAnnotation(Column.class);
            insertColumn.append(columnAnn.name());

            if (columnAnn.type() == Column.TEXT) {
                insertValues.append(String.format("'%s'", field.get(tableModel)));
            } else {
                insertValues.append(field.get(tableModel));
            }
            if (fields.size() != count) {
                insertColumn.append(", ");
                insertValues.append(", ");
            }
            count++;
        }
        insert.append(" (");
        insert.append(insertColumn.toString());
        insert.append(" ) VALUES (");
        insert.append(insertValues.toString());
        insert.append(" );");

        return insert.toString();
    }

    public String createTable() {
        StringBuilder createTable = new StringBuilder();

        createTable.append("CREATE TABLE ");
        createTable.append(tableName);
        createTable.append(" (");

        int i = 1;
        for (Column columnAnnot : columnFields) {
            createTable.append(columnAnnot.name());

            if (columnAnnot.type() == Column.INTEGER) {
                createTable.append(" INTEGER");
            } else if (columnAnnot.type() == Column.TEXT) {
                createTable.append(" TEXT");
            } else if (columnAnnot.type() == Column.NUMERIC) {
                createTable.append(" NUMERIC");
            } else if (columnAnnot.type() == Column.REAL) {
                createTable.append(" REAL");
            } else if (columnAnnot.type() == Column.BLOB) {
                createTable.append(" BLOB");
            }

            if (columnAnnot.primaryKey()) {
                createTable.append("  PRIMARY KEY");
                if (columnAnnot.autoIncrement()) {
                    createTable.append(" AUTOINCREMENT");
                }
            }
            if (i != columnFields.size()) {
                createTable.append(", ");
            }
            i++;
        }
        createTable.append(" );");
        return createTable.toString().trim();
    }

    public String createView(){

        if(tableView == null){
            return null;
        }

        StringBuilder cv = new StringBuilder();

        cv.append("CREATE VIEW ");
        cv.append(tableName);
        cv.append(" AS ");

        cv.append(tableView.getCreateSQL());

        return cv.toString();
    }

    public String dropTable() {
        StringBuilder dropTable = new StringBuilder();
        dropTable.append("DROP TABLE IF EXISTS ");
        dropTable.append(tableName);
        return dropTable.toString().trim();
    }

    public String dropView() {
        StringBuilder dropTable = new StringBuilder();
        dropTable.append("DROP VIEW IF EXISTS ");
        dropTable.append(tableName);
        return dropTable.toString().trim();
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumnFields() {
        return columnFields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getTablePrimaryKeyColumn() {
        return tablePrimaryKeyColumn;
    }

    public Object getTablePrimaryKeyValue() {
        return tablePrimaryKeyValue;
    }
}
