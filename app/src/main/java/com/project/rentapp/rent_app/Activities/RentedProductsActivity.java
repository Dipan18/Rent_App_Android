package com.project.rentapp.rent_app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.R;
import com.project.rentapp.rent_app.RecyclerViews.RentedProductsAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentedProductsActivity extends BaseNavigationActivity {

    private RecyclerView mRecyclerView;
    private RentedProductsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rented Items");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_rented_products, null, false);
        drawer.addView(contentView, 0);

        mRecyclerView = findViewById(R.id.rented_products_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getRentedProducts();
    }

    private void getRentedProducts() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        if (id == -1) { return; }

        Call call = RetrofitClient.getmInstance().getApi().getRentedProductsByUser(id);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RentedProductsActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }

                List<Product> rentedProductsByUser = response.body();

                if (rentedProductsByUser == null || rentedProductsByUser.isEmpty()) {
                    Toast.makeText(RentedProductsActivity.this, "No Rented Products Found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAdapter = new RentedProductsAdapter(rentedProductsByUser);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(RentedProductsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
