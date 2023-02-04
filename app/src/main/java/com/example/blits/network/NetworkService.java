package com.example.blits.network;

import com.example.blits.model.ModelUser;
import com.example.blits.model.PesananModel;
import com.example.blits.response.DriverResponse;
import com.example.blits.response.PesananResponse;
import com.example.blits.response.UserRespon;
import com.example.blits.util.CommonRespon;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NetworkService {

    @PUT("users/user-update/{guid}")
    Call<UserRespon> updateProfile(@Path("guid") String guid , @Body ModelUser model);

    @POST("pesanan/input")
    Call<CommonRespon> createPesanan(@Body PesananModel data);

    @GET("drivers/get-driver")
    Call<DriverResponse> getListDriver();

    @GET("pesanan/byuser/{guid_user}")
    Call<PesananResponse> getPesanan(@Path("guid_user") String guid_user);

    @GET("pesanan/historyByuser/{guid_user}")
    Call<PesananResponse> getHistoryPesananByUser(@Path("guid_user") String guid_user);

    @GET("pesanan/historyByDriver/{guid_driver}")
    Call<PesananResponse> getHistoryPesananByDriver(@Path("guid_driver") String guid_user);

    @GET("pesanan/bydriver/{guid_driver}")
    Call<PesananResponse> getPesananByDriver(@Path("guid_driver") String guid_driver);

    @PUT("pesanan/update-pesanan/{guidpesanan}")
    Call<CommonRespon> pickOrder(@Path("guidpesanan") String guidpesanan ,@Body PesananModel model);

    @GET("drivers/getDetailDriver/{guid}")
    Call<DriverResponse> getDriverByGuid(@Path("guid") String guid_user);
}
