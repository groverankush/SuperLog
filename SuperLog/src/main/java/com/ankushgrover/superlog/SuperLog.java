package com.ankushgrover.superlog;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ankushgrover.superlog.bg.LogCatThread;
import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.helpers.SuperLogDbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.mail.GMailSender;
import com.ankushgrover.superlog.model.SuperLogModel;
import com.ankushgrover.superlog.utils.NetworkUtils;
import com.ankushgrover.superlog.utils.Utils;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 24/8/17.
 */

public class SuperLog implements SuperLogConstants {
    private static final String TAG = SuperLog.class.getSimpleName();
    private static Builder BUILDER;

    //private static Application CONTEXT;

    private static void log(int type, String msg) {

        if (!(!BUILDER.isAddLogs() || Utils.isEmpty(msg))) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(LOG, new SuperLogModel(msg, type));

            SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.INSERT_LOG, bundle, new DataLoadListener<Object>() {
                @Override
                public void onDataLoaded(Object obj, int key) {

                }
            });
        }
    }


    public static void sendMail() {


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

                        if (intent.resolveActivity(BUILDER.getContext().getPackageManager()) != null) {
                            BUILDER.getContext().startActivity(intent);
                        }

                    } catch (Exception e) {
                        Log.e("Error Sending mail", e.getMessage());
                    }

                }

            }
        });


    }

    public static void mailLogCat(final String senderEmailId, final String password, final String recipientEmailId) {
        String logs = BUILDER.getLogCat();
        if (Utils.isEmpty(logs)) {
            return;
        }


        if (NetworkUtils.isNetworkAvailable(BUILDER.getContext(), true)) {
            try {
                GMailSender sender = new GMailSender(senderEmailId, password);
                sender.sendMail("SuperLogs",
                        logs,
                        senderEmailId, recipientEmailId);
            } catch (Exception e) {
                Log.e("Error Sending mail", e.getMessage());
            }
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


        private Application context;
        private boolean superLogViewVisibility;
        private boolean addLogs;
        private String email, pass;
        private LogCatThread logCatThread;

        public Builder(Application context) {
            this.context = context;
            this.superLogViewVisibility = true;
            this.addLogs = true;
        }

        public Builder addLogsToDb(boolean addLogs) {
            this.addLogs = addLogs;
            return this;
        }

        public Builder setCredentials(@NonNull String email, @NonNull String pass) {

            //// TODO: 13-09-2017 Add validation and save in preferences (check)
            this.email = email;
            this.pass = pass;

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

        public String getLogCat() {
            return "";
/*            String string = "";
            if (logCatThread != null && !logCatThread.isInterrupted()) {
                string = logCatThread.getLogCatString();
            }
            return string;*/
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

        public Application getContext() {
            return context;
        }

        public void setContext(Application context) {
            this.context = context;
        }

    }

    /*public static Application getContext() {
        return CONTEXT;
    }*/

}
