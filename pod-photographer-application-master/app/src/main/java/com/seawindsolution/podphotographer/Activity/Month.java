package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

public class Month extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    //ArrayList<Dashboard_yojna_feedback> arrayList = new ArrayList<>();
    ArrayList<Booking_data> arrayList = new ArrayList<>();
    Button bt_view_receipt, bt_get_help;
    String id;
    SwipeRefreshLayout mSwipeRefreshLayout;
    S_My_Orders adapter;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        recyclerView = findViewById(R.id.id_Recycleview);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        progressDialog = ProgressDialog.show(Month.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        SessionManager session = new SessionManager(Month.this);
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.KEY_ID);

        getbookinglist(Integer.parseInt(id));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(Month.this);
        recyclerView.setLayoutManager(llm);
        adapter = new S_My_Orders(Month.this, arrayList);
        adapter.notifyDataSetChanged();
    }

    void refreshItems() {
        // Load items
        getbookinglist(Integer.parseInt(id));
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getbookinglist(int id) {

        arrayList.clear();
        progressDialog.show();

        Url.getWebService().getOrderByPhotohrapherId(id).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.isSuccessful()) {
                    JSONObject responseObject = AppConstant.getResponseObject(response);
                    if (responseObject != null) {
                        if (responseObject.optBoolean("IsSuccess")) {
                            List<Booking_data> temp_survey = new Gson().fromJson(responseObject
                                    .optString("ResponseData"), new TypeToken<List<Booking_data>>() {
                            }.getType());

                            arrayList.addAll(temp_survey);

                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(Month.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Month.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Month.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(Month.this, R.string.error_pod,
                        Toast.LENGTH_SHORT).show();
                Log.d("error", "Error in Ticket Category : " + t.getMessage());
            }
        });
    }
}
