package com.project.rentapp.rent_app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rentapp.rent_app.Api.RetrofitClient;
import com.project.rentapp.rent_app.Models.Category;
import com.project.rentapp.rent_app.Models.DefaultResponse;
import com.project.rentapp.rent_app.Models.User;
import com.project.rentapp.rent_app.R;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class SubmitAdActivity extends BaseNavigationActivity implements View.OnClickListener {
    private int catId;
    private int SELECT_PICTURES = 1;
    private List<String> imagePathList;

    private TextView textViewProTitle;
    private TextView textViewProDesc;
    private TextView textViewProPrice;
    private TextView textViewRentPeriod;
    private TextView textViewPincode;
    private TextView textViewAddress;
    private TextView editTextAddImgMessage;

    private Spinner proCategories;

    private Button submitBtn;
    private Button addImagesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Submit Ad");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_submit_ad, null, false);
        drawer.addView(contentView, 0);

        initToolbar();
        initValues();
        getCategories();
        fillAddressAndPincode();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sa_submit_btn:
                validateInput();
                break;

            case R.id.add_image:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURES && resultCode == RESULT_OK && data != null) {
            imagePathList = new ArrayList<>();

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();

                if (count > 5) {
                    Toast.makeText(this, "Can not upload more that 5 images!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imagePathList.add(getImageFilePath(imageUri, SubmitAdActivity.this));
                }
            } else if (data.getData() != null) {
                Uri imgUri = data.getData();
                imagePathList.add(getImageFilePath(imgUri, SubmitAdActivity.this));
            }
        }
    }

    private String getImageFilePath(Uri uri, Context context) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    private void initValues() {
        textViewProTitle = findViewById(R.id.sa_pro_title);
        textViewProDesc = findViewById(R.id.sa_pro_desc);
        textViewProPrice = findViewById(R.id.sa_pro_price);
        textViewRentPeriod = findViewById(R.id.sa_pro_rent_period);
        textViewPincode = findViewById(R.id.sa_pro_pincode);
        textViewAddress = findViewById(R.id.sa_pro_address);
        proCategories = findViewById(R.id.sa_pro_category);
        editTextAddImgMessage = findViewById(R.id.add_image_message);
        submitBtn = findViewById(R.id.sa_submit_btn);
        submitBtn.setOnClickListener(this);
        addImagesBtn = findViewById(R.id.add_image);
        addImagesBtn.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getCategories() {
        Call<List<Category>> call = RetrofitClient.getmInstance().getApi().getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SubmitAdActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Category> categories = response.body();
                addCategoriesToSpinner(categories);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(SubmitAdActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillAddressAndPincode() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", null);

        if (email == null) { return; }

        Call call = RetrofitClient.getmInstance().getApi().getUserDetails(email);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                User user = response.body();

                textViewPincode.setText(user.getPincode());
                textViewAddress.setText(user.getAddress());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                return;
            }
        });
    }

    private void addCategoriesToSpinner(final List<Category> categories) {
        ArrayList<String> categoryNames = new ArrayList<>();

        for (Category category : categories) {
            categoryNames.add(category.getCat_name());
        }

        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<String>(SubmitAdActivity.this,
                R.layout.spinner_item, categoryNames);

        proCategories.setAdapter(categoryArrayAdapter);

        proCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = (String) parent.getItemAtPosition(position);

                for (Category category : categories) {
                    if (category.getCat_name().equals(categoryName)) {
                        catId = category.getCat_id();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void validateInput() {
        String proTitle = textViewProTitle.getText().toString().trim();
        String proDesc = textViewProDesc.getText().toString().trim();
        String proPrice = textViewProPrice.getText().toString().trim();
        String rentPeriod = textViewRentPeriod.getText().toString().trim();
        String pincode = textViewPincode.getText().toString().trim();
        String address = textViewAddress.getText().toString().trim();

        if (proTitle.isEmpty()) {
            textViewProTitle.setError("Title is required");
            textViewProTitle.requestFocus();
            return;
        }

        if (proDesc.isEmpty()) {
            textViewProDesc.setError("Description is required");
            textViewProDesc.requestFocus();
            return;
        }

        if (proPrice.isEmpty()) {
            textViewProPrice.setError("Price is required");
            textViewProPrice.requestFocus();
            return;
        }

        if (rentPeriod.isEmpty()) {
            textViewRentPeriod.setError("Rent Period is required");
            textViewRentPeriod.requestFocus();
            return;
        }

        if (imagePathList == null) {
            editTextAddImgMessage.setText("At Least Upload 1 Image.");
            editTextAddImgMessage.setTextColor(getResources().getColor(R.color.red));
            return;
        }

        if (pincode.isEmpty()) {
            textViewPincode.setError("Pincode is required");
            textViewPincode.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            textViewAddress.setError("Address is required");
            textViewAddress.requestFocus();
            return;
        }
        submitAd(proTitle, proDesc, proPrice, rentPeriod, pincode, address);
    }

    private void submitAd(String proTitle, String proDesc, String proPrice, String rentPeriod, String pincode, String address) {
        submitBtn.setEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        final int userId = sharedPreferences.getInt("id", 0);

        if (userId == 0) { return; }

        List<File> images = new ArrayList<>();
        for (String path : imagePathList) {
            images.add(new File(path));
        }

        MultipartBody.Part[] parts = new MultipartBody.Part[images.size()];
        for (int i = 0; i < images.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), images.get(i));
            parts[i] = MultipartBody.Part.createFormData("ProductImages[]", images.get(i).getName(), requestBody);
        }

        Call call = RetrofitClient.getmInstance().getApi().submitAd(
                userId,
                proTitle,
                catId,
                proDesc,
                Integer.parseInt(proPrice),
                Integer.parseInt(rentPeriod),
                parts,
                Integer.parseInt(pincode),
                address
        );

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SubmitAdActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }

                DefaultResponse res = response.body();

                Toast.makeText(SubmitAdActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                if (!res.isError()) {
                    startActivity(new Intent(SubmitAdActivity.this, ProductListActivity.class));
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(SubmitAdActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
