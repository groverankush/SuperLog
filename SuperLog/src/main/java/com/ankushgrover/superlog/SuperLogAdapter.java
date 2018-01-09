package com.ankushgrover.superlog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 29/8/17.
 */

public class SuperLogAdapter extends RecyclerView.Adapter<SuperLogAdapter.Holder> implements SuperLogConstants {

    private Context context;
    private ArrayList<SuperLogModel> list;
    private ArrayList<SuperLogModel> filtered;


    public SuperLogAdapter(Context context, ArrayList<SuperLogModel> list) {
        this.context = context;
        this.list = list;

        this.filtered = new ArrayList<>();
        this.filtered.addAll(list);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_super_log, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        SuperLogModel log = filtered.get(position);
        holder.log.setText(Utils.getLogString(log.getMessage(), log.getTimestamp()));
        holder.log.setTextColor(ContextCompat.getColor(context, Utils.getLogType(log.getType()).getColor()));


    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }


    /**
     * Method to filter results based on user input.
     *
     * @param text
     */
    public void filter(String text) {
        filtered.clear();
        if (text == null || text.length() == 0) {
            filtered.addAll(list);
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMessage().toLowerCase().contains(text.toLowerCase()))
                    filtered.add(list.get(i));
            }
        }
        ((SuperLogActivity) context).displayEmptyText(filtered.size() == 0);

        notifyDataSetChanged();
    }


    public int checkAndAddLog(SuperLogModel log, String query) {
        if (Utils.isEmpty(query) || log.getMessage().toLowerCase().contains(query)) {
            filtered.add(log);
            notifyDataSetChanged();
            return filtered.size();
        }
        return 0;
    }

    public void removeAllLogs() {
        filtered.clear();
        list.clear();
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder {

        private TextView log;

        public Holder(View itemView) {
            super(itemView);

            log = (TextView) itemView.findViewById(R.id.log);

        }
    }

}
