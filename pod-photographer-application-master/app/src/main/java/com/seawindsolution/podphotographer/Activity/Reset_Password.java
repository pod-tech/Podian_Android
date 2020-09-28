package com.seawindsolution.podphotographer.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.seawindsolution.podphotographer.Activity.Registration.isValidPassword;

public class Reset_Password extends AppCompatActivity {

    EditText ed_current_password, ed_password, ed_confirm_password;
    Button bt_submit;
    String password, old_password, confirm_password;
    ProgressDialog progressDialog;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        progressDialog = ProgressDialog.show(Reset_Password.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        ed_current_password = findViewById(R.id.ed_current_password);
        ed_password = findViewById(R.id.ed_password);
        ed_confirm_password = findViewById(R.id.ed_confirm_password);
        bt_submit = findViewById(R.id.bt_submit);

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

                old_password = ed_current_password.getText().toString();
                password = ed_password.getText().toString();
                confirm_password = ed_confirm_password.getText().toString();

                if (!old_password.equals("")) {
                    if (password.equals(confirm_password)) {

                        if (!isValidPassword(ed_password.getText().toString())) {
                            ed_password.setError("Password must contains one lowercase characters, one uppercase characters,one special symbols in the list \"@#$%\" length at least 6 characters and maximum of 20");
                        } else {
                            progressDialog.show();

                            SessionManager session = new SessionManager(getApplicationContext());
                            HashMap<String, String> user = session.getUserDetails();
                            String id = user.get(SessionManager.KEY_ID);

                            System.out.println("iiiiiiiiiii     " + id + "      " + old_password + "        " + password);

                            RequestBody id_s = RequestBody.create(MediaType.parse("text/plain"), id);
                            RequestBody OldpasswordS = RequestBody.create(MediaType.parse("text/plain"), old_password);
                            RequestBody New_Passsword = RequestBody.create(MediaType.parse("text/plain"), password);

                            Url.getWebService().changephotographerPassword(id_s, OldpasswordS, New_Passsword).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();

                                    if (response.isSuccessful()) {
                                        try {
                                            // progressDialog.dismiss();
                                            JSONObject responseObject = AppConstant.getResponseObject(response);

                                            System.out.println("response reset    " + response.body().toString());

                                            if (responseObject.optBoolean("IsSuccess")) {

                                                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Reset_Password.this);
                                                myAlertDialog.setTitle("Your Password has been submit successfully reset");
                                                myAlertDialog.setPositiveButton("ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                                ed_current_password.getText().clear();
                                                                ed_password.getText().clear();
                                                                ed_confirm_password.getText().clear();
                                                                onBackPressed();
                                                            }
                                                        });
                                                myAlertDialog.setCancelable(true);
                                                myAlertDialog.show();

                                            } else {
                                                Toast.makeText(Reset_Password.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Toast.makeText(Reset_Password.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // progressDialog.dismiss();
                                    progressDialog.dismiss();
                                    Toast.makeText(Reset_Password.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                                }
                            });
                        }
                    } else {
                        progressDialog.dismiss();
                        ed_confirm_password.setError("Password not match");
                    }
                } else {
                    ed_current_password.setError("Required");
                }
            }
        });
    }
}
