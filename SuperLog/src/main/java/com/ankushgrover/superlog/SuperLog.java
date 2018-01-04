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
    private static Builder BUILDER;

    //private static Application CONTEXT;

    /**
     * Debug log
     *
     * @param tag
     * @param msg
     */
    public static void d(@NonNull String tag, @NonNull String msg) {
        Log.d(tag, msg);
        log(DEBUG, tag, msg);
    }


    /**
     * Error log
     *
     * @param tag
     * @param msg
     */
    public static void e(@NonNull String tag, @NonNull String msg) {
        Log.e(tag, msg);
        log(ERROR, tag, msg);
    }

    /**
     * Warning log
     *
     * @param tag
     * @param msg
     */
    public static void w(@NonNull String tag, @NonNull String msg) {
        Log.w(tag, msg);
        log(WARNING, tag, msg);
    }

    /**
     * Verbose log
     *
     * @param tag
     * @param msg
     */
    public static void v(@NonNull String tag, @NonNull String msg) {
        Log.v(tag, msg);
        log(VERBOSE, tag, msg);
    }

    public static void i(@NonNull String tag, @NonNull String msg) {
        Log.i(tag, msg);
        log(INFO, tag, msg);
    }

    /**
     * Log without tag.
     *
     * @param msg
     */
    public static void log(@NonNull String msg) {
        Log.v(SuperLog.class.getSimpleName(), msg);
        log(NORMAL, SuperLog.class.getSimpleName(), msg);
    }


    private static void log(int type, String tag, String msg) {

        if (!(!BUILDER.isAddLogs() || Utils.isEmpty(tag) || Utils.isEmpty(msg))) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(LOG, new SuperLogModel(tag, msg, type));

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
                    SuperLog.v("SuperLog Db Empty", "Tried sending mail, but the db is empty.");

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
                        SuperLog.e("Error Sending mail", e.getMessage());
                    }

                }

            }
        });


    }

    /**
     * Method to send mail in background.
     * <p>
     * INFO: IT IS ASSUMED THAT THE USER HAS ALREADY SUPPLIED HIS/HER GMAIL'S USERNAME AND PASSWORD
     * FOR THE SAME.
     * <p>
     * CAUTION: YOU NEED TO ALLOW LESS SECURE APPS FOR ACCESSING THIS FEATURE.
     * Please refer https://myaccount.google.com/lesssecureapps
     *
     * @param senderEmailId
     * @param password
     * @param recipientEmailId
     */
    public static void sendMailInBackground(final String senderEmailId, final String password, final String recipientEmailId) {

        SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.GET_ALL_LOGS_FOR_MAIL, null, new DataLoadListener<Object>() {
            @Override
            public void onDataLoaded(Object obj, int key) {
                final String logs = (String) obj;
                if (Utils.isEmpty(logs))
                    SuperLog.v("SuperLog Db Empty", "Tried sending mail, but the db is empty.");

                else {

                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {

                            if (NetworkUtils.isNetworkAvailable(BUILDER.getContext(), true)) {
                                try {
                                    GMailSender sender = new GMailSender(senderEmailId, password);
                                    sender.sendMail("SuperLogs",
                                            logs,
                                            senderEmailId, recipientEmailId);
                                } catch (Exception e) {
                                    SuperLog.e("Error Sending mail", e.getMessage());
                                }
                            }


                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();

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
                SuperLog.e("Error Sending mail", e.getMessage());
            }
        }
    }

    /**
     * Method to send mail in background. Recipient's email id is required.
     * <p>
     * INFO: IT IS ASSUMED THAT THE USER HAS ALREADY SUPPLIED HIS/HER GMAIL'S USERNAME AND PASSWORD
     * FOR THE SAME.
     * <p>
     * CAUTION: YOU NEED TO ALLOW LESS SECURE APPS FOR ACCESSING THIS FEATURE.
     * Please refer https://myaccount.google.com/lesssecureapps
     *
     * @param recipientEmailId
     */
    public static void sendMailInBackground(final String recipientEmailId) {
        String email = BUILDER.getEmail();
        String pass = BUILDER.getPass();

        if (Utils.isEmpty(email) || Utils.isEmpty(pass)) {
            SuperLog.log(BUILDER.getContext().getString(R.string.empty_credentials));
            return;
        }

        if (Utils.isEmpty(recipientEmailId)) {
            SuperLog.log(BUILDER.getContext().getString(R.string.empty_recipient_address));
            return;
        }

        sendMailInBackground(email, pass, recipientEmailId);

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
