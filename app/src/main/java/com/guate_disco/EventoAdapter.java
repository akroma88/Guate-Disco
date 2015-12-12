package com.guate_disco;

/**
 * Created by akroma on 25/09/15.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EventoAdapter extends ArrayAdapter<DTO_Evento> {
    static DTO_Evento item;
    static List<DTO_Evento> obj;
    private Context mContext;
    public LayoutInflater inflater=null;

    public EventoAdapter(Context context, List<DTO_Evento> objects) {
        super(context, 0, objects);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        obj = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View v = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            v = inflater.inflate(
                    R.layout.row_evento,
                    parent,
                    false);
        }

        TextView nombre = (TextView)v.findViewById(R.id.nameEvent); // artist name
        TextView descripcion = (TextView)v.findViewById(R.id.textEvento); // duration
        ImageButton fecha = (ImageButton)v.findViewById(R.id.linkButtonEvent); // duration


        //Obteniendo instancia de la Tarea en la posición actual
        item = getItem(position);

        nombre.setText(item.getNombre());
        descripcion.setText(item.getDescripcion());
        //fecha.setText(item.getEnlace());


        //Devolver al ListView la fila creada
        return v;

    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}