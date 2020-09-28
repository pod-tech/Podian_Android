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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.podphotographer.Pojo.Booking_data;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Notification_P extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ImageView menu;
    ArrayList<Notification_PF> arrayList = new ArrayList<>();
    My_Notification_s adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_p);

        progressDialog = ProgressDialog.show(Notification_P.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        recyclerView = findViewById(R.id.id_Recycleview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        adapter = new My_Notification_s(getApplicationContext(), arrayList);
        recyclerView.setAdapter(adapter);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FloatingActionButton fab_filter = findViewById(R.id.add_video);
        fab_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Notification_P.this);
                myAlertDialog.setTitle("Are you sure do you want to clear all notification?");
                myAlertDialog.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                try {
                                    SessionManager session = new SessionManager(getApplicationContext());
                                    HashMap<String, String> user = session.getUserDetails();
                                    String id = user.get(SessionManager.KEY_ID);

                                    Url.getWebService().clearAllPhotographerNotificationByphotographerId(Integer.parseInt(id)).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            if (response.isSuccessful()) {
                                                getnotification();
                                            } else {
                                                Toast.makeText(Notification_P.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            Toast.makeText(Notification_P.this, R.string.error_pod,
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("error", "Error in Ticket Category : " + t.getMessage());
                                        }
                                    });


                                } catch (Exception e) {

                                }
                            }
                        });
                myAlertDialog.setCancelable(false);
                myAlertDialog.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                            }
                        });
                myAlertDialog.setCancelable(false);
                myAlertDialog.show();
            }
        });

        adapter.setOnItemClickListener(new My_Notification_s.OnRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, String type, String cid, String pid, String gid) {
                if (type.equals("1")) {

                    Intent intent = new Intent(Notification_P.this, S_upcoming.class);
                    intent.putExtra("id", gid);
                    startActivity(intent);

                } else if (type.equals("6")) {

                    Intent intent = new Intent(Notification_P.this, Chat.class);
                    intent.putExtra("Customer_id", cid);
                    intent.putExtra("Photographer_id", pid);
                    intent.putExtra("orderId", gid);
                    startActivity(intent);
                } else if (type.equals("2")) {


                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        adapter.setOnItemClickContinueListener(new My_Notification_s.OnRecyclerViewClickContinueListener() {
            @Override
            public void onItemClickLisner(View view, String id) {
                try {

                    progressDialog.show();

                    Url.getWebService().deleteCustomNotificationPhotographer(Integer.parseInt(id)).enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                JSONObject responseObject = AppConstant.getResponseObject(response);
                                String detail = responseObject.optString("ResponseData");
                                System.out.println(detail);

                                if (responseObject != null) {
                                    progressDialog.dismiss();
                                    getnotification();

                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.error_pod,
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (Exception e) {

                }

            }
        });

        getnotification();
    }

    void refreshItems() {
        // Load items
        getnotification();
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getnotification() {
        try {
            SessionManager session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            String id = user.get(SessionManager.KEY_ID);

            progressDialog.show();

            Url.getWebService().getCustomNotificationPhotographer(Integer.parseInt(id)).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    arrayList.clear();
                    if (response.isSuccessful()) {
                        JSONObject responseObject = AppConstant.getResponseObject(response);

                        if (responseObject != null) {
                            progressDialog.dismiss();
                            if (responseObject.optBoolean("IsSuccess")) {
                                List<Notification_PF> temp_survey = new Gson().fromJson(responseObject
                                        .optString("ResponseData"), new TypeToken<List<Notification_PF>>() {
                                }.getType());

                                arrayList.addAll(temp_survey);
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getApplicationContext(), responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.error_pod,
                            Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {

        }
    }
}

