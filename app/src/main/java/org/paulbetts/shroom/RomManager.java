package org.paulbetts.shroom;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.paulbetts.shroom.cloudapi.CloudFileApi;
import org.paulbetts.shroom.core.CupboardSQLiteOpenHelper;
import org.paulbetts.shroom.models.RomInfo;

import javax.inject.Inject;

import nl.qbusict.cupboard.DatabaseCompartment;
import rx.Observable;
import rx.Subscriber;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class RomManager {
    @Inject
    CupboardSQLiteOpenHelper db;

    @Inject
    CloudFileApi dropboxApi;

    public Observable<PlayableRom> scanForRomsFull() {
        return Observable.create((Subscriber<? super PlayableRom> subscriber) -> {
            Observable<PlayableRom> scan =  dropboxApi.scanForRoms()
                    .map(x ->  new PlayableRom(x))
                    .publish()
                    .refCount();

            subscriber.add(scan.toList().subscribe(xs -> {
                SQLiteDatabase sqlite = db.getWritableDatabase();
                DatabaseCompartment database = cupboard().withDatabase(sqlite);

                database.delete(PlayableRom.class, "");

                sqlite.beginTransaction();

                for(PlayableRom x: xs) {
                    database.put(x.cloudRomInfo);
                    database.put(x);
                }

                sqlite.setTransactionSuccessful();
                sqlite.endTransaction();
                sqlite.close();
            }));

            scan.subscribe(subscriber);
        });
    }
}
