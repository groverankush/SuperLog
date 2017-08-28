package com.ankushgrover.superlog.db.helpers;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;

import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.DbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.db.table.SuperLogTable;
import com.ankushgrover.superlog.model.SuperLogModel;
import com.ankushgrover.superlog.utils.TimeUtils;


/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class SuperLogDbHelper implements SuperLogConstants {

    public static final int INSERT_LOG = 100;

    private static SuperLogDbHelper mInstance;

    public static synchronized SuperLogDbHelper getInstance() {
        if (mInstance == null)
            mInstance = new SuperLogDbHelper();
        return mInstance;
    }

    private void insert(SuperLogModel log) {
        ContentValues values = new ContentValues();
        values.put(SuperLogTable.TAG, log.getTag());
        values.put(SuperLogTable.MESSAGE, log.getMessage());
        values.put(SuperLogTable.TYPE, log.getType());
        values.put(SuperLogTable.TIMESTAMP, TimeUtils.getTimeStamp());

        DbHelper.getInstance().getWritableDatabase().insert(SuperLogTable.TABLE_NAME, null, values);

    }


    public void perform(int taskId, Bundle bundle, DataLoadListener<Object> listener) {
        new LogAsyncTask(taskId, bundle, listener);
    }

    private class LogAsyncTask extends AsyncTask<Void, Void, Object> {

        private final int taskId;
        private final Bundle bundle;
        private final DataLoadListener<Object> listener;

        LogAsyncTask(int taskId, Bundle bundle, DataLoadListener<Object> listener) {
            this.taskId = taskId;
            this.bundle = bundle;
            this.listener = listener;

            execute();
        }

        @Override
        protected Object doInBackground(Void... voids) {

            SuperLogModel log;

            switch (taskId) {
                case INSERT_LOG:
                    log = bundle.getParcelable(LOG);
                    insert(log);
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            listener.onDataLoaded(o, taskId);
        }
    }

}
