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
import android.widget.ArrayAdapter;;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.ArrayList;
import java.util.List;
import com.guate_disco.LazyAdapter;
import com.guate_disco.DTO_Discoteca;

/**
 * Created by akroma on 24/09/15.
 */
public class ListDiscos extends AppCompatActivity {

    ListView lv;
    Context context;
    ListView list;
    Spinner spinnerZonas, spinnerCuidades;
    LazyAdapter adapter;
    String json="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_disco);
        context=this;
        spinnerZonas = (Spinner)findViewById(R.id.zonas_spinner);
        spinnerCuidades = (Spinner)findViewById(R.id.ciudad_spinner);


        final List<String> list = new ArrayList<String>();
        list.add("todos");
        list.add("1"); list.add("2"); list.add("3"); list.add("4");
        list.add("5"); list.add("6"); list.add("7"); list.add("8");
        list.add("9"); list.add("10"); list.add("11"); list.add("12");
        list.add("13"); list.add("14"); list.add("15"); list.add("16");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZonas.setAdapter(dataAdapter);

        List<String> list2 = new ArrayList<String>();
        list2.add("Capital");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuidades.setAdapter(dataAdapter2);

        final int[] selectionCurrent = {spinnerZonas.getSelectedItemPosition()};


        spinnerZonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectionCurrent[0] != position) {
                    Log.i("zona", "" + list.get(position));
                    if(checkConnectivity()){
                        getDiscos("" + list.get(position));
                    }else{
                        CharSequence text = "No tienes conexion a internet!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
                selectionCurrent[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if(checkConnectivity()){
            getDiscos("todos");
        }else{
            CharSequence text = "No tienes conexion a internet!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }

    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ArrayList<DTO_Discoteca> discos= new ArrayList<DTO_Discoteca>();
                    Object obj = JSONValue.parse(json);
                    JSONArray mJsonArray = (JSONArray) obj;

                    JSONObject mJsonObject = new JSONObject();
                    for (int i = 0; i < mJsonArray.size(); i++) {
                        mJsonObject = (JSONObject) mJsonArray.get(i);
                        DTO_Discoteca disco = new DTO_Discoteca();
                        disco.setId("" + mJsonObject.get("id"));
                        disco.setNombre("" + mJsonObject.get("nombre"));
                        disco.setDireccion("" + mJsonObject.get("direccion"));
                        disco.setTelefono("" + mJsonObject.get("telefono"));
                        disco.setRt(Integer.parseInt("" + mJsonObject.get("rt")));
                        discos.add(disco);
                    }

                    //context=this;
                    list=(ListView) findViewById(R.id.list);

                    // Getting adapter by passing xml data ArrayList
                    adapter=new LazyAdapter(ListDiscos.this, discos);
                    list.setAdapter(adapter);



                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            LazyAdapter.item = LazyAdapter.obj.get(position);

                            Discoteca.nombre = LazyAdapter.item.getNombre();
                            Discoteca.direccion = LazyAdapter.item.getDireccion();
                            Discoteca.telefono = LazyAdapter.item.getTelefono();
                            Discoteca.idDisco = LazyAdapter.item.getId();

                            Intent intent = new Intent(ListDiscos.this, Discoteca.class);
                            startActivity(intent);

                        }
                    });
                    break;
            }
            return false;
        }
    });

    private final void getDiscos(final String toConvert) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                json = ex.getObtainData(toConvert,"getDiscos","zonas");
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    private boolean checkConnectivity()
    {
        boolean enabled = true;

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable()))
        {
            enabled = false;
        }
        return enabled;
    }

}
