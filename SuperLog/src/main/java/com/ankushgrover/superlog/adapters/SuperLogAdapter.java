package com.ankushgrover.superlog.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankushgrover.superlog.R;
import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.model.SuperLogModel;
import com.ankushgrover.superlog.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 29/8/17.
 */

public class SuperLogAdapter extends RecyclerView.Adapter<SuperLogAdapter.Holder> implements SuperLogConstants {

    private Context context;
    private ArrayList<SuperLogModel> list;

    public SuperLogAdapter(Context context, ArrayList<SuperLogModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_super_log, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        SuperLogModel log = list.get(position);
        holder.log.setText(Utils.getLogString(log.getTag(), log.getMessage(), log.getTimestamp()));
        holder.log.setTextColor(ContextCompat.getColor(context, Utils.getLogType(log.getType()).getColor()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private TextView log;

        public Holder(View itemView) {
            super(itemView);

            log = (TextView) itemView.findViewById(R.id.log);

        }
    }

}
