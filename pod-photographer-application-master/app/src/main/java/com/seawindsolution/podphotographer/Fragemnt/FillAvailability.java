package com.seawindsolution.podphotographer.Fragemnt;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seawindsolution.podphotographer.Activity.Daily_Availability;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FillAvailability extends Fragment {

    private LinearLayout btnDatePicker;
    private TextView tv_date;
    private Button bt_submit;
    private ProgressDialog progressDialog;
    private CheckBox r_1, r_2, r_3, r_4, r_5, r_6, r_7, r_8, r_9, r_10, r_11, r_12, r_13, r_14, r_15, r_16, r_17, r_18, r_19, r_20, r_21, r_22, r_23, r_24, r_31, r_32, r_33, r_34, r_35, r_36, r_37, r_38, r_39, r_40, r_41, r_42, r_43, r_44, r_45, r_46, r_47, r_48, r_49, r_50, r_51, r_52, r_53, r_54, r_night_all, r_morning_all, r_afternoon_all, r_evening_all;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r31, r32, r33, r34, r35, r36, r37, r38, r39, r40, r41, r42, r43, r44, r45, r46, r47, r48, r49, r50, r51, r52, r53, r54;
    private int id;
    private int num = 1;

    public FillAvailability() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fill_availability, container, false);

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        id = Integer.parseInt(Objects.requireNonNull(user.get(SessionManager.KEY_ID)));

        progressDialog = ProgressDialog.show(getContext(), null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        btnDatePicker = view.findViewById(R.id.card_date);
        tv_date = view.findViewById(R.id.tv_date);

        r_1 = view.findViewById(R.id.r_1);
        r_2 = view.findViewById(R.id.r_2);
        r_3 = view.findViewById(R.id.r_3);
        r_4 = view.findViewById(R.id.r_4);
        r_5 = view.findViewById(R.id.r_5);
        r_6 = view.findViewById(R.id.r_6);
        r_7 = view.findViewById(R.id.r_7);
        r_8 = view.findViewById(R.id.r_8);
        r_9 = view.findViewById(R.id.r_9);
        r_10 = view.findViewById(R.id.r_10);
        r_11 = view.findViewById(R.id.r_11);
        r_12 = view.findViewById(R.id.r_12);
        r_13 = view.findViewById(R.id.r_13);
        r_14 = view.findViewById(R.id.r_14);
        r_15 = view.findViewById(R.id.r_15);
        r_16 = view.findViewById(R.id.r_16);
        r_17 = view.findViewById(R.id.r_17);
        r_18 = view.findViewById(R.id.r_18);
        r_19 = view.findViewById(R.id.r_19);
        r_20 = view.findViewById(R.id.r_20);
        r_21 = view.findViewById(R.id.r_21);
        r_22 = view.findViewById(R.id.r_22);
        r_23 = view.findViewById(R.id.r_23);
        r_24 = view.findViewById(R.id.r_24);

        r_31 = view.findViewById(R.id.r_31);
        r_32 = view.findViewById(R.id.r_32);
        r_33 = view.findViewById(R.id.r_33);
        r_34 = view.findViewById(R.id.r_34);
        r_35 = view.findViewById(R.id.r_35);
        r_36 = view.findViewById(R.id.r_36);
        r_37 = view.findViewById(R.id.r_37);
        r_38 = view.findViewById(R.id.r_38);
        r_39 = view.findViewById(R.id.r_39);
        r_40 = view.findViewById(R.id.r_40);
        r_41 = view.findViewById(R.id.r_41);
        r_42 = view.findViewById(R.id.r_42);
        r_43 = view.findViewById(R.id.r_43);
        r_44 = view.findViewById(R.id.r_44);
        r_45 = view.findViewById(R.id.r_45);
        r_46 = view.findViewById(R.id.r_46);
        r_47 = view.findViewById(R.id.r_47);
        r_48 = view.findViewById(R.id.r_48);
        r_49 = view.findViewById(R.id.r_49);
        r_50 = view.findViewById(R.id.r_50);
        r_51 = view.findViewById(R.id.r_51);
        r_52 = view.findViewById(R.id.r_52);
        r_53 = view.findViewById(R.id.r_53);
        r_54 = view.findViewById(R.id.r_54);

        r_night_all = view.findViewById(R.id.r_night_all);
        r_morning_all = view.findViewById(R.id.r_morning_all);
        r_afternoon_all = view.findViewById(R.id.r_afternoon_all);
        r_evening_all = view.findViewById(R.id.r_evening_all);

        bt_submit = view.findViewById(R.id.bt_submit);

        if (num == 3) {
            r_1.setText("12 - 03 AM");
            r_2.setText("03 - 06 AM");
            r_3.setText("06 - 09 AM");
            r_4.setText("09 - 12 AM");
            r_5.setText("12 - 15 PM");
            r_6.setText("15 - 18 PM");
            r_7.setText("18 - 21 PM");
            r_8.setText("21 - 24 PM");

            r_9.setVisibility(View.GONE);
            r_10.setVisibility(View.GONE);
            r_11.setVisibility(View.GONE);
            r_12.setVisibility(View.GONE);
            r_13.setVisibility(View.GONE);
            r_14.setVisibility(View.GONE);
            r_15.setVisibility(View.GONE);
            r_16.setVisibility(View.GONE);
            r_17.setVisibility(View.GONE);
            r_18.setVisibility(View.GONE);
            r_19.setVisibility(View.GONE);
            r_20.setVisibility(View.GONE);
            r_21.setVisibility(View.GONE);
            r_22.setVisibility(View.GONE);
            r_23.setVisibility(View.GONE);
            r_24.setVisibility(View.GONE);

        } else if (num == 2) {
            r_1.setText("12 - 02 AM");
            r_2.setText("02 - 04 AM");
            r_3.setText("04 - 06 AM");
            r_4.setText("06 - 08 AM");
            r_5.setText("08 - 10 AM");
            r_6.setText("10 - 12 AM");
            r_7.setText("12 - 14 PM");
            r_8.setText("14 - 16 PM");
            r_9.setText("16 - 18 PM");
            r_10.setText("18 - 20 PM");
            r_11.setText("20 - 22 PM");
            r_12.setText("22 - 24 PM");

            r_13.setVisibility(View.GONE);
            r_14.setVisibility(View.GONE);
            r_15.setVisibility(View.GONE);
            r_16.setVisibility(View.GONE);
            r_17.setVisibility(View.GONE);
            r_18.setVisibility(View.GONE);
            r_19.setVisibility(View.GONE);
            r_20.setVisibility(View.GONE);
            r_21.setVisibility(View.GONE);
            r_22.setVisibility(View.GONE);
            r_23.setVisibility(View.GONE);
            r_24.setVisibility(View.GONE);

        } else if (num == 1) {
            r_1.setText("12 - 12:30 AM");
            r_2.setText("12:30 - 01 AM");
            r_3.setText("01 - 01:30 AM");

            r_31.setText("1:30 - 02 AM");
            r_32.setText("02 - 02:30 AM");
            r_33.setText("2:30 - 03 AM");

            r_4.setText("03 - 3:30 AM");
            r_5.setText("3:30 - 04 AM");
            r_6.setText("04 - 4:30 AM");

            r_34.setText("4:30 - 05 AM");
            r_35.setText("05 - 5:30 AM");
            r_36.setText("5:30 - 06 AM");

            r_7.setText("06 - 6:30 AM");
            r_8.setText("6:30 - 07 AM");
            r_9.setText("07 - 7:30 AM");

            r_37.setText("7:30 - 08 AM");
            r_38.setText("08 - 8:30 AM");
            r_39.setText("8:30 - 09 AM");

            r_10.setText("09 - 9:30 AM");
            r_11.setText("9:30 - 10 AM");
            r_12.setText("10 - 10:30 AM");

            r_40.setText("10:30 - 11 AM");
            r_41.setText("11 - 11:30 AM");
            r_42.setText("11:30 - 12 AM");

            r_13.setText("12 - 12:30 PM");
            r_14.setText("12:30 - 13 PM");
            r_15.setText("13 - 13:30 PM");

            r_43.setText("13:30 - 14 PM");
            r_44.setText("14 - 14:30 PM");
            r_45.setText("14:30 - 15 PM");

            r_16.setText("15 - 15:30 PM");
            r_17.setText("15:30 - 16 PM");
            r_18.setText("16 - 16:30 PM");

            r_46.setText("16:30 - 17 PM");
            r_47.setText("17 - 17:30 PM");
            r_48.setText("17:30 - 18 PM");

            r_19.setText("18 - 18:30 PM");
            r_20.setText("18:30 - 19 PM");
            r_21.setText("19 - 19:30 PM");

            r_49.setText("19:30 - 20 PM");
            r_50.setText("20 - 20:30 PM");
            r_51.setText("20:30 - 21 PM");

            r_22.setText("21 - 21:30 PM");
            r_23.setText("21:30 - 22 PM");
            r_24.setText("22 - 22:30 PM");

            r_52.setText("22:30 - 23 PM");
            r_53.setText("23 - 23:30 PM");
            r_54.setText("23:30 - 24 PM");
        }

        r_night_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    r_1.setChecked(true);
                    r_2.setChecked(true);
                    r_3.setChecked(true);
                    r_4.setChecked(true);
                    r_5.setChecked(true);
                    r_6.setChecked(true);

                    r_31.setChecked(true);
                    r_32.setChecked(true);
                    r_33.setChecked(true);
                    r_34.setChecked(true);
                    r_35.setChecked(true);
                    r_36.setChecked(true);

                } else {
                    r_1.setChecked(false);
                    r_2.setChecked(false);
                    r_3.setChecked(false);
                    r_4.setChecked(false);
                    r_5.setChecked(false);
                    r_6.setChecked(false);

                    r_31.setChecked(false);
                    r_32.setChecked(false);
                    r_33.setChecked(false);
                    r_34.setChecked(false);
                    r_35.setChecked(false);
                    r_36.setChecked(false);
                }
            }
        });

        r_morning_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    r_7.setChecked(true);
                    r_8.setChecked(true);
                    r_9.setChecked(true);
                    r_10.setChecked(true);
                    r_11.setChecked(true);
                    r_12.setChecked(true);

                    r_37.setChecked(true);
                    r_38.setChecked(true);
                    r_39.setChecked(true);
                    r_40.setChecked(true);
                    r_41.setChecked(true);
                    r_42.setChecked(true);
                } else {
                    r_7.setChecked(false);
                    r_8.setChecked(false);
                    r_9.setChecked(false);
                    r_10.setChecked(false);
                    r_11.setChecked(false);
                    r_12.setChecked(false);

                    r_37.setChecked(false);
                    r_38.setChecked(false);
                    r_39.setChecked(false);
                    r_40.setChecked(false);
                    r_41.setChecked(false);
                    r_42.setChecked(false);
                }
            }
        });

        r_afternoon_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    r_13.setChecked(true);
                    r_14.setChecked(true);
                    r_15.setChecked(true);
                    r_16.setChecked(true);
                    r_17.setChecked(true);
                    r_18.setChecked(true);

                    r_43.setChecked(true);
                    r_44.setChecked(true);
                    r_45.setChecked(true);
                    r_46.setChecked(true);
                    r_47.setChecked(true);
                    r_48.setChecked(true);
                } else {
                    r_13.setChecked(false);
                    r_14.setChecked(false);
                    r_15.setChecked(false);
                    r_16.setChecked(false);
                    r_17.setChecked(false);
                    r_18.setChecked(false);

                    r_43.setChecked(false);
                    r_44.setChecked(false);
                    r_45.setChecked(false);
                    r_46.setChecked(false);
                    r_47.setChecked(false);
                    r_48.setChecked(false);
                }
            }
        });

        r_evening_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    r_19.setChecked(true);
                    r_20.setChecked(true);
                    r_21.setChecked(true);
                    r_22.setChecked(true);
                    r_23.setChecked(true);
                    r_24.setChecked(true);

                    r_49.setChecked(true);
                    r_50.setChecked(true);
                    r_51.setChecked(true);
                    r_52.setChecked(true);
                    r_53.setChecked(true);
                    r_54.setChecked(true);
                } else {
                    r_19.setChecked(false);
                    r_20.setChecked(false);
                    r_21.setChecked(false);
                    r_22.setChecked(false);
                    r_23.setChecked(false);
                    r_24.setChecked(false);

                    r_49.setChecked(false);
                    r_50.setChecked(false);
                    r_51.setChecked(false);
                    r_52.setChecked(false);
                    r_53.setChecked(false);
                    r_54.setChecked(false);
                }
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (r_1.isChecked()) {
                    r1 = 1;
                } else {
                    r1 = 0;
                }
                if (r_2.isChecked()) {
                    r2 = 1;
                } else {
                    r2 = 0;
                }
                if (r_3.isChecked()) {
                    r3 = 1;
                } else {
                    r3 = 0;
                }
                if (r_4.isChecked()) {
                    r4 = 1;
                } else {
                    r4 = 0;
                }
                if (r_5.isChecked()) {
                    r5 = 1;
                } else {
                    r5 = 0;
                }
                if (r_6.isChecked()) {
                    r6 = 1;
                } else {
                    r6 = 0;
                }
                if (r_7.isChecked()) {
                    r7 = 1;
                } else {
                    r7 = 0;
                }
                if (r_8.isChecked()) {
                    r8 = 1;
                } else {
                    r8 = 0;
                }
                if (r_9.isChecked()) {
                    r9 = 1;
                } else {
                    r9 = 0;
                }
                if (r_10.isChecked()) {
                    r10 = 1;
                } else {
                    r10 = 0;
                }
                if (r_11.isChecked()) {
                    r11 = 1;
                } else {
                    r11 = 0;
                }
                if (r_12.isChecked()) {
                    r12 = 1;
                } else {
                    r12 = 0;
                }
                if (r_13.isChecked()) {
                    r13 = 1;
                } else {
                    r13 = 0;
                }
                if (r_14.isChecked()) {
                    r14 = 1;
                } else {
                    r14 = 0;
                }
                if (r_15.isChecked()) {
                    r15 = 1;
                } else {
                    r15 = 0;
                }
                if (r_16.isChecked()) {
                    r16 = 1;
                } else {
                    r16 = 0;
                }
                if (r_17.isChecked()) {
                    r17 = 1;
                } else {
                    r17 = 0;
                }
                if (r_18.isChecked()) {
                    r18 = 1;
                } else {
                    r18 = 0;
                }
                if (r_19.isChecked()) {
                    r19 = 1;
                } else {
                    r19 = 0;
                }
                if (r_20.isChecked()) {
                    r20 = 1;
                } else {
                    r20 = 0;
                }
                if (r_21.isChecked()) {
                    r21 = 1;
                } else {
                    r21 = 0;
                }
                if (r_22.isChecked()) {
                    r22 = 1;
                } else {
                    r22 = 0;
                }
                if (r_23.isChecked()) {
                    r23 = 1;
                } else {
                    r23 = 0;
                }
                if (r_24.isChecked()) {
                    r24 = 1;
                } else {
                    r24 = 0;
                }

                if (r_31.isChecked()) {
                    r31 = 1;
                } else {
                    r31 = 0;
                }
                if (r_32.isChecked()) {
                    r32 = 1;
                } else {
                    r32 = 0;
                }
                if (r_33.isChecked()) {
                    r33 = 1;
                } else {
                    r33 = 0;
                }
                if (r_34.isChecked()) {
                    r34 = 1;
                } else {
                    r34 = 0;
                }
                if (r_35.isChecked()) {
                    r35 = 1;
                } else {
                    r35 = 0;
                }
                if (r_36.isChecked()) {
                    r36 = 1;
                } else {
                    r36 = 0;
                }
                if (r_37.isChecked()) {
                    r37 = 1;
                } else {
                    r37 = 0;
                }
                if (r_38.isChecked()) {
                    r38 = 1;
                } else {
                    r38 = 0;
                }
                if (r_39.isChecked()) {
                    r39 = 1;
                } else {
                    r39 = 0;
                }
                if (r_40.isChecked()) {
                    r40 = 1;
                } else {
                    r40 = 0;
                }
                if (r_41.isChecked()) {
                    r41 = 1;
                } else {
                    r41 = 0;
                }
                if (r_42.isChecked()) {
                    r42 = 1;
                } else {
                    r42 = 0;
                }
                if (r_43.isChecked()) {
                    r43 = 1;
                } else {
                    r43 = 0;
                }
                if (r_44.isChecked()) {
                    r44 = 1;
                } else {
                    r44 = 0;
                }
                if (r_45.isChecked()) {
                    r45 = 1;
                } else {
                    r45 = 0;
                }
                if (r_46.isChecked()) {
                    r46 = 1;
                } else {
                    r46 = 0;
                }
                if (r_47.isChecked()) {
                    r47 = 1;
                } else {
                    r47 = 0;
                }
                if (r_48.isChecked()) {
                    r48 = 1;
                } else {
                    r48 = 0;
                }
                if (r_49.isChecked()) {
                    r49 = 1;
                } else {
                    r49 = 0;
                }
                if (r_50.isChecked()) {
                    r50 = 1;
                } else {
                    r50 = 0;
                }
                if (r_51.isChecked()) {
                    r51 = 1;
                } else {
                    r51 = 0;
                }
                if (r_52.isChecked()) {
                    r52 = 1;
                } else {
                    r52 = 0;
                }
                if (r_53.isChecked()) {
                    r53 = 1;
                } else {
                    r53 = 0;
                }
                if (r_54.isChecked()) {
                    r54 = 1;
                } else {
                    r54 = 0;
                }

                if (num == 3) {
                    System.out.println(r1 + " " + r2 + " " + r3 + " " + r4 + " " + r5 + " " + r6 + " " + r7 + " " + r8);
                } else if (num == 2) {
                    System.out.println(r1 + " " + r2 + " " + r3 + " " + r4 + " " + r5 + " " + r6 + " " + r7 + " " + r8 + " " + r9 + " " + r10 + " " + r11 + " " + r12);
                } else {
                    System.out.println(tv_date.getText().toString() + "   " + r1 + " " + r2 + " " + r3 + " " + r4 + " " + r5 + " " + r6 + " " + r7 + " " +
                            r8 + " " + r9 + " " + r10 + " " + r11 + " " + r12 + " " + r13 + " " + r14 + " " + r15 + " " + r16 + " " + r17 + " " +
                            r18 + " " + r19 + " " + r20 + " " + r21 + " " + r22 + " " + r23 + " " + r24);

                    RequestBody date_s = RequestBody.create(MediaType.parse("text/plain"), tv_date.getText().toString());

                    if (r1 == 0 && r2 == 0 && r3 == 0 && r4 == 0 && r5 == 0 && r6 == 0 && r7 == 0 &&
                            r8 == 0 && r9 == 0 && r10 == 0 && r11 == 0 && r12 == 0 && r13 == 0 && r14 == 0 && r15 == 0 && r16 == 0 && r17 == 0 &&
                            r18 == 0 && r19 == 0 && r20 == 0 && r21 == 0 && r22 == 0 && r23 == 0 && r24 == 0 &&


                            r31 == 0 && r32 == 0 && r33 == 0 && r34 == 0 && r35 == 0 && r36 == 0 && r37 == 0 &&
                            r38 == 0 && r39 == 0 && r40 == 0 && r41 == 0 && r42 == 0 && r43 == 0 && r44 == 0 && r45 == 0 && r46 == 0
                            && r47 == 0 &&
                            r48 == 0 && r49 == 0 && r50 == 0 && r51 == 0 && r52 == 0 && r53 == 0 && r54 == 0
                    ) {

                        Toast.makeText(getApplicationContext(), "Please select Time", Toast.LENGTH_LONG).show();

                    } else {
                        if (!tv_date.getText().toString().equals("") && !tv_date.getText().toString().equals("Enter date")) {
                            progressDialog.show();
                            Url.getWebService().addAvailibility(id, date_s, r1, r2, r3, r31, r32, r33, r4, r5, r6, r34, r35, r36,
                                    r7, r8, r9, r37, r38, r39, r10, r11, r12, r40, r41, r42, r13, r14, r15, r43, r44, r45, r16, r17,
                                    r18, r46, r47, r48, r19, r20, r21, r49, r50, r51, r22, r23, r24, r52, r53, r54).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    progressDialog.dismiss();
                                    if (response.isSuccessful()) {
                                        try {
                                            JSONObject responseObject = AppConstant.getResponseObject(response);
                                            System.out.println(responseObject.toString());

                                            if (responseObject.optBoolean("IsSuccess")) {
                                                //Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                                                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getContext());
                                                myAlertDialog.setTitle("Your Availability has been submit successfully");
                                                myAlertDialog.setPositiveButton("ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                                getActivity().onBackPressed();
                                                            }
                                                        });
                                                myAlertDialog.setCancelable(false);
                                                myAlertDialog.show();

                                            } else {
                                                Toast.makeText(getContext(), responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Toast.makeText(getContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), R.string.error_pod,
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                                }
                            });
                        } else {
                            tv_date.setError("Required");
                        }
                    }

                }
            }
        });

        return view;
    }
}
