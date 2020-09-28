package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seawindsolution.podphotographer.R;
import com.seawindsolution.podphotographer.Servicew.SessionManager;
import com.seawindsolution.podphotographer.Servicew.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Application_issue extends AppCompatActivity {

    EditText ed_screen_shoot;
    Button bt_screen_shoot, bt_submit;
    protected static final int AP_GAL = 2;
    Bitmap bitmap;
    String selectedImagePath, path_1 = "";
    ProgressDialog progressDialog;
    TextView tv_path_1;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_issue);
        ed_screen_shoot = findViewById(R.id.ed_screen_shoot);
        bt_screen_shoot = findViewById(R.id.bt_screen_shoot);
        bt_submit = findViewById(R.id.bt_submit);
        tv_path_1 = findViewById(R.id.tv_path_1);

        progressDialog = ProgressDialog.show(Application_issue.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bt_screen_shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pictureActionIntent = null;
                pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pictureActionIntent, AP_GAL);
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SessionManager session = new SessionManager(getApplicationContext());
                    HashMap<String, String> user = session.getUserDetails();
                    String Name = user.get(SessionManager.KEY_NAME);
                    String Email = user.get(SessionManager.KEY_EMAIL);

                    String message = ed_screen_shoot.getText().toString();

                    if (!path_1.equals("") && !path_1.isEmpty()) {
                        if (!message.equals("") && !message.equals("Enter Message")) {

                            progressDialog.show();

                            RequestBody id_name = RequestBody.create(MediaType.parse("text/plain"), Name);
                            RequestBody id_email = RequestBody.create(MediaType.parse("text/plain"), Email);
                            RequestBody id_Message = RequestBody.create(MediaType.parse("text/plain"), Email);

                            File idProofFile = new File(path_1);
                            String fileType = URLConnection.guessContentTypeFromName(idProofFile.getName());
                            String fileName = idProofFile.getName().replaceAll(" ", "_");
                            RequestBody fileBody = RequestBody.create(MediaType.parse(fileType), idProofFile);
                            MultipartBody.Part id_Image = MultipartBody.Part.createFormData("ProfileImage", fileName, fileBody);
                            try {
                                Url.getWebService().addGeneralQuery(id_name, id_email, id_Message, id_Image).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        System.out.println(response.body().toString());
                                        if (response.isSuccessful()) {
                                            try {
                                                progressDialog.dismiss();
                                                JSONObject responseObject = new JSONObject(response.body().string());

                                                if (responseObject.optBoolean("IsSuccess")) {
                                                    progressDialog.dismiss();
                                                    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Application_issue.this);
                                                    myAlertDialog.setTitle("Your inquiry has been submit successfully");
                                                    myAlertDialog.setMessage(getResources().getText(R.string.booknow));
                                                    myAlertDialog.setPositiveButton("ok",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    Intent intent = new Intent(Application_issue.this, HomeScreen.class);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                    myAlertDialog.setCancelable(false);
                                                    myAlertDialog.show();
                                                }
                                            } catch (JSONException | IOException e) {
                                                e.printStackTrace();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), R.string.error_pod,
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        Log.d("", "Error in Ticket Category : " + t.getMessage());
                                    }
                                });

                            }catch (Exception e){
                                Toast.makeText(Application_issue.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            ed_screen_shoot.setError("Required");
                        }

                    } else {
                        tv_path_1.setError("Required");
                    }
                }catch (Exception e){
                    Toast.makeText(Application_issue.this, R.string.error_pod, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Application_issue.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent pictureActionIntent = null;
                        pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pictureActionIntent, AP_GAL);


                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, AP_CAM);
                    }
                });
        myAlertDialog.show();
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

       /* if (resultCode == RESULT_OK && requestCode == AP_CAM) {

            File f = new File(Environment.getExternalStorageDirectory().toString());
            try {
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("Cam_image.jpg")) {
                        f = temp;
                        break;
                    }
                }
            } catch (Exception e) {

            }

            if (!f.exists()) {
                Toast.makeText(getApplicationContext(), "Error while capturing image", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);

                path_1 = f.getAbsolutePath();
                System.out.println("qqqq        " + f.getAbsolutePath());

                File idProofFiles = new File(path_1);
                String fileTypes = URLConnection.guessContentTypeFromName(idProofFiles.getName());
                String fileNames = idProofFiles.getName().replaceAll(" ", "_");
                RequestBody fileBodys = RequestBody.create(MediaType.parse(fileTypes), idProofFiles);
                bt_screen_shoot.setText(fileNames);
                //storeImageTosdCard(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else */
        if (resultCode == RESULT_OK && requestCode == AP_GAL) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = Application_issue.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                path_1 = c.getString(columnIndex);
                c.close();

                if (path_1 != null) {
                    //txt_image_path.setText(selectedImagePath);
                }

                bitmap = BitmapFactory.decodeFile(path_1); // load
                // preview image
                bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(path_1);
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);

                File idProofFiles = new File(path_1);
                String fileTypes = URLConnection.guessContentTypeFromName(idProofFiles.getName());
                String fileNames = idProofFiles.getName().replaceAll(" ", "_");
                RequestBody fileBodys = RequestBody.create(MediaType.parse(fileTypes), idProofFiles);
                bt_screen_shoot.setText(fileNames);
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}