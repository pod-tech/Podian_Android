package com.seawindsolution.podphotographer.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.io.File;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Freelancer_final extends AppCompatActivity {

    EditText ed_why_question;
    CheckBox ch_check;
    RadioButton rd_yes, rd_no;
    Button bt_submit;
    RadioGroup rd_radio_6;
    String why_q;
    Spinner referral;
    ProgressDialog progressDialog;
    MultipartBody.Part Profile_s_s, image_s_s;
    ImageView menu;
    Spinner referrals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_final);

        progressDialog = ProgressDialog.show(Freelancer_final.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        ed_why_question = findViewById(R.id.ed_why_question);

        rd_yes = findViewById(R.id.rd_yes_1);
        rd_no = findViewById(R.id.rd_no_1);

        rd_radio_6 = findViewById(R.id.rd_radio_6);

        ch_check = findViewById(R.id.ch_done);
        bt_submit = findViewById(R.id.bt_submit);

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        referrals = findViewById(R.id.sp_spinner_referrals);
        String[] itemes = new String[]{"STUDIO", "FREELANCER", "ALL OTHER"};
        ArrayAdapter<String> adapteres = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemes);
        referrals.setAdapter(adapteres);

        //get the spinner from the xml.
        referral = findViewById(R.id.sp_spinner_referral);
        String[] items = new String[]{"Facebook", "Instagram", "Friends", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        referral.setAdapter(adapter);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                why_q = ed_why_question.getText().toString();

                if (!why_q.equals("")) {

                    if (ch_check.isChecked()) {
                        progressDialog.show();

                        int selectedId6 = rd_radio_6.getCheckedRadioButtonId();
                        RadioButton radioSexButton6 = (RadioButton) findViewById(selectedId6);

                        String gender6 = String.valueOf(radioSexButton6.getText());
                        String referral_s = (referral.getSelectedItem().toString());
                        String referral_es = (referrals.getSelectedItem().toString());

                        System.out.println("Screen 1  " + Freelancer.profile + "        " + Freelancer.name + "       " + Freelancer.email + "       " +
                                Freelancer.phone + "       " + Freelancer.gender + "   Screen 2    " + Freelancer_second.employee + "       "
                                + Freelancer_second.landmark + "       " + Freelancer_second.city + "       " + Freelancer_second.state + "       " +
                                Freelancer_second.aadhar + "       " + Freelancer_second.pin + "       " + Freelancer_second.path_1
                                + "   Screen 3    " + Freelancer_third.experience + "       " + Freelancer_third.s + "    Screen 4   " + Freelancer_forth.insta_url
                                + "       " + Freelancer_forth.portfolio_url + "       " + Freelancer_forth.cam_body + "       " + Freelancer_forth.cam_lenses_1
                                + "       " + Freelancer_forth.cam_lenses_2 + "       " + Freelancer_forth.cam_lenses_3 + "   Screen 5    " + why_q + "       " + gender6
                                + "      " + " " + referral_s + " " + referral_es);

                        File idProofFile = new File(Freelancer.profile);
                        String fileType = URLConnection.guessContentTypeFromName(idProofFile.getName());
                        String fileName = idProofFile.getName().replaceAll(" ", "_");
                        RequestBody fileBody = RequestBody.create(MediaType.parse(fileType), idProofFile);
                        Profile_s_s = MultipartBody.Part.createFormData("ProfileImage", fileName, fileBody);

                        File idProofFiles = new File(Freelancer_second.path_1);
                        String fileTypes = URLConnection.guessContentTypeFromName(idProofFiles.getName());
                        String fileNames = idProofFiles.getName().replaceAll(" ", "_");
                        RequestBody fileBodys = RequestBody.create(MediaType.parse(fileTypes), idProofFiles);
                        image_s_s = MultipartBody.Part.createFormData("IdProof", fileNames, fileBodys);

                        RequestBody name_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer.name);
                        RequestBody email_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer.email);
                        RequestBody phone_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer.phone);
                        RequestBody gender_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer.gender);
                        RequestBody date_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer.date);
                        RequestBody employee_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_second.employee);
                        RequestBody area_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_second.landmark);
                        RequestBody city_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_second.city);
                        RequestBody state_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_second.state);
                        RequestBody pin_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_second.pin);
                        RequestBody aadhar_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_second.aadhar);
                        RequestBody experience_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_third.experience);
                        RequestBody checks_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_third.s);
                        RequestBody insta_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_forth.insta_url);
                        RequestBody portfolio_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_forth.portfolio_url);
                        RequestBody cam_body_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_forth.cam_body);
                        RequestBody cam_lenses_1_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_forth.cam_lenses_1);
                        RequestBody cam_lenses_2_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_forth.cam_lenses_2);
                        RequestBody cam_lenses_3_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_forth.cam_lenses_3);
                        RequestBody availability_s = RequestBody.create(MediaType.parse("text/plain"), Freelancer_forth.availability);
                        RequestBody why_question_s = RequestBody.create(MediaType.parse("text/plain"), why_q);
                        RequestBody yes_no_s = RequestBody.create(MediaType.parse("text/plain"), gender6);
                        RequestBody referral_S = RequestBody.create(MediaType.parse("text/plain"), referral_s);
                        RequestBody referral_eS = RequestBody.create(MediaType.parse("text/plain"), referral_es);
                        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), "INDIA");

                        Url.getWebService().joinFreelancer(name_s, email_s, phone_s, gender_s, date_s, employee_s, area_s, city_s, state_s, pin_s,
                                country, aadhar_s, experience_s, checks_s, insta_s, portfolio_s, cam_body_s, cam_lenses_1_s,
                                cam_lenses_2_s, cam_lenses_3_s, availability_s,
                                yes_no_s, why_question_s, referral_S, referral_eS, Profile_s_s, image_s_s).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.isSuccessful()) {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject responseObject = AppConstant.getResponseObject(response);
                                        System.out.println(responseObject.toString());

                                        if (responseObject.optBoolean("IsSuccess")) {

                                            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Freelancer_final.this);
                                            myAlertDialog.setMessage("Your request has been successfully submitted");
                                            myAlertDialog.setPositiveButton("ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface arg0, int arg1) {
                                                            Toast.makeText(Freelancer_final.this, "Success", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(Freelancer_final.this, Login.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        }
                                                    });
                                            myAlertDialog.setCancelable(false);
                                            myAlertDialog.show();

                                        } else {
                                            Toast.makeText(Freelancer_final.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(Freelancer_final.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                progressDialog.dismiss();
                                Toast.makeText(Freelancer_final.this, R.string.error_pod,
                                        Toast.LENGTH_SHORT).show();
                                Log.d("", "Error in Ticket Category : " + t.getMessage());
                            }
                        });

                    } else {
                        Toast.makeText(Freelancer_final.this, "Please accept terms and condition", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    ed_why_question.setError("Required");
                }
            }
        });
    }
}

