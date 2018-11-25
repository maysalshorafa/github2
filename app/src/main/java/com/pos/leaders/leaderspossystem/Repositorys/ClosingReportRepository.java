/*package com.pos.leaders.leaderspossystem.Repositorys;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.pos.leaders.leaderspossystem.DAO.ClosingReportDao;
import com.pos.leaders.leaderspossystem.DataSource.RoomDB;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Win8.1 on 11/13/2018.


public class ClosingReportRepository implements Repository<ClosingReport,Long> {
    private ClosingReportDao closingReportDao;
    private LiveData<List<ClosingReport>> allClosingReport;
    public ClosingReportRepository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        closingReportDao = db.closingReportDao();
        allClosingReport = closingReportDao.getAll();
    }
    public LiveData<List<ClosingReport>> getAll(){
        return allClosingReport;
    }
    @Override
    public void add(ClosingReport item) {
        new InsertAsyncTask(closingReportDao).execute(item);
    }
    @Override
    public ClosingReport create(ClosingReport item) {
        return null;
    }
    @Override
    public ClosingReport getById(Long id) throws ExecutionException, InterruptedException {
        return new GetAsyncTask(closingReportDao).execute(id).get();
    }
    @Override
    public void add(Iterable<ClosingReport> items) {
        new InsertAllAsyncTask(closingReportDao).execute(items);
    }
    @Override
    public void update(ClosingReport item) {
        new UpdateAsyncTask(closingReportDao).execute(item);
    }
    @Override
    public void remove(ClosingReport item) {
        new RemoveAsyncTask(closingReportDao).execute(item);
    }
    @Override
    public List<ClosingReport> query(Specification specification) {
        return null;
    }
    private static class InsertAsyncTask extends AsyncTask<ClosingReport, Void, Void> {
        private ClosingReportDao closingReportDao;
        InsertAsyncTask(ClosingReportDao closingReportDao) {
            this.closingReportDao = closingReportDao;
        }
        @Override
        protected Void doInBackground(ClosingReport... closingReport) {
            this.closingReportDao.add(closingReport[0]);
            return null;
        }
    }
    private static class GetAsyncTask extends AsyncTask<Long, Void, ClosingReport> {
        private ClosingReportDao closingReportDao;
        GetAsyncTask(ClosingReportDao closingReportDao) {
            this.closingReportDao = closingReportDao;
        }
        @Override
        protected ClosingReport doInBackground(Long... Ids) {
            return closingReportDao.getById(Ids[0]);
        }
    }
    private static class InsertAllAsyncTask extends AsyncTask<Iterable<ClosingReport>,Void,Void>{
        private ClosingReportDao closingReportDao;
        InsertAllAsyncTask(ClosingReportDao closingReportDao) {
            this.closingReportDao = closingReportDao;
        }
        @SafeVarargs
        @Override
        protected final Void doInBackground(Iterable<ClosingReport>... trackings) {
            closingReportDao.add(trackings[0]);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<ClosingReport, Void, Void> {
        ClosingReportDao closingReportDao;
        public UpdateAsyncTask(ClosingReportDao closingReportDao) {
            this.closingReportDao = closingReportDao;
        }
        @Override
        protected Void doInBackground(ClosingReport... transactionsTrackings) {
            closingReportDao.update(transactionsTrackings[0]);
            return null;
        }
    }
    private static class RemoveAsyncTask extends AsyncTask<ClosingReport,Void,Void>{
        ClosingReportDao closingReportDao;
        RemoveAsyncTask(ClosingReportDao closingReportDao) {
            this.closingReportDao = closingReportDao;
        }
        @Override
        protected Void doInBackground(ClosingReport... transactionsTrackings) {
            closingReportDao.remove(transactionsTrackings[0]);
            return null;
        }
    }
}
**/
