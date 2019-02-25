package com.project.rentapp.rent_app.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.DefaultResponse;
import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.R;
import com.project.rentapp.rent_app.RecyclerViews.UserAdsAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private UserAdsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_ads, container, false);

        getActivity().setTitle("Your Ads");

        mRecyclerView = view.findViewById(R.id.user_ads_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getUserAds();

        return view;
    }

    private void getUserAds() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        if (id == -1) {
            return;
        }

        Call call = RetrofitClient.getmInstance().getApi().getUserAds(id);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Product> productList = response.body();

                mAdapter = new UserAdsAdapter(productList);
                mRecyclerView.setAdapter(mAdapter);

                setClickListeners();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setClickListeners() {
        mAdapter.setOnCardClickListener(new UserAdsAdapter.OnItemClickListener() {
            @Override
            public void onCardClick(int pro_id) {
                ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("pro_id", pro_id);
                productDetailsFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.user_ads_fragment_container
                        , productDetailsFragment, "productDetails").addToBackStack("productDetails").commit();
            }

            @Override
            public void onRemoveClick(int pro_id) {
                final int product_id = pro_id;

                final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Delete Item?")
                        .setPositiveButton("Delete", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button confirmDelete = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirmDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        removeItem(product_id);
                    }
                });
            }
        });
    }

    private void removeItem(int pro_id) {
        Call call = RetrofitClient.getmInstance().getApi().removeAd(pro_id);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                DefaultResponse res = response.body();
                Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_SHORT).show();

                if (!res.isError()) {
                    Fragment userAdsList = getActivity().getSupportFragmentManager().findFragmentByTag("userAdsList");
                    final FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.detach(userAdsList);
                    fragmentTransaction.attach(userAdsList);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
