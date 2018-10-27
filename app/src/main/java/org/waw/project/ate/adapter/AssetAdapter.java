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
import org.waw.project.ate.model.Asset;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.MyViewHolder> {

    private Context mCtx;
    private List<Asset> assetList;


    public AssetAdapter(Context mCtx, List<Asset> assetList) {
        this.mCtx = mCtx;
        this.assetList = assetList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.asset_list, null);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Asset asset = assetList.get(i);
        /*
        //loading the image
        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);
        */
        myViewHolder.textViewIdMonthYear.setText(asset.getAssetTypeName().substring(0, 1));
        myViewHolder.textViewAssetTypeID.setText(asset.getAssetTypeName());
        myViewHolder.textViewMonthYear.setText("Registration Period : " + asset.getMonth_() + " / " + asset.getYear_());
        myViewHolder.textViewFileCount.setText(asset.getFileCount() + " Images");

        applyProfilePicture(myViewHolder, asset);
    }

    private void applyProfilePicture(MyViewHolder holder, Asset asset) {
        holder.imageViewMonthYear.setImageResource(R.drawable.bg_circle);
        holder.imageViewMonthYear.setColorFilter(asset.getColor());
        holder.imageViewMonthYear.setVisibility(View.VISIBLE);


    }
    @Override
    public int getItemCount() {
        return assetList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewAssetTypeID, textViewMonthYear, textViewFileCount, textViewIdMonthYear;
        ImageView imageViewMonthYear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssetTypeID = itemView.findViewById(R.id.assetType);
            textViewMonthYear = itemView.findViewById(R.id.monthYear);
            textViewFileCount = itemView.findViewById(R.id.fileCount);
            textViewIdMonthYear = itemView.findViewById(R.id.icon_text);
            imageViewMonthYear = itemView.findViewById(R.id.icon_profile);

        }

    }
}
