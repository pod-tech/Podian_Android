package com.seawindsolution.podphotographer.Activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Update;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeScreen extends AppCompatActivity implements LocationListener {

    CardView crd_profile, crd_availability, crd_order, crd_settings;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    Boolean crdbookbg = false, crdinqbg = false;
    int inquiry_dialog;
    ImageView menu;
    FrameLayout notification;
    public static String MY_PREFS_NAME = "nameOfSharedPreferences";
    String token, Latitude, Longitude;
    LocationManager locationManager;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<Notification_PFt> arrayLists = new ArrayList<>();
    TextView textCartItemCount;
    String Name, Email, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        startService(new Intent(HomeScreen.this, Update.class));
        getLocation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FirebaseMessaging.getInstance().subscribeToTopic("global").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Name = user.get(SessionManager.KEY_NAME);
        Email = user.get(SessionManager.KEY_EMAIL);
        id = user.get(SessionManager.KEY_ID);

        crd_profile = findViewById(R.id.crd_profile);
        crd_availability = findViewById(R.id.crd_availability);
        crd_order = findViewById(R.id.crd_order);
        crd_settings = findViewById(R.id.crd_settings);
        notification = findViewById(R.id.notification);
        textCartItemCount = (TextView) findViewById(R.id.cart_badge);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        /*googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)layout_loading_dialog
                .build();*/

        crd_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Profile_Activity.class);
                startActivity(intent);
            }
        });

        crd_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Daily_Availability.class);
                startActivity(intent);
            }
        });

        crd_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Order_Activity.class);
                startActivity(intent);
            }
        });

        crd_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Settings.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, Notification_P.class);
                startActivity(intent);
            }
        });

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
                        System.out.println("RRRRR       " + token + "           " + msg);


                    }
                });
        /*NotificationHelper.cancelAlarmRTC();
            NotificationHelper.disableBootReceiver(mContext);*/

        try {
            arrayLists.clear();
            Url.getWebService().getCustomNotificationPhotographer(Integer.parseInt(id)).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        JSONObject responseObject = AppConstant.getResponseObject(response);

                        if (responseObject != null) {
                            if (responseObject.optBoolean("IsSuccess")) {
                                List<Notification_PFt> temp_survey = new Gson().fromJson(responseObject
                                        .optString("ResponseData"), new TypeToken<List<Notification_PFt>>() {
                                }.getType());
                                arrayLists.clear();
                                arrayLists.addAll(temp_survey);
                                String a = String.valueOf(arrayLists.size());
                                textCartItemCount.setText(a);
                                arrayLists.clear();
                            } else {
                                Toast.makeText(getApplicationContext(), responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), R.string.error_pod,
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            Url.getWebService().getCustomNotificationPhotographer(Integer.parseInt(id)).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        JSONObject responseObject = AppConstant.getResponseObject(response);

                        if (responseObject != null) {
                            if (responseObject.optBoolean("IsSuccess")) {
                                List<Notification_PFt> temp_survey = new Gson().fromJson(responseObject
                                        .optString("ResponseData"), new TypeToken<List<Notification_PFt>>() {
                                }.getType());
                                arrayLists.clear();
                                arrayLists.addAll(temp_survey);
                                String a = String.valueOf(arrayLists.size());
                                textCartItemCount.setText(a);
                                arrayLists.clear();
                            } else {
                                Toast.makeText(getApplicationContext(), responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), R.string.error_pod,
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            Latitude = String.valueOf(location.getLatitude());
            Longitude = String.valueOf(location.getLongitude());
            System.out.println("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude() + "\n Device id: " + token);

            SessionManager session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            String id = user.get(SessionManager.KEY_ID);

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
}
