package org.paulbetts.shroom.core;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

public class CupboardSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shroom.db";
    private static final int DATABASE_VERSION = 1;

    @Inject
    public CupboardSQLiteOpenHelper(RxDaggerActivity activity, CupboardModuleRegistration moduleRegistration) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
        moduleRegistration.register(cupboard());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // this will ensure that all tables are created
        cupboard().withDatabase(db).createTables();
        // add indexes and other database tweaks
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        cupboard().withDatabase(db).upgradeTables();
        // do migration work
    }
}
