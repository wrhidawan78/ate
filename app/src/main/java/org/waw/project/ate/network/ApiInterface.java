package org.waw.project.ate.network;

import org.waw.project.ate.helper.UploadObject;
import org.waw.project.ate.model.Asset;
import org.waw.project.ate.model.AssetDetail;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/ci-rest/api/Users/user_registration")
    Call<ResponseBody> registerRequest(@Field("device_id") String device_id,
                                       @Field("nik") String nik,
                                       @Field("email") String email,
                                       @Field("password") String password);
    @FormUrlEncoded
    @POST("/ci-rest/api/Users/user_login")
    Call<ResponseBody> loginRequest(@Field("device_id") String device_id,
                                       @Field("email") String email,
                                       @Field("password") String password);


    @Multipart
    @POST("/ci-rest/api/AssetRegistration/asset_registration")
    Call<UploadObject> uploadSingleFile(@Part("assetTypeName") RequestBody assetTypeName,
                                        @Part("assetID") RequestBody assetID,
                                        @Part("deviceID") RequestBody deviceID,
                                        @Part MultipartBody.Part file,
                                        @Part("fileName") RequestBody name,
                                        @Part("projectCode") RequestBody projectCode,
                                        @Part("remark") RequestBody remark);


    @GET("/ci-rest/api/AssetRegistration/asset_registration/{device_id}")
    Call<List<Asset>> getAssetRegistrationData(@Query("device_id") String device_id);

    @GET("/ci-rest/api/AssetRegistration/asset_registration_detail/{period}")
    Call<List<AssetDetail>> getAssetRegistrationDetailData(
            @Query("device_id") String period,
            @Query("month_") String month_,
            @Query("year_") String year_
            );


}
