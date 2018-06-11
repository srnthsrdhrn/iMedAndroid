package io.iqube.srinath.imeddispenser.network;

import java.util.List;

import io.iqube.srinath.imeddispenser.models.Prescription;
import io.iqube.srinath.imeddispenser.models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Srinath on 12/04/2018.
 */

public interface API_interface {

    @FormUrlEncoded
    @POST("login/")
    Call<User> userLogin(@Field("username") String username,
                         @Field("password") String password);

    @GET("user_1_prescription")
    Call<List<Prescription>> fetchPrescription(@Query("username") String username, @Query("user_id") int user_id);

    @FormUrlEncoded
    @POST("load")
    Call<Void> postLoad(@Field("data") String data);
//    @POST("forgotPassword/")
//    Call<ForgotPassword> forgotPassword(@Body HashMap<String, String> Email);

}
