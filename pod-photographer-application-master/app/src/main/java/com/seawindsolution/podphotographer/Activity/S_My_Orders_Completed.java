package com.seawindsolution.podphotographer.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.seawindsolution.podphotographer.Pojo.Booking_data;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class S_My_Orders_Completed extends RecyclerView.Adapter<S_My_Orders_Completed.ViewHolder> {

    public Context context;
    private List<Booking_data> arrayList;
    ProgressDialog progressDialog;
    Date endDate;

    public S_My_Orders_Completed(Context context, List<Booking_data> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_orders, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        progressDialog = ProgressDialog.show(context, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        Booking_data booking_data = arrayList.get(position);

        holder.tv_total_hrs.setText(booking_data.getShootingHours());
        holder.tv_shooting_date.setText(booking_data.getShootingDate());
        holder.tv_start_time.setText(booking_data.getShootingStartTime() + " o'clock");


        try {
            if (booking_data.getStatus().toString().equals("6")) {
                if (booking_data.getStatus().equals("1")) {
                    holder.tv_status.setText("Placed");
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.placed));
                } else if (booking_data.getStatus().equals("2")) {
                    holder.tv_status.setText("Confirmed");
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.confirm));
                } else if (booking_data.getStatus().equals("3")) {
                    holder.tv_status.setText("Reschedule");
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.confirm));
                } else if (booking_data.getStatus().equals("4")) {
                    holder.tv_status.setText("Cancel");
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.cancel));
                } else if (booking_data.getStatus().equals("5")) {
                    holder.tv_status.setText("Shooting");
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.confirm));
                } else if (booking_data.getStatus().equals("6")) {
                    holder.tv_status.setText("Completed");
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.confirm));
                }

            } else {
                holder.crd.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

        try {
            holder.crd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), S_completed.class);
                    intent.putExtra("id", booking_data.getId());
                    v.getContext().startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_total_hrs, tv_shooting_date, tv_start_time, tv_status;
        CardView crd;

        ViewHolder(View view) {
            super(view);

            crd = view.findViewById(R.id.cards);
            tv_total_hrs = view.findViewById(R.id.tv_total_hr);
            tv_shooting_date = view.findViewById(R.id.tv_shooting_dates);
            tv_start_time = view.findViewById(R.id.tv_start_times);
            tv_status = view.findViewById(R.id.tv_statu);
        }
    }
}
