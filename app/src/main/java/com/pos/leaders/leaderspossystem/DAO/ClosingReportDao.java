/**package com.pos.leaders.leaderspossystem.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pos.leaders.leaderspossystem.Models.ClosingReport;

import java.util.List;

/**
 * Created by Win8.1 on 11/12/2018.

@Dao
public interface ClosingReportDao {
    @Insert
    void insert(ClosingReport closingReport);

    @Query("DELETE FROM closing_report")
    void deleteAll();

    @Query("SELECT * from closing_report ORDER BY closingReportId ASC")
    List<ClosingReport> getAllWords();

    @Insert
  void create(ClosingReport closingReport);

    @Query("SELECT * FROM closing_report WHERE closingReportId = :id")
    ClosingReport getById(Long id);

    @Query("SELECT * FROM closing_report;")
    LiveData<List<ClosingReport>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Iterable<ClosingReport> closingReport);

    @Update
    void update(ClosingReport closingReport);

    @Delete
    void remove(ClosingReport closingReport);
}
**/