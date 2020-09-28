package com.seawindsolution.podphotographer.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {

    EditText ed_name, ed_email, ed_address, ed_phone, ed_otp, ed_psd;
    CheckBox reg_checkbox;
    Button bt_continue;
    TextView tv_resend;
    TextInputLayout card_otp, card_psd;
    CircleImageView ig_profile_image;
    MultipartBody.Part Profile;
    ProgressDialog progressDialog;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    //int a = 0;

    Bitmap bitmap;
    String selectedImagePath, full_name_s, email_s, address_s, mobile_no_s, otp_s, psd_s;

    /*Dialog camera*/
    private Dialog mChoosingDialog;
    private ImageView ivbChooseClose, tm_cam;
    private LinearLayout llCamera, llGallery;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dialog_choosing();

        progressDialog = ProgressDialog.show(Registration.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        ed_name = findViewById(R.id.ed_name);
        ed_email = findViewById(R.id.ed_email);
        ed_address = findViewById(R.id.ed_address);
        ed_phone = findViewById(R.id.ed_phone);
        ed_otp = findViewById(R.id.ed_otp);
        ed_psd = findViewById(R.id.ed_psd);
        card_otp = findViewById(R.id.card_otp);
        card_psd = findViewById(R.id.card_psd);
        bt_continue = findViewById(R.id.bt_continue);
        tv_resend = findViewById(R.id.tv_resend);
        reg_checkbox = findViewById(R.id.reg_checkbox);
        ig_profile_image = findViewById(R.id.ig_profile_image);
        tm_cam = findViewById(R.id.tm_cam);

        bt_continue.setClickable(false);
        //a = 0;

        ig_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChoosingDialog.show();
            }
        });

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    full_name_s = ed_name.getText().toString();
                    email_s = ed_email.getText().toString();
                    mobile_no_s = ed_phone.getText().toString();

                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), full_name_s);
                    RequestBody email = RequestBody.create(MediaType.parse("text/plain"), email_s);
                    RequestBody mobile_number = RequestBody.create(MediaType.parse("text/plain"), mobile_no_s);

                    if (!full_name_s.equals("")) {
                        if (!email_s.equals("") && Patterns.EMAIL_ADDRESS.matcher(email_s).matches()) {
                            if (!address_s.equals("")) {
                                if (ed_phone.length() == 10 && android.util.Patterns.PHONE.matcher(mobile_no_s).matches()) {

                                    progressDialog.show();

                                    Url.getWebService().photographerRegistrationReSendOTP(name, email, mobile_number).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            progressDialog.dismiss();
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject responseObject = AppConstant.getResponseObject(response);

                                                    System.out.println("response otp    " + response.body().toString());

                                                    if (responseObject.optBoolean("IsSuccess")) {

                                                        Toast.makeText(Registration.this, "OTP successfully send", Toast.LENGTH_SHORT).show();

                                                    } else {
                                                        Toast.makeText(Registration.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                Toast.makeText(Registration.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Registration.this, R.string.error_pod,
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("", "Error in Ticket Category : " + t.getMessage());
                                        }
                                    });

                                } else {
                                    card_otp.setVisibility(View.GONE);
                                    card_psd.setVisibility(View.GONE);
                                    reg_checkbox.setVisibility(View.GONE);
                                    tv_resend.setVisibility(View.GONE);
                                    bt_continue.setText("VERIFY OTP");
                                    ed_phone.setError("Invalid");
                                }
                            } else {
                                ed_address.setError("Required");
                            }
                        } else {
                            ed_email.setError("Invalid email address");
                        }
                    } else {
                        ed_name.setError("Required");
                    }
                } catch (Exception e) {
                    Toast.makeText(Registration.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                   /* if (a == 0) {
                        Toast.makeText(Registration.this, "Please select profile image", Toast.LENGTH_SHORT).show();
                    } else {*/
                    full_name_s = ed_name.getText().toString();
                    email_s = ed_email.getText().toString();
                    address_s = ed_address.getText().toString();
                    mobile_no_s = ed_phone.getText().toString();
                    otp_s = ed_otp.getText().toString();
                    psd_s = ed_psd.getText().toString();

                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), full_name_s);
                    RequestBody email = RequestBody.create(MediaType.parse("text/plain"), email_s);
                    RequestBody address = RequestBody.create(MediaType.parse("text/plain"), address_s);
                    RequestBody mobile_number = RequestBody.create(MediaType.parse("text/plain"), mobile_no_s);
                    RequestBody otp = RequestBody.create(MediaType.parse("text/plain"), otp_s);
                    RequestBody psd = RequestBody.create(MediaType.parse("text/plain"), psd_s);
                    RequestBody SignBy = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1));
                    RequestBody social_id = RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody Profileimage_url = RequestBody.create(MediaType.parse("text/plain"), "");

                    if (!full_name_s.equals("")) {
                        if (!email_s.equals("") && Patterns.EMAIL_ADDRESS.matcher(email_s).matches()) {
                            if (!address_s.equals("")) {
                                if (ed_phone.length() == 10 && android.util.Patterns.PHONE.matcher(mobile_no_s).matches()) {
                                    if (bt_continue.getText().toString().equals("VERIFY OTP")) {

                                        progressDialog.show();

                                        Url.getWebService().photographerRegistrationSendOTP(name, email, mobile_number).enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                progressDialog.dismiss();
                                                if (response.isSuccessful()) {
                                                    try {
                                                        JSONObject responseObject = AppConstant.getResponseObject(response);

                                                        System.out.println("response otp    " + response.body().toString());

                                                        if (responseObject.optBoolean("IsSuccess")) {

                                                            card_otp.setVisibility(View.VISIBLE);
                                                            reg_checkbox.setVisibility(View.VISIBLE);
                                                            tv_resend.setVisibility(View.VISIBLE);
                                                            card_psd.setVisibility(View.VISIBLE);
                                                            bt_continue.setText("SIGN IN");

                                                            ig_profile_image.setClickable(false);
                                                            ed_name.setEnabled(false);
                                                            ed_email.setEnabled(false);
                                                            ed_address.setEnabled(false);
                                                            ed_phone.setEnabled(false);

                                                        } else {
                                                            Toast.makeText(Registration.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    Toast.makeText(Registration.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                progressDialog.dismiss();
                                                Toast.makeText(Registration.this, R.string.error_pod,
                                                        Toast.LENGTH_SHORT).show();
                                                Log.d("", "Error in Ticket Category : " + t.getMessage());
                                            }
                                        });
                                    }

                                    if (bt_continue.getText().toString().equals("SIGN IN")) {
                                        if (bt_continue.getText().toString().equals("SIGN IN")) {
                                            if (reg_checkbox.isChecked()) {
                                                try {
                                                    if (!isValidPassword(ed_psd.getText().toString())) {
                                                        ed_psd.setError("Password must contains one lowercase characters, one uppercase characters,one special symbols in the list \"@#$%\" length at least 6 characters and maximum of 20");
                                                    } else {
                                                        progressDialog.show();
                                                        try {
                                                            if (selectedImagePath == null) {

                                                            } else {
                                                                File idProofFile = new File(selectedImagePath);
                                                                String fileType = URLConnection.guessContentTypeFromName(idProofFile.getName());
                                                                String fileName = idProofFile.getName().replaceAll(" ", "_");
                                                                RequestBody fileBody = RequestBody.create(MediaType.parse(fileType), idProofFile);
                                                                Profile = MultipartBody.Part.createFormData("ProfileImage", fileName, fileBody);

                                                                System.out.println("Register    " + full_name_s + "   " + email_s + "     " + mobile_no_s + "     " + address_s
                                                                        + "      " + otp_s + "       " + psd_s + "   " + SignBy.toString() + "       " + Profile.toString() + "       " + fileType);

                                                            }
                                                        } catch (Exception e) {

                                                        }
                                                        progressDialog.show();
                                                        try {
                                                            Url.getWebService().photographerRegistration(name, email, mobile_number, address, otp, psd, SignBy, social_id, Profileimage_url, Profile).enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                                    progressDialog.dismiss();
                                                                    if (response.isSuccessful()) {
                                                                        try {
                                                                            JSONObject responseObject = AppConstant.getResponseObject(response);

                                                                            if (responseObject.optBoolean("IsSuccess")) {

                                                                                Intent intent = new Intent(Registration.this, Login.class);
                                                                                startActivity(intent);
                                                                            } else {
                                                                                Toast.makeText(Registration.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        } catch (Exception e) {

                                                                            e.printStackTrace();
                                                                        }

                                                                    } else {
                                                                        Toast.makeText(Registration.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(Registration.this, R.string.error_pod,
                                                                            Toast.LENGTH_SHORT).show();
                                                                    Log.d("", "Error in Ticket Category : " + t.getMessage());
                                                                }
                                                            });
                                                        } catch (Exception e) {

                                                        }
                                                    }
                                                } catch (Exception e) {

                                                }
                                            } else {
                                                Toast.makeText(Registration.this, "Accept terms and condition", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {

                                    }

                                } else {
                                    card_otp.setVisibility(View.GONE);
                                    card_psd.setVisibility(View.GONE);
                                    tv_resend.setVisibility(View.GONE);
                                    reg_checkbox.setVisibility(View.GONE);
                                    bt_continue.setText("VERIFY OTP");
                                    ed_phone.setError("Invalid");
                                }
                            } else {
                                ed_address.setError("Required");
                            }
                        } else {
                            ed_email.setError("Invalid email address");
                        }
                    } else {
                        ed_name.setError("Required");
                    }
                    //}
                } catch (Exception e) {
                    Toast.makeText(Registration.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean isValidPassword(String password) {
        //Matcher matcher = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})").matcher(password);
        Matcher matcher = Pattern.compile("\\b\\w{4,20}\\b").matcher(password);
        return matcher.matches();
    }

    private void dialog_choosing() {
        mChoosingDialog = new Dialog(Registration.this, R.style.AppTheme);
        mChoosingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        mChoosingDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        mChoosingDialog.getWindow().setGravity(Gravity.BOTTOM);
        mChoosingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mChoosingDialog.setCancelable(true);
        mChoosingDialog.setContentView(R.layout.dialog_choose_cameragallery);

        ivbChooseClose = (ImageView) mChoosingDialog.findViewById(R.id.ivbChooseClose);
        llCamera = (LinearLayout) mChoosingDialog.findViewById(R.id.llCamera);
        llGallery = (LinearLayout) mChoosingDialog.findViewById(R.id.llGallery);

        llCamera.setOnClickListener(this::onClick);
        llGallery.setOnClickListener(this::onClick);
        ivbChooseClose.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivbChooseClose:
                mChoosingDialog.dismiss();
                break;
            case R.id.llCamera:
                mChoosingDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {
                        openCamera();
                        mChoosingDialog.dismiss();
                    }
                } else {
                    openCamera();
                    mChoosingDialog.dismiss();
                }
                break;
            case R.id.llGallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                        mChoosingDialog.dismiss();
                    } else {
                        ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 401);
                        mChoosingDialog.dismiss();
                    }
                } else {
                    openGallery();
                    mChoosingDialog.dismiss();
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 402);
            return false;
        }
        return true;
    }

    private void openGallery() {
        Intent pictureActionIntent = null;
        pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pictureActionIntent, 501);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 502);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 501 && resultCode == RESULT_OK && data != null) {
            Uri selectedURI = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getApplicationContext().getContentResolver().query(selectedURI, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            selectedImagePath = c.getString(columnIndex);
            c.close();
            System.out.println(selectedImagePath);

            bt_continue.setClickable(true);
            //a = 1;

            try {
                Bitmap bitmap = convert_UriToBitmap(selectedURI);
                ig_profile_image.setImageBitmap(bitmap);
                tm_cam.setVisibility(View.GONE);

            } catch (IOException e) {
                e.printStackTrace();
            }

//            CropImage.activity(selectedURI).start(this);

        } else if (requestCode == 502 && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            ig_profile_image.setImageBitmap(mphoto);
            tm_cam.setVisibility(View.GONE);

            Uri selectedURI = getImageUri(mphoto);

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getApplicationContext().getContentResolver().query(selectedURI, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            selectedImagePath = c.getString(columnIndex);
            c.close();
            System.out.println(selectedImagePath);

            bt_continue.setClickable(true);
            //a = 1;
           /* Uri selectedURI = getImageUri(mphoto);

            CropImage.activity(selectedURI).start(this);*/

//            ImageLoad.onBitmapLoadCirlce(DrawerActivity.this, mphoto, civProfilePic);
        } /*else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri selectedURI = result.getUri();

                try {
                    Bitmap bitmap = convert_UriToBitmap(selectedURI);
                    ivImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 401) {
            if (grantResults.length == 0 || grantResults == null) {
//                Logger.e(TAG, "Null Every thing");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }
        } else if (requestCode == 402) {
            if (grantResults.length == 0 || grantResults == null) {
//                Logger.e(TAG, "Null Every thing");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }
        }
    }

    private Bitmap convert_UriToBitmap(Uri selectedURI) throws IOException {
        return (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), selectedURI);
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (bitmap != null) {
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            return Uri.parse(path);
        } else {
            return Uri.parse("");
        }
    }

    @Override
    public void onBackPressed() {
        if (mChoosingDialog.isShowing()) {
            mChoosingDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mChoosingDialog.isShowing()) {
            mChoosingDialog.dismiss();
        }
        super.onDestroy();

    }
}
