package com.elazizi.emp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elazizi.emp.beans.Service;

import org.json.JSONException;
import org.json.JSONObject;
public class AddSr extends AppCompatActivity implements View.OnClickListener {
    private EditText nom;
    private Button bnAdd;
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.141:8082/api/service/save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sr);

        nom = findViewById(R.id.nom);
        bnAdd = findViewById(R.id.add);

        bnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Your onClick implementation
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nom", nom.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(AddSr.this, "new prof added !", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }
}
