package org.tom.database.core;

import android.database.sqlite.SQLiteDatabase;

import org.tom.database.core.Creator;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {

    private AtomicInteger openCounter = new AtomicInteger();

    private static Creator databaseHelper;
    private static DatabaseManager instance;
    private SQLiteDatabase database;


    public static synchronized void initialize(Creator helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            databaseHelper = helper;
        }
    }

    private DatabaseManager() {}

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() + " is not initialized, call initialize(..) method first.");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (openCounter.incrementAndGet() == 1) {
            database = databaseHelper.getWritableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase() {
        if (openCounter.decrementAndGet() == 0) {
            database.close();
        }
    }
}
