package com.elazizi.emp.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.elazizi.emp.Adapter.ServiceAdapter;
import com.elazizi.emp.AddPr;
import com.elazizi.emp.AddSr;
import com.elazizi.emp.R;
import com.elazizi.emp.beans.Service;
import com.elazizi.emp.beans.Service;
import com.elazizi.emp.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GalleryFragment extends Fragment  {


    private static final String URL_LOAD = "http://192.168.1.141:8082/api/service/all";

    private ListView listView;
    private List<Service> personneList;
    private String nom;




    private ServiceAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        listView = view.findViewById(R.id.list);
        personneList = new ArrayList<Service>();
        adapter = new ServiceAdapter(getActivity(), R.layout.item12,personneList);
        listView.setAdapter(adapter);

        ImageView imageView = view.findViewById(R.id.img);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddSr.class);
                startActivity(intent);
            }
        });
        loadServices();
        return view;

    }

    private void loadServices() {
        personneList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_LOAD,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String nom = jsonObject.getString("nom");

                                // Créer un objet Service avec les données récupérées
                                Service service = new Service(id, nom);
                                // Ajouter le service à la liste personneList
                                personneList.add(service);
                            }

                            // Mettre à jour l'adaptateur avec la nouvelle liste de services
                            adapter.clear();
                            adapter.addAll(personneList);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gestion des erreurs
                    }
                }
        );

        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }



}