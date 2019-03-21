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

public class RentedProductsAdapter extends RecyclerView.Adapter<RentedProductsAdapter.RentedProductsViewHolder> {
    private List<Product> rentedProducts;

    public RentedProductsAdapter(List<Product> productsList) {
        rentedProducts = productsList;
    }

    @NonNull
    @Override
    public RentedProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rented_products_card, viewGroup, false);
        return new RentedProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentedProductsViewHolder rentedProductsViewHolder, int i) {
        Product currentItem = rentedProducts.get(i);
        String[] imagePath = currentItem.getImages();
        String ownerName = "Owner: " + currentItem.getUser().getFirstName() + ' ' + currentItem.getUser().getLastName();

        rentedProductsViewHolder.rentedProductsCardTitleTv.setText(currentItem.getProTitle().toUpperCase());
        Picasso.get().load(imagePath[0]).into(rentedProductsViewHolder.rentedProductsCardImageView);
        rentedProductsViewHolder.rentedProductsCardOwnerTv.setText(ownerName);

        if (currentItem.getStatus().toLowerCase().equals("pending") ||
            currentItem.getStatus().toLowerCase().equals("rejected")) {

            String requestedOn = "Requested On: " + currentItem.getRequestedOn();
            String status = "Status: " + currentItem.getStatus();

            rentedProductsViewHolder.rentedProductsCardRentedOnTV.setText(requestedOn);
            rentedProductsViewHolder.rentedProductsCardDaysRemainingTv.setText(status);
        } else {
            String rentedOn = "Rented On: " + currentItem.getRentedOn();
            String daysRemaining = "Days Remaining: " + String.valueOf(currentItem.getDaysRemaining());

            rentedProductsViewHolder.rentedProductsCardRentedOnTV.setText(rentedOn);
            rentedProductsViewHolder.rentedProductsCardDaysRemainingTv.setText(daysRemaining);
        }
    }

    @Override
    public int getItemCount() {
        return rentedProducts.size();
    }

    public static class RentedProductsViewHolder extends RecyclerView.ViewHolder {
        public ImageView rentedProductsCardImageView;
        public TextView rentedProductsCardTitleTv;
        public TextView rentedProductsCardOwnerTv;
        public TextView rentedProductsCardRentedOnTV;
        public TextView rentedProductsCardDaysRemainingTv;

        public RentedProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            rentedProductsCardImageView = itemView.findViewById(R.id.rented_products_card_img);
            rentedProductsCardTitleTv = itemView.findViewById(R.id.rented_products_card_title);
            rentedProductsCardOwnerTv = itemView.findViewById(R.id.rented_products_card_owner);
            rentedProductsCardDaysRemainingTv = itemView.findViewById(R.id.rented_products_card_days_remaining);
            rentedProductsCardRentedOnTV = itemView.findViewById(R.id.rented_products_card_rented_on);
        }
    }
}
