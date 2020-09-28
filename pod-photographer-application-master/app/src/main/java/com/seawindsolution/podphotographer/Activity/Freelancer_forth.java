package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.io.File;
import java.net.URLConnection;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Freelancer_forth extends AppCompatActivity {

    EditText ed_instagram_url, ed_portfolio_url, ed_cam_body, ed_cam_lenses_1, ed_cam_lenses_2, ed_cam_lenses_3;
    Button bt_submit;
    RadioGroup rd_radio_5;
    public static String availability, insta_url, portfolio_url, cam_body, cam_lenses_1, cam_lenses_2, cam_lenses_3;
    ProgressDialog progressDialog;
    ImageView menu;
    public static Freelancer_forth freelancer_forth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_forth);

        freelancer_forth = this;

        progressDialog = ProgressDialog.show(Freelancer_forth.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        ed_instagram_url = findViewById(R.id.ed_insta_url);
        ed_portfolio_url = findViewById(R.id.ed_portfolio_url);
        ed_cam_body = findViewById(R.id.ed_cam_body);
        ed_cam_lenses_1 = findViewById(R.id.ed_cam_lenses_1);
        ed_cam_lenses_2 = findViewById(R.id.ed_cam_lenses_2);
        ed_cam_lenses_3 = findViewById(R.id.ed_cam_lenses_3);

        rd_radio_5 = findViewById(R.id.rd_radio_5);

        bt_submit = findViewById(R.id.bt_continue_4);

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

                insta_url = ed_instagram_url.getText().toString();
                portfolio_url = ed_portfolio_url.getText().toString();
                cam_body = ed_cam_body.getText().toString();
                cam_lenses_1 = ed_cam_lenses_1.getText().toString();
                cam_lenses_2 = ed_cam_lenses_2.getText().toString();
                cam_lenses_3 = ed_cam_lenses_3.getText().toString();

                if (!insta_url.equals("")&& Patterns.WEB_URL.matcher(insta_url).matches()) {

                    if (!portfolio_url.equals("")&& Patterns.WEB_URL.matcher(portfolio_url).matches()) {

                        if (!cam_body.equals("")) {

                            if (!cam_lenses_1.equals("")) {

                                if (!cam_lenses_2.equals("")) {

                                    if (!cam_lenses_3.equals("")) {

                                        int selectedId5 = rd_radio_5.getCheckedRadioButtonId();
                                        RadioButton radioSexButton5 = (RadioButton) findViewById(selectedId5);

                                        availability = String.valueOf(radioSexButton5.getText());

                                        if (!availability.equals("")) {
                                            Intent intent = new Intent(Freelancer_forth.this, Freelancer_final.class);
                                            startActivity(intent);
                                        } else {

                                        }

                                    } else {
                                        ed_cam_lenses_3.setError("Required");
                                    }

                                } else {
                                    ed_cam_lenses_2.setError("Required");
                                }


                            } else {
                                ed_cam_lenses_1.setError("Required");
                            }

                        } else {
                            ed_cam_body.setError("Required");
                        }

                    } else {
                        ed_portfolio_url.setError("Required");
                    }
                } else {
                    ed_instagram_url.setError("Required");
                }
            }
        });
    }
}
