package com.project.rentapp.rent_app.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.R;
import com.project.rentapp.rent_app.RecyclerViews.ProductListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProductListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        mRecyclerView = view.findViewById(R.id.product_list_recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProductListAdapter(new ArrayList<Product>());
        mRecyclerView.setAdapter(mAdapter);

        getSearchResults(getArguments().getString("query"));
        setOnCardListener();

        return view;
    }

    private void getSearchResults(String query) {
        Call call = RetrofitClient.getmInstance().getApi().getSearchResults(query);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Product> searchResult = response.body();
                mAdapter.addProductsToList(searchResult);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setOnCardListener() {
        mAdapter.OnCardClickListener(new ProductListAdapter.OnItemClickListener() {
            @Override
            public void onCardClick(int pro_id) {
                ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("pro_id", pro_id);
                productDetailsFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.search_result_fragment_container
                        , productDetailsFragment, "productDetails").addToBackStack("productList").commit();
            }
        });
    }
}
