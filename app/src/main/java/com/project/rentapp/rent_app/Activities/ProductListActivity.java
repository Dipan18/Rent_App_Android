package com.project.rentapp.rent_app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.rentapp.rent_app.Fragments.ProductListFragment;
import com.project.rentapp.rent_app.R;

import java.io.Serializable;

public class ProductListActivity extends BaseNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_product_list, null, false);
        drawer.addView(contentView, 0);

        Intent intent = getIntent();
        int cat_id = intent.getIntExtra("cat_id", 0);
        String cat_name = intent.getStringExtra("cat_name");

        ProductListFragment productListFragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt("cat_id", cat_id);
        args.putString("cat_name", cat_name);
        productListFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.product_list_fragment_container,
                productListFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(ProductListActivity.this, SearchActivity.class);
                intent.putExtra("query", s);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

}
