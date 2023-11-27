package com.elazizi.emp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elazizi.emp.beans.Personne;
import com.elazizi.emp.beans.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPr extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerSpe,spinnerPers;
    private EditText nom, prenom, date;
    List<String> speList = new ArrayList<>();
    List<String> perList = new ArrayList<>();
Personne per;
    Service spe;
    String selectedSpe;
    String selectedPer;

    private Button bnAdd;
    List<Service> spes = new ArrayList<>();
    List<Personne> pers = new ArrayList<>();

    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.141:8082/api/employe/save";
    String getspe = "http://192.168.1.141:8082/api/service/all";
    String getpers = "http://192.168.1.141:8082/api/employe/all";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pr);

        spinnerSpe = findViewById(R.id.spe);
        spinnerPers= findViewById(R.id.pers);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);

        date = findViewById(R.id.date);
        bnAdd = findViewById(R.id.add);

        bnAdd.setOnClickListener(this);
        loadspe();
        loadpers();


        spinnerSpe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpe = (String) spinnerSpe.getSelectedItem();
                for (Service r : spes) {
                    if (r.getNom().equals(selectedSpe)) {
                        spe = new Service(r.getId(), r.getNom());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPer = (String) spinnerPers.getSelectedItem();
                for (Personne r : pers) {
                    if (r.getNom().equals(selectedPer)) {
                        per = new Personne(r.getId(), r.getNom(), r.getPrenom(), r.getDate(), r.getService());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
});


    }




    private void loadspe() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getspe,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String code = jsonObject.getString("nom");

                                spes.add(new Service(id, code));
                                speList.add(code);
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(AddPr.this, android.R.layout.simple_spinner_item, speList);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSpe.setAdapter(spinnerAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void loadpers() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getpers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String nom = jsonObject.getString("nom");
                                String prenom = jsonObject.getString("prenom");
                                String dateStr = jsonObject.getString("date");
                                String serviceStr = jsonObject.getString("service");

                                // Convertir la chaîne de date en objet Date (à condition qu'elle soit dans un format valide)
                                Date date = null;
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                                    date = sdf.parse(dateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                // Créer un objet Personne
                                Personne personne = new Personne(id, nom, prenom, date, new Service(serviceStr));

                                // Ajouter la personne à la liste et à l'adaptateur
                                pers.add(personne);
                                perList.add(nom);

                            }

                            // Mettre à jour l'adaptateur après la boucle
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(AddPr.this, android.R.layout.simple_spinner_item, perList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerPers.setAdapter(spinnerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérer les erreurs
                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
}

    @Override
    public void onClick(View view) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nom", nom.getText().toString());
            jsonBody.put("prenom", prenom.getText().toString());

            // Formater la date dans le format attendu par le serveur
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = sdf.format(new Date());

            jsonBody.put("date", formattedDate);

            // Utiliser l'ID de la personne sélectionnée
            JSONObject speObj = new JSONObject();
            speObj.put("id", spe.getId());
            speObj.put("nom", spe.getNom());
            jsonBody.put("service", speObj);

            // Utiliser l'ID de la personne sélectionnée
            JSONObject empObj = new JSONObject();
            empObj.put("id", per.getId());  // Utiliser l'ID de la personne sélectionnée
            jsonBody.put("emp", empObj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(AddPr.this, "New person added!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());

                // Ajouter des logs pour déboguer
                if (error.networkResponse != null) {
                    Log.d("Error Status", String.valueOf(error.networkResponse.statusCode));
                    Log.d("Error Data", new String(error.networkResponse.data));
                }

                // Afficher l'erreur dans un Toast
                Toast.makeText(AddPr.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
}
}