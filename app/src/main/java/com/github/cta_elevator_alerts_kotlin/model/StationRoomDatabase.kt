package com.github.cta_elevator_alerts_kotlin.model

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database (station_table) for stations
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Database(entities = [Station::class], version = 1)
abstract class StationRoomDatabase : RoomDatabase() {
    //
    //    private static final StationRoomDatabase.Callback sStationRoomDatabaseCallback = new StationRoomDatabase.Callback(){
    //
    //        @Override
    //        public void onCreate (@NonNull SupportSQLiteDatabase db){
    //            super.onCreate(db);
    //            Thread thread = new Thread() {
    //                public void run() {
    //                    StationDao stationDao = INSTANCE.stationDao();
    //
    //
    //                }
    //            };
    //            thread.start();
    //        }
    //    };

    val dao: StationDao
        get() = stationDao()

    abstract fun stationDao(): StationDao

    companion object {

        @Volatile
        private var INSTANCE: StationRoomDatabase? = null

        internal fun getDatabase(context: Context): StationRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(StationRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                StationRoomDatabase::class.java, "station_database")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}