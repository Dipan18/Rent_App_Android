package com.project.rentapp.rent_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rentapp.rent_app.Activities.ProfileActivity;
import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.DefaultResponse;
import com.project.rentapp.rent_app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment implements View.OnClickListener {
    TextView editTextFirstName;
    TextView editTextLastName;
    TextView editTextPincode;
    TextView editTextAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        view.findViewById(R.id.update_profile_btn).setOnClickListener(this);
        getActivity().setTitle("Update Profile");

        editTextFirstName = view.findViewById(R.id.et_first_name_edit_profile);
        editTextLastName = view.findViewById(R.id.et_last_name_edit_profile);
        editTextPincode = view.findViewById(R.id.et_pincode_edit_profile);
        editTextAddress = view.findViewById(R.id.et_address_edit_profile);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_profile_btn:
                validateInput();
                break;
        }
    }

    private void validateInput() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String pincode = editTextPincode.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        int userId = sharedPreferences.getInt("id", -1);

        if (userId == -1) {
            Toast.makeText(getActivity(), "Shared Preferences Empty!", Toast.LENGTH_LONG).show();
            return;
        }

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

        if (pincode.isEmpty()) {
            editTextPincode.setError("Pincode is Required");
            editTextPincode.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
            return;
        }

        sendDataToApi(userId, firstName, lastName, pincode, address);
    }

    private void sendDataToApi(int userId, String firstName, String lastName, String pincode, String address) {
        Call call = RetrofitClient
                .getmInstance()
                .getApi()
                .updateProfile(userId, firstName, lastName, pincode, address);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                DefaultResponse res = response.body();

                if (res.isError()) {
                    Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ((ProfileActivity)getActivity()).logout();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
