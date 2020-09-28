package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.podphotographer.Activity.My_Orders;
import com.seawindsolution.podphotographer.Fragemnt.Completed_orders;
import com.seawindsolution.podphotographer.Fragemnt.FillAvailability;
import com.seawindsolution.podphotographer.Fragemnt.GetAvailability;
import com.seawindsolution.podphotographer.Fragemnt.Upcoming_order;
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

public class Order_Activity extends AppCompatActivity {

    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        Order_Activity.ViewPagerAdapter adapter = new Order_Activity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Upcoming_order(), "Orders");
        adapter.addFragment(new Completed_orders(), "Completed Orders");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
