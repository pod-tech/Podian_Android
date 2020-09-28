package com.seawindsolution.podphotographer.Fragemnt;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.podphotographer.Activity.My_Orders;
import com.seawindsolution.podphotographer.Pojo.Availability;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GetAvailability extends Fragment {

    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<Availability> arrayList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int id;

    public GetAvailability() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_availability, container, false);

        recyclerView = view.findViewById(R.id.id_Recycleview);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
         id = Integer.parseInt(Objects.requireNonNull(user.get(SessionManager.KEY_ID)));

        progressDialog = ProgressDialog.show(getContext(), null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);

        getavailability(id);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        return view;
    }

    void refreshItems() {
        // Load items
        getavailability(id);
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getavailability(int id) {

        progressDialog.show();

        Url.getWebService().getAvailibility(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                arrayList.clear();
                if (response.isSuccessful()) {
                    JSONObject responseObject = AppConstant.getResponseObject(response);
                    if (responseObject != null) {
                        if (responseObject.optBoolean("IsSuccess")) {
                            List<Availability> temp_survey = new Gson().fromJson(responseObject
                                    .optString("ResponseData"), new TypeToken<List<Availability>>() {
                            }.getType());

                            System.out.println(id + "     " + responseObject.toString());
                            arrayList.addAll(temp_survey);
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(llm);
                            My_Availability adapter = new My_Availability(getApplicationContext(), arrayList);
                            recyclerView.setAdapter(adapter);
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

}
