package com.ankushgrover.superlog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.helpers.SuperLogDbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.model.SuperLogModel;
import com.ankushgrover.superlog.utils.Utils;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 24/8/17.
 */

public class SuperLog implements SuperLogConstants {
    private static final String TAG = SuperLog.class.getSimpleName();
    private static Builder BUILDER;


    static void log(int type, String msg, ContextWrapper wrapper) {

        if (!(!BUILDER.isAddLogs() || Utils.isEmpty(msg))) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(LOG, new SuperLogModel(msg, type));

            SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.INSERT_LOG, bundle, new DataLoadListener<Object>() {
                @Override
                public void onDataLoaded(Object obj, int key) {

                }
            }, wrapper);
        }
    }

    static void sendMail(final ContextWrapper wrapper) {


        SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.GET_ALL_LOGS_FOR_MAIL, null, new DataLoadListener<Object>() {
            @Override
            public void onDataLoaded(Object obj, int key) {
                final String logs = (String) obj;
                if (Utils.isEmpty(logs))
                    Log.v("SuperLog Db Empty", "Tried sending mail, but the db is empty.");

                else {

                    try {

                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_SUBJECT, SuperLog.class.getSimpleName());
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_TEXT, logs);
                        //intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});

                        if (intent.resolveActivity(wrapper.getContext().getPackageManager()) != null) {
                            wrapper.getContext().startActivity(intent);
                        }

                    } catch (Exception e) {
                        Log.e("Error Sending mail", e.getMessage());
                    }

                }

            }
        }, wrapper);


    }

    /**
     * Method to initialize SuperLog
     *
     * @param builder
     */
    public static void init(@NonNull Builder builder, ContextWrapper wrapper) {
        BUILDER = builder;

        builder.startLogCat(wrapper);

    }

    public static Builder getBuilder() {
        return BUILDER;
    }


    public static class Builder {


        private boolean superLogViewVisibility;
        private boolean addLogs;
        private LogCatThread logCatThread;

        public Builder() {
            this.superLogViewVisibility = true;
            this.addLogs = true;
        }

        public Builder addLogsToDb(boolean addLogs) {
            this.addLogs = addLogs;
            return this;
        }

        public boolean isSuperLogViewVisibility() {
            return superLogViewVisibility;
        }

        public Builder setSuperLogViewVisibility(boolean isVisible) {

            this.superLogViewVisibility = isVisible;
            return this;
        }

        private void startLogCat(ContextWrapper wrapper) {
            this.logCatThread = new LogCatThread(wrapper);
            logCatThread.start();

        }

        /*public Builder stopLogCat() {
            if (this.logCatThread != null) {
                this.logCatThread.interrupt();
                this.logCatThread = null;
            }
            return this;
        }*/

        public boolean isAddLogs() {
            return addLogs;
        }

        public void setAddLogs(boolean addLogs) {
            this.addLogs = addLogs;
        }

        /**
         * @return true: Should show view; false: Should not show view
         */
        public boolean isSuperLogViewVisible() {
            return superLogViewVisibility;
        }


    }

}
