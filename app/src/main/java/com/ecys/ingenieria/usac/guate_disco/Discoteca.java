package com.ecys.ingenieria.usac.guate_disco;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by akroma on 27/09/15.
 */
public class Discoteca extends AppCompatActivity {
    static String nombre;
    static String direccion;
    static String idDisco;
    static String telefono;

    TextView textViewNombre;
    TextView textViewDireccion;
    TextView textViewTelefono;
    Button btnEscribOpionion;
    ListView list;
    CommentAdapter commentAdapter;
    String json="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discoteca);

        textViewNombre = (TextView)findViewById(R.id.textNameDisco);
        textViewDireccion = (TextView)findViewById(R.id.textDirDisco);
        textViewTelefono = (TextView)findViewById(R.id.textTelDisco);
        list = (ListView)findViewById(R.id.listComment);
        btnEscribOpionion = (Button)findViewById(R.id.button_escribe_opinion);

        textViewNombre.setText(this.nombre);
        textViewDireccion.setText(this.direccion);
        textViewTelefono.setText(this.telefono);

        getCommentarios(idDisco);



        btnEscribOpionion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Discoteca.this, NewComment.class);
                startActivity(intent);
                NewComment.idDiscoteca = idDisco;
                NewComment.nameDiscoteca= nombre;
            }
        });
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

}
