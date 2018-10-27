package org.waw.project.ate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.waw.project.ate.R;
import org.waw.project.ate.model.Dashboard;
import java.util.ArrayList;

public class DashAdapter extends RecyclerView.Adapter<DashAdapter.ViewHolder> {
    private Context mCtx;
    private ArrayList<Dashboard> dashModelArrayList;

    public DashAdapter(Context mCtx, ArrayList<Dashboard> dashModelArrayList) {
        this.mCtx = mCtx;
        this.dashModelArrayList = dashModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dashboard_list_row, null);

        /*
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_row,parent,null);
        */
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Dashboard Dashboard = dashModelArrayList.get(position);

        holder.header.setText(Dashboard.getHead());
        holder.sub_header.setText(Dashboard.getSub());
        holder.images.setImageResource(Dashboard.getImage());
    }


    @Override
    public int getItemCount() {
        return dashModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView header,sub_header;
        ImageView images;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.header);
            sub_header = itemView.findViewById(R.id.sub_header);
            images = itemView.findViewById(R.id.dash_image);
        }
    }
}