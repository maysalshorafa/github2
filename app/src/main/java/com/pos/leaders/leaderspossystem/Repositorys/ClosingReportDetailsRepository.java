package com.pos.leaders.leaderspossystem.Repositorys;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.pos.leaders.leaderspossystem.DAO.ClosingReportDetailsDao;
import com.pos.leaders.leaderspossystem.DataSource.RoomDB;
import com.pos.leaders.leaderspossystem.Models.ClosingReportDetails;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Win8.1 on 11/13/2018.
 */

public class ClosingReportDetailsRepository implements Repository<ClosingReportDetails,Long> {
    private ClosingReportDetailsDao closingReportDetailsDao;
    private LiveData<List<ClosingReportDetails>> allClosingReportDetails;
    public ClosingReportDetailsRepository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        closingReportDetailsDao = db.closingReportDetailsDao();
        allClosingReportDetails = closingReportDetailsDao.getAll();
    }
    public LiveData<List<ClosingReportDetails>> getAll(){
        return allClosingReportDetails;
    }
    @Override
    public void add(ClosingReportDetails item) {
        new ClosingReportDetailsRepository.InsertAsyncTask(closingReportDetailsDao).execute(item);
    }
    @Override
    public ClosingReportDetails create(ClosingReportDetails item) {
        return null;
    }
    @Override
    public ClosingReportDetails getById(Long id) throws ExecutionException, InterruptedException {
        return new ClosingReportDetailsRepository.GetAsyncTask(closingReportDetailsDao).execute(id).get();
    }
    @Override
    public void add(Iterable<ClosingReportDetails> items) {
        new ClosingReportDetailsRepository.InsertAllAsyncTask(closingReportDetailsDao).execute(items);
    }
    @Override
    public void update(ClosingReportDetails item) {
        new ClosingReportDetailsRepository.UpdateAsyncTask(closingReportDetailsDao).execute(item);
    }
    @Override
    public void remove(ClosingReportDetails item) {
        new ClosingReportDetailsRepository.RemoveAsyncTask(closingReportDetailsDao).execute(item);

    }
    @Override
    public List<ClosingReportDetails> query(Specification specification) {
        return null;
    }
    private static class InsertAsyncTask extends AsyncTask<ClosingReportDetails, Void, Void> {
        private ClosingReportDetailsDao closingReportDetailsDao;
        InsertAsyncTask(ClosingReportDetailsDao closingReportDetailsDao) {
            this.closingReportDetailsDao = closingReportDetailsDao;
        }
        @Override
        protected Void doInBackground(ClosingReportDetails... closingReportDetailses) {
            this.closingReportDetailsDao.add(closingReportDetailses[0]);
            return null;
        }
    }
    private static class GetAsyncTask extends AsyncTask<Long, Void, ClosingReportDetails> {
        private ClosingReportDetailsDao closingReportDetailsDao;
        GetAsyncTask(ClosingReportDetailsDao closingReportDetailsDao) {
            this.closingReportDetailsDao = closingReportDetailsDao;
        }
        @Override
        protected ClosingReportDetails doInBackground(Long... Ids) {
            return closingReportDetailsDao.getById(Ids[0]);
        }
    }
    private static class InsertAllAsyncTask extends AsyncTask<Iterable<ClosingReportDetails>,Void,Void>{
        private ClosingReportDetailsDao closingReportDetailsDao;
        InsertAllAsyncTask(ClosingReportDetailsDao closingReportDetailsDao) {
            this.closingReportDetailsDao = closingReportDetailsDao;
        }
        @SafeVarargs
        @Override
        protected final Void doInBackground(Iterable<ClosingReportDetails>... trackings) {
            closingReportDetailsDao.add(trackings[0]);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<ClosingReportDetails, Void, Void> {
        ClosingReportDetailsDao closingReportDetailsDao;
        public UpdateAsyncTask(ClosingReportDetailsDao closingReportDetailsDao) {
            this.closingReportDetailsDao = closingReportDetailsDao;
        }
        @Override
        protected Void doInBackground(ClosingReportDetails... transactionsTrackings) {
            closingReportDetailsDao.update(transactionsTrackings[0]);
            return null;
        }
    }
    private static class RemoveAsyncTask extends AsyncTask<ClosingReportDetails,Void,Void>{
        ClosingReportDetailsDao closingReportDetailsDao;
        RemoveAsyncTask(ClosingReportDetailsDao closingReportDetailsDao) {
            this.closingReportDetailsDao = closingReportDetailsDao;
        }
        @Override
        protected Void doInBackground(ClosingReportDetails... transactionsTrackings) {
            closingReportDetailsDao.remove(transactionsTrackings[0]);
            return null;
        }
    }
}


