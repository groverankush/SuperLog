package com.ankushgrover.superlog.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ankushgrover.superlog.R;
import com.ankushgrover.superlog.SuperLog;
import com.ankushgrover.superlog.adapters.SuperLogAdapter;
import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.DbHelper;
import com.ankushgrover.superlog.db.helpers.SuperLogDbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.model.SuperLogModel;
import com.ankushgrover.superlog.utils.Utils;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SuperLogActivity extends AppCompatActivity implements DataLoadListener<Object>, SuperLogConstants, View.OnClickListener, SearchView.OnQueryTextListener {

    private ArrayList<SuperLogModel> logs;
    private RecyclerView recycler;
    private SuperLogAdapter adapter;
    private ProgressBar progressBar;
    private BroadcastReceiver receiver;
    private boolean scroll = true;
    private SearchView searchView;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton moveFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_log);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        } else {
            findViewById(R.id.toolbar).setVisibility(View.GONE);
        }

        setTitle("SuperLog");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        moveFab = (FloatingActionButton) findViewById(R.id.move);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        findViewById(R.id.fab).setOnClickListener(this);
        moveFab.setOnClickListener(this);
        SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.GET_ALL_LOGS, null, this);
    }

    private void initRecycler() {
        recycler = (RecyclerView) findViewById(R.id.recycler);
        adapter = new SuperLogAdapter(this, logs);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        recycler.scrollToPosition(logs.size() - 1);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < 0 && moveFab.getVisibility() == View.GONE) {
                    scroll = false;
                    Utils.manageViewVisibilityReveal(moveFab, true);
                } else {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == (logs.size() - 1) &&
                            moveFab.getVisibility() == View.VISIBLE) {
                        scroll = true;
                        Utils.manageViewVisibilityReveal(moveFab, false);
                    }
                }
            }
        });


        registerReceiver();
    }

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getAction()) {
                    case ACTION_LOG_INSERTED:
                        SuperLogModel log = intent.getParcelableExtra(LOG);
                        logs.add(log);
                        if (searchView != null) {
                            int size = adapter.checkAndAddLog(log, searchView.getQuery().toString());
                            if (scroll && size > 0)
                                recycler.smoothScrollToPosition(size - 1);
                        }

                        break;
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LOG_INSERTED);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        //registerReceiver(receiver, filter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_search) {

        } else if (id == R.id.action_delete) {
            DbHelper.getInstance().clear();
            adapter.removeAllLogs();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataLoaded(Object obj, int key) {
        switch (key) {
            case SuperLogDbHelper.GET_ALL_LOGS:
                progressBar.setVisibility(View.GONE);
                logs = new ArrayList<>();
                logs = ((ArrayList<SuperLogModel>) obj);
                if (logs.size() == 0)
                    displayEmptyText(true);
                initRecycler();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.fab) {
            SuperLog.sendMail();

        } else if (id == R.id.move) {
            recycler.smoothScrollToPosition(logs.size() - 1);
            scroll = true;

        }
    }

    /**
     * Method to display empty list message on screen when filtered countries are 0.
     *
     * @param show
     */
    public void displayEmptyText(boolean show) {

        findViewById(R.id.empty).setVisibility(show ? View.VISIBLE : View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ag_sl_menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);

        searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(R.string.search));

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                displayEmptyText(false);
                return true;
            }
        });

        final EditText searchText = (EditText) searchView.findViewById(R.id.search_src_text);

        searchView.findViewById(R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEmptyText(false);

                searchText.setText("");
                searchView.setQuery("", false);

            }
        });


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (adapter != null)
            adapter.filter(newText);
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}
