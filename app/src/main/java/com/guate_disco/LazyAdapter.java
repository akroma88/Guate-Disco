package com.guate_disco;

/**
 * Created by akroma on 25/09/15.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class LazyAdapter extends ArrayAdapter<DTO_Discoteca> {
    static DTO_Discoteca item;
    static List<DTO_Discoteca> obj;

    public LazyAdapter(Context context, List<DTO_Discoteca> objects) {
        super(context, 0, objects);
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
                    R.layout.row_disco,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView title = (TextView)v.findViewById(R.id.title); // nombre
        TextView artist = (TextView)v.findViewById(R.id.artist); // direccion
        RatingBar rt = (RatingBar)v.findViewById(R.id.rtbDiscRating); //rating
        ImageView thumb_image=(ImageView)v.findViewById(R.id.list_image); // thumb image


        //Obteniendo instancia de la Tarea en la posición actual
        item = getItem(position);

        title.setText(item.getNombre());
        artist.setText(item.getDireccion());
        float ret =item.getRt();
        rt.setRating(ret);
        //imagenAnimal.setImageResource(convertirRutaEnId(item.getImagen()));

        //Devolver al ListView la fila creada
        return v;

    }

    /**
     * Este método nos permite obtener el Id de un drawable a través
     * de su nombre
     * @param nombre  Nombre del drawable sin la extensión de la imagen
     *
     * @return Retorna un tipo int que representa el Id del recurso
     */
    private int convertirRutaEnId(String nombre){
        Context context = getContext();
        return context.getResources()
                .getIdentifier(nombre, "drawable", context.getPackageName());
    }
}