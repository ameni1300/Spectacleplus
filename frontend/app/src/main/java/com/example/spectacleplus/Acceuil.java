package com.example.spectacleplus;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Acceuil extends AppCompatActivity {

    private EditText searchBar;
    private Spinner filterSpinner;
    private RecyclerView recyclerPopulaires, recyclerNouveautes;
    private SpectacleAdapter adapterPopulaires, adapterNouveautes;
    private List<Spectacle> spectaclesList = new ArrayList<>();
    private List<Spectacle> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        // Initialisation des composants
        searchBar = findViewById(R.id.search_bar);
        filterSpinner = findViewById(R.id.filter_spinner);
        recyclerPopulaires = findViewById(R.id.recycler_populaires);
        recyclerNouveautes = findViewById(R.id.recycler_nouveautes);

        // Configuration des adaptateurs
        adapterPopulaires = new SpectacleAdapter(this, filteredList);
        adapterNouveautes = new SpectacleAdapter(this, filteredList);

        recyclerPopulaires.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerPopulaires.setAdapter(adapterPopulaires);

        recyclerNouveautes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerNouveautes.setAdapter(adapterNouveautes);

        // Spinner
        String[] filters = {"Titre", "Date", "H_DebutS", "Nom Lieu", "Localisation"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filters);
        filterSpinner.setAdapter(spinnerAdapter);

        // Barre de recherche
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString(), filterSpinner.getSelectedItem().toString());
            }
        });

        // Charger les données depuis l'API
        fetchSpectacles();
    }

    private void fetchSpectacles() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Spectacle>> call = apiService.getSpectacles();

        call.enqueue(new Callback<List<Spectacle>>() {
            @Override
            public void onResponse(Call<List<Spectacle>> call, Response<List<Spectacle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    spectaclesList.clear();
                    spectaclesList.addAll(response.body());

                    filteredList.clear();
                    filteredList.addAll(spectaclesList);

                    adapterPopulaires.notifyDataSetChanged();
                    adapterNouveautes.notifyDataSetChanged();
                } else {
                    Toast.makeText(Acceuil.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Spectacle>> call, Throwable t) {
                Toast.makeText(Acceuil.this, "Échec de la connexion: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterList(String query, String filterType) {
        filteredList.clear();
        for (Spectacle spectacle : spectaclesList) {
            switch (filterType) {
                case "Titre":
                    if (spectacle.getTitre().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(spectacle);
                    }
                    break;
                case "Date":
                    if (spectacle.getDate().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(spectacle);
                    }
                    break;
                case "H_DebutS":
                    if (spectacle.getHeureDebut().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(spectacle);
                    }
                    break;
                case "Nom Lieu":
                    if (spectacle.getLieu().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(spectacle);
                    }
                    break;
                case "Localisation":
                    if (spectacle.getlocalisation().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(spectacle);
                    }
                    break;
            }
        }
        adapterPopulaires.notifyDataSetChanged();
        adapterNouveautes.notifyDataSetChanged();
    }
}
