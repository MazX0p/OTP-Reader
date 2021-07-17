package com.pandasdroid.otpreader;

import com.pandasdroid.otpreader.pojomessages.ResMessages;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    public static String BASE_URL = "http://vpsakhi.com/";

    @FormUrlEncoded
    @POST("otpreader/index.php")
    Call<ResMessages> GetMessages(@Field("user") String user_id);

    @FormUrlEncoded
    @POST("otpreader/index.php")
    Call<ResponseBody> DeleteUser(@Field("delete_user") String user_id);

    @FormUrlEncoded
    @POST("otpreader/index.php")
    Call<ResponseBody> AddUser(@Field("add_user") String add_user);

    @FormUrlEncoded
    @POST("otpreader/index.php")
    Call<ResponseBody> Login(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("otpreader/index.php")
    Call<ResponseBody> ClearOTP(@Field("clean_user_id") String clean);

    @FormUrlEncoded
    @POST("otpreader/index.php")
    Call<ResUsers> GetUserList(@Field("get_user_list") String get_user_list);
}
