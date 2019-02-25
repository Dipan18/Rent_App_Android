package com.project.rentapp.rent_app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Fragments.ProductListFragment;
import com.project.rentapp.rent_app.Models.Category;
import com.project.rentapp.rent_app.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    private int numberOfCategories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getCategories();
        showProfileMenu();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                ProductListFragment productListFragment = new ProductListFragment();
//                Bundle args = new Bundle();
//                args.putString("query", s);
//                productListFragment.setArguments(args);
//
//                getSupportFragmentManager().beginTransaction().add(R.id.product_list_fragment_container,
//                         productListFragment).commit();
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.all_tools) {
            startActivity(new Intent(this, ProductListActivity.class));
        }

        else if (menuItem.getItemId() >= 1 && menuItem.getItemId() <= numberOfCategories) {
            ProductListFragment productListFragment = new ProductListFragment();
            Bundle args = new Bundle();
            args.putInt("cat_id", menuItem.getItemId());
            args.putString("cat_name", (String) menuItem.getTitle());
            productListFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.product_list_fragment_container
                    , productListFragment).commit();
        }

        else if (menuItem.getItemId() == R.id.nav_logout) {
            logout();
        }

        else if (menuItem.getItemId() == R.id.nav_edit_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        else if (menuItem.getItemId() == R.id.nav_ads) {
            Intent userAdsIntent = new Intent(this, UserAdsActivity.class);
            userAdsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(userAdsIntent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void addCategoriesToNav(List<Category> categories) {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();

        if (categories == null) {
            Toast.makeText(this, "Failed to fetch Categories", Toast.LENGTH_LONG).show();
            return;
        }

        for (Category category : categories) {
            menu.add(R.id.categories, category.getCat_id(), Menu.NONE, category.getCat_name()).setIcon(R.drawable.ic_handyman_tools);
        }
        navView.invalidate();
    }

    private void getCategories() {
        Call<List<Category>> call = RetrofitClient
                .getmInstance()
                .getApi()
                .getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(BaseNavigationActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                final List<Category> categories = response.body();
                numberOfCategories = categories.size();
                addCategoriesToNav(categories);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(BaseNavigationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean("logged_in", false);

        return isLoggedIn;
    }

    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!isUserLoggedIn()) {
            Toast.makeText(this, "You are not Logged In!", Toast.LENGTH_LONG).show();
        }

        editor.clear();
        if (editor.commit()) {
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to Logout!", Toast.LENGTH_LONG).show();
        }
    }

    protected void showProfileMenu() {
        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        TextView usernameHeader = headerView.findViewById(R.id.nav_header_username);
        TextView loginHeader = headerView.findViewById(R.id.nav_header_login);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);

        if (isUserLoggedIn()) {
            Menu profileMenu = navView.getMenu();
            profileMenu.setGroupVisible(R.id.profile, true);
            String username = sp.getString("first_name", "") + ' ' + sp.getString("last_name", "");
            loginHeader.setVisibility(View.GONE);
            usernameHeader.setVisibility(View.VISIBLE);
            usernameHeader.setText(username);
        } else {
            usernameHeader.setVisibility(View.GONE);
            loginHeader.setVisibility(View.VISIBLE);

            loginHeader.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(BaseNavigationActivity.this, LoginActivity.class));
                }
            });
        }
    }
}
