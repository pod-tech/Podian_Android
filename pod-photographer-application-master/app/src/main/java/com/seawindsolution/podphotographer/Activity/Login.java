package com.seawindsolution.podphotographer.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    ImageView ig_facebook, ig_google;
    EditText ed_mobile_no, ed_password;
    TextView ll_registration;
    TextView tv_forgot_password;
    Button bt_submit;
    LinearLayout tv_freelancer;
    LoginButton loginButton;
    ProgressDialog progressDialog;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    CallbackManager callbackManager;
    boolean doubleBackToExitPressedOnce = false;

    public void onStart() {
        super.onStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            useLoginInformation(accessToken);
            Intent intent = new Intent(Login.this, HomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            Intent intent = new Intent(Login.this, HomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = ProgressDialog.show(Login.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        ig_facebook = findViewById(R.id.ig_facebook);
        ig_google = findViewById(R.id.ig_google);
        ed_mobile_no = findViewById(R.id.ed_mobile_no);
        ed_password = findViewById(R.id.ed_password);
        ll_registration = findViewById(R.id.ll_registration);
        bt_submit = findViewById(R.id.bt_submit);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_freelancer = findViewById(R.id.tv_freelancer);

        //////////////////////////////////////////////////////////////////////////////////////////// Sign in with Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ig_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////// Sign in with Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));

        ig_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        // Creating CallbackManager
        callbackManager = CallbackManager.Factory.create();
        // Registering CallbackManager with the LoginButton
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                useLoginInformation(accessToken);
                Intent intent = new Intent(Login.this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("error       " + error);
            }
        });

        getKeyHash();

        tv_freelancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Freelancer.class);
                startActivity(intent);
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////// Manual login
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile_no = ed_mobile_no.getText().toString();

                String id = ed_mobile_no.getText().toString();

                if (id.matches("\\d+(?:\\.\\d+)?")) {
                    System.out.println("Number");
                    if (id.length() == 10 && android.util.Patterns.PHONE.matcher(id).matches()) {
                        call_loin();
                    } else {
                        ed_mobile_no.setError("Invalid Mobile Number");
                    }
                } else {
                    System.out.println("email");
                    if (!id.equals("") && Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
                        call_loin();
                    } else {
                        ed_mobile_no.setError("Invalid Email ID");
                    }
                }
            }
        });

        ll_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Forgot_password.class);
                startActivity(intent);
            }
        });
    }

    private void call_loin() {
        if (!ed_password.equals("")) {
            progressDialog.show();
            String User_id = ed_mobile_no.getText().toString();
            String Psd = ed_password.getText().toString();
            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), User_id);
            RequestBody psd = RequestBody.create(MediaType.parse("text/plain"), Psd);

            Url.getWebService().photographerLogin(user_id, psd).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println("response     " + response.toString());
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject responseObject = AppConstant.getResponseObject(response);

                            System.out.println("response login    " + responseObject.toString());

                            if (responseObject.optBoolean("IsSuccess")) {
                                String detail = responseObject.optString("ResponseData");
                                JSONObject jsonObject = new JSONObject(detail);
                                String Id = jsonObject.optString("Id");
                                String Name = jsonObject.optString("Name");
                                String Email = jsonObject.optString("Email");

                                System.out.println("        idddd" + Id + "   " + Name + "        " + Email);

                                // Session Manager
                                SessionManager session = new SessionManager(getApplicationContext());
                                session.createLoginSession(Name, Email, Id);

                                Intent intent = new Intent(Login.this, HomeScreen.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(Login.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(Login.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, R.string.error_pod,
                            Toast.LENGTH_SHORT).show();
                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////// Google Details
  /*  @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            try {

                System.out.println(account.getId());
                System.out.println(account.getDisplayName());
                System.out.println(account.getEmail());
                System.out.println(account.getPhotoUrl());
                System.out.println(account.getDisplayName());
                System.out.println(account.getGivenName());
                System.out.println(account.getIdToken());
                System.out.println(account.getServerAuthCode());

                String socialid_s = account.getId();
                String name_s = account.getDisplayName();
                String email_s = account.getEmail();
                String image_url = String.valueOf(account.getPhotoUrl());

                assert socialid_s != null;
                RequestBody social_id = RequestBody.create(MediaType.parse("text/plain"), socialid_s);
                assert name_s != null;
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), name_s);
                assert email_s != null;
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), email_s);
                RequestBody SignBy = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(3));
                RequestBody other = RequestBody.create(MediaType.parse("text/plain"), "");

                RequestBody imageprofleurl = RequestBody.create(MediaType.parse("text/plain"), image_url);

                if (!socialid_s.equals("")) {
                    if (!email_s.equals("")) {
                        if (!name_s.equals("")) {
                            try {
                                System.out.println(name_s.toString()+"  "+email_s.toString()+"  "+other+"       "+String.valueOf(3)+"   "+socialid_s.toString()+"    "+image_url+"  "+imageprofleurl);
                                Url.getWebService().photographerRegistration(name, email, other, other, other, other, SignBy, social_id, imageprofleurl, null).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        System.out.println(32);

                                        if (response.isSuccessful()) {
                                            try {
                                                JSONObject responseObject = AppConstant.getResponseObject(response);
                                                System.out.println(323+response.toString());
                                                if (responseObject.optBoolean("IsSuccess")) {
                                                    String detail = responseObject.optString("ResponseData");
                                                    System.out.println(35423);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(detail);
                                                        String Id = jsonObject.optString("Id");
                                                        String Name = jsonObject.optString("Name");
                                                        String Email = jsonObject.optString("Email");

                                                        SessionManager session = new SessionManager(getApplicationContext());
                                                        session.createLoginSession(Name, Email, Id);
                                                        gotoProfile();
                                                    } catch (Exception e) {

                                                    }
                                                } else {
                                                    Toast.makeText(Login.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            Toast.makeText(Login.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        Toast.makeText(Login.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                        Log.d("", "Error in Ticket Category : " + t.getMessage());
                                    }
                                });
                            } catch (Exception e) {

                            }
                        } else {
                        }
                    } else {
                    }
                } else {
                }
            } catch (Exception e) {
                System.out.println(1189114);
                e.printStackTrace();
            }

        } else {
            System.out.println(118989114);
            // Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile() {
        Intent intent = new Intent(Login.this, HomeScreen.class);
        startActivity(intent);
    }

    //////////////////////////////////////////////////////////////////////////////////////////// Facebook Details
    private void useLoginInformation(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String socialid_s = object.getString("id");
                    String name_s = object.getString("name");
                    String email_s = object.getString("email");

                    System.out.println("Fname   " + name_s + "  " + email_s);

                    RequestBody social_id = RequestBody.create(MediaType.parse("text/plain"), socialid_s);
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), name_s);
                    RequestBody email = RequestBody.create(MediaType.parse("text/plain"), email_s);
                    RequestBody SignBy = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(2));
                    RequestBody other = RequestBody.create(MediaType.parse("text/plain"), "");

                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    RequestBody imageprofleurl = RequestBody.create(MediaType.parse("text/plain"), image);

                    System.out.println("Fname   " + name + "  " + email + "  " + image);

                    if (imageprofleurl.equals(null)) {
                        imageprofleurl = RequestBody.create(MediaType.parse("text/plain"), "NA");
                    }

                    if (!socialid_s.equals("")) {
                        if (!email_s.equals("")) {
                            if (!name_s.equals("")) {
                                try {
                                    Url.getWebService().photographerRegistration(name, email, other, other, other, other, SignBy, social_id, imageprofleurl, null).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject responseObject = AppConstant.getResponseObject(response);

                                                    if (responseObject.optBoolean("IsSuccess")) {
                                                        String detail = responseObject.optString("ResponseData");
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(detail);
                                                            String Id = jsonObject.optString("Id");
                                                            String Name = jsonObject.optString("Name");
                                                            String Email = jsonObject.optString("Email");

                                                            SessionManager session = new SessionManager(getApplicationContext());
                                                            session.createLoginSession(Name, Email, Id);

                                                        } catch (Exception e) {

                                                        }
                                                    } else {
                                                        Toast.makeText(Login.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                Toast.makeText(Login.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            Toast.makeText(Login.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                            Log.d("", "Error in Ticket Category : " + t.getMessage());
                                        }
                                    });
                                } catch (Exception e) {

                                }
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    //////////////////////////////////////////////////////////////////////////////////////////// Facebook Hash key
    private void getKeyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.seawindsolution.pod", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
                System.out.println("hash    " + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
