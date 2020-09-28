package com.seawindsolution.podphotographer.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.seawindsolution.podphotographer.R;

import java.io.File;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Freelancer extends AppCompatActivity {

    CardView btnDatePicker;
    EditText ed_name, ed_email, ed_phone;
    RadioButton rd_radio_male, rd_radio_female, rd_radio_no_ans;
    TextView tv_date;
    Button bt_submit;
    CircleImageView ig_profile_image;
    ImageView tm_cam;
    RadioGroup rd_radio_1;
    private int mYear, mMonth, mDay;
    public static String profile, name, email, phone, date, gender;
    ProgressDialog progressDialog;

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;

    Bitmap bitmap;
    String selectedImagePath;

    public static Freelancer freelancer;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer);

        freelancer = this;

        progressDialog = ProgressDialog.show(Freelancer.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        ed_name = findViewById(R.id.ed_name);
        ed_email = findViewById(R.id.ed_email);
        ed_phone = findViewById(R.id.ed_phone);
        tm_cam = findViewById(R.id.tm_cam);
        tv_date = findViewById(R.id.tv_date);

        rd_radio_male = findViewById(R.id.rd_radioMale);
        rd_radio_female = findViewById(R.id.rd_radioFemale);
        rd_radio_no_ans = findViewById(R.id.rd_radioNoAns);

        rd_radio_1 = findViewById(R.id.rd_radio);

        ig_profile_image = findViewById(R.id.ig_profile_image);
        bt_submit = findViewById(R.id.bt_continue_1);
        btnDatePicker = findViewById(R.id.card_date);

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
                startDialog();
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Freelancer.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = ed_name.getText().toString();
                email = ed_email.getText().toString();
                phone = ed_phone.getText().toString();
                date = tv_date.getText().toString();

                if (!name.equals("")) {

                    if (!email.equals("") && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        if (!phone.equals("") && ed_phone.length() == 10) {

                            if (rd_radio_male.isChecked() || rd_radio_female.isChecked() || rd_radio_no_ans.isChecked()) {

                                if (!date.equals("") && !date.equals("Select Date of Birth")) {

                                    if (selectedImagePath != null) {

                                        profile = selectedImagePath;

                                        int selectedId = rd_radio_1.getCheckedRadioButtonId();
                                        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);

                                        gender = String.valueOf(radioSexButton.getText());

                                        if (!gender.equals("")) {
                                            Intent intent = new Intent(Freelancer.this, Freelancer_second.class);
                                            startActivity(intent);
                                        } else {

                                        }

                                    } else {
                                        Toast.makeText(Freelancer.this, "Please select Profile", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    tv_date.setError("Required");
                                }

                            } else {

                            }

                        } else {
                            ed_phone.setError("Required");
                        }

                    } else {
                        ed_email.setError("Required");
                    }
                } else {
                    ed_name.setError("Required");
                }
            }
        });
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Freelancer.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent pictureActionIntent = null;
                        pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);


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
                        startActivityForResult(intent, CAMERA_REQUEST);
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

            if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

                File f = new File(Environment.getExternalStorageDirectory().toString());
                try {
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
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

                    ig_profile_image.setImageBitmap(bitmap);
                    tm_cam.setVisibility(View.GONE);

                    selectedImagePath = f.getAbsolutePath();
                    System.out.println("qqqq d       " + f.getAbsolutePath());
                    //storeImageTosdCard(bitmap);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
                if (data != null) {

                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = Freelancer.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    selectedImagePath = c.getString(columnIndex);
                    c.close();

                    if (selectedImagePath != null) {
                        //txt_image_path.setText(selectedImagePath);
                    }

                    bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                    // preview image
                    //bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    int rotate = 0;
                    try {
                        ExifInterface exif = new ExifInterface(selectedImagePath);
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

                    ig_profile_image.setImageBitmap(bitmap);
                    tm_cam.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(freelancer, "You don't provide camera permission", Toast.LENGTH_SHORT).show();
        }
    }
}
