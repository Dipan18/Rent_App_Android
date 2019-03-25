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

public class RentRequestsAdapter extends RecyclerView.Adapter<RentRequestsAdapter.RentRequestsViewHolder> {
    private List<Product> requests;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAcceptClick(int pro_id);

        void onRejectClick(int pro_id);
    }

    public void setOnCardClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public RentRequestsAdapter(List<Product> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public RentRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rent_requests_card, viewGroup, false);
        return new RentRequestsViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RentRequestsViewHolder rentRequestsViewHolder, int i) {
        Product currentItem = requests.get(i);
        String[] images = currentItem.getImages();
        String requestedBy = "Requested By: " + currentItem.getUser().getFirstName() + ' ' + currentItem.getUser().getLastName();
        String status = "Status: " + currentItem.getStatus();

        rentRequestsViewHolder.pro_id = currentItem.getProId();
        Picasso.get().load(images[0]).into(rentRequestsViewHolder.rentRequestsCardImg);
        rentRequestsViewHolder.rentRequestsCardTitle.setText(currentItem.getProTitle().toUpperCase());
        rentRequestsViewHolder.rentRequestsCardRequestedBy.setText(requestedBy);
        rentRequestsViewHolder.rentRequestCardStatus.setText(status);

        if (currentItem.getStatus().toLowerCase().equals("pending")) {
            rentRequestsViewHolder.rentRequestCardAccept.setVisibility(View.VISIBLE);
            rentRequestsViewHolder.rentRequestCardReject.setVisibility(View.VISIBLE);
        }

        if (currentItem.getStatus().toLowerCase().equals("pending") ||
                currentItem.getStatus().toLowerCase().equals("rejected")) {

            String requestedOn = "Requested On: " + currentItem.getRequestedOn();

            rentRequestsViewHolder.rentRequestsCardRequestedOn.setText(requestedOn);
        } else {
            String rentedOn = "Rented On: " + currentItem.getRentedOn();
            String expiryDate = "Expiry Date: " + currentItem.getExpiryDate();

            rentRequestsViewHolder.rentRequestsCardRequestedOn.setText(rentedOn);
            rentRequestsViewHolder.rentRequestCardExpiryDate.setVisibility(View.VISIBLE);
            rentRequestsViewHolder.rentRequestCardExpiryDate.setText(expiryDate);
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class RentRequestsViewHolder extends RecyclerView.ViewHolder {
        public ImageView rentRequestsCardImg;
        public TextView rentRequestsCardTitle;
        public TextView rentRequestsCardRequestedBy;
        public TextView rentRequestsCardRequestedOn;
        public TextView rentRequestCardStatus;
        public TextView rentRequestCardExpiryDate;
        public ImageView rentRequestCardAccept;
        public ImageView rentRequestCardReject;

        public int pro_id;

        public RentRequestsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            rentRequestsCardImg = itemView.findViewById(R.id.rent_requests_card_img);
            rentRequestsCardTitle = itemView.findViewById(R.id.rent_requests_card_title);
            rentRequestsCardRequestedBy = itemView.findViewById(R.id.rent_requests_card_requested_by);
            rentRequestsCardRequestedOn = itemView.findViewById(R.id.rent_requests_card_requested_on);
            rentRequestCardStatus = itemView.findViewById(R.id.rent_requests_card_status);
            rentRequestCardExpiryDate = itemView.findViewById(R.id.rent_requests_card_expiry_date);
            rentRequestCardAccept = itemView.findViewById(R.id.rent_requests_card_accept_img);
            rentRequestCardReject = itemView.findViewById(R.id.rent_requests_card_reject_img);

            rentRequestCardReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRejectClick(pro_id);
                    }
                }
            });

            rentRequestCardAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAcceptClick(pro_id);
                    }
                }
            });
        }
    }
}
