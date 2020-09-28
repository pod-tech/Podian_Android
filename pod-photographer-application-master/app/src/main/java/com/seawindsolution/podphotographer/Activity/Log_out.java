package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Log_out extends AppCompatActivity {

    public GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog;

    public Log_out() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        progressDialog = ProgressDialog.show(Log_out.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        try {
            progressDialog.show();
            SessionManager session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            String id = user.get(SessionManager.KEY_ID);

            assert id != null;
            RequestBody id_S = RequestBody.create(MediaType.parse("text/plain"), id);
            RequestBody lat_s = RequestBody.create(MediaType.parse("text/plain"), "00");
            RequestBody long_s = RequestBody.create(MediaType.parse("text/plain"), "00");
            RequestBody device_s = RequestBody.create(MediaType.parse("text/plain"), "");

            Url.getWebService().insertPhotographerTrackingData(id_S, device_s, lat_s, long_s).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();
                    SessionManager session = new SessionManager(getApplicationContext());
                    session.logoutUser();
                    LoginManager.getInstance().logOut();
                    GoogleSignInOptions gso = new GoogleSignInOptions.
                            Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                            build();

                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                    googleSignInClient.signOut();
                    SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = pref.edit();
                    edt.clear();
                    edt.apply();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            SessionManager session = new SessionManager(getApplicationContext());
            session.logoutUser();
            LoginManager.getInstance().logOut();
            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
            googleSignInClient.signOut();
            SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = pref.edit();
            edt.clear();
            edt.apply();
        }

    }
}
