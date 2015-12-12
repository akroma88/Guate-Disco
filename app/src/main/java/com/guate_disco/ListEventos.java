package com.guate_disco;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by akroma on 24/09/15.
 */
public class ListEventos extends AppCompatActivity {
    
    Context context;
    ListView list;
    static String idDisco;
    EventoAdapter adapter;
    String json="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_evento);
        context=this;

        getEventos(idDisco);

    }

    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ArrayList<DTO_Evento> eventos= new ArrayList<DTO_Evento>();
                    Object obj = JSONValue.parse(json);
                    JSONArray mJsonArray = (JSONArray) obj;

                    JSONObject mJsonObject = new JSONObject();
                    for (int i = 0; i < mJsonArray.size(); i++) {
                        mJsonObject = (JSONObject) mJsonArray.get(i);
                        DTO_Evento evento = new DTO_Evento();
                        evento.setNombre("" + mJsonObject.get("nombre"));
                        Log.i("event",""+mJsonObject.get("nombre"));
                        evento.setDescripcion("" + mJsonObject.get("descripcion"));
                        evento.setEnlace("" + mJsonObject.get("enlace"));
                        eventos.add(evento);
                    }

                    //context=this;
                    list=(ListView) findViewById(R.id.listEventos);

                    // Getting adapter by passing xml data ArrayList
                    adapter=new EventoAdapter(ListEventos.this, eventos);
                    list.setAdapter(adapter);
                    break;
            }
            return false;
        }
    });

    private final void getEventos(final String toConvert) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                json = ex.getObtainData(toConvert,"getEventos","idDisco");
                handler.sendEmptyMessage(0);
            }
        }).start();
    }




}
