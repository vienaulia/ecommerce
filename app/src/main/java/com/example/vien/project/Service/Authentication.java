package com.example.vien.project.Service;

import com.example.vien.project.Response.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Vien on 26/01/2018.
 */

public interface Authentication {

    @FormUrlEncoded
    @POST("auth/register")
    Call<Register> doRegister(@Field("app_key") String app_key, @Field("email") String email, @Field("nama") String nama, @Field("telepon") String telepon, @Field("password") String password, @Field("guid") String guid);
}
