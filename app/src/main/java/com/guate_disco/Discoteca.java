package com.guate_disco;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akroma on 27/09/15.
 */
public class Discoteca extends AppCompatActivity {
    static String nombre;
    static String direccion;
    static String idDisco;
    static String telefono;

    TextView textViewNombre, textViewDireccion, textViewTelefono, textDescripcion;
    Button btnEscribOpionion;
    ImageButton btnOfertas, btnMapa, btnWeb, btnFb, btnTwitt;
    ListView list;
    CommentAdapter commentAdapter;
    String json="";
    String jsonDisco="";
    String webSite,facebook,twitter;
    Double latitud, altitud;

    @Override
    protected void onResume(){
        super.onResume();
        getCommentarios(idDisco);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discoteca);

        textViewNombre = (TextView)findViewById(R.id.textNameDisco);
        textViewDireccion = (TextView)findViewById(R.id.textDirDisco);
        textViewTelefono = (TextView)findViewById(R.id.textTelDisco);
        list = (ListView)findViewById(R.id.listComment);
        btnEscribOpionion = (Button)findViewById(R.id.button_escribe_opinion);
        btnOfertas = (ImageButton)findViewById(R.id.btn_promo);
        btnMapa = (ImageButton)findViewById(R.id.btnMapa);
        btnWeb = (ImageButton)findViewById(R.id.btnWeb);
        btnFb = (ImageButton)findViewById(R.id.btnFb);
        btnTwitt = (ImageButton)findViewById(R.id.btnTwitter);

        textViewNombre.setText(this.nombre);
        textViewDireccion.setText(this.direccion);
        textViewTelefono.setText(this.telefono);

        getCommentarios(idDisco);
        getDisco(idDisco);


        btnEscribOpionion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Discoteca.this, NewComment.class);
                startActivity(intent);
                NewComment.idDiscoteca = idDisco;
                NewComment.nameDiscoteca = nombre;
            }
        });

        btnOfertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Muy pronto!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
/*
                Intent intent = new Intent(Discoteca.this, ListEventos.class);
                startActivity(intent);
                ListEventos.idDisco = idDisco;*/

            }
        });

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Discoteca.this, MapsActivity.class);
                intent.putExtra("latitud",latitud);
                intent.putExtra("altitud",altitud);
                intent.putExtra("place",nombre);
                startActivity(intent);
            }
        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(webSite);
            }
        });

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(facebook);


                /*final String url = "fb://page/172531009566234";
                Intent facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                facebookAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(facebookAppIntent);*/

            }
        });

        btnTwitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(twitter);
            }
        });
    }


    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case 0:

                    ArrayList<DTO_Comentario> comments= new ArrayList<DTO_Comentario>();
                    Object obj = JSONValue.parse(json);
                    JSONArray mJsonArray = (JSONArray) obj;

                    JSONObject mJsonObject = new JSONObject();
                    for (int i = 0; i < mJsonArray.size(); i++) {
                        mJsonObject = (JSONObject) mJsonArray.get(i);
                        DTO_Comentario comment = new DTO_Comentario();
                        comment.setText("" + mJsonObject.get("texto"));
                        comment.setFecha("" + mJsonObject.get("fecha"));
                        comment.setRt(Float.parseFloat(mJsonObject.get("rt").toString()));
                        comment.setUser("" + mJsonObject.get("user"));
                        comment.setUrlPic("" + mJsonObject.get("urlPic"));
                        comments.add(comment);
                    }

                    //context=this;
                    list=(ListView) findViewById(R.id.listComment);

                    // Getting adapter by passing xml data ArrayList
                    commentAdapter=new CommentAdapter(Discoteca.this, comments);
                    list.setAdapter(commentAdapter);

                    break;
            }
            return false;
        }
    });

    public Handler handlerDisco = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    Object obj = JSONValue.parse(jsonDisco);
                    JSONArray mJsonArray = (JSONArray) obj;
                    JSONObject mJsonObject = new JSONObject();
                    mJsonObject = (JSONObject) mJsonArray.get(0);
                    latitud=Double.parseDouble(mJsonObject.get("latitud").toString());
                    altitud=Double.parseDouble(mJsonObject.get("altitud").toString());

                    btnWeb = (ImageButton)findViewById(R.id.btnWeb);
                    btnFb = (ImageButton)findViewById(R.id.btnFb);
                    btnTwitt = (ImageButton)findViewById(R.id.btnTwitter);
                    textDescripcion = (TextView)findViewById(R.id.descripcion);

                    textDescripcion.setText(mJsonObject.get("descripcion").toString());

                    webSite=mJsonObject.get("webSite").toString();
                    if(!webSite.equalsIgnoreCase("null")){
                        btnWeb.setVisibility(View.VISIBLE);
                    }

                    facebook=mJsonObject.get("facebook").toString();
                    if(!facebook.equalsIgnoreCase("null")){
                        btnFb.setVisibility(View.VISIBLE);
                    }

                    twitter=mJsonObject.get("twitter").toString();
                    if(!twitter.equalsIgnoreCase("null")){
                        btnTwitt.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return false;
        }
    });


    private final void getCommentarios(final String toConvert) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                json = ex.getObtainData(toConvert,"getComments","idDisc");
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    private final void getDisco(final String toConvert) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                jsonDisco = ex.getObtainData(toConvert,"getDisco","idDisc");
                handlerDisco.sendEmptyMessage(0);
            }
        }).start();
    }
}
