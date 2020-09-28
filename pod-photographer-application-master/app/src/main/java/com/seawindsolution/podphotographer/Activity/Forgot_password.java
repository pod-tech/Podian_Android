package com.seawindsolution.podphotographer.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.seawindsolution.podphotographer.Activity.Registration.isValidPassword;

public class Forgot_password extends AppCompatActivity {

    EditText ed_id, ed_otp, ed_confirm_password, ed_password;
    Button bt_submit;
    TextView tv_login, tv_resend;
    CardView card_otp, card_password, card_confirm_password;
    ProgressDialog progressDialog;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ed_id = findViewById(R.id.ed_id);
        ed_otp = findViewById(R.id.ed_otp);
        ed_confirm_password = findViewById(R.id.ed_confirm_password);
        ed_password = findViewById(R.id.ed_password);
        bt_submit = findViewById(R.id.bt_submit);
        tv_login = findViewById(R.id.tv_login);
        card_otp = findViewById(R.id.card_otp);
        card_password = findViewById(R.id.card_password);
        card_confirm_password = findViewById(R.id.card_confirm_password);
        tv_resend = findViewById(R.id.tv_resend);

        progressDialog = ProgressDialog.show(Forgot_password.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = ed_id.getText().toString();

                if (id.matches("\\d+(?:\\.\\d+)?")) {
                    System.out.println("Number");
                    if (id.length() == 10 && android.util.Patterns.PHONE.matcher(id).matches()) {
                        call_forgot_password();
                    } else {
                        ed_id.setError("Invalid Mobile Number");
                    }
                } else {
                    System.out.println("email");
                    if (!id.equals("") && Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
                        call_forgot_password();
                    } else {
                        ed_id.setError("Invalid Email ID");
                    }
                }
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_id = ed_id.getText().toString();
                RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);

                Url.getWebService().photographerForgotPasswordReSendOTP(id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        System.out.println("response forgot    " + response.toString());

                        if (response.isSuccessful()) {
                            try {
                                JSONObject responseObject = AppConstant.getResponseObject(response);

                                System.out.println("response otp    " + response.body().toString());

                                if (responseObject.optBoolean("IsSuccess")) {

                                    Toast.makeText(Forgot_password.this, "OTP successfully send", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(Forgot_password.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                    tv_resend.setVisibility(View.VISIBLE);
                                }

                            } catch (Exception e) {
                                tv_resend.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(Forgot_password.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                            tv_resend.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        tv_resend.setVisibility(View.VISIBLE);
                        Toast.makeText(Forgot_password.this, R.string.error_pod,
                                Toast.LENGTH_SHORT).show();
                        Log.d("", "Error in Ticket Category : " + t.getMessage());
                    }
                });
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forgot_password.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void call_forgot_password() {

        if (bt_submit.getText().toString().equals("Continue")) {
            call_forgot_password_continue();
        } else if (bt_submit.getText().toString().equals("Submit")) {
            call_forgot_password_submit();
        } else {

        }
    }

    private void call_forgot_password_continue() {

        try {
            progressDialog.show();
            String E_O_P_S = ed_id.getText().toString();
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), E_O_P_S);

            Url.getWebService().photographerForgotPasswordSendOTP(id).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();
                    System.out.println("response forgot    " + response.toString());

                    if (response.isSuccessful()) {
                        try {
                            JSONObject responseObject = AppConstant.getResponseObject(response);

                            System.out.println("response otp    " + response.body().toString());

                            if (responseObject.optBoolean("IsSuccess")) {

                                tv_resend.setVisibility(View.VISIBLE);
                                ed_id.setVisibility(View.GONE);
                                card_otp.setVisibility(View.VISIBLE);
                                card_password.setVisibility(View.VISIBLE);
                                card_confirm_password.setVisibility(View.VISIBLE);

                                bt_submit.setText("Submit");

                            } else {
                                Toast.makeText(Forgot_password.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                tv_resend.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            tv_resend.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(Forgot_password.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                        tv_resend.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    tv_resend.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    Toast.makeText(Forgot_password.this, R.string.error_pod,
                            Toast.LENGTH_SHORT).show();
                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                }
            });
        }catch (Exception e){
            Toast.makeText(Forgot_password.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
        }
    }

    private void call_forgot_password_submit() {

        try {
            String password = ed_password.getText().toString();
            String confirm_password = ed_confirm_password.getText().toString();

            System.out.println(password + "       " + confirm_password);
            if (password.equals(confirm_password)) {
                if (!isValidPassword(ed_password.getText().toString())) {
                    ed_password.setError("Password must contains one lowercase characters, one uppercase characters,one special symbols in the list \"@#$%\" length at least 6 characters and maximum of 20");
                } else {

                    progressDialog.show();
                    String E_O_P_S = ed_id.getText().toString();
                    String Password_S = ed_password.getText().toString();
                    String OTP_S = ed_otp.getText().toString();
                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"), E_O_P_S);
                    RequestBody psd = RequestBody.create(MediaType.parse("text/plain"), Password_S);
                    RequestBody otp = RequestBody.create(MediaType.parse("text/plain"), OTP_S);

                    Url.getWebService().photographerResetPassword(id, psd, otp).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            System.out.println("response forgot    " + response.toString());

                            if (response.isSuccessful()) {
                                try {
                                    JSONObject responseObject = AppConstant.getResponseObject(response);

                                    System.out.println("response otp    " + response.body().toString());

                                    if (responseObject.optBoolean("IsSuccess")) {

                                        Intent intent = new Intent(Forgot_password.this, Login.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(Forgot_password.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(Forgot_password.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(Forgot_password.this, R.string.error_pod,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("", "Error in Ticket Category : " + t.getMessage());
                        }
                    });
                }
                // call btn submit

            } else {
                ed_confirm_password.setError("Password not match");
            }
        }catch (Exception e){
            Toast.makeText(Forgot_password.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
        }

    }
}
