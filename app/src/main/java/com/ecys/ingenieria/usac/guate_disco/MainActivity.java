package com.ecys.ingenieria.usac.guate_disco;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity{
    Button btnSegundo;
    ImageView imageView12;
    TextView texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        btnSegundo = (Button) findViewById(R.id.buttonSig);
        texto = (TextView)findViewById(R.id.queEsGD);

        imageView12 = (ImageView)findViewById(R.id.imageGD);
        Picasso.with(MainActivity.this).load("http://1.bp.blogspot.com/-C_WvY5YfBP0/VZ8rSn_VGbI/AAAAAAAAAzs/NmkCirkNE84/s1600/rojo%2B%25281%2529.png").resize(350, 350)
                .transform(new CropCircleTransformation())
                .into(imageView12);

        texto.setText("El mejor donde podras encontrar las mejores discotecas de Guatemala");
    }

    public  void onClickButtonSig(View view){

        Intent intent = new Intent(this, ListDiscos.class);
        startActivity(intent);
    }



}
