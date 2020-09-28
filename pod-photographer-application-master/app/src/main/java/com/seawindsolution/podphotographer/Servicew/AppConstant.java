package com.seawindsolution.podphotographer.Servicew;

import android.annotation.SuppressLint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

@SuppressLint("SimpleDateFormat")
public class AppConstant {

    public static String HEADER_KEY = "X-API-KEY";
    public static String HEADER_KEY_VALUE = "123456789123456789";

    public static JSONObject getResponseObject(Response<ResponseBody> response) {
        try {
            return new JSONObject(response.body().string());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
