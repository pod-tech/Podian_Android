package com.seawindsolution.podphotographer.Fragemnt;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.podphotographer.Activity.Freelancer_forth;
import com.seawindsolution.podphotographer.Activity.Freelancer_third;
import com.seawindsolution.podphotographer.Activity.My_Orders;
import com.seawindsolution.podphotographer.Activity.Order_Activity;
import com.seawindsolution.podphotographer.Activity.S_My_Orders;
import com.seawindsolution.podphotographer.Activity.Upcoming_orders;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Upcoming_order extends Fragment {

    CardView today, tomorrow, week, upcoming;

    public Upcoming_order() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_order, container, false);

        today = view.findViewById(R.id.crd_today);
        tomorrow = view.findViewById(R.id.crd_tomorrow);
        week = view.findViewById(R.id.crd_week);
        upcoming = view.findViewById(R.id.crd_upcoming);

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Upcoming_orders.class);
                intent.putExtra("val", "1");
                startActivity(intent);
            }
        });

        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Upcoming_orders.class);
                intent.putExtra("val", "2");
                startActivity(intent);
            }
        });

        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Upcoming_orders.class);
                intent.putExtra("val", "3");
                startActivity(intent);
            }
        });

        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Upcoming_orders.class);
                intent.putExtra("val", "4");
                startActivity(intent);
            }
        });

        return view;
    }
}
