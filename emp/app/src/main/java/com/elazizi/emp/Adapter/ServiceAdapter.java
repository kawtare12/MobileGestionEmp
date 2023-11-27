package com.elazizi.emp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.elazizi.emp.R;
import com.elazizi.emp.beans.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ServiceAdapter  extends ArrayAdapter<Service> {

    private static class ViewHolder {
        TextView id;
        TextView nom;

      
    }

    public ServiceAdapter(@NonNull Context context, int resource, @NonNull List<Service> persones) {
        super(context, resource, persones);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.item12, parent, false);

            holder = new ViewHolder();
            holder.id = item.findViewById(R.id.id);
            holder.nom = item.findViewById(R.id.nom);
           

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        Service Service = getItem(position);
        if (Service != null) {
            holder.id.setText(String.valueOf(Service.getId()));
            holder.nom.setText(Service.getNom());


        }

        return item;
    }
}
