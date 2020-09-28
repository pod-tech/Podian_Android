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

public class Freelancer_third extends AppCompatActivity {

    CheckBox ch_1, ch_2, ch_3, ch_4, ch_5, ch_6, ch_7, ch_8, ch_9, ch_10;
    RadioButton rd_1, rd_2, rd_3, rd_4;
    Button bt_submit;
    RadioGroup rd_radio_4;
    TextView error1;
    public static String s = "", experience;
    ProgressDialog progressDialog;
    ImageView menu;
    public static Freelancer_third freelancerThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_third);

        freelancerThird = this;

        progressDialog = ProgressDialog.show(Freelancer_third.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        rd_1 = findViewById(R.id.rd_1);
        rd_2 = findViewById(R.id.rd_2);
        rd_3 = findViewById(R.id.rd_3);
        rd_4 = findViewById(R.id.rd_4);

        ch_1 = findViewById(R.id.ch_1);
        ch_2 = findViewById(R.id.ch_2);
        ch_3 = findViewById(R.id.ch_3);
        ch_4 = findViewById(R.id.ch_4);
        ch_5 = findViewById(R.id.ch_5);
        ch_6 = findViewById(R.id.ch_6);
        ch_7 = findViewById(R.id.ch_7);
        ch_8 = findViewById(R.id.ch_8);
        ch_9 = findViewById(R.id.ch_9);
        ch_10 = findViewById(R.id.ch_10);

        error1 = findViewById(R.id.tv_error1);

        rd_radio_4 = findViewById(R.id.rd_radio_4);

        bt_submit = findViewById(R.id.bt_continue_3);

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

                if (rd_1.isChecked() || rd_2.isChecked() || rd_3.isChecked() || rd_4.isChecked()) {

                    if (ch_1.isChecked() || ch_2.isChecked() || ch_3.isChecked() || ch_4.isChecked() || ch_5.isChecked() || ch_6.isChecked() || ch_7.isChecked() || ch_8.isChecked() || ch_9.isChecked() || ch_10.isChecked()) {

                        error1.setVisibility(View.GONE);

                        if (ch_1.isChecked()) {
                            s = s + "," + ch_1.getText().toString();
                        }
                        if (ch_2.isChecked()) {
                            s = s + "," + ch_2.getText().toString();
                        }
                        if (ch_3.isChecked()) {
                            s = s + "," + ch_3.getText().toString();
                        }
                        if (ch_4.isChecked()) {
                            s = s + "," + ch_4.getText().toString();
                        }
                        if (ch_5.isChecked()) {
                            s = s + "," + ch_5.getText().toString();
                        }
                        if (ch_6.isChecked()) {
                            s = s + "," + ch_6.getText().toString();
                        }
                        if (ch_7.isChecked()) {
                            s = s + "," + ch_7.getText().toString();
                        }
                        if (ch_8.isChecked()) {
                            s = s + "," + ch_8.getText().toString();
                        }
                        if (ch_9.isChecked()) {
                            s = s + "," + ch_9.getText().toString();
                        }
                        if (ch_10.isChecked()) {
                            s = s + "," + ch_10.getText().toString();
                        }


                        int selectedId4 = rd_radio_4.getCheckedRadioButtonId();
                        RadioButton radioSexButton4 = (RadioButton) findViewById(selectedId4);
                        //@Name,@Email,@Phone,@TypeOfShoot,@Gender,@DateOfBirth,@BlockNo,@Street1,@Street2,@Pin,@Area,@City,@State,@Country,@Employment,@Designation,@Pan,@Aadhar,@InstagramURL,@Experience,@ElectiveSubject,@ElectivePractice,@CameraLences,@Availibility,@ReadyToTrawelPodProject,@aboutPOD,@Lat,@Lng,@ProfileImage,@IdProof,@Images[]

                        experience = String.valueOf(radioSexButton4.getText());

                        if (!experience.equals("")) {
                            Intent intent = new Intent(Freelancer_third.this, Freelancer_forth.class);
                            startActivity(intent);
                        } else {

                        }

                    } else {
                        error1.setVisibility(View.VISIBLE);
                    }

                } else {

                }
            }
        });
    }
}
