package com.anton.testtagsoft;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopUp_CharacterActivity extends AppCompatActivity {

    String name, status,species, gender;
    Bitmap image;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.name)
    TextView nametxt;
    @BindView(R.id.status)
    TextView statustxt;
    @BindView(R.id.species)
    TextView speciestxt;
    @BindView(R.id.gender)
    TextView gendertxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        image = intent.getParcelableExtra("image");
        status = intent.getStringExtra("status");
        species = intent.getStringExtra("species");
        gender = intent.getStringExtra("gender");

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width*.8),(int)(height*.5));
        ButterKnife.bind(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        imageView.setImageBitmap(image);
        nametxt.setText(name);
        statustxt.setText(status);
        gendertxt.setText(gender);
        speciestxt.setText(species);
    }
}