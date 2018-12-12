/*
Room DB
Created by Maxim G. on December 12, 2018
*/

package com.dvinasystems.heroes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = Person.class, version = 1)
abstract class PeopleDatabase extends RoomDatabase {
    public abstract PersonDao personDAO();

    private static PeopleDatabase databaseInstance;

    static PeopleDatabase getDatabase(final Context context) {
        if (databaseInstance == null) {
            synchronized (PeopleDatabase.class) {
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(context.getApplicationContext(), PeopleDatabase.class, "People_Database").build();
                }
            }
        }
        return databaseInstance;
    }
}
