package com.practica.ismael.foodcal.ui.about;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.utils.SnackbarUtils;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    private Button button;
    private int counter = 11;
    private ImageView imageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        setupViews();
    }

    private void setupViews() {
        String urlGif = "https://thumbs.gfycat.com/ImpishDiscreteGonolek-size_restricted.gif";
        imageView = findViewById(R.id.imgGif);
        imageView.setVisibility(View.INVISIBLE);

        Uri uri = Uri.parse(urlGif);
        Glide.with(getApplicationContext()).load(uri).into(imageView);

        setSupportActionBar(findViewById(R.id.aboutToolbar));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        button = findViewById(R.id.btnSecret);

        button.setOnClickListener(v -> secret());
    }

    private void secret() {
        if (counter != 1) {
            counter--;
            SnackbarUtils.snackbar(button, getString(R.string.tap) + " " + counter + " " + getString(R.string.times), Snackbar.LENGTH_SHORT);
        }else{
            imageView.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
