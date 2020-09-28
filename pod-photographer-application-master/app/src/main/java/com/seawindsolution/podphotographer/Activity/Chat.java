package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.podphotographer.Pojo.Chat_adapter_p;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity implements LocationListener {

    LinearLayout button_location_send;
    ImageView button_chatbox_send;
    TextView edit_text_chat_box;
    private ArrayList<Chat_adapter_p> arrayList = new ArrayList<>();
    RecyclerView reyclerview_message_list;
    Chat_adapter adapter;
    String Customer_id, Photographer_id, Latitude, Longitude, orderId;
    LinearLayoutManager llm;
    LocationManager locationManager;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Customer_id = (String.valueOf(0));
            Photographer_id = (String.valueOf(0));
            orderId = (String.valueOf(0));
        } else {
            Customer_id = (extras.getString("Customer_id"));
            Photographer_id = (extras.getString("Photographer_id"));
            orderId = (extras.getString("orderId"));
        }

        getLocation();

        button_location_send = findViewById(R.id.button_location_send);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        edit_text_chat_box = findViewById(R.id.edittext_chatbox);
        reyclerview_message_list = findViewById(R.id.reyclerview_message_list);

        reyclerview_message_list.setHasFixedSize(true);
        llm = new LinearLayoutManager(getApplicationContext());
        reyclerview_message_list.setLayoutManager(llm);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        adapter = new Chat_adapter(Chat.this, arrayList);
        reyclerview_message_list.setAdapter(adapter);

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                getchat();
            }
        };

        // schedule the task to run starting now and then every hour...
        timer.schedule(hourlyTask, 0l, 7000);   // 1000*10*60 every 10 minut

        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Chat.this);
                myAlertDialog.setMessage("Do you want to ask user current location?");
                myAlertDialog.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), "Dear user please share your current location to photographer.");
                                RequestBody utype = RequestBody.create(MediaType.parse("text/plain"), "p");
                                RequestBody isLocation = RequestBody.create(MediaType.parse("text/plain"), "No");
                                RequestBody OrderId = RequestBody.create(MediaType.parse("text/plain"), orderId);
                                System.out.println(Photographer_id + "        " + orderId + "     " + Customer_id);
                                //edit_text_chat_box.getText().clear();
                                try {
                                    Url.getWebService().sendMessages(Integer.parseInt(Photographer_id), Integer.parseInt(Customer_id), msg, utype, isLocation, OrderId).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject responseObject = AppConstant.getResponseObject(response);

                                                    if (responseObject.optBoolean("IsSuccess")) {
                                                        getchat();
                                                    } else {
                                                        Toast.makeText(Chat.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                Toast.makeText(Chat.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(Chat.this, R.string.error_pod,
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("", "Error in Ticket Category : " + t.getMessage());
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(Chat.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                myAlertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.cancel();
                            }
                        });
                myAlertDialog.setCancelable(false);
                myAlertDialog.show();

            }
        });

        button_location_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();

                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Chat.this);
                myAlertDialog.setMessage("Are You sure do you want share your location?");
                myAlertDialog.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                String uri = "https://www.google.com/maps/search/?api=1&query=" + Latitude + "," + Longitude;

                                RequestBody msg = RequestBody.create(MediaType.parse("text/url"), uri);
                                RequestBody utype = RequestBody.create(MediaType.parse("text/plain"), "p");
                                RequestBody isLocation = RequestBody.create(MediaType.parse("text/plain"), "Yes");
                                RequestBody OrderId = RequestBody.create(MediaType.parse("text/plain"), orderId);

                                //edit_text_chat_box.getText().clear();
                                try {
                                    Url.getWebService().sendMessages(Integer.parseInt(Photographer_id), Integer.parseInt(Customer_id), msg, utype, isLocation, OrderId).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject responseObject = AppConstant.getResponseObject(response);

                                                    if (responseObject.optBoolean("IsSuccess")) {
                                                        getchat();
                                                    } else {
                                                        Toast.makeText(Chat.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                Toast.makeText(Chat.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(Chat.this, R.string.error_pod,
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("", "Error in Ticket Category : " + t.getMessage());
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(Chat.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                myAlertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.cancel();
                            }
                        });
                myAlertDialog.setCancelable(false);
                myAlertDialog.show();
            }
        });

        getchat();
    }

    private void getchat() {
        try {

            System.out.println(Photographer_id + "        " + Customer_id + "        " + orderId);
            Url.getWebService().getMessages(Integer.parseInt(Photographer_id), Integer.parseInt(Customer_id), Integer.parseInt(orderId)).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    arrayList.clear();
                    if (response.isSuccessful()) {
                        JSONObject responseObject = AppConstant.getResponseObject(response);
                        String detail = responseObject.optString("Message");
                        System.out.println(detail);
                        if (responseObject != null) {
                            if (responseObject.optBoolean("IsSuccess")) {
                                List<Chat_adapter_p> temp_survey = new Gson().fromJson(responseObject
                                        .optString("ResponseData"), new TypeToken<List<Chat_adapter_p>>() {
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
                        Toast.makeText(getApplicationContext(), R.string.error_pod, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), R.string.error_pod,
                            Toast.LENGTH_SHORT).show();
                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                }
            });

        } catch (Exception e) {
        }

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Latitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());
        System.out.println("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
