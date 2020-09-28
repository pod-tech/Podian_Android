package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.seawindsolution.podphotographer.R;

public class Settings extends AppCompatActivity {

    TextView tv_reset_password, about_us, privacy_policy, terms_and_condition, contact_us, log_out, version;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tv_reset_password = findViewById(R.id.tv_reset_password);
        about_us = findViewById(R.id.about_us);
        privacy_policy = findViewById(R.id.privacy_policy);
        terms_and_condition = findViewById(R.id.terms_and_condition);
        contact_us = findViewById(R.id.contact_us);
        log_out = findViewById(R.id.log_out);
        version = findViewById(R.id.version);

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version_number = pInfo.versionName;
            version.setText(version_number);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tv_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, Reset_Password.class);
                startActivity(intent);
            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, About_us.class);
                startActivity(intent);
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, Privacy_policy.class);
                startActivity(intent);
            }
        });

        terms_and_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, Terms_and_condition.class);
                startActivity(intent);
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, Contact_us.class);
                startActivity(intent);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, Log_out.class);
                startActivity(intent);
            }
        });
    }
}
