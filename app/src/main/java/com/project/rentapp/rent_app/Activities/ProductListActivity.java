package com.project.rentapp.rent_app.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.project.rentapp.rent_app.Fragments.ProductListFragment;
import com.project.rentapp.rent_app.R;

public class ProductListActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_product_list, null, false);
        drawer.addView(contentView, 0);

        getSupportFragmentManager().beginTransaction().replace(R.id.product_list_fragment_container,
                new ProductListFragment()).commit();
    }
}
