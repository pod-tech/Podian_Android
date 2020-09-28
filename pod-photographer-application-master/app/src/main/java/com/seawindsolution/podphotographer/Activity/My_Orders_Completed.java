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

public class My_Orders_Completed extends RecyclerView.Adapter<My_Orders_Completed.ViewHolder> {

    public Context context;
    private List<Booking_data> arrayList;
    ProgressDialog progressDialog;
    Date endDate;

    private OnRecyclerViewClickListener mOnClickListener = null;

    public interface OnRecyclerViewClickListener {
        void onItemClick(View view);
    }

    public void setOnItemClickListener(OnRecyclerViewClickListener listener) {
        this.mOnClickListener = listener;
    }

    public My_Orders_Completed(Context context, List<Booking_data> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders, parent, false);
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

        holder.tv_order_id.setText(booking_data.getOrderNo());
        holder.tv_total_hrs.setText(booking_data.getShootingHours());
        holder.tv_shooting_date.setText(booking_data.getShootingDate());
        holder.tv_start_time.setText(booking_data.getShootingStartTime() + " o'clock");
        holder.tv_end_time.setText(booking_data.getShootingeEndTime() + " o'clock");
        holder.tv_type.setText(booking_data.getProductTitle());
        holder.tv_address.setText(booking_data.getShootingAddress());
        holder.tv_meeting_point.setText((booking_data.getShootingmeetpoint() != null)  ? booking_data.getShootingmeetpoint() : "");

        String address = String.valueOf(booking_data.getShootingAddress());
        String Lat = String.valueOf(booking_data.getShootingLat());
        String Lng = String.valueOf(booking_data.getShootingLng());

        holder.bt_pod_care_2.setVisibility(View.GONE);

        if (booking_data.getDisplayFlg().equals("No")) {
            holder.head_linear_1.setVisibility(View.GONE);
            holder.head_linear_2.setVisibility(View.GONE);
            holder.head_linear_mp.setVisibility(View.GONE);
            holder.bt_direction.setVisibility(View.GONE);
            holder.tv_chat.setVisibility(View.GONE);
            holder.bt_submit_otp.setVisibility(View.GONE);
            holder.bt_pod_care.setVisibility(View.GONE);

            holder.view1.setVisibility(View.GONE);
            holder.view2.setVisibility(View.GONE);
        } else {
            holder.tv_chat.setVisibility(View.VISIBLE);
            holder.head_linear_1.setVisibility(View.VISIBLE);
            holder.head_linear_2.setVisibility(View.VISIBLE);
            holder.head_linear_mp.setVisibility(View.VISIBLE);
            holder.bt_direction.setVisibility(View.VISIBLE);

            holder.bt_submit_otp.setVisibility(View.VISIBLE);
            holder.bt_pod_care.setVisibility(View.VISIBLE);

            holder.view1.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.GONE);
        }

        holder.bt_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Direction.class);
                intent.putExtra("address", address);
                intent.putExtra("Lat", Lat);
                intent.putExtra("Lng", Lng);
                v.getContext().startActivity(intent);
            }
        });

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse(currentTime);
            System.out.println(booking_data.getExtStatus());
            if (booking_data.getExtStatus().equals("4")) {
                endDate = simpleDateFormat.parse(booking_data.getExtEndTime());
            } else {
                endDate = simpleDateFormat.parse(booking_data.getShootingeEndTime());
            }

            long difference = endDate.getTime() - startDate.getTime();
            if (difference < 0) {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
            } else if (difference == 0) {
                RequestBody Booking_id = RequestBody.create(MediaType.parse("text/plain"), booking_data.getId());
                Url.getWebService().complateOrderByOrderId(Booking_id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject responseObject = AppConstant.getResponseObject(response);
                                System.out.println(responseObject.toString());

                                if (responseObject.optBoolean("IsSuccess")) {
                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(context, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(context, R.string.error_pod, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, R.string.error_pod,
                                Toast.LENGTH_SHORT).show();
                        Log.d("", "Error in Ticket Category : " + t.getMessage());
                    }
                });
            }
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

            String time = hours + ":" + min;
            String[] split = time.split(":");
            if (split.length == 2) {
                long minutes = TimeUnit.HOURS.toMinutes(Integer.parseInt(split[0])) +
                        Integer.parseInt(split[1]);
                System.out.println(minutes);

                new CountDownTimer(TimeUnit.MINUTES.toMillis(minutes), 60000) {

                    public void onTick(long millisUntilFinished) {
                        //here you can have your logic to set text to edittext
                        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String text = formatter.format(new Date(millisUntilFinished));
                        holder.tv_reverse.setText("Remaining time: " + text);
                    }

                    public void onFinish() {
                    }

                }.start();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            if (booking_data.getStatus().toString().equals("6")) {
                if (booking_data.getStatus().equals("1")) {
                    holder.tv_status.setText("Placed");
                    holder.tv_reverse.setVisibility(View.GONE);
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.placed));
                } else if (booking_data.getStatus().equals("2")) {
                    holder.tv_status.setText("Confirmed");
                    holder.tv_reverse.setVisibility(View.GONE);
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.confirm));
                } else if (booking_data.getStatus().equals("3")) {
                    holder.tv_status.setText("Reschedule");
                    holder.tv_reverse.setVisibility(View.GONE);
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.confirm));
                } else if (booking_data.getStatus().equals("4")) {
                    holder.tv_status.setText("Cancel");
                    holder.tv_reverse.setVisibility(View.GONE);
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.cancel));
                } else if (booking_data.getStatus().equals("5")) {
                    holder.tv_status.setText("Shooting");
                    holder.bt_submit_otp.setText("Complete Shooting");
                    holder.tv_reverse.setVisibility(View.VISIBLE);
                    holder.tv_chat.setVisibility(View.GONE);
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.confirm));
                } else if (booking_data.getStatus().equals("6")) {
                    holder.tv_status.setText("Completed");
                    holder.bt_submit_otp.setVisibility(View.GONE);
                    holder.bt_pod_care.setVisibility(View.GONE);
                    holder.bt_pod_care_2.setVisibility(View.VISIBLE);
                    holder.bt_direction.setVisibility(View.GONE);
                    holder.tv_reverse.setVisibility(View.GONE);
                    holder.tv_chat.setVisibility(View.GONE);
                    holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.confirm));

                    holder.head_linear_1.setVisibility(View.GONE);
                    holder.head_linear_2.setVisibility(View.GONE);
                    holder.head_linear_mp.setVisibility(View.GONE);
                    holder.bt_direction.setVisibility(View.GONE);

                    holder.bt_submit_otp.setVisibility(View.GONE);
                    holder.bt_pod_care.setVisibility(View.GONE);

                    holder.view1.setVisibility(View.GONE);
                    holder.view2.setVisibility(View.GONE);
                }
                if (booking_data.getExtStatus().equals("2")) {
                    if (booking_data.getExtReqFlg().equals("No")) {
                        holder.bt_extends.setVisibility(View.GONE);
                    } else {
                        holder.bt_extends.setVisibility(View.VISIBLE);
                    }

                    if (booking_data.getExtEndTime().equals("0")) {
                        holder.extend_hrs.setVisibility(View.GONE);
                        holder.extend_time.setVisibility(View.GONE);
                    } else {
                        holder.extend_hrs.setVisibility(View.VISIBLE);
                        holder.extend_time.setVisibility(View.VISIBLE);

                        holder.tv_enxtend_total_hrs.setText(booking_data.getExtHours());
                        holder.tv_extended_end_time.setText(booking_data.getExtEndTime());
                    }

                } else if (booking_data.getExtStatus().equals("3")) {
                    if (booking_data.getExtReqFlg().equals("No")) {
                        holder.bt_extends.setVisibility(View.GONE);
                    } else {
                        holder.bt_extends.setVisibility(View.VISIBLE);
                    }

                } else if (booking_data.getExtStatus().equals("4")) {
                    if (booking_data.getExtReqFlg().equals("No")) {
                        holder.bt_extends.setVisibility(View.GONE);
                    } else {
                        holder.bt_extends.setVisibility(View.VISIBLE);
                    }

                    if (booking_data.getExtEndTime().equals("0")) {
                        holder.extend_hrs.setVisibility(View.GONE);
                        holder.extend_time.setVisibility(View.GONE);
                    } else {
                        holder.extend_hrs.setVisibility(View.VISIBLE);
                        holder.extend_time.setVisibility(View.VISIBLE);

                        holder.tv_enxtend_total_hrs.setText(booking_data.getExtHours());
                        holder.tv_extended_end_time.setText(booking_data.getExtEndTime());
                    }

                } else if (booking_data.getExtStatus().equals("5")) {
                    if (booking_data.getExtReqFlg().equals("No")) {
                        holder.bt_extends.setVisibility(View.GONE);
                    } else {
                        holder.bt_extends.setVisibility(View.VISIBLE);
                    }

                    if (booking_data.getExtEndTime().equals("0")) {
                        holder.extend_hrs.setVisibility(View.GONE);
                        holder.extend_time.setVisibility(View.GONE);
                    } else {
                        holder.extend_hrs.setVisibility(View.VISIBLE);
                        holder.extend_time.setVisibility(View.VISIBLE);

                        holder.tv_enxtend_total_hrs.setText(booking_data.getExtHours());
                        holder.tv_extended_end_time.setText(booking_data.getExtEndTime());
                    }
                }

                if (booking_data.getExtStatus().equals("1")) {
                    holder.bt_extends.setVisibility(View.VISIBLE);
                } else {
                    holder.bt_extends.setVisibility(View.GONE);
                }
            } else {
                holder.crd.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

        holder.bt_extends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(context);
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

                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        RequestBody ExtId = RequestBody.create(MediaType.parse("text/plain"), booking_data.getExtId());
                        RequestBody ExtPhotographerId = RequestBody.create(MediaType.parse("text/plain"), booking_data.getPhotographerId());
                        RequestBody ExtCustomerId = RequestBody.create(MediaType.parse("text/plain"), booking_data.getCustomerId());
                        RequestBody ExtOrderId = RequestBody.create(MediaType.parse("text/plain"), booking_data.getExtOrderId());
                        RequestBody ExtApprove = RequestBody.create(MediaType.parse("text/plain"), "2");

                        try {
                            Url.getWebService().extendOrderRequestChangeStatus(ExtId, ExtOrderId, ExtPhotographerId, ExtCustomerId, ExtApprove).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    if (response.isSuccessful()) {

                                        alertDialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                        } catch (Exception e) {
                            Toast.makeText(context, R.string.error_pod, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressDialog.show();
                        RequestBody ExtId = RequestBody.create(MediaType.parse("text/plain"), booking_data.getExtId());
                        RequestBody ExtPhotographerId = RequestBody.create(MediaType.parse("text/plain"), booking_data.getPhotographerId());
                        RequestBody ExtCustomerId = RequestBody.create(MediaType.parse("text/plain"), booking_data.getCustomerId());
                        RequestBody ExtOrderId = RequestBody.create(MediaType.parse("text/plain"), booking_data.getExtOrderId());
                        RequestBody ExtReject = RequestBody.create(MediaType.parse("text/plain"), "3");

                        try {
                            Url.getWebService().extendOrderRequestChangeStatus(ExtId, ExtOrderId, ExtPhotographerId, ExtCustomerId, ExtReject).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    if (response.isSuccessful()) {

                                        alertDialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(context, R.string.error_pod, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // show it
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        holder.bt_submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.bt_submit_otp.getText().toString().equals("Submit OTP")) {

                    LayoutInflater factory = LayoutInflater.from(context);
                    View views = factory.inflate(R.layout.view_receipt, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(views);

                    EditText ed_otp = (EditText) views.findViewById(R.id.ed_otp);
                    TextView tv_resend = (TextView) views.findViewById(R.id.tv_resend);
                    Button bt_close = (Button) views.findViewById(R.id.bt_close);
                    Button bt_otp = (Button) views.findViewById(R.id.bt_otp);

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setView(views);

                    SessionManager session = new SessionManager(getApplicationContext());
                    HashMap<String, String> user = session.getUserDetails();
                    String id = user.get(SessionManager.KEY_ID);
                    RequestBody Booking_id = RequestBody.create(MediaType.parse("text/plain"), booking_data.getId());
                    RequestBody Photographer_id = RequestBody.create(MediaType.parse("text/plain"), id);
                    RequestBody Customer_id = RequestBody.create(MediaType.parse("text/plain"), booking_data.getCustomerId());

                    Url.getWebService().sendOrderOTPtoCustomer(Booking_id, Photographer_id, Customer_id).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject responseObject = AppConstant.getResponseObject(response);
                                    System.out.println(responseObject.toString());

                                    if (responseObject.optBoolean("IsSuccess")) {
                                        Toast.makeText(view.getContext(), "Otp send successfully please enter", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(view.getContext(), responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(view.getContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), R.string.error_pod,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("", "Error in Ticket Category : " + t.getMessage());
                        }
                    });

                    tv_resend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressDialog.show();
                            Url.getWebService().reSendOrderOTPtoCustomer(Booking_id, Photographer_id, Customer_id).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    if (response.isSuccessful()) {
                                        try {
                                            JSONObject responseObject = AppConstant.getResponseObject(response);
                                            System.out.println(responseObject.toString());

                                            if (responseObject.optBoolean("IsSuccess")) {
                                                Toast.makeText(view.getContext(), "Otp send successfully please wait", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(view.getContext(), responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Toast.makeText(view.getContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(view.getContext(), R.string.error_pod,
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                                }
                            });
                        }
                    });

                    bt_otp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressDialog.show();
                            RequestBody otp = RequestBody.create(MediaType.parse("text/plain"), ed_otp.getText().toString());
                            Url.getWebService().verifyOrderOTP(Booking_id, Photographer_id, Customer_id, otp).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    alertDialog.dismiss();
                                    if (response.isSuccessful()) {
                                        try {
                                            JSONObject responseObject = AppConstant.getResponseObject(response);
                                            System.out.println(responseObject.toString());

                                            if (responseObject.optBoolean("IsSuccess")) {
                                                Toast.makeText(view.getContext(), "Session Complete successfully", Toast.LENGTH_LONG).show();
                                                holder.bt_submit_otp.setText("Complete Shooting");
                                                alertDialog.dismiss();
                                                notifyDataSetChanged();
                                                try {
                                                    if (mOnClickListener != null) {
                                                        mOnClickListener.onItemClick(view);
                                                    }
                                                } catch (Exception e) {

                                                }

                                            } else {
                                                Toast.makeText(view.getContext(), responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Toast.makeText(view.getContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(view.getContext(), R.string.error_pod,
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                                }
                            });
                        }
                    });

                    bt_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    // show it
                    alertDialog.show();
                } else {
                    progressDialog.show();
                    RequestBody Booking_id = RequestBody.create(MediaType.parse("text/plain"), booking_data.getId());
                    Url.getWebService().complateOrderByOrderId(Booking_id).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject responseObject = AppConstant.getResponseObject(response);
                                    System.out.println(responseObject.toString());

                                    if (responseObject.optBoolean("IsSuccess")) {
                                        Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();

                                        holder.bt_submit_otp.setVisibility(View.GONE);
                                        holder.bt_pod_care.setVisibility(View.GONE);
                                        holder.bt_pod_care_2.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(view.getContext(), responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(view.getContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), R.string.error_pod,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("", "Error in Ticket Category : " + t.getMessage());
                        }
                    });

                }
            }
        });

        holder.tv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Chat.class);
                intent.putExtra("Customer_id", booking_data.getCustomerId());
                intent.putExtra("Photographer_id", booking_data.getPhotographerId());
                intent.putExtra("orderId", booking_data.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_order_id, tv_total_hrs, tv_shooting_date, tv_start_time, tv_end_time, tv_type, tv_booking_date, tv_booking_time, tv_address, tv_status, tv_reverse, tv_enxtend_total_hrs, tv_extended_end_time,tv_meeting_point;
        Button bt_submit_otp, bt_pod_care, bt_direction, bt_pod_care_2, bt_extends;
        LinearLayout head_linear_1, head_linear_2, extend_time, extend_hrs,head_linear_mp;
        View view1, view2;
        ImageView tv_chat;
        CardView crd;

        ViewHolder(View view) {
            super(view);

            tv_order_id = view.findViewById(R.id.tv_order_id);
            tv_total_hrs = view.findViewById(R.id.tv_total_hrs);
            tv_shooting_date = view.findViewById(R.id.tv_shooting_date);
            tv_start_time = view.findViewById(R.id.tv_start_time);
            tv_end_time = view.findViewById(R.id.tv_end_time);
            tv_type = view.findViewById(R.id.tv_type);
            tv_booking_date = view.findViewById(R.id.tv_booking_date);
            tv_booking_time = view.findViewById(R.id.tv_booking_time);
            tv_address = view.findViewById(R.id.tv_address);
            tv_status = view.findViewById(R.id.tv_status);
            bt_submit_otp = view.findViewById(R.id.bt_submit_otp);
            bt_pod_care = view.findViewById(R.id.bt_pod_care);
            bt_pod_care_2 = view.findViewById(R.id.bt_pod_care_2);
            bt_direction = view.findViewById(R.id.bt_direction);
            head_linear_1 = view.findViewById(R.id.head_a);
            head_linear_2 = view.findViewById(R.id.head_b);
            head_linear_mp = view.findViewById(R.id.head_mp);
            tv_meeting_point = view.findViewById(R.id.tv_meeting_point);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            tv_reverse = view.findViewById(R.id.tv_reverse);

            tv_chat = view.findViewById(R.id.tv_chat);

            tv_enxtend_total_hrs = view.findViewById(R.id.tv_enxtend_total_hrs);
            tv_extended_end_time = view.findViewById(R.id.tv_extended_end_time);
            extend_hrs = view.findViewById(R.id.ext_hrs);
            extend_time = view.findViewById(R.id.ext_time);
            //reverse_count = view.findViewById(R.id.reverse_count);
            crd = view.findViewById(R.id.crd);
            bt_extends = view.findViewById(R.id.bt_extends);
        }
    }
}
