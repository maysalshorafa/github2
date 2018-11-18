package com.pos.leaders.leaderspossystem.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pos.leaders.leaderspossystem.Models.ClosingReportDetails;

import java.util.List;

/**
 * Created by Win8.1 on 11/13/2018.
 */
@Dao
public interface ClosingReportDetailsDao {
    @Insert
    void insert(ClosingReportDetails closingReportDetails);

    @Query("DELETE FROM closing_report_details")
    void deleteAll();

    @Query("SELECT * from closing_report_details ORDER BY closing_report_detailsId ASC")
    List<ClosingReportDetails> getAllWords();

    @Insert
 void create(ClosingReportDetails closingReportDetails);

    @Query("SELECT * FROM closing_report_details WHERE closing_report_detailsId = :id")
    ClosingReportDetails getById(Long id);

    @Query("SELECT * FROM closing_report_details;")
    LiveData<List<ClosingReportDetails>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Iterable<ClosingReportDetails> closingReportDetails);

    @Update
    void update(ClosingReportDetails closingReportDetails);

    @Delete
    void remove(ClosingReportDetails closingReportDetails);
}
