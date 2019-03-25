package com.project.rentapp.rent_app.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.DefaultResponse;
import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.R;
import com.project.rentapp.rent_app.RecyclerViews.RentRequestsAdapter;
import com.project.rentapp.rent_app.RecyclerViews.RentedProductsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentRequestsActivity extends BaseNavigationActivity{

    private RecyclerView mRecyclerView;
    private RentRequestsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rent Requests");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_rent_requests, null, false);
        drawer.addView(contentView, 0);

        mRecyclerView = findViewById(R.id.rent_requests_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getRequestsOnMyProducts();
    }

    private void getRequestsOnMyProducts() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        if (id == -1) { return; }

        Call call = RetrofitClient.getmInstance().getApi().getRequestsOnMyProducts(id);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RentRequestsActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Product> rentRequests = response.body();

                mAdapter = new RentRequestsAdapter(rentRequests);
                mRecyclerView.setAdapter(mAdapter);
                setClickListeners();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(RentRequestsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListeners() {
        mAdapter.setOnCardClickListener(new RentRequestsAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(final int pro_id) {
                final AlertDialog dialog = new AlertDialog.Builder(RentRequestsActivity.this)
                        .setMessage("Accept This Request?")
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button confirmAccept = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirmAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        acceptRentRequest(pro_id);
                    }
                });
            }

            @Override
            public void onRejectClick(final int pro_id) {
                final AlertDialog dialog = new AlertDialog.Builder(RentRequestsActivity.this)
                        .setMessage("Reject This Request?")
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button confirmReject= dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirmReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rejectRentRequest(pro_id);
                    }
                });
            }
        });
    }

    private void acceptRentRequest(int pro_id) {
        Call call = RetrofitClient.getmInstance().getApi().acceptRentRequest(pro_id);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RentRequestsActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                DefaultResponse res = response.body();

                Toast.makeText(RentRequestsActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                if (!res.isError()) {
                    getRequestsOnMyProducts();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(RentRequestsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectRentRequest(int pro_id) {
        Call call = RetrofitClient.getmInstance().getApi().rejectRentRequest(pro_id);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RentRequestsActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                DefaultResponse res = response.body();

                Toast.makeText(RentRequestsActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                if (!res.isError()) {
                    getRequestsOnMyProducts();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(RentRequestsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
