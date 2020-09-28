package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.podphotographer.Pojo.Booking_data;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class S_upcoming extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    //ArrayList<Dashboard_yojna_feedback> arrayList = new ArrayList<>();
    ArrayList<Booking_data> arrayList = new ArrayList<>();
    Button bt_view_receipt, bt_get_help;
    String id, id_s;
    SwipeRefreshLayout mSwipeRefreshLayout;
    My_Orders adapter;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_upcoming);

        recyclerView = findViewById(R.id.id_Recycleview);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        progressDialog = ProgressDialog.show(S_upcoming.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            id = (String.valueOf(0));
        } else {
            id = (extras.getString("id"));
        }

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        id_s = user.get(SessionManager.KEY_ID);

        getbookinglist(Integer.parseInt(id_s), Integer.parseInt(id));
        System.out.println("aa      " + id_s + "      " + id);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        adapter = new My_Orders(S_upcoming.this, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new My_Orders.OnRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view) {

                /*LayoutInflater factory = LayoutInflater.from(S_upcoming.this);
                View views = factory.inflate(R.layout.extend_ui, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(views);

                TextView tv_val = (TextView) views.findViewById(R.id.msg);

                Button approve = (Button) views.findViewById(R.id.bt_request);
                Button decline = (Button) views.findViewById(R.id.bt_close);
                // create alert dialog

                if (booking_data.getExtAvlFlg().equals("Yes")) {
                    tv_val.setText("Would you like to continue your or extend your session?");
                } else if (booking_data.getExtAvlFlg().equals("No")) {
                    tv_val.setText("Sorry, You can't accept this session because you can't available.");
                    approve.setVisibility(View.GONE);
                }

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setView(views);




                // show it
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                getbookinglist(Integer.parseInt(id_s), Integer.parseInt(id));*/

            }
        });
    }

    void refreshItems() {
        // Load items
        getbookinglist(Integer.parseInt(id_s), Integer.parseInt(id));
        // Load complete
        onItemsLoadComplete();
    }

    private void getbookinglist(int id_s, int id_ss) {

        arrayList.clear();
        progressDialog.show();

        Url.getWebService().getPhotographerOrderByOrderId(id_s, id_ss).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.isSuccessful()) {

                    JSONObject responseObject = AppConstant.getResponseObject(response);
                    if (responseObject != null) {
                        if (responseObject.optBoolean("IsSuccess")) {
                            try {
                                List<Booking_data> temp_survey = new Gson().fromJson(responseObject
                                        .optString("ResponseData"), new TypeToken<List<Booking_data>>() {
                                }.getType());

                                arrayList.addAll(temp_survey);
                                adapter.notifyDataSetChanged();

                                try {
                                    String detail = responseObject.optString("ResponseData");

                                    JSONArray arr = new JSONArray(detail);
                                    JSONObject jObj = arr.getJSONObject(0);
                                    String CheckFlg = jObj.getString("CheckFlg");
                                    String DisplayCheckList = jObj.getString("DisplayCheckList");
                                    System.out.println(CheckFlg);

                                    if (CheckFlg.equals("No") && DisplayCheckList.equals("Yes")) {
                                        LayoutInflater factory = LayoutInflater.from(S_upcoming.this);
                                        View views = factory.inflate(R.layout.check_list, null);

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(S_upcoming.this);

                                        // set prompts.xml to alertdialog builder
                                        alertDialogBuilder.setView(views);

                                        CheckBox ch_1 = (CheckBox) views.findViewById(R.id.ch_1);
                                        CheckBox ch_2 = (CheckBox) views.findViewById(R.id.ch_2);
                                        CheckBox ch_3 = (CheckBox) views.findViewById(R.id.ch_3);
                                        CheckBox ch_4 = (CheckBox) views.findViewById(R.id.ch_4);
                                        CheckBox ch_5 = (CheckBox) views.findViewById(R.id.ch_5);
                                        CheckBox ch_6 = (CheckBox) views.findViewById(R.id.ch_6);
                                        CheckBox ch_7 = (CheckBox) views.findViewById(R.id.ch_7);
                                        CheckBox ch_8 = (CheckBox) views.findViewById(R.id.ch_8);
                                        CheckBox ch_9 = (CheckBox) views.findViewById(R.id.ch_9);
                                        CheckBox ch_10 = (CheckBox) views.findViewById(R.id.ch_10);
                                        Button bt_close = (Button) views.findViewById(R.id.bt_submit);

                                        // create alert dialog
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.setView(views);

                                        bt_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                if (ch_1.isChecked() && ch_2.isChecked() && ch_3.isChecked() && ch_4.isChecked() && ch_5.isChecked() && ch_6.isChecked() && ch_7.isChecked() && ch_8.isChecked()
                                                        && ch_9.isChecked() && ch_10.isChecked()) {

                                                    RequestBody Checklist = RequestBody.create(MediaType.parse("text/plain"), "Yes");

                                                    try {
                                                        progressDialog.show();
                                                        Url.getWebService().checklist(id_s, id_ss, Checklist).enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                System.out.println(response.body().toString());
                                                                if (response.isSuccessful()) {
                                                                    try {
                                                                        alertDialog.dismiss();
                                                                        progressDialog.dismiss();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                Toast.makeText(getApplicationContext(), R.string.error_pod,
                                                                        Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                Log.d("", "Error in Ticket Category : " + t.getMessage());
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        Toast.makeText(S_upcoming.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(S_upcoming.this, "Please select all", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        alertDialog.setCancelable(false);
                                        // show it
                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        alertDialog.show();
                                    } else {

                                    }
                                } catch (Exception e) {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
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
                Log.d("error", "Error in Ticket Category : " + t.getMessage());
            }
        });
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
