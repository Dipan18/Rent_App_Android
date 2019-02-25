package com.project.rentapp.rent_app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rentapp.rent_app.Activities.BaseNavigationActivity;
import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.Models.User;
import com.project.rentapp.rent_app.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends Fragment {
    private ImageView productImages;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private TextView cardViewPrice;
    private TextView cardViewRentPeriod;
    private TextView productDetailsTitle;
    private TextView pdCardDescription;
    private TextView pdCardCategory;
    private TextView pdCardPincode;
    private TextView pdCardAddress;
    private TextView cuCardName;
    private TextView cuCardEmail;
    private TextView cuCardPhone;

    private String images[];
    int currentImageIndex = 0;
    int imagesCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        productImages = view.findViewById(R.id.product_details_images);
        leftArrow = view.findViewById(R.id.product_details_left_arrow);
        rightArrow = view.findViewById(R.id.product_details_right_arrow);
        cardViewPrice = view.findViewById(R.id.card_view_pd_price);
        cardViewRentPeriod = view.findViewById(R.id.card_view_pd_rent_period);
        productDetailsTitle = view.findViewById(R.id.product_details_title);
        pdCardDescription = view.findViewById(R.id.pd_card_description);
        pdCardCategory = view.findViewById(R.id.pd_card_category);
        pdCardPincode = view.findViewById(R.id.pd_card_pincode);
        pdCardAddress = view.findViewById(R.id.pd_card_address);
        cuCardName = view.findViewById(R.id.cu_card_name);
        cuCardEmail = view.findViewById(R.id.cu_card_email);
        cuCardPhone = view.findViewById(R.id.cu_card_phone);

        int pro_id = getArguments().getInt("pro_id");
        getProductDetails(pro_id);

        return view;
    }

    private void getProductDetails(int pro_id) {
        Call call = RetrofitClient.getmInstance().getApi().getProductDetails(pro_id);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Product productDetails = response.body();
                bindDataToView(productDetails);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bindDataToView(Product product) {
        images = product.getImages();
        setImageChangeNavigation();
        User userInfo = product.getUser();

        String price = " Price: " + String.valueOf(product.getProPrice());
        String rentPeriod = " Rent Period: " + String.valueOf(product.getRentPeriod()) + " Days";
        String description = "Description: " + product.getProDesc();
        String pincode = "Pincode: " + String.valueOf(product.getProPincode());
        String category = "Category: " + product.getCatName();
        String address = "Address: " + product.getProAddress();
        String name = "Name: " + userInfo.getFirstName() + ' ' + userInfo.getLastName();
        String email = "Email: " + userInfo.getEmail();
        String phone_no = "Phone: " + userInfo.getPhoneNo();

        productDetailsTitle.setText(product.getProTitle());
        cardViewPrice.setText(price);
        cardViewRentPeriod.setText(rentPeriod);
        pdCardDescription.setText(description);
        pdCardCategory.setText(category);
        pdCardPincode.setText(pincode);
        pdCardAddress.setText(address);
        cuCardName.setText(name);
        cuCardEmail.setText(email);
        cuCardPhone.setText(phone_no);
    }

    private void setImageChangeNavigation() {
        currentImageIndex = 0;
        imagesCount = images.length;

        Picasso.get().load(images[currentImageIndex]).into(productImages);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex - 1 >= 0) {
                    currentImageIndex--;
                    Picasso.get().load(images[currentImageIndex]).into(productImages);
                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex + 1 < imagesCount) {
                    currentImageIndex++;
                    Picasso.get().load(images[currentImageIndex]).into(productImages);
                }
            }
        });
    }
}
