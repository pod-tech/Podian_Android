package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Contact_us extends AppCompatActivity {

    TextView name, about, address, mail, phone, web;
    Button bt_continue;
    ProgressDialog progressDialog;
    ImageView menu;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        name = findViewById(R.id.tv_name);
        about = findViewById(R.id.tv_about);
        address = findViewById(R.id.tv_address);
        mail = findViewById(R.id.tv_email);
        phone = findViewById(R.id.tv_phone);
        web = findViewById(R.id.tv_web);
        menu = findViewById(R.id.menu);
        bt_continue = findViewById(R.id.bt_continue);

        progressDialog = ProgressDialog.show(Contact_us.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), Application_issue.class);
                v.getContext().startActivity(intent);

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        id = Integer.parseInt(user.get(SessionManager.KEY_ID));
        getbookinglist(id);
    }

    private void getbookinglist(int id) {

        progressDialog.show();
        try {
            Url.getWebService().get247SupportSettings().enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        JSONObject responseObject = AppConstant.getResponseObject(response);
                        if (responseObject != null) {
                            if (responseObject.optBoolean("IsSuccess")) {

                                String detail = responseObject.optString("ResponseData");
                                try {
                                    JSONObject jsonObject = new JSONObject(detail);
                                    String Id = jsonObject.optString("Id");
                                    String About = jsonObject.optString("About");
                                    String Name = jsonObject.optString("Name");
                                    String Address = jsonObject.optString("Address");
                                    String Email = jsonObject.optString("Email");
                                    String Phone = jsonObject.optString("Phone");
                                    String Web = jsonObject.optString("Web");

                                    name.setText(Name);
                                    about.setText(About);
                                    address.setText(Address);
                                    mail.setText(Email);
                                    phone.setText(Phone);
                                    web.setText(Web);

                                } catch (Exception e) {

                                }

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
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.error_pod,
                            Toast.LENGTH_SHORT).show();
                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                }
            });

        }catch (Exception e){
            Toast.makeText(Contact_us.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
        }
    }
}
