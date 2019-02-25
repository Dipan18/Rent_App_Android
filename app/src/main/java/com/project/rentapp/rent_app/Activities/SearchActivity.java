package com.project.rentapp.rent_app.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.project.rentapp.rent_app.Fragments.ProductDetailsFragment;
import com.project.rentapp.rent_app.Fragments.SearchFragment;
import com.project.rentapp.rent_app.R;

public class SearchActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Search Results");

        initToolbar();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search, null, false);
        drawer.addView(contentView, 0);

        loadSearchResultFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailsFragment productDetailsFragment = (ProductDetailsFragment) getSupportFragmentManager()
                        .findFragmentByTag("productDetails");

                if (productDetailsFragment != null && productDetailsFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().remove(productDetailsFragment).commit();
                } else {
                    finish();
                }
            }
        });
    }

    private void loadSearchResultFragment() {
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("query", getIntent().getStringExtra("query"));
        searchFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.search_result_fragment_container,
                searchFragment).commit();
    }

}
