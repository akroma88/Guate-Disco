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
import android.widget.ImageButton;
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
    ImageButton btnSegundo;
    ImageView imageView12;
    TextView texto;
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
    }

    public  void onClickButtonSig(View view){

        Intent intent = new Intent(this, ListDiscos.class);
        startActivity(intent);
    }



}
