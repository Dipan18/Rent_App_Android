package com.project.rentapp.rent_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rentapp.rent_app.Activities.LoginActivity;
import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.User;
import com.project.rentapp.rent_app.R;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView phoneNo;
    private TextView pincode;
    private TextView address;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().setTitle("Profile");
        getProfileData();

        view.findViewById(R.id.edit_profile_btn).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile_btn:
                getFragmentManager().beginTransaction().replace(R.id.profile_fragment_container,
                        new EditProfileFragment()).addToBackStack("profile").commit();
                break;
        }
    }

    private void getProfileData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        if (email == null) { return; }

        Call call = RetrofitClient
                .getmInstance()
                .getApi()
                .getUserDetails(email);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User userData = response.body();
                displayProfile(userData);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayProfile(User user) {
        firstName = getActivity().findViewById(R.id.card_text_first_name);
        firstName.setText(user.getFirstName());
        lastName = getActivity().findViewById(R.id.card_text_last_name);
        lastName.setText(user.getLastName());
        email = getActivity().findViewById(R.id.card_text_email);
        email.setText(user.getEmail());
        phoneNo = getActivity().findViewById(R.id.card_text_phone);
        phoneNo.setText(user.getPhoneNo());
        pincode = getActivity().findViewById(R.id.card_text_pincode);
        pincode.setText(user.getPincode());
        address = getActivity().findViewById(R.id.card_text_address);
        address.setText(user.getAddress());
    }
}
