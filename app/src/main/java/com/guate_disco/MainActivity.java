package com.guate_disco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity{
    ImageButton btnSegundo;
    ImageView imageView12;
    TextView texto;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        btnSegundo = (ImageButton) findViewById(R.id.buttonSig);
        texto = (TextView)findViewById(R.id.queEsGD);

        imageView12 = (ImageView)findViewById(R.id.imageGD);
        Picasso.with(MainActivity.this).load(R.drawable.logo).resize(350, 350)
                .into(imageView12);

        texto.setText("El lugar donde podras encontrar las mejores discotecas de Guatemala");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public  void onClickButtonSig(View view){

        Intent intent = new Intent(this, ListDiscos.class);
        startActivity(intent);
    }



}
