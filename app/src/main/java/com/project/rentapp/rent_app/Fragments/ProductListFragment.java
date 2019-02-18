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
import com.project.rentapp.rent_app.Models.DefaultResponse;
import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.R;
import com.project.rentapp.rent_app.RecyclerViews.ProductListAdapter;
import com.project.rentapp.rent_app.RecyclerViews.UserAdsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductListFragment extends Fragment {
    private int page = 0;
    private boolean noMoreProducts = false;

    private RecyclerView mRecyclerView;
    private ProductListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private boolean isScrolling = false;
    private ProgressBar progressBar;

    private int currentItems;
    private int scrolledItems;
    private int totalItems;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("");

        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        progressBar = view.findViewById(R.id.product_list_progress_bar);
        mRecyclerView = view.findViewById(R.id.product_list_recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProductListAdapter(new ArrayList<Product>());
        mRecyclerView.setAdapter(mAdapter);

        getProducts();
        setOnCardListener();
        initOnScrollListener();

        return view;
    }

    private void getProducts() {
        page++;

        Call call = RetrofitClient.getmInstance().getApi().getProducts(page);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body().isEmpty()) {
                    Toast.makeText(getActivity(), "No Products Found!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    noMoreProducts = true;
                    return;
                }

                List<Product> productList = response.body();

                loadProducts(productList);

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadProducts(List<Product> products) {
        mAdapter.addProductsToList(products);
        progressBar.setVisibility(View.GONE);
    }


    private void setOnCardListener() {
        mAdapter.OnCardClickListener(new ProductListAdapter.OnItemClickListener() {
            @Override
            public void onCardClick(int pro_id) {
                ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("pro_id", pro_id);
                productDetailsFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.product_list_fragment_container
                , productDetailsFragment).addToBackStack("product_list").commit();
            }
        });
    }

    private void initOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrolledItems = mLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrolledItems == totalItems) && !noMoreProducts) {
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);

                    getProducts();
                }
            }
        });
    }

}
