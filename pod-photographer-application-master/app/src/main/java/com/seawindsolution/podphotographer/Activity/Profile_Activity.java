package com.seawindsolution.podphotographer.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.AppConstant;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Profile_Activity extends AppCompatActivity {

    private CircleImageView ig_profile_image;
    private EditText ed_name, ed_email, ed_phone, ed_address;
    private String name;
    private String email;
    private String phone;
    private String address;
    TextView tv_id_number;
    Button bt_continue;
    MultipartBody.Part Profile;
    RequestBody id_s, name_s, add_s, phone_s;
    ProgressDialog progressDialog;

    Bitmap bitmap;
    String selectedImagePath;
    ImageView menu;

    /*Dialog camera*/
    private Dialog mChoosingDialog;
    private ImageView ivbChooseClose, tm_cam;
    private LinearLayout llCamera, llGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dialog_choosing();

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(SessionManager.KEY_ID);

        System.out.println("iddddd          " + id);
        progressDialog = ProgressDialog.show(Profile_Activity.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);

        ig_profile_image = findViewById(R.id.ig_profile_image);
        ed_name = findViewById(R.id.ed_name);
        ed_email = findViewById(R.id.ed_email);
        ed_phone = findViewById(R.id.ed_phone);
        ed_address = findViewById(R.id.ed_address);
        bt_continue = findViewById(R.id.bt_continue);
        tm_cam = findViewById(R.id.tm_cam);

        tv_id_number = findViewById(R.id.tv_id_number);

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ig_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChoosingDialog.show();
            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = ed_name.getText().toString();
                phone = ed_phone.getText().toString();
                address = ed_address.getText().toString();

                System.out.println(name + "       " + email + "       " + phone + "       " + address);
                if (!name.equals("")) {

                    if (phone.length() == 10 && android.util.Patterns.PHONE.matcher(phone).matches()) {

                        if (!address.equals("")) {

                            Submit_profile(id, name, phone, address);

                        } else {
                            ed_address.setError("Invalid address");
                        }
                    } else {
                        ed_phone.setError("Invalid mobile number");
                    }

                } else {
                    ed_name.setError("Required");
                }
            }
        });

        getprofile(id);
    }

    private void Submit_profile(String id, String name, String phone, String address) {

        try {
            progressDialog.show();
            id_s = RequestBody.create(MediaType.parse("text/plain"), id);
            name_s = RequestBody.create(MediaType.parse("text/plain"), name);
            add_s = RequestBody.create(MediaType.parse("text/plain"), address);
            phone_s = RequestBody.create(MediaType.parse("text/plain"), phone);

            File idProofFile = new File(selectedImagePath);
            String fileType = URLConnection.guessContentTypeFromName(idProofFile.getName());
            String fileName = idProofFile.getName().replaceAll(" ", "_");
            RequestBody fileBody = RequestBody.create(MediaType.parse(fileType), idProofFile);
            Profile = MultipartBody.Part.createFormData("ProfileImage", fileName, fileBody);

        } catch (Exception e) {

        }

        Url.getWebService().updatephotographerProfile(id_s, name_s, phone_s, add_s, Profile).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.toString());
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    SessionManager session = new SessionManager(getApplicationContext());
                    HashMap<String, String> user = session.getUserDetails();
                    String Email = user.get(SessionManager.KEY_EMAIL);

                    SessionManager session_update = new SessionManager(getApplicationContext());
                    session_update.createLoginSession(name, Email, id);

                    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Profile_Activity.this);
                    myAlertDialog.setTitle("Your Profile has been update successfully");
                    myAlertDialog.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(Profile_Activity.this, HomeScreen.class);
                                    startActivity(intent);
                                }
                            });
                    myAlertDialog.setCancelable(false);
                    myAlertDialog.show();

                } else {
                    JSONObject responseObject = AppConstant.getResponseObject(response);
                    System.out.println("r   :" + responseObject.toString());

                    Toast.makeText(Profile_Activity.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Profile_Activity.this, R.string.error_pod,
                        Toast.LENGTH_SHORT).show();
                Log.d("aaaaaaaaa", "Error in Ticket Category : " + t.getMessage());
            }
        });
    }

    private void getprofile(String id) {
        progressDialog.show();

        Url.getWebService().getphotographerProfileByphotographerId(Integer.parseInt(id)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.body().toString());
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject = AppConstant.getResponseObject(response);

                        if (responseObject.optBoolean("IsSuccess")) {
                            String detail = responseObject.optString("ResponseData");
                            try {
                                JSONObject jsonObject = new JSONObject(detail);
                                String Id = jsonObject.optString("Id");
                                String Name = jsonObject.optString("Name");
                                String Email = jsonObject.optString("Email");
                                String Phone = jsonObject.optString("Phone");
                                String Address = jsonObject.optString("Address");
                                String ProfileImage = jsonObject.optString("ProfileImage");

                                String id_number = jsonObject.optString("pId");

                                if (!id_number.isEmpty()) {
                                    tv_id_number.setText("Photographer id : " + id_number);
                                } else {
                                    tv_id_number.setText("Photographer id : #123456789");
                                }

                                if (Name.equals("")) {

                                } else {
                                    ed_name.setText(Name);
                                }
                                if (Email.equals("")) {

                                } else {
                                    ed_email.setEnabled(false);
                                    ed_email.setText(Email);
                                }
                                if (Phone.equals("")) {

                                } else {
                                    ed_phone.setEnabled(false);
                                    ed_phone.setText(Phone);
                                }
                                if (Address.equals("")) {

                                } else {
                                    ed_address.setText(Address);
                                }

                                Glide.with(Profile_Activity.this.getApplicationContext()).load(ProfileImage).apply(RequestOptions.placeholderOf(R.drawable.logo)).into(ig_profile_image);

                                bt_continue.setVisibility(View.VISIBLE);

                            } catch (Exception e) {

                            }
                        } else {
                            Toast.makeText(Profile_Activity.this, responseObject.optString("Message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(Profile_Activity.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Profile_Activity.this, R.string.error_pod,
                        Toast.LENGTH_SHORT).show();
                Log.d("", "Error in Ticket Category : " + t.getMessage());
            }
        });
    }

    private void dialog_choosing() {
        mChoosingDialog = new Dialog(Profile_Activity.this, R.style.AppTheme);
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
                    if (ContextCompat.checkSelfPermission(Profile_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                        mChoosingDialog.dismiss();
                    } else {
                        ActivityCompat.requestPermissions(Profile_Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 401);
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
