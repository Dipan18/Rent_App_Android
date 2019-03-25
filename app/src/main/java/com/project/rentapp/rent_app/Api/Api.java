package com.project.rentapp.rent_app.Api;

import com.project.rentapp.rent_app.Models.Category;
import com.project.rentapp.rent_app.Models.DefaultResponse;
import com.project.rentapp.rent_app.Models.Product;
import com.project.rentapp.rent_app.Models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("Signup.php")
    Call<ResponseBody> signUp(
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("email") String email,
            @Field("phone_no") String phoneNumber,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST("Authenticate.php")
    Call<DefaultResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("get_categories.php")
    Call<List<Category>> getCategories();

    @GET("get_userdata.php")
    Call<User> getUserDetails(@Query("email") String email);

    @FormUrlEncoded
    @PUT("update_profile.php")
    Call<DefaultResponse> updateProfile(
            @Field("id") int id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("pincode") String pincode,
            @Field("address") String address
    );

    @GET("user_ads.php")
    Call<List<Product>> getUserAds(@Query("id") int id);

    @GET("product_details.php")
    Call<Product> getProductDetails(@Query("product_id") int product_id);

    @GET("remove_ad.php")
    Call<DefaultResponse> removeAd(@Query("id") int id);

    @GET("get_products.php")
    Call<List<Product>> getProducts(@Query("page") int page);

    @GET("products_by_category.php")
    Call<List<Product>> getCategorizedProducts(@Query("category_id") int cat_id, @Query("page") int page);

    @GET("search.php")
    Call<List<Product>> getSearchResults(@Query("query") String query);

    @Multipart
    @POST("submit_ad.php")
    Call<DefaultResponse> submitAd(
            @Part("user_id") int userId,
            @Part("pro_title") String proTitle,
            @Part("cat_id") int catId,
            @Part("pro_desc") String proDesc,
            @Part("pro_price") int proPrice,
            @Part("rent_period") int rentPeriod,
            @Part MultipartBody.Part[] productImages,
            @Part("pincode") int pincode,
            @Part("address") String address
    );

    @GET("rented_products.php")
    Call<List<Product>> getRentedProductsByUser(@Query("id") int id);

    @GET("my_requests.php")
    Call<List<Product>> getRequestsOnMyProducts(@Query("id") int id);

    @GET("accept_rent_request.php")
    Call<DefaultResponse> acceptRentRequest(@Query("id") int id);

    @GET("reject_rent_request.php")
    Call<DefaultResponse> rejectRentRequest(@Query("id") int id);
}
