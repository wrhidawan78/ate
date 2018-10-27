package org.waw.project.ate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.waw.project.ate.R;
import org.waw.project.ate.model.AssetDetail;

import java.util.List;

public class AssetDetailAdapter extends RecyclerView.Adapter<AssetDetailAdapter.MyViewHolder> {

    private Context context;
    private List<AssetDetail> assetDetails;

    public AssetDetailAdapter(Context context, List<AssetDetail> assetDetails) {
        this.context = context;
        this.assetDetails = assetDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.asset_detail_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final AssetDetail assetDetail = assetDetails.get(i);

        //loading the image
        Glide.with(context)
                .load(assetDetail.getFilePath())
                .into(myViewHolder.imageView);


        myViewHolder.textViewAssetTypeName.setText(assetDetail.getAssetTypeName());
        myViewHolder.textViewAssetID.setText(assetDetail.getAssetID());

        //applyProfilePicture(myViewHolder, asset);

    }

    @Override
    public int getItemCount() {
        return assetDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAssetTypeName, textViewAssetID;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewAssetTypeName = itemView.findViewById(R.id.assetType);
            textViewAssetID = itemView.findViewById(R.id.assetID);
            imageView = itemView.findViewById(R.id.card_view_image);

        }
    }
}
