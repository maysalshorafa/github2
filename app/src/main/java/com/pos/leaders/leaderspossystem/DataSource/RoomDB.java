package com.pos.leaders.leaderspossystem.DataSource;

/**
 * Created by Win8.1 on 11/13/2018.
 */

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pos.leaders.leaderspossystem.DAO.ClosingReportDetailsDao;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.DAO.ClosingReportDao;
import com.pos.leaders.leaderspossystem.Models.ClosingReportDetails;

@Database(entities = {ClosingReport.class, ClosingReportDetails.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB INSTANCE;
    private static final String DB_NAME = "app_db";
    public static RoomDB getDatabase(final Context context) {
        if(INSTANCE==null){
            synchronized (RoomDB.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, RoomDB.class, DB_NAME)
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }
    public static void destroyInstance(){
        INSTANCE = null;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
    public abstract ClosingReportDao closingReportDao();
    public abstract ClosingReportDetailsDao closingReportDetailsDao();

}
