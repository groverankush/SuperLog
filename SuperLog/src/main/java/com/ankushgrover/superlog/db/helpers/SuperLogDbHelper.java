package com.ankushgrover.superlog.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.ankushgrover.superlog.ContextWrapper;
import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.DbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.db.table.SuperLogTable;
import com.ankushgrover.superlog.model.SuperLogModel;
import com.ankushgrover.superlog.utils.TimeUtils;
import com.ankushgrover.superlog.utils.Utils;

import java.util.ArrayList;


/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class SuperLogDbHelper implements SuperLogConstants {

    public static final int INSERT_LOG = 100;
    public static final int GET_ALL_LOGS = 101;
    public static final int GET_ALL_LOGS_FOR_MAIL = 102;

    private static SuperLogDbHelper mInstance;

    public static synchronized SuperLogDbHelper getInstance() {
        if (mInstance == null)
            mInstance = new SuperLogDbHelper();
        return mInstance;
    }

    /**
     * Method to insert log in database.
     *
     * @param log : log data in form of model.
     */
    private synchronized void insert(Context context, SuperLogModel log) {
        log.setTimestamp(TimeUtils.getTimeStamp());
        ContentValues values = new ContentValues();
        values.put(SuperLogTable.MESSAGE, log.getMessage());
        values.put(SuperLogTable.TYPE, log.getType());
        values.put(SuperLogTable.TIMESTAMP, log.getTimestamp());

        DbHelper.getInstance(context).getWritableDatabase().insert(SuperLogTable.TABLE_NAME, null, values);
        sendBroadcast(context, ACTION_LOG_INSERTED, log);
    }


    private synchronized ArrayList<SuperLogModel> getLogs(Context context) {
        ArrayList<SuperLogModel> logs = new ArrayList<>();

        Cursor cursor = DbHelper.getInstance(context).getReadableDatabase().query(SuperLogTable.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            SuperLogModel log = new SuperLogModel();
            log.setTimestamp(cursor.getString(cursor.getColumnIndex(SuperLogTable.TIMESTAMP)));
            log.setMessage(cursor.getString(cursor.getColumnIndex(SuperLogTable.MESSAGE)));
            log.setType(cursor.getInt(cursor.getColumnIndex(SuperLogTable.TYPE)));
            logs.add(log);
        }

        cursor.close();

        return logs;

    }

    /**
     * Method to get logs string for mailing purposes.
     *
     * @return
     */
    private synchronized String getLogsString(Context context, boolean forMail) {


        StringBuilder logs = new StringBuilder();

        Cursor cursor = DbHelper.getInstance(context).getReadableDatabase().query(SuperLogTable.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            SuperLogModel log = new SuperLogModel();
            log.setTimestamp(cursor.getString(cursor.getColumnIndex(SuperLogTable.TIMESTAMP)));
            log.setMessage(cursor.getString(cursor.getColumnIndex(SuperLogTable.MESSAGE)));
            log.setType(cursor.getInt(cursor.getColumnIndex(SuperLogTable.TYPE)));

            logs.append(Utils.getLogStringForMail(log.getMessage(), log.getTimestamp()));
        }

        cursor.close();

        if (forMail) {
            if (!Utils.isEmpty(logs.toString())) {
                String deviceInfo = "Device: " + Build.BRAND + " " + Build.MODEL + "\n" + "OS Version: " + Build.VERSION.RELEASE + "\n\n";
                logs.insert(0, deviceInfo);
            }
            DbHelper.getInstance(context).clear();
        }


        return logs.toString();
    }

    /**
     * Common method to send broadcast.
     *
     * @param action: Action to perform. It's a string constant defined in {@link SuperLogConstants}
     * @param log:    log data in form of {@link SuperLogModel}
     */
    private void sendBroadcast(Context context, String action, SuperLogModel log) {
        Intent intent = new Intent(action);
        intent.putExtra(LOG, log);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    public void perform(int taskId, Bundle bundle, DataLoadListener<Object> listener, ContextWrapper wrapper) {
        new LogAsyncTask(taskId, bundle, listener, wrapper);
    }


    private class LogAsyncTask extends AsyncTask<Void, Void, Object> {

        private final int taskId;
        private final Bundle bundle;
        private final DataLoadListener<Object> listener;
        private ContextWrapper wrapper;

        LogAsyncTask(int taskId, Bundle bundle, DataLoadListener<Object> listener, ContextWrapper wrapper) {
            this.taskId = taskId;
            this.bundle = bundle;
            this.listener = listener;
            this.wrapper = wrapper;

            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        @Override
        protected Object doInBackground(Void... voids) {

            SuperLogModel log;

            switch (taskId) {
                case INSERT_LOG:
                    log = bundle.getParcelable(LOG);
                    insert(wrapper.getContext(), log);
                    break;

                case GET_ALL_LOGS:
                    return getLogs(wrapper.getContext());

                case GET_ALL_LOGS_FOR_MAIL:
                    return getLogsString(wrapper.getContext(), true);


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
