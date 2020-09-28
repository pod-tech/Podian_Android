package com.seawindsolution.podphotographer.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.seawindsolution.podphotographer.Pojo.AppVersionBean;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    ImageView GifImageView;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean isGPS = false;
    private boolean isMinimized = false;

    @Override
    protected void onStart() {
        super.onStart();

        if(isMinimized){

            isMinimized = false;
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        GifImageView = findViewById(R.id.image_gif);


        checkAppVersion();

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean storageaAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean gpsAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean finelocationAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (storageaAccepted && cameraAccepted && gpsAccepted && finelocationAccepted) {

                        SessionManager session = new SessionManager(getApplicationContext());

                        int SPLASH_DISPLAY_LENGTH = 2900;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /* Create an Intent that will start the Menu-Activity. */

                                if (!session.isLoggedIn()) {
                                    Intent mainIntent = new Intent(MainActivity.this, Login.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    MainActivity.this.startActivity(mainIntent);
                                    MainActivity.this.finish();
                                }
                                if (!isGPS) {
                                    Toast.makeText(MainActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(MainActivity.this);
                                    myAlertDialog.setMessage("Please Turn On Gps");
                                    myAlertDialog.setPositiveButton("Exit",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    finish();
                                                }
                                            });
                                    myAlertDialog.setCancelable(false);
                                    myAlertDialog.show();
                                    return;
                                } else {
                                    if (!session.isLoggedIn()) {
                                        Intent mainIntent = new Intent(MainActivity.this, Login.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        MainActivity.this.startActivity(mainIntent);
                                        MainActivity.this.finish();
                                    } else {
                                        Intent mainIntent = new Intent(MainActivity.this, HomeScreen.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        MainActivity.this.startActivity(mainIntent);
                                    }
                                }
                            }
                        }, SPLASH_DISPLAY_LENGTH);

                        Glide.with(this)
                                .load(R.raw.gifpod)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                                .into(GifImageView);
                    } else {

                        requestPermission();
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isMinimized = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    public void checkAppVersion() {
        ProgressDialog progressDialog;

        progressDialog = ProgressDialog.show(MainActivity.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            Url.getWebService().getAppVersion(2).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject responseObject = AppConstant.getResponseObject(response);
                            assert responseObject != null;
                            if (responseObject.optBoolean("IsSuccess")) {
                                //String detail = responseObject.optString("ResponseData");
                                try {

                                    AppVersionBean appVersionBean = new Gson().fromJson(responseObject.toString(), AppVersionBean.class);

                                    if (appVersionBean != null) {
                                        if (appVersionBean.getResponseData() != null && appVersionBean.getResponseData().size() > 0) {
                                            String version = appVersionBean.getResponseData().get(0).getVandroid();
                                            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                                            String appversions = pInfo.versionName;
//                                            version = appversions;
                                            if (!appversions.equals(version)) {

                                                Intent mainIntent = new Intent(MainActivity.this, VersionUpdateActivity.class);
                                                MainActivity.this.startActivity(mainIntent);
                                                MainActivity.this.finish();

                                            } else {
                                                requestPermission();
                                                new GpsUtils(MainActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                                                    @Override
                                                    public void gpsStatus(boolean isGPSEnable) {
                                                        // turn on GPS
                                                        isGPS = isGPSEnable;
                                                    }
                                                });
                                            }
                                        }
                                    }


                                } catch (Exception e) {

                                }
                            } else {
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Log.d("", "Error in check App version: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
        }
    }
}
