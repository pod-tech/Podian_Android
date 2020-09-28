package com.seawindsolution.podphotographer.Fragemnt;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.podphotographer.Activity.My_Orders;
import com.seawindsolution.podphotographer.Activity.My_Orders_Completed;
import com.seawindsolution.podphotographer.Activity.S_My_Orders_Completed;
import com.seawindsolution.podphotographer.Pojo.Booking_data;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Completed_orders extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    //ArrayList<Dashboard_yojna_feedback> arrayList = new ArrayList<>();
    ArrayList<Booking_data> arrayList = new ArrayList<>();
    Button bt_export, bt_get_help;
    String id;
    SwipeRefreshLayout mSwipeRefreshLayout;
    S_My_Orders_Completed adapter;
    ImageView menu;
    int mFromYear, mFromMonth, mFromDay;
    int mToYear, mToMonth, mToDay;

    public Completed_orders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);

        recyclerView = view.findViewById(R.id.id_Recycleview);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        bt_export = view.findViewById(R.id.bt_export);

        progressDialog = ProgressDialog.show(getContext(), null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        SessionManager session = new SessionManager(getApplicationContext());
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
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        adapter = new S_My_Orders_Completed(getContext(), arrayList);
        adapter.notifyDataSetChanged();

        bt_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenExportDialog();
            }
        });
        return view;
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

    public void OpenExportDialog() {

        Dialog introDialog = new Dialog(getContext(), R.style.TransparentDialogTheme);
        introDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        introDialog.setContentView(R.layout.dialog_layout_export_orders);
        introDialog.setCancelable(false);

        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout_export_orders, null);
        introDialog.getWindow().setContentView(dialogView);

        AppCompatImageView ivClose = (AppCompatImageView) dialogView.findViewById(R.id.ivClose);
        AppCompatEditText edSelectFromDate = (AppCompatEditText) dialogView.findViewById(R.id.edSelectFromDate);
        AppCompatEditText edSelectToDate = (AppCompatEditText) dialogView.findViewById(R.id.edSelectToDate);
        Button bt_export = (Button) dialogView.findViewById(R.id.bt_export);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introDialog.dismiss();
            }
        });

        edSelectFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                            edSelectFromDate.setText(getStringDate(year, monthOfYear, dayOfMonth));

                            mFromYear = year;
                            mFromMonth =monthOfYear;
                            mFromMonth = dayOfMonth;
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        edSelectToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                            edSelectToDate.setText(getStringDate(year, monthOfYear, dayOfMonth));
                            mToYear = year;
                            mToMonth = monthOfYear ;
                            mToMonth = dayOfMonth;
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        bt_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFromYear == 0 && mFromMonth == 0 && mFromDay == 0) {
                    Toast.makeText(getContext(), "Please select from date", Toast.LENGTH_LONG).show();
                } else if (mToYear == 0 && mToMonth == 0 && mToDay == 0) {
                    Toast.makeText(getContext(), "Please select to date", Toast.LENGTH_LONG).show();
                } else if (!isCompare(edSelectFromDate.getText().toString().trim(), edSelectToDate.getText().toString().trim())) {
                    Toast.makeText(getContext(), "From date not greater than To date", Toast.LENGTH_LONG).show();
                } else {
                    exportOrders(edSelectFromDate.getText().toString().trim(), edSelectToDate.getText().toString().trim());
                    introDialog.dismiss();
                }
            }
        });
        introDialog.show();

    }

    @SuppressLint("SimpleDateFormat")
    public String getStringDate(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
    }

    public boolean isCompare(String str1, String str2) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = formatter.parse(str1);
            Date date2 = formatter.parse(str2);
            if (date1 != null) {
                if (date1.compareTo(date2) < 0 || date1.compareTo(date2) == 0) {
                    System.out.println("date2 is Greater than my date1");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return false;
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

    private void exportOrders(String fromDate, String toDate) {

        progressDialog.show();
        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(SessionManager.KEY_ID);
        int userId = Integer.parseInt(id);
        Url.getWebService().exportOrders(fromDate, toDate, userId).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.isSuccessful()) {
                    JSONObject responseObject = AppConstant.getResponseObject(response);
                    if (responseObject != null) {
                        if (responseObject.optBoolean("IsSuccess")) {

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
