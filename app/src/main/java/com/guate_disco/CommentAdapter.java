package com.guate_disco;

/**
 * Created by akroma on 25/09/15.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<DTO_Comentario> {
    static DTO_Comentario item;
    static List<DTO_Comentario> obj;
    private Context mContext;
    public LayoutInflater inflater=null;

    public CommentAdapter(Context context, List<DTO_Comentario> objects) {
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
                    R.layout.row_comment,
                    parent,
                    false);
        }

        TextView user = (TextView)v.findViewById(R.id.nameUser); // artist name
        TextView coment = (TextView)v.findViewById(R.id.textComment); // duration
        RatingBar rt = (RatingBar)v.findViewById(R.id.commetDiscRating); //rating
        TextView fecha = (TextView)v.findViewById(R.id.commentDate); // duration
        ImageView thumb_image=(ImageView)v.findViewById(R.id.imageUser); // thumb image


        //Obteniendo instancia de la Tarea en la posición actual
        item = getItem(position);

        user.setText(item.getUser());
        coment.setText(item.getText());
        float ret =item.getRt();
        rt.setRating(ret);
        fecha.setText(item.getFecha());

        Picasso.with(mContext).load(item.getUrlPic()).resize(130, 130)
                .transform(new CropCircleTransformation())
                .into(thumb_image);

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