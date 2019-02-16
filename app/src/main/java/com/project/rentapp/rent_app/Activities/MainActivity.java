package com.project.rentapp.rent_app.Activities;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseNavigationActivity implements View.OnClickListener {
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Sign up");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLoginLink).setOnClickListener(this);
    }


    public void validateData() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        if (firstName.isEmpty()) {
            editTextFirstName.setError("First name is required");
            editTextFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            editTextLastName.setError("Last name is required");
            editTextLastName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("E-Mail is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid E-mail address");
            editTextEmail.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Phone number is required");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (phoneNumber.length() != 10) {
            editTextPhoneNumber.setError("Phone number should be 10 digits long.");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        registerUser(firstName, lastName, email, phoneNumber, password);
    }


    public void registerUser(String firstName, String lastName, String email, String phoneNumber, String password) {
        Call<ResponseBody> call = RetrofitClient
                .getmInstance()
                .getApi()
                .signUp(firstName, lastName, email, phoneNumber, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = null;

                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        if (!jsonObject.getBoolean("error")) {
//                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                validateData();
                break;

            case R.id.textViewLoginLink:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
