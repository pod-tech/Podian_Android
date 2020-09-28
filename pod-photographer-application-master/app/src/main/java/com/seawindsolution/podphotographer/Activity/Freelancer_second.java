package com.seawindsolution.podphotographer.Activity;

import android.app.AlertDialog;
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
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seawindsolution.podphotographer.R;

import java.io.File;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.seawindsolution.podphotographer.Activity.Freelancer_third.s;

public class Freelancer_second extends AppCompatActivity {

    EditText ed_landmark, ed_city, ed_state, ed_pin, ed_aadhar;
    RadioButton rd_employee, rd_unemployee, rd_self_employee;
    TextView tv_path_1, error1;
    Button bt_submit, bt_aadhar;
    RadioGroup rd_radio_2;
    public static String landmark, city, state, pin, employee, aadhar, path_1 = "";
    ProgressDialog progressDialog;

    protected static final int AP_GAL = 2;
    protected static final int AP_CAM = 1;

    Bitmap bitmap;
    String selectedImagePath;

    public static Freelancer_second freelancer_second;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_second);

        freelancer_second = this;

        progressDialog = ProgressDialog.show(Freelancer_second.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        ed_landmark = findViewById(R.id.ed_landmark);
        ed_city = findViewById(R.id.ed_city);
        ed_state = findViewById(R.id.ed_state);
        ed_pin = findViewById(R.id.ed_zip);
        ed_aadhar = findViewById(R.id.ed_aadhar);

        ed_city.setText("Ahmedabad");
        ed_state.setText("Gujarat");
        tv_path_1 = findViewById(R.id.tv_path_1);
        error1 = findViewById(R.id.tv_error1);

        rd_employee = findViewById(R.id.rd_employee);
        rd_unemployee = findViewById(R.id.rd_un_employee);
        rd_self_employee = findViewById(R.id.rd_self_employee);

        rd_radio_2 = findViewById(R.id.rd_radio_2);

        bt_submit = findViewById(R.id.bt_continue_2);
        bt_aadhar = findViewById(R.id.bt_aathar);

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bt_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startDialog();

            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                landmark = ed_landmark.getText().toString();
                city = ed_city.getText().toString();
                state = ed_state.getText().toString();
                pin = ed_pin.getText().toString();
                aadhar = ed_aadhar.getText().toString();

                if (!aadhar.isEmpty()) {

                    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
                    Matcher matcher = pattern.matcher(aadhar);

                    if (Aadhaar.validateAadharNumber(aadhar)||matcher.matches()) {
                        call_next();
                    } else{
                        ed_aadhar.setError("Invalid number");
                    }
                }

            }
        });
    }

    private void call_next() {

        if (!landmark.equals("")) {

            if (!city.equals("")) {

                if (!state.equals("")) {

                    if (!pin.equals("") && pin.length() == 6) {

                        if (!aadhar.equals("")) {

                            if (!path_1.equals("")) {

                                int selectedId2 = rd_radio_2.getCheckedRadioButtonId();
                                RadioButton radioSexButton2 = (RadioButton) findViewById(selectedId2);

                                employee = String.valueOf(radioSexButton2.getText());

                                if (!employee.equals("")) {
                                    Intent intent = new Intent(Freelancer_second.this, Freelancer_third.class);
                                    startActivity(intent);
                                } else {

                                }

                            } else {
                                tv_path_1.setError("Required");
                            }

                        } else {

                        }

                    } else {
                        ed_pin.setError("Required");
                    }

                } else {
                    ed_state.setError("Required");
                }

            } else {
                ed_city.setError("Required");
            }

        } else {
            ed_landmark.setError("Required");
        }

    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Freelancer_second.this);
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
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "Cam_image.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, AP_CAM);
                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            bitmap = null;
            selectedImagePath = null;

            if (resultCode == RESULT_OK && requestCode == AP_CAM) {

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
                    //bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
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
                    System.out.println("qqqq    t    " + f.getAbsolutePath());

                    File idProofFiles = new File(path_1);
                    String fileTypes = URLConnection.guessContentTypeFromName(idProofFiles.getName());
                    String fileNames = idProofFiles.getName().replaceAll(" ", "_");
                    RequestBody fileBodys = RequestBody.create(MediaType.parse(fileTypes), idProofFiles);
                    bt_aadhar.setText(fileNames);
                    //storeImageTosdCard(bitmap);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_OK && requestCode == AP_GAL) {
                if (data != null) {

                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = Freelancer_second.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    path_1 = c.getString(columnIndex);
                    c.close();

                    if (path_1 != null) {
                        //txt_image_path.setText(selectedImagePath);
                    }

                    bitmap = BitmapFactory.decodeFile(path_1); // load
                    // preview image
                    //bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
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
                    bt_aadhar.setText(fileNames);
                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(freelancer_second, "You don't provide camera permission", Toast.LENGTH_SHORT).show();
        }
    }
}
