package com.seawindsolution.podphotographer.Servicew;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.seawindsolution.podphotographer.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ronak Gopani on 11/12/19 at 4:47 PM.
 */
public class Update extends Service implements GoogleApiClient.OnConnectionFailedListener, LocationListener {

    LocationManager locationManager;
    public static String token, Latitude, Longitude;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            SessionManager session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            String id = user.get(SessionManager.KEY_ID);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            token = task.getResult().getToken();
                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d("", msg);
                        }
                    });

            assert id != null;

            getLocation();

        } catch (Exception e) {

        }

        return super.onStartCommand(intent, flags, startId);
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            Latitude = String.valueOf(location.getLatitude());
            Longitude = String.valueOf(location.getLongitude());
            System.out.println("loc + de+to: " + location.getLatitude() + "loc + de+to: " + location.getLongitude() + "loc + de+to " + token);

            SessionManager session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            String id = user.get(SessionManager.KEY_ID);

            assert id != null;
            RequestBody id_S = RequestBody.create(MediaType.parse("text/plain"), id);
            RequestBody lat_s = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.getLatitude()));
            RequestBody long_s = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.getLongitude()));
            RequestBody device_s = RequestBody.create(MediaType.parse("text/plain"), token);

            Url.getWebService().insertPhotographerTrackingData(id_S, device_s, lat_s, long_s).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
                            JSONObject responseObject = AppConstant.getResponseObject(response);

                            System.out.println("response otp    " + responseObject.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                }
            });

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        } catch (Exception e) {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}