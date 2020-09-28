package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class About_us extends AppCompatActivity {

    TextView title, description;
    ProgressDialog progressDialog;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        menu = findViewById(R.id.menu);

        progressDialog = ProgressDialog.show(About_us.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String slug = ("about-us");

        System.out.println(slug);
        Url.getWebService().getPage(slug).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        progressDialog.dismiss();
                        JSONObject responseObject = AppConstant.getResponseObject(response);

                        if (responseObject.optBoolean("IsSuccess")) {
                            String detail = responseObject.optString("ResponseData");
                            try {
                                JSONObject jsonObject = new JSONObject(detail);
                                String Title = String.valueOf(jsonObject.optString("Title"));
                                String Content = String.valueOf(jsonObject.optString("Content"));

                                title.setText(Title);
                                description.setText(HtmlCompat.fromHtml(Content, 0));

                            } catch (Exception e) {

                            }
                        } else {
                            Toast.makeText(About_us.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(About_us.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(About_us.this, R.string.error_pod,
                        Toast.LENGTH_SHORT).show();
                Log.d("", "Error in Ticket Category : " + t.getMessage());
            }
        });
    }
}
