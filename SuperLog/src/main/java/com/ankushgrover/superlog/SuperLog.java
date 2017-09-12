package com.ankushgrover.superlog;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.helpers.SuperLogDbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.mail.GMailSender;
import com.ankushgrover.superlog.model.SuperLogModel;
import com.ankushgrover.superlog.utils.Utils;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 24/8/17.
 */

public class SuperLog implements SuperLogConstants {

    private static Application CONTEXT;

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
    public static void log(String msg) {
        Log.v(SuperLog.class.getSimpleName(), msg);
        log(NORMAL, SuperLog.class.getSimpleName(), msg);
    }


    private static void log(int type, String tag, String msg) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(LOG, new SuperLogModel(tag, msg, type));

        SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.INSERT_LOG, bundle, new DataLoadListener<Object>() {
            @Override
            public void onDataLoaded(Object obj, int key) {

            }
        });

    }


    public static void sendMail() {


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

                            try {

                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_SUBJECT, SuperLog.class.getSimpleName());
                                intent.setData(Uri.parse("mailto:"));
                                intent.putExtra(Intent.EXTRA_TEXT, logs);
                                //intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});

                                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                    getContext().startActivity(intent);
                                }

                            } catch (Exception e) {
                                SuperLog.e("Error Sending mail", e.getMessage());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            SuperLog.i("Mail Sent Status", "Success");
                        }
                    }.execute();

                }

            }
        });


    }

    /**
     * Method to send mail to recipient's email id.
     *
     * @param senderEmailId
     * @param password
     * @param recipientEmailId
     */
    public static void sendMail(final String senderEmailId, final String password, final String recipientEmailId) {

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

                            try {
                                GMailSender sender = new GMailSender(senderEmailId, password);
                                sender.sendMail("SuperLogs",
                                        logs,
                                        senderEmailId, recipientEmailId);
                            } catch (Exception e) {
                                SuperLog.e("Error Sending mail", e.getMessage());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            SuperLog.i("Mail Sent Status", "Success");
                        }
                    }.execute();

                }

            }
        });
    }


    public static final class Builder {
        private Application CONTEXT;
        private boolean showSuperLogView = true;
        private String email, pass;

        public Builder() {
        }

        public Builder init(@NonNull Application context) {
            this.CONTEXT = context;
            return this;
        }

        public Builder showSuperLogView(boolean show) {
            this.showSuperLogView = show;
            return this;
        }

        public Builder setCredentials(@NonNull String email, @NonNull String pass) {

            //// TODO: 13-09-2017 Add validation and save in preferences (check)
            this.email = email;
            this.pass = pass;

            return this;
        }


    }


    public static void init(@NonNull Application context) {
        CONTEXT = context;

    }

    public static Application getContext() {
        return CONTEXT;
    }

}
