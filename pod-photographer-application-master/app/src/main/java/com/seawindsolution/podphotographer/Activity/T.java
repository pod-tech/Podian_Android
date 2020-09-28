package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.seawindsolution.podphotographer.R;

public class T extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t);

        recyclerView = findViewById(R.id.id_Recycleview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        /*ImageView image = (ImageView)findViewById(R.id.imageViews);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        image.startAnimation(animation1);

        ImageView images = (ImageView)findViewById(R.id.imageView);
        Animation animation1s =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ss);
        images.startAnimation(animation1s);*/
    }
}
