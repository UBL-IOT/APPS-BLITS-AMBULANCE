package com.example.blits.network;

import com.example.blits.model.PesananModel;
import com.example.blits.response.DriverResponse;
import com.example.blits.response.PesananResponse;
import com.example.blits.util.CommonRespon;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {
    @POST("pesanan/input")
    Call<CommonRespon> createPesanan(@Body PesananModel data);

    @GET("drivers/get-driver")
    Call<DriverResponse> getListDriver();

    @GET("pesanan/byuser/{guid_user}")
    Call<PesananResponse> getPesanan(@Path("guid_user") String guid_user);

    @GET("drivers/getDetailDriver/{guid}")
    Call<DriverResponse> getDriverByGuid(@Path("guid") String guid_user);
}
