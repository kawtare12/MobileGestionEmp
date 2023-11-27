package com.elazizi.emp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.elazizi.emp.R;
import com.elazizi.emp.beans.Personne;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PersoneAdapter  extends ArrayAdapter<Personne> {

    private static class ViewHolder {
        TextView id;
        TextView nom;

        TextView date;
        TextView service;
    }

    public PersoneAdapter(@NonNull Context context, int resource, @NonNull List<Personne> persones) {
        super(context, resource, persones);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.item11, parent, false);

            holder = new ViewHolder();
            holder.id = item.findViewById(R.id.id);
            holder.nom = item.findViewById(R.id.nom);
            holder.date = item.findViewById(R.id.date);
            holder.service = item.findViewById(R.id.spe);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        Personne Personne = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat(("yyyy-MM-dd"), Locale.getDefault());
        if (Personne != null) {
            holder.id.setText(String.valueOf(Personne.getId()));
            holder.nom.setText(Personne.getNom());


            String formattedDate = dateFormat.format(Personne.getDate());
            holder.date.setText(formattedDate);

            holder.service.setText(Personne.getService().getNom());
        }

        return item;
    }
}
