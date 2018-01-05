package com.ankushgrover.superlog;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ankushgrover.superlog.bg.LogCatThread;
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


    public static void log(int type, String msg, ContextWrapper wrapper) {

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


    /**
     * Method to initialize SuperLog
     *
     * @param builder
     */
    public static void init(@NonNull Builder builder) {
        BUILDER = builder;
    }

    public static Builder getBuilder() {
        return BUILDER;
    }


    public static class Builder {


        private boolean superLogViewVisibility;
        private boolean addLogs;
        private String email, pass;
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

        public Builder startLogCat() {
            this.logCatThread = new LogCatThread();
            logCatThread.run();
            return this;

        }

        public Builder stopLogCat() {
            if (this.logCatThread != null) {
                this.logCatThread.interrupt();
                this.logCatThread = null;
            }
            return this;
        }

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

    }

}
