package com.project.rentapp.rent_app.RecyclerViews;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {
    private List<Product> mProductList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onCardClick(int pro_id);
    }

    public void OnCardClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductListAdapter(List<Product> mProductList) {
        this.mProductList = mProductList;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_card, viewGroup, false);
        ProductListViewHolder productListViewHolder= new ProductListViewHolder(view, mListener);
        return productListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder productListViewHolder, int i) {
        Product currentItem = mProductList.get(i);
        String price = "\u20B9" + String.valueOf(currentItem.getProPrice());
        String[] currentImgPath = currentItem.getImages();

        productListViewHolder.product_id = currentItem.getProId();
        productListViewHolder.productListCardTitle.setText(currentItem.getProTitle());
        productListViewHolder.productListCardPrice.setText(price);
        Picasso.get().load(currentImgPath[0]).fit().centerInside().into(productListViewHolder.productListCardImg);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public void addProductsToList(List<Product> products) {
        mProductList.addAll(products);
        notifyDataSetChanged();
    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder {
        public ImageView productListCardImg;
        public TextView productListCardTitle;
        public TextView productListCardPrice;
        public int product_id;

        public ProductListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            productListCardImg = itemView.findViewById(R.id.product_list_card_img);
            productListCardTitle = itemView.findViewById(R.id.product_list_card_title);
            productListCardPrice = itemView.findViewById(R.id.product_list_card_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pro_id = product_id;
                        listener.onCardClick(pro_id);
                    }
                }
            });

        }
    }
}
