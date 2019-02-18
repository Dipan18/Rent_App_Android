package com.project.rentapp.rent_app.RecyclerViews;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdsAdapter extends RecyclerView.Adapter<UserAdsAdapter.UserAdsViewHolder> {
    private List<Product> mProductList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onCardClick(int pro_id);

        void onRemoveClick(int pro_id);
    }

    public void setOnCardClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public UserAdsAdapter(List<Product> productList) {
        mProductList = productList;
    }

    @NonNull
    @Override
    public UserAdsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_ads_card, viewGroup, false);
        UserAdsViewHolder userAdsViewHolder = new UserAdsViewHolder(view, mListener);
        return userAdsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdsViewHolder userAdsViewHolder, int i) {
        Product currentItem = mProductList.get(i);
        String[] currentImgPath = currentItem.getImages();

        userAdsViewHolder.product_id = currentItem.getProId();
        userAdsViewHolder.userAdsCardTitle.setText(currentItem.getProTitle());
        userAdsViewHolder.userAdsCardDate.setText(currentItem.getCreatedAt());
        Picasso.get().load(currentImgPath[0]).into(userAdsViewHolder.userAdsCardImg);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class UserAdsViewHolder extends RecyclerView.ViewHolder {
        public TextView userAdsCardTitle;
        public TextView userAdsCardDate;
        public ImageView userAdsCardImg;
        public ImageView userAdsCardRemove;
        public int product_id;

        public UserAdsViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            userAdsCardTitle = itemView.findViewById(R.id.user_ads_card_title);
            userAdsCardDate = itemView.findViewById(R.id.user_ads_card_date);
            userAdsCardImg = itemView.findViewById(R.id.user_ads_card_img);
            userAdsCardRemove = itemView.findViewById(R.id.user_ads_card_remove);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pro_id = product_id;
                        listener.onCardClick(pro_id);
                    }
                }
            });

            userAdsCardRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pro_id = product_id;
                        listener.onRemoveClick(pro_id);
                    }
                }
            });
        }
    }
}
