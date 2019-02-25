package com.project.rentapp.rent_app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;


import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.DefaultResponse;
import com.project.rentapp.rent_app.Models.User;
import com.project.rentapp.rent_app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseNavigationActivity implements View.OnClickListener {
    private EditText editTextEmailLogin;
    private EditText editTextPasswordLogin;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Login");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, null, false);
        drawer.addView(contentView, 0);

        if (isUserLoggedIn()) { this.finish(); }

        editTextEmailLogin = findViewById(R.id.editTextemailLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);

        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.textViewSignUpLink).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                validateData();
                break;

            case R.id.textViewSignUpLink:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    public void validateData() {
        String email = editTextEmailLogin.getText().toString().trim();
        String password = editTextPasswordLogin.getText().toString();

        if (email.isEmpty()) {
            editTextEmailLogin.setError("E-mail address is required");
            editTextEmailLogin.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailLogin.setError("Enter a valid E-mail address");
            editTextEmailLogin.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPasswordLogin.setError("Password is required.");
            editTextPasswordLogin.requestFocus();
            return;
        }

        userLogin(email, password);
    }

    public void userLogin(String email, String password) {
        Call<DefaultResponse> call = RetrofitClient
                .getmInstance()
                .getApi()
                .login(email, password);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (!response.isSuccessful()) { // where there is http error code (like 404)
                    Toast.makeText(LoginActivity.this, response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                final DefaultResponse res = response.body();

                if (res.isError()) { // error variable in json
                    Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                getUserData();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserData() {
        String email = editTextEmailLogin.getText().toString().trim();

        Call<User> call = RetrofitClient
                .getmInstance()
                .getApi()
                .getUserDetails(email);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                final User user = response.body();
                storeUserData(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void storeUserData(User user) {
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        if (user == null) {
            Toast.makeText(this, "Failed to fetch user Info from API", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logged_in", true);
        editor.putInt("id", user.getId());
        editor.putString("first_name", user.getFirstName());
        editor.putString("last_name", user.getLastName());
        editor.putString("email", user.getEmail());
        editor.putString("pincode", user.getPincode());
        editor.putString("phone_no", user.getPhoneNo());
        editor.putString("address", user.getAddress());

        boolean result = editor.commit();

        if (result) {
            Toast.makeText(this, "Saved SharedPreferences", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ProductListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to save SharedPreferences", Toast.LENGTH_LONG).show();
        }

    }
}
