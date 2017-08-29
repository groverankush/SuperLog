package com.ankushgrover.superlog.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.ankushgrover.superlog.R;
import com.ankushgrover.superlog.SuperLog;
import com.ankushgrover.superlog.adapters.SuperLogAdapter;
import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.helpers.SuperLogDbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.model.SuperLogModel;

import java.util.ArrayList;

public class SuperLogActivity extends AppCompatActivity implements DataLoadListener<Object>, SuperLogConstants {

    private ArrayList<SuperLogModel> logs;
    private RecyclerView recycler;
    private SuperLogAdapter adapter;
    private ProgressBar progressBar;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_log);

/*        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            SuperLog.log("ACTION BAR NULL");
        }

        initializeToolbar("SuperLog");*/

        progressBar = (ProgressBar) findViewById(R.id.progress);
        SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.GET_ALL_LOGS, null, this);
    }

    private void initRecycler() {
        recycler = (RecyclerView) findViewById(R.id.recycler);
        adapter = new SuperLogAdapter(this, logs);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        recycler.scrollToPosition(logs.size() - 1);


        registerReceiver();
    }

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getAction()) {
                    case ACTION_LOG_INSERTED:
                        logs.add((SuperLogModel) intent.getParcelableExtra(LOG));
                        adapter.notifyDataSetChanged();
                        //recycler.scrollToPosition(logs.size() - 1);
                        break;
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LOG_INSERTED);

        registerReceiver(receiver, filter);

    }

    public Toolbar initializeToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        toolbar.setTitle(title);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return toolbar;
    }

    @Override
    public void onDataLoaded(Object obj, int key) {
        switch (key) {
            case SuperLogDbHelper.GET_ALL_LOGS:
                progressBar.setVisibility(View.GONE);
                logs = new ArrayList<>();
                logs = ((ArrayList<SuperLogModel>) obj);
                initRecycler();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
